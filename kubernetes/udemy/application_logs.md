## Application Logs

### Logs - Docker

Docker의 로깅부터 살펴보자

가령, 아래와 같은 docker container를 실행

```Bash
docker run -d postgres
```

그리고 `docker logs`를 로그 확인

```Bash
docker run -d postgres
2024-03-14 14:43:27.963 UTC [963] DETAIL:  trigger notify_counters on table activity depends on function tg_notify_counters()
2024-03-14 14:43:27.963 UTC [963] HINT:  Use DROP ... CASCADE to drop the dependent objects too.
2024-03-14 14:43:27.963 UTC [963] STATEMENT:  drop function public.tg_notify_counters()
2024-03-14 14:43:44.381 UTC [960] ERROR:  cannot drop function tg_notify_counters() because other objects depend on it
2024-03-14 14:43:44.381 UTC [960] DETAIL:  trigger notify_counters on table activity depends on function tg_notify_counters()
2024-03-14 14:43:44.381 UTC [960] HINT:  Use DROP ... CASCADE to drop the dependent objects too.
2024-03-14 14:43:44.381 UTC [960] STATEMENT:  drop function tg_notify_counters()
2024-03-14 14:46:18.705 UTC [696] ERROR:  permission denied for table activity
2024-03-14 14:46:18.705 UTC [696] STATEMENT:  insert into public.activity(message_id, action) values (33, 'rt'),
```

`-f` option을 사용하면 로그를 라이브로 확인 가능

로그 이벤트는 표준 출력을 통해 스트리밍됨


### Logs - Kubernetes 

쿠버네티스에서 Pod definition 파일을 통해 Docker 이미지의 Pod를 생성

Pod 실행 후, `kubectl logs` 명령어를 통해 로그를 볼 수 있음

```Bash
❯ kubectl logs -f monitoring-daemon-vtnsp                                                                                                                            ─╯
/docker-entrypoint.sh: Configuration complete; ready for start up
2024/03/30 11:45:13 [notice] 1#1: using the "epoll" event method
2024/03/30 11:45:13 [notice] 1#1: nginx/1.25.4
2024/03/30 11:45:13 [notice] 1#1: built by gcc 12.2.0 (Debian 12.2.0-14)
2024/03/30 11:45:13 [notice] 1#1: OS: Linux 5.15.49-linuxkit-pr
2024/03/30 11:45:13 [notice] 1#1: getrlimit(RLIMIT_NOFILE): 1048576:1048576
2024/03/30 11:45:13 [notice] 1#1: start worker processes
2024/03/30 11:45:13 [notice] 1#1: start worker process 32
2024/03/30 11:45:13 [notice] 1#1: start worker process 33
2024/03/30 11:45:13 [notice] 1#1: start worker process 34
2024/03/30 11:45:13 [notice] 1#1: start worker process 35
2024/03/30 11:45:13 [notice] 1#1: start worker process 3
```

`-f` option을 사용하면 로그를 라이브로 확인 가능

Pod는 여러 개의 도커 컨테이너를 포함할 수 있는데, 그렇다면 Pod logging에는 어떤 내용이 담길까?

Pod 내 여러 개의 컨테이너가 있다면, Pod 이름 다음 명령에 컨테이너 이름을 명시적으로 입력해야 함

_입력하지 않으면 이름을 명시해달라는 오류를 발생_


가령, 아래와 같은 Pod 정의 파일이 있다면,

_event-simulator.yaml_

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: event-simulator-pod
spec:
  containers:
  - name: event-simulator
    image: kodekloud/event-simulator
  - name: image-processor
    image: some-image-processor
```

아래와 같이 event-simulator Container 로그를 확인할 수 있음

```Bash
kubectl logs -f event-simulator-pod event-simulator
```

