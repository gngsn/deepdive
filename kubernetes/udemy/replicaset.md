# ReplicaSets

<br/>

![img1](./img/replicaset_img1.png)

만약 어떠한 이유로 애플리케이션이나 파드가 동작하지 않을 경우에 어떻게 해야 할까요? 유저들이 더 이상 애플리케이션에 접근하지 못할 경우에요. 사용자의 접근 실패를 보호하기 위해서, 항상 둘 이상의 인스턴스나 파드를 실행하고 있어야 합니다. 그럼, 하나가 실패해도 다른 하나는 실행하고 있으니, 접근이 가능한 상태가 되죠.

Replication Controller는 쿠버네티스 클러스터 내에 한 파드에 대해 여러 인스턴스를 실행하도록 합니다. 이로써, 가용성 (High Availability)을 제공할 수 있습니다.

그렇다고 Replication Controller를 사용할 때 매번 둘 이상의 인스턴스를 가져야 하는 건 아닌데요. Replication Controller는 하나의 인스턴스를 가질 때, 하나가 실패하면 자동으로 다시 새로운 Pod를 생성해줍니다.

#2. Replication Controller는 또한, 부하를 나누기 위한 새로운 파드들을 생성할 때도 유용합니다.

가령, 기존 두 개의 파드들이 사용자들의 트래픽, 즉 부하를 받고 있었다고 가정할 때, 부하가 늘어났을 때를 생각해봅시다. 많은 유저들이 접근을 시도하고 더 많은 파드에 부하를 분산시켜야 할 필요를 느낄 수 있습니다. Replication Controller는 이 때, 부하를 분산시키도록 추가적인 파드를 생성해줍니다. 더 나아가, 여러 노드에 걸쳐 파드를 분산시킬 수도 있습니다.

![img2](./img/replicaset_img2.png)

여기서 짚고 넘어가야 할 내용이 있는데요. Replication Controller와 Replica Set 의 차이입니다.  둘은 동일한 목적을 가지고 있지만, 동일하지 않습니다.

Replication Controller는 조금 더 오래부터 사용되어, 이제는 Replica Set 으로 인해 대체되는 기술입니다. Replica Set은 사용을 권장하는 새로운 레플리케이션을 설정하는 방식입니다. 하지만 여전히 두 기술 모두 적용가능합니다.

Replication Controller 생성

```yaml
apiVersion: v1
kind: ReplicationController
metadata:
  name: myapp-rc
  labels:
    app: myapp
    type: front-end
spec:
  template:
    metadata:
      name: myapp-pod
      labels:
        app: myapp
        type: front-app
    spec:
      containers:
      - name: nginx-container
        image: nginx
  replicas: 3
```

```yaml
❯ kubectl create -f rc-definition.yml
replicationcontroller/myapp-rc created

❯ kubectl get replicationcontroller
NAME       DESIRED   CURRENT   READY   AGE
myapp-rc   3         3         3       2m15s

❯ kubectl get pods
NAME             READY   STATUS    RESTARTS   AGE
myapp-pod        1/1     Running   0          22h
myapp-rc-6r45q   1/1     Running   0          2m26s
myapp-rc-tjj88   1/1     Running   0          2m26s
myapp-rc-zm5bd   1/1     Running   0          2m26s
```

`selector` 필드는 Replica Set 하위에 어떤 Pods를 구성할지 식별할 때를 위해 필요합니다.

그런데, 만약 템플릿 내에 Pod 정의 파일 내용 자체를 제공한다면, 왜 어떤 Pod가 하위에 구성되어야 하는지를 명시해야 할까요?

그 이유는 Replica Set은 Replica Set을 생성할 때 만들어지는 Pod 이외의 Pod도 관리할 수 있기 때문입니다.

가령, `selector` 필드에 명시된 레이블과 일치하는 복제본 세트를 만들기 전에 포드가 만들어졌다고 치죠. 복제본을 만들 때 복제본 세트도 그 포드를 고려할 거예요. 다음 슬라이드에서 더 자세히 얘기하겠지만 그 전에 선택기는 복제 컨트롤러와 복제본 세트의 가장 큰 차이점 중 하나예요.

```yaml
# replicaset-definition.yml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: myapp-replicaset
  labels:
    app: myapp
    type: front-end
spec:
  template:
    metadata:
      name: myapp-pod
      labels:
        app: myapp
        type: front-end
    spec:
      containers:
      - name: nginx-container
        image: nginx
  replicas: 3
  selector:
    matchLabels:
      type: front-end
```

```yaml
❯ kubectl create -f replicaset-definition.yml
replicaset.apps/myapp-replicaset created

❯ kubectl get replicaset
NAME               DESIRED   CURRENT   READY   AGE
myapp-replicaset   3         3         3       8s
```

```yaml
❯ kubectl get pods                                                                  ─╯
NAME                     READY   STATUS    RESTARTS   AGE
myapp-pod                1/1     Running   0          24h
myapp-rc-6r45q           1/1     Running   0          122m
myapp-rc-tjj88           1/1     Running   0          122m
myapp-rc-zm5bd           1/1     Running   0          122m
**myapp-replicaset-jtpdv   1/1     Running   0          2m19s
myapp-replicaset-vmtz6   1/1     Running   0          2m19s**
```