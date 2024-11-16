# Storage in Docker

Docker가 로컬 파일 시스템에 데이터를 저장하는 방법?

처음 도커를 설치하게 되면 `/var/lib/docker` 폴더가 생성되는데, 다양한 하위 폴더가 구성됨

```
/var/lib/docker
├─ aufs
├─ containers
├─ image
└─ volumes
```

각 폴더 하위에는 Docker 호스트에서 실행되는 이미지나 컨테이너들과 관련된 데이터들이 저장되어 있음

<br><br>

### Docker Layered architecture

Docker는 이미지를 구축할 때 레이어드 아키텍처로 구축하는데,
Dockerfile 의 각각의 명령어 줄은 이전 레이어에서 변경된 것에 한해 Docker 이미지에 새 레이어를 생성

<table>
<tr>
<th>Dockerfile</th>
<th>Processing</th>
</tr>
<tr>
<td>
<pre><code lang="dockerfile">FROM ubuntu

RUN apt-get update && apt-get -y install python

RUN pip install flask flask-mysql

COPY . /opt/source-code

ENTRYPOINT FLASK_APP=/opt/source-code/app.py flask tun
</code></pre>
</td>
<td>
<pre><code lang="dockerfile">docker build Dockerfile -t gngsn/custom-app</code></pre>

<table>
<tr>
<th>Layers</th>
<th>Size</th>
</tr>
<tr>
<td>Layer 1. Base Ubuntu Layer</td>
<td>120 MB</td>
</tr>
<tr>
<td>Layer 2. Changes in apt packages</td>
<td>306 MB</td>
</tr>
<tr>
<td>Layer 3. Changes in pip packages</td>
<td>6.3 MB</td>
</tr>
<tr>
<td>Layer 4. Source code</td>
<td>229 B</td>
</tr>
<tr>
<td>Layer 5. Update Entrypoint</td>
<td>0 B</td>
</tr>
</table>

</td>
</tr>
</table>

각각의 레이어 변화를 저장할 때, 이 때 사이즈 또한 반영됨

Layered architecture 의 장점을 제대로 이해하려면, 두 번째 Dockerfile 을 처리할 때와 비교해보면 됨

<table>
<tr>
<th>Dockerfile2</th>
<th>Processing</th>
</tr>
<tr>
<td>
<pre><code lang="dockerfile">FROM ubuntu

RUN apt-get update && apt-get -y install python

RUN pip install flask flask-mysql

COPY app2.py /opt/source-code

ENTRYPOINT FLASK_APP=/opt/source-code/app2.py flask tun
</code></pre>
</td>
<td>
<pre><code lang="dockerfile">docker build Dockerfile2 -t gngsn/custom-app-2</code></pre>

<table>
<tr>
<th></th>
<th>Layers</th>
<th>Size</th>
</tr>
<tr>
<td>Cached</td>
<td>Layer 1. Base Ubuntu Layer</td>
<td>0 MB</td>
</tr>
<tr>
<td>Cached</td>
<td>Layer 2. Changes in apt packages</td>
<td>0 MB</td>
</tr>
<tr>
<td>Cached</td>
<td>Layer 3. Changes in pip packages</td>
<td>0 MB</td>
</tr>
<tr>
<td></td>
<td>Layer 4. Source code</td>
<td>229 B</td>
</tr>
<tr>
<td></td>
<td>Layer 5. Update Entrypoint</td>
<td>0 B</td>
</tr>
</table>

Dockerfile 1 과의 차이인 Layer 4 부터만 레이어를 빌드

이 때는 캐시된 레이어를 사용하는데, **속도** 뿐만 아니라 **디스크 저장 공간**에도 효과적임 

</td>
</tr>
</table>

한 번 빌드된 레이어는 수정할 수 없고 새로운 빌드를 통해 재정의하는 방법 밖에 없음

```Bash
docker run gngsn/custom-app
```

해당 이미지로 컨테이너를 실행하면 새로운 Container Layer를 Image Layer 위에 생성하게 됨

<table>
<tr><th>Read/Write</th><td>Layer 6. Container Layer</td></tr>
<tr><th rowspan="6">Read Only</th></tr>
<tr><td>Layer 5. Update Entrypoint</td></tr>
<tr><td>Layer 4. Source code</td></tr>
<tr><td>Layer 3. Changes in pip packages</td></tr>
<tr><td>Layer 2. Changes in apt packages</td></tr>
<tr><td>Layer 1. Base Ubuntu Layer</td></tr>
</table>

Container Layer 는 쓰기 가능하며, 컨테이너에 의해 생성된 데이터를 저장하는 데 사용됨

