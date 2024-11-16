## Application Commands

Kubernetes 명령어를 알아보기 전, Docker를 먼저 살펴보도록 하자

### Docker

Ubuntu 이미지로 Docker 컨테이너를 운용한다고 할 때, 
docker run 명령을 입력하면 Ubuntu 이미지 인스턴스를 실행한 뒤 즉시 종료함 

그래서 바로 docker ps 명령어를 통해 실행 중인 컨테이너를 나열하려고 하면 컨테이너를 확인할 수 없음

모든 컨테이너를 확인하는 `docker ps -a`까지 입력하면 `Exited` 상태인 Ubuntu 컨테이너를 확인할 수 있음 

<br/><br/>

**왜일까?** 

가상 컴퓨터와 달리 컨테이너는 운영 체제를 호스팅하도록 되어 있지 않음

컨테이너는 특정 작업이나 프로세스를 실행
(Web Server, Application, Database 등의 인스턴스를 호스팅하거나 연산 또는 분석 등)

작업이 끝나면 컨테이너는 종료(`Exited`) 

컨테이너는 그 안의 프로세스가 살아 있는 동안에만 살아있음

컨테이너 내 프로세스가 멈추거나 충돌하면 컨테이너는 `Exited`


그럼 컨테이너 내에서 실행되는 프로세스는 어떻게 정의?

가령 NGINX Docker 이미지를 보면 `CMD` 부분이 존재 

명령어를 뜻하는데 **컨테이너 시작 시 실행될 명령** 정의

NGINX 이미지는 '`nginx`' 명령어를 입력하는 것을 확인할 수 있음

<br/>

```dockerfile
#
# NOTE: THIS DOCKERFILE IS GENERATED VIA "update.sh"
#
# PLEASE DO NOT EDIT IT DIRECTLY.
#
FROM debian:bookworm-slim

LABEL maintainer="NGINX Docker Maintainers <docker-maint@nginx.com>"

ENV NGINX_VERSION   1.25.4
...
EXPOSE 80

STOPSIGNAL SIGQUIT

CMD ["nginx"]
```

<br/>

MySQL 이미지는 '`mysqld`' 명령


일반 Ubuntu 운영 체제로 컨테이너를 실행하는 파일을 보면 기본이 '`bash`' 명령어인데, 
이는 Web Server 나 Database 처럼 프로세스가 아님

_(터미널의 입력을 받아들이는 Shell 일 뿐, 터미널을 못 찾으면 종료)_


Ubuntu 컨테이너를 실행했을 때, Docker는 Ubuntu 이미지로부터 컨테이너를 만들어 bash 프로그램을 시작했지만,
기본적으로 Docker는 실행 중에 컨테이너를 터미널에 연결하지 않음

bash 프로그램은 Terminal을 찾지 못해 종료

컨테이너가 생성된 뒤, 이 과정이 시작되기 때문에 컨테이너도 종료


<br/>

```dockerfile
#
# Ubuntu Dockerfile
#
# https://github.com/dockerfile/ubuntu
#

# Pull base image.
FROM ubuntu:14.04

# Install.
RUN \
  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list && \
  apt-get update && \
  apt-get -y upgrade && \
  apt-get install -y build-essential && \
  apt-get install -y software-properties-common && \
  apt-get install -y byobu curl git htop man unzip vim wget && \
  rm -rf /var/lib/apt/lists/*

# Add files.
ADD root/.bashrc /root/.bashrc
ADD root/.gitconfig /root/.gitconfig
ADD root/.scripts /root/.scripts

# Set environment variables.
ENV HOME /root

# Define working directory.
WORKDIR /root

# Define default command.
CMD ["bash"]
```

<br/><br/>

### 컨테이너 시작 명령 지정 방법


**Option1. Docker 실행 명령에 명령 옵션 추가**

그럼 이미지에 지정된 기본 명령을 재정의하게 됨

<br/>

```Bash
$ docker run ubuntu [COMMAND]
# ex. docker run ubuntu sleep 5
```

<br/>

위의 예시는 기본적으로 Docker를 실행하는 Ubuntu 명령을 실행하고, `sleep 5` 명령은 추가 옵션으로 실행됨

컨테이너가 실행 시 sleep 프로세스를 5초 실행 후 종료 (`Exit`)

<br/>

**Option2. Docker 실행 명령에 명령 옵션 추가**

컨테이너 시작 시, `sleep` 명령을 항상 실행하는 이미지를 원한다면,
기본 Ubuntu 이미지로부터 새로운 이미지를 생성하고 새 명령을 명시

<br/>

```dockerfile
FROM ubuntu

CMD sleep 5
```

<br/>

**명령을 지정하는 방법 두 가지**

**1. Shell 형식**

```dockerfile
CMD command param 2
# ex. CMD sleep 5
```

<br/>

**2. JSON 배열 포맷**

