# Init Containers

[🔗 Kubernetes - Init Container](https://kubernetes.io/docs/concepts/workloads/pods/init-containers/) 

Init Container란 Pod의 Application 컨테이너들 이전에 실행되는 컨테이너
Pod 초기화 단계에 완료

init 컨테이너들은 앱 이미지에 존재하지 않는 유틸리티들 또는 셋업 스크립트들을 포함할 수 있음
Pod 정의 파일에서 .spec.containersp[] 배열 하위에 다른 App Container 와 함께 컨테이너를 지정할 수 있음

Kubernetes에서 사이드카 컨테이너는 주 응용 컨테이너보다 먼저 시작하여 계속 실행되는 컨테이너

---

기본적으로 다중 컨테이너 Pod에서 각각의 Container 는 Pod이 살아 있는 동안 항상 프로세스가 지속

예를 들어, 웹 애플리케이션과 로깅 에이전트가 있는 다중 컨테이너 Pod에서, 두 컨테이너는 모두 항상 살아 있을 것으로 예상

둘 중 하나라도 실패하면, Pod는 다시 시작

하지만, 때로 컨테이너의 실행만 완료하고 실행되는 프로세스를 실행하기를 원할 수도 있음

### Examples

아래의 Pod가 처음 생성될 때 한 번만 실행하면 되는 작업에 여기서 initContainers를 사용할 수 있음

- 메인 웹 애플리케이션이 사용할 코드나 바이너리를 원격 저장소에서 가져오는 프로세스
  - ```Shell
    for i in {1..100}; do sleep 1; if nslookup myservice; then exit 0; fi; done; exit 1
    ```
- 외부 서비스나 데이터베이스가 작동할 때까지 기다리기 위해 통신 연결 시까지 대기하는 프로세스
  - ```Shell
    curl -X POST http://$MANAGEMENT_SERVICE_HOST:$MANAGEMENT_SERVICE_PORT/register -d 'instance=$(<POD_NAME>)&ip=$(<POD_IP>)'
    ```
- App 컨테이너가 초기화 되기 까지 기다리는 작업 
  - ```Shell
    sleep 60
    ```
- Git 레포지토리를 클론해 Volume에 저장 
- 설정 파일에 값을 넣고 Template 툴를 실행하여 메인 앱 컨테이너에 대한 설정 파일을 동적으로 생성
  - 가령, POD_IP 값을 설정에 넣고 Jinja 를 이용하여 메인 앱 설정 파일을 생성


---

### Init containers in use

initContainers는 `initContainers` 섹션에 지정된 것을 제외하고는 다른 컨테이너와 동일하게 구성됨

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app.kubernetes.io/name: MyApp
spec:
  containers:
  - name: myapp-container
    image: busybox:1.28
    command: ['sh', '-c', 'echo The app is running! && sleep 3600']
  initContainers:
  - name: init-myservice
    image: busybox:1.28
    command: ['sh', '-c', "until nslookup myservice.$(cat /var/run/secrets/kubernetes.io/serviceaccount/namespace).svc.cluster.local; do echo waiting for myservice; sleep 2; done"]
  - name: init-mydb
    image: busybox:1.28
    command: ['sh', '-c', "until nslookup mydb.$(cat /var/run/secrets/kubernetes.io/serviceaccount/namespace).svc.cluster.local; do echo waiting for mydb; sleep 2; done"]
```

다중 컨테이너 포드의 경우와 같이 여러 개의 initContainers를 구성할 수도 있음

이 경우 각 initContainers는 순차적으로 한 번에 하나씩 실행

Pod가 처음 생성되면 initContainer가 실행되는데,
응용 프로그램을 호스팅하는 실제 컨테이너가 시작되기 전까지 initContainer의 프로세스 실행 완료됨

initContainers 중 하나라도 완료되지 않으면 Kubernetes는 InitContainers가 성공할 때까지 Pod를 반복적으로 재시작함


---

Init Error: container 로그 확인하려면 `kubectl logs <<pods>> -c <<container>>`
