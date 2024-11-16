# Image Security


`image: nginx` 에서 `nginx` 앞에는 Default Account 인 `library/` 가 붙는데, 가령 `image: library/nginx` 임

```
image: library/nginx
```

- `docker.io`: Registry
- `library`: User/Account
- `niginx`: Image/Repository

Image Registry 중에는 Public/Private 을 구분할 수 있음

공개 쿠버네티스 Registry에는 가령 Google의 `gcr.io`가 있는데, 다양한 유용한 쿠버네티스 관련 이미지들이 업로드되어 있음

가령, 쿠버네티스 End-to-End Test를 위해 `gcr.io/kubernetes-e2e-test-images/dnsutils` 가 있음

```
FYI. `gcr.io/kubernetes-e2e-test-images/dnsutils`
외부에 공개돼선 안 되는 내부에서 제작된 응용 프로그램이 있을 경우,
내부 개인 레지스트리를 제공하는 게 좋은 해결책일 수 있음
```

AWS, Azure, GCP와 같은 많은 클라우드 서비스 제공자는 기본값으로 개인 레지스트리를 제공

Docker Hub나 구글 저장소나 혹은 사설 개인 레지스트리 등, 개인 레지스트리를 고르게 될 때, 
자격 증명에 접근할 수 있어야 함

<br>

### Private Repository - Docker

가령, Docker 에서는 개인 이미지를 이용해 컨테이너를 실행하려면 먼저 개인 레지스트리에 로그인해야 함 

Docker login 명령을 사용할 수 있음

```Bash
docker login private-registry.io
```

로그인 성공 후, 개인 레지스트리의 이미지를 이용해 애플리케이션을 실행할 수 있음

```Bash
docker run private-registry.io/apps/internal-app
```

기본 레지스트리(`docker.io`)가 아닌 특정 레지스트리를 사용하고 싶다면, 
이미지 경로에 특정 레지스트리를 명시해야 함

<br>
<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
    - name: nginx
      image: <b>private-registry.io</b>/apps/internal-app
</code></pre>

<br>

### Private Repository - Kubernetes

쿠버네티스는 각 워커 노드에서 이미지를 pull 받아 Container Runtime을 실행시킴

쿠버네티스에서 자격 증명을 하는 방법

<br>

**1. 자격 증명을 가진 Secret을 생성**

`kubectl create secret docker-registry` 명령어를 통해 regcred 라는 시크릿을 생성

```Bash
kubectl create secret docker-registry regcred \
    --docker-server=private-registry.io \
    --docker-username=registry-user \
    --docker-password=registry-password \
    --docker-email=registry-user@org.com
```

`docker-registry`는 Docker 자격 증명을 저장하기 위한 `Secret` 유형

<br>

**2. Pod 정의 파일에 Secret 지정**

`imagePullSecrets` 속성에 생성한 Secret의 이름 입력

<br>
<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
    - name: nginx
      image: <b>private-registry.io</b>/apps/internal-app
  <b>imagePullSecrets:
    - name: regcred</b>
</code></pre>


