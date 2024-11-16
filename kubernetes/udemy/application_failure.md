# Application Failure

Web Application 테스트를 한다고 가정

Database 부터 Web Application 까지 접근 가능

가령, 사용자가 Web Application 에 접근하지 못했을 때 어떻게 확인할 수 있을지 확인

<br/>

**1. User**
- `curl` 명령어를 통해 접근 가능성을 테스트

<br/>

**2. Web Service**
- `kubectl describe` 를 통해 Web Pod가 정상적으로 띄워져 있는지 확인

<table>
<tr><td>

```bash
❯ kubectl describe service web-service
Name:              web-service
Namespace:         default
Annotations:       <none>
Selector:          name=webapp-mysql
Type:              NodePort
IP:                10.96.0.1
Port:              https  8080/TCP
TargetPort:        8080/TCP
Endpoints:         172.21.0.2:8080
Session Affinity:  None
Events:            <none>
```

</td><td>

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: webapp-mysql
labels:
    app: example-app
    name: webapp-mysql
spec:
  containers:
  - name: webapp-mysql
    image: simple-webapp-mysql
    ports:
    - containerPort: 8080
```

</td></tr>
</table>

서비스에서 구성된 `selector`가 `pod` 정의 파일에 명시된 것과 일치한지 확인

<br/>

**3. Web Application**

Pod 자체를 확인 → **Status 확인** 

- Pod의 **상태**와 **재시작 횟수**를 보면 앱이 작동 중인지 재시작되는지 알 수 있음
- `kubectl logs` 로 애플리케이션 로그를 확인

컨테이너가 제대로 동작하지 않아서 포드가 재시동한다면,
지난 실패 기록이 날라가서 원인을 보기 힘들 수도 있음.

이럴 땐, `-f` 옵션을 통해 로그를 보면서 실패하는 상황을 지켜보거나, 
`--previous` 옵션을 통해 이전 Pod의 로그를 확인할 수 있음

<br/>

**4. DB Service**

위와 동일하게 DB 서비스 상태를 체크

<br/>

**5. DB**

DB Pod 자체 체크 - DB Pod log를 확인하여 오류 있는지 확인

