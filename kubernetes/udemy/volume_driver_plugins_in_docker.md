# Volume Driver Plugins in Docker

### Volume Driver

Volume은 '**Volume Driver Plugin**'으로 처리 (not Storage Driver) 

기본 Volume Driver Plugin은 Local

<br>

**Local Volume Driver Plugin**

: Docker 호스트에 볼륨 생성을 도와 그 데이터를 `var/lib/docker` 볼륨 디렉터리 아래에 저장

- Azure File Storage
- Convoy
- DigitalOcean
- Block Storage
- Flocker
- gce-docker
- GlusterFS
- NetApp
- REX-Ray
- Portworx
- VMware vSphere Storage
- ...

이외에도 다양한 볼륨 드라이버 존재

```Bash
docker run -it \ 
    --name mysql
    --volume-driver rexrasy/ebs
    --mount src=ebs-vol,target=/var/lib/mnysql
    mysql
```

`--volume-driver` 옵션을 통해 특정 볼륨 드라이버를 지정할 수 있음