가령, 로그 파일이나 컨테이너에 의해 생성된 임시 파일 혹은 사용자가 수정된 파일 등

하지만 이런 파일들은 컨테이너가 살아있는 동안에만 유지

짚어볼 점은, 한 이미지로 생성된 여러 컨테이너가 사용하는 이미지 레이어는 모두 동일

이미지에 작성된 특정 파일을 수정하지 못하는 것은 아님

정확히 짚어 보면, 이미지 내의 파일은 컨테이너가 생성될 때 복사되고 이를 수정할 수 있음 

<br>

```
+--- Container Layer ---+
|                       |
|  📄 app.py (modified) |
|                       |
+----- Image Layer -----+
|                       |
|      📄 app.py        |
|                       |
+-----------------------+
```

<br>

즉, 컨테이너로 복사된 동일한 파일을 수정하는 것

이 떄 컨테이너를 제거하면 어떨까 - 모든 Container Layer의 데이터가 제거됨

<br>

<pre><code>+--- Container Layer ---+
|                       |
|  <s>📄 app.py (modified)</s> |   ➡️  All Removed
|                       |
+----- Image Layer -----+
|                       |
|      📄 app.py        |
|                       |
+-----------------------+
</code></pre>

<br>

**→ 데이터를 유지 하고 싶다면?**

---

## Volumes

생성된 데이터를 저장하고 싶다면 컨테이너에 영구적인 볼륨을 추가할 수 있음

먼저 볼륨 생성

```Bash
docker volume create data_volume
```

`docker volume`을 실행하면 `/var/lib/docker/volumes` 하위에 디렉터리를 만듦

<br>
<pre><code>
├─ ...
├─ volumes
...└─ <b>data_volume</b> 
</code></pre>
<br>

이후, docker 컨테이너를 실행할 때 `docker run -v` 명령으로, 
이 볼륨을 docker 컨테이너 내부에 마운트할 수 있음

<br>

### Volume Mounting

-v option 에 새로 생성한 **`<<Volume Name>>:<<Container 내 Mount 할 위치>>`** 형식으로 입력

```Bash
docker run -v data_volume:/var/lib/mysql mysql
```

가령 `mysql`의 경우 기본 위치가 `/var/lib/mysql` 이므로 위와 같이 설정할 수 있음 

<br>

```
+--------------------------- Docker Host -------------------------+
|  +--- Container Layer ---+    +-----------------------------+   |
|  |                       +----+         data_volume         |   |
|  |    /var/lib/mysql     |    |   /var/lib/docker/volumes   |   |
|  |     <read·write>      |    +-----------------------------+   |
|  |                       |                                      |
|  +----- Image Layer -----+                                      |
|  |        mysql          |                                      |
|  |      <readonly>       |                                      |
|  +-----------------------+                                      |
+-----------------------------------------------------------------+
```

→ **Volume Mounting**

데이터베이스가 작성한 모든 데이터는 Docker 호스트에 생성된 볼륨에 저장됨

즉, 컨테이너가 파괴돼도 데이터는 살아 있음

<br>

### Bind Mounting

이미 다른 곳에 있는 저장소에 해당 볼륨에 데이터베이스를 저장하고 싶다면, 마운트하려는 저장소 폴더의 절대 경로를 입력

```Bash
docker run -v /data/mysql:/var/lib/mysql mysql
```

```
+---------------- Docker Host -------------------+
|  +--- Container Layer ---+    +------------+   |
|  |                       +----+   mysql    |   |
|  |    /var/lib/mysql     |    |   /mysql   |   |
|  |     <read·write>      |    +------------+   |
|  |                       |                     |
|  +----- Image Layer -----+                     |
|  |        mysql          |                     |
|  |      <readonly>       |                     |
|  +-----------------------+                     |
+------------------------------------------------+
```

→ **Bind Mounting**

---

### docker run --mount 

`-v` 는 구식 옵션이고, `--mount` 를 사용할 수 있음

가령, 위의 명령어는 아래와 같이 대치 가능함

```Bash
docker run --mount type=bind,source=/data/mysql.target=/var/lib/mysql mysql
```

위 파일을 관리하는 주체는 Storage Driver.

<br>

### Storage Drivers

- AUFS
- ZFS
- BTRFS
- Device Mapper
- Overlay
- Overlay2

Ubuntu 의 default storage driver 는 AUFS 임 

Fedora나 CentOS는 AUFS 를 지원하지 않는데, 이런 경우엔 Device Mapper 를 사용하는 게 최선의 선택임

