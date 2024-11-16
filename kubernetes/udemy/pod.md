# Lecture 22. Pods with YAML

YAML in Kubernetes

```yaml
# pod-definiation.yml
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app: myapp
    type: front-end
spec:
  containers:
        - name: nginx-container
        image: nginx
```

Kubernetes 정의 파일은 최상위에 항상  API version, kind, metadata, spec 을 가지고 있습니다.

이 네 개의 속성은 필수 속성이라 반드시 정의해주어야 합니다. 하나씩 살펴보면 다음과 같습니다.

1. API version: 객체를 생성하기 위해 사용하는 Kubernetes API 버전입니다. 어떤 객체를 생성할지에 따라 적합한 API 버전이 필요합니다. 현재, V1 을 사용하며, 이 밖에 `apps/V1beta`, `extensions/V1beta` 등을 사용할 수 있습니다. 
2. kind: 어떤 타입의 객체를 생성할지 명시합니다. 현재 Pod 으로 설정하며, 이 밖에도 아래와 같은 값들을 입력할 수 있습니다. (kind 이름은 case sensitive source ~/.zshrc, 즉 대소문자를 구분합니다.)

    | Kind | Version |
    | --- | --- |
    | Pod | v1 |
    | Service | v1 |
    | ReplicaSet | apps/v1 |
    | Deployment | apps/v1 |

3. metadata: name, labels 등등 객체의 데이터를 명시합니다. metadata 하위에는 key와 value의 형식을 갖는 dictionary 입니다. 위의 예시에는, name과 labels이 정의되어 있는 것을 확인할 수 있습니다. yaml 파일 특성 상 들여쓰기(인덴트) 에 주의해야 합니다. 
    
    어떤 필드가 어떤 파라미터를 갖는지 파악하는 것도 중요한데요. 참고로, `labels` 필드는 다른 필드들과는 다르게, 어떤 key와 value를 추가해도 상관없습니다.
    
4. spec: spec은 해당 객체의 구성을 정의하는 dictionary 입니다. 하위에 `containers` List/Array 필드를 정의할 수 있습니다. 
    
    dash `-` 는 리스트의 가장 첫 요소를 표현하는 방식입니다. 
    

위 처럼 파일을 작성하고 나면 아래와 같은 명령어로 정의된 내용에 맞게 Pod 객체가 생성됩니다.

```yaml
❯ **kubectl create -f pod-definiation.yml**
pod/myapp-pod created
```

### Commands

```bash
❯ kubectl get pods
NAME        READY   STATUS    RESTARTS   AGE
myapp-pod   1/1     Running   0          13s

❯ kubectl describe pod myapp-pod
Name:             myapp-pod
Namespace:        default
Priority:         0
Service Account:  default
Node:             kind-control-plane/172.21.0.2
Start Time:       Fri, 01 Mar 2024 23:37:54 +0900
Labels:           app=myapp
                  type=front-end
Annotations:      <none>
Status:           Running
IP:               10.244.0.99
IPs:
  IP:  10.244.0.99
Containers:
  nginx-container:
    Container ID:   containerd://274fcd34a8f7aa938019f7a6bd31ff2083bace14de06d87dc6b1189da5911c2e
    Image:          nginx
    Image ID:       docker.io/library/nginx@sha256:c26ae7472d624ba1fafd296e73cecc4f93f853088e6a9c13c0d52f6ca5865107
    Port:           <none>
    Host Port:      <none>
    State:          Running
      Started:      Fri, 01 Mar 2024 23:37:56 +0900
    Ready:          True
    Restart Count:  0
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-4tqh6 (ro)
Conditions:
  Type                        Status
  PodReadyToStartContainers   True 
  Initialized                 True 
  Ready                       True 
  ContainersReady             True 
  PodScheduled                True 
Volumes:
  kube-api-access-4tqh6:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  22s   default-scheduler  Successfully assigned default/myapp-pod to kind-control-plane
  Normal  Pulling    22s   kubelet            Pulling image "nginx"
  Normal  Pulled     20s   kubelet            Successfully pulled image "nginx" in 1.695s (1.695s including waiting)
  Normal  Created    20s   kubelet            Created container nginx-container
  Normal  Started    20s   kubelet            Started container nginx-container
```