```dockerfile
CMD ["command", "param 2"]
# ex. CMD ["sleep", "5"]
```

**⚠️ JSON 배열 포맷의 첫 번째 요소는 '실행 가능한 명령어'여야 함**
_ex. `CMD ["sleep 5"]` ❌_

<br/>

새로운 이미지 `ubuntu-sleeper` 를 `docker build` 명령을 이용해 생성

<br/>

```Bash
$ docker build -t ubuntu-sleeper .
$ docker run ubuntu-sleeper
```

그럼 5초 동안의 `sleep` 상태 이후 종료됨

<br/>

만약, 외부에서 입력받은 `sleep` 시간을 수정하고 싶다면, `ENTRYPOINT`를 사용할 수 있음

<br/>

```Bash
FROM ubuntu

ENTRYPOINT ["sleep"]
```

<br/>

`ENTRYPOINT` 지시자는 `CMD` 지시자랑 비슷하게 컨테이너가 시작될 때 실행될 프로그램을 지정할 수 있음

```Bash
$ docker run ubuntu-sleeper 10
# >> Command at Startup: sleep 10
```

**`CMD` 지시자는 초기 명령을 완전 바꾸는데, `ENTRYPOINT` 는 기존 명령어에 매개 변수를 추가하는 형태**

만약, 추가 매개변수를 입력하지 않으면, `sleep` 명령어의 매개 변수가 누락되었다는 오류가 발생하는데,

```Bash
$ docker run ubuntu-sleeper
# ERROR >> sleep: missing operand 
```

<br/>

이럴 때 `CMD`와 `ENTRYPOINT`를 같이 사용할 수 있음

```Bash
FROM ubuntu

ENTRYPOINT ["sleep"]

CMD ["5"]
```

그럼 궁극적으로 컨테이너 실행 시 `sleep 5` 를 실행하게 됨

**⚠️ `CMD`와 `ENTRYPOINT`를 같이 사용하려면 반드시 JSON 형식으로 작성해야 함**

<br/>

```Bash
$ docker run ubuntu-sleeper 10
# >> Command at Startup: sleep 10
```

혹은 위와 같이 `ENTRYPOINT` 를 입력하면 `sleep 10` 를 실행할 수 있음

<br/>

런타임 동안 `ENTRYPOINT`을 수정하고 싶다면?

가령, sleep 에서 sleep2.0로 넘어가고 싶다면,

그 경우 Docker 실행 명령에서 `--entrypoint` 옵션을 사용해 재정의할 수 있음

```Bash
$ docker run --entrypoint sleep2.0 ubuntu-sleeper 10
# >> Command at Startup: sleep2.0 10
```

그럼, `sleep2.0 10` 명령어를 실행할 수 있음

<br/><br/>

### Kubernetes

위의 내용을 Pod로 생성해보자

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: ubuntu-sleeper-pod
spec:
  containers:
    - name: ubuntu-sleeper
      image: ubuntu-sleeper
```

Pod는 위에서 생성한 것 처럼 5초간 sleep 상태를 실행하고 종료될 것

`ENTRYPOINT`는 시작 시 실행되는 명령, `CMD` 는 기본으로 전달되는 매개 변수

Kubernetes에서는 `CMD` 는 `args`, `ENTRYPOINT` 는 `command` 로 오버라이드

<br/>

#### `args` (=`CMD`)

만약, 컨테이너를 10초 동안 sleep 시키고 싶다면, `args` 속성을 사용할 수 있음

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: ubuntu-sleeper-pod
spec:
  containers:
    - name: ubuntu-sleeper
      image: ubuntu-sleeper
      args: ["10"]
```

Pod 정의 파일의 args 옵션은, docker 파일의 cmd 지시문을 Override

<br/>

#### `command` (=`ENTRYPOINT`)


```Bash
$ docker run --name ubuntu-sleeper \
     --entrypoint sleep2.0 \
     ubuntu-sleeper 10
```

Docker에서 사용하던 `ENTRYPOINT`를 사용하고 싶다면, `command` 속성에 명시 

`ENTRYPOINT` 지시와 일치


⚠️ `command`는 `CMD`가 아님을 주의


#### args & command

Args 나 Command 속성을 입력하는 형식은 두 가지

```yaml
...
spec:
  containers:
    - name: ubuntu-sleeper
      image: ubuntu-sleeper
      command: ["sleep", "10"]
```

단, 모든 명령어는 String 값으로 입력해야 함 

```yaml
...
spec:
  containers:
  - name: ubuntu-sleeper
    image: ubuntu-sleeper
    command:
    - "sleep"
    - "10"
```

---

**강제 업데이트 복습**

```Bash
kubectl replace --force -f /tmp/kubectl-edit-241231451342.yaml
```

```Bash
kubectl run webapp-green --image=kodekloud/webapp-color -- --color green
```