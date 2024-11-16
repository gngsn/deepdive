# Manual Scheduling

클러스터에 스케쥴러가 없을 땐, 내장된 스케줄러에 의존하는 대신 포드를 직접 스케줄링할 수 있음

스케줄러는 백엔드에서 어떻게 작동할까?

<br/>

_pod-definition.yml_

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    name: nginx
spec:
  containers:
  - name: nginx
    image: nginx
    ports:
      - containerPort: 8080
  <b>nodeName: node02</b>
</code></pre>
<br/>

모든 Pod 객체에는 `nodeName` 필드를 가질 수 있으며, 기본값은 설정되어 있지 않음 

직접 해당 필드를 설정하진 않지만, 쿠버네티스가 자동으로 설정해줌

스케줄러는 모든 포드를 살펴보고 해당 필드가 없는 노드들을 찾음
이후, 스케줄링 알고리즘을 실행해 Pod가 배치될 적절한 Node를 찾고, 
nodeName 필드에 해당 노드를 지정함으로써 스케줄링하는데, 내부적으론 Binding Object를 생성

그래서 스케쥴러가 없다면, 단순히 `nodeName` 필드에 노드를 직접 명시해서 노드에 할당 시킬 수 있음

쿠버네테스는 한 포드의 노드 이름 속성을 수정하지 못함

`nodeName`은 생성 시에만 지정할 수 있음
이미 생성되어 있는 상태에서 노드에 할당시키고 싶다면,
`Binding` 객체를 생성해서 포드의 binding API에 POST 요청을 보냄 (스케줄러의 일을 모방)

<br/>

_pod-bind-definition.yml_

<pre><code lang="yaml">apiVersion: v1
kind: Binding
metadata:
  name: nginx
target:
  apiVersion: v1
  kind: Node
  name: <b>node02</b>
</code></pre>
<br/>

바인딩 개체에서 노드 이름을 가진 대상 노드를 지정

그럼 Binding 객체 데이터를 JSON 포맷을 Pod의 Binding API으로 POST 요청 

<br/>

```Bash
curl --header "Content-Type:application/json --request POST --data '{"apiVersion": "v1", "kind": "Binding", ...}'
http://#SERVER/api/v1/namespaces/default/pods/$PODNAME/binding/
```

<br/><br/>

**강제 수정 delete and recreate pod**

(cannot move a running pod)

```Bash
❯ kubectl replace --force -f pod-definition.yml
pod "nginx" deleted
pod/nginx replaced
```

delete 시, 오랜 시간이 걸릴 수 있는데, graceful shut down 때문

<br/>

**pod watch**

```Bash
❯ kubectl get pods --watch
```

