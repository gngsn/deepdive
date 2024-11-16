# Security Contexts

Docker 컨테이너를 실행할 때 보안 표준 집합을 정의할 옵션이 있음

컨테이너를 실행하는 데 사용되는 사용자의 ID 같은,
컨테이너에서 추가 또는 제거할 수 있는 리눅스 기능 등과 같은 일련의 보안 표준을 정의할 수 있는 옵션이 있음

```Bash
$ docker run --user=1001 ubuntu sleep 3600
$ docker run --cap-add MAC_ADMIN ubuntu
```

쿠버네티스에서 또한 이 설정을 할 수 있음 

쿠버네티스에서 컨테이너는 Pod에 안에 캡슐화 되어 있는데,
컨테이너 레벨과 Pod 레벨 중 보안 설정을 할 곳을 고를 것임

만약 이를 Pod 레벨에서 설정한다면, 해당 Pod 내에 있는 모든 컨테이너에서 해당 세팅이 공유될 것 

혹은 컨테이너 레벨과 Pod 레벨 둘 모두에 설정한다면 컨테이너 세팅이 Pod 세팅 값이 오버라이딩 됨 

---

### Security Context

Pod 레벨로 Security Context를 설정하려고 하기 위해서는 Pod 명세 파일의 `.spec.securityContext` 필드를 추가

`runAsUser` 필드에 Pod에서 사용할 사용자 ID를 설정 - 아래 예시에선 `1000`

<br>

**✔️ Pod Level**

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: web-pod
spec:
  <b>securityContext:
    runAsUser: 1000</b>

  containers:
  - name: ubuntu
    image: ubuntu
    command: ["sleep", "3600"]</code></pre>

컨테이너 레벨 설정을 원한다면, 해당 섹션을 컨테이너 정의 하위에 동일한 구성을 입력

<br>

**✔️ Container Level**

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: web-pod
spec:
  containers:
  - name: ubuntu
    image: ubuntu
    command: ["sleep", "3600"]
    <b>securityContext:
      runAsUser: 1000
      capabilities:
        add: ["MAC_ADMIN"]</b>
</code></pre>

Linux의 capabilities 를 추가하고 싶다면, `capabilities` 필드에 리스트 형태로 추가할 수 있음

단, Capabilities 는 오직 Container 레벨에서만 지원되며 Pod 레벨에서는 지원되지 않음

[🔗 Linux Capabilities](https://man7.org/linux/man-pages/man7/capabilities.7.html)