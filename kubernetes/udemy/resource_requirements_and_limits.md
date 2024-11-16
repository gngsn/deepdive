# Resource Requirements and Limits


모든 Pod를 운영하려면 자원이 필요하며,
모든 노드엔 사용 가능한 CPU와 메모리 리소스를 가지고 있음


쿠버네티스 스케줄러는 Pod에 요구되는 리소스 양을 고려하여, 
노드에서 사용 가능한 리소스에 따라 Pod가 배치될 노드를 결정

그리고 포드를 설치할 최적의 노드를 결정

노드에 충분한 리소스가 없으면, 스케줄러는 해당 노드에 Pod를 두는 걸 피하고 리소스가 충분히 사용 가능한 곳에 배치

모든 노드에 사용 가능한 리소스가 충분하지 않으면 스케줄러가 Pod의 스케줄링을 보류

Pod은 현재 Pending 상태 유지

kubectl Events를 보면 불충분한 CPU가 있는 걸 볼 수 있음

<br/><img src="./img/resource_requirements_and_limits_img1.png" width="80%" /><br/>

**Example.**

3개의 노드를 가진 쿠버네티스 클러스터를 가정해보자

```
🔋 CPU  /  🛢 Memory

Pod 1  🔋🔋🛢
Pod 2  🔋🛢🛢🛢
Pod 3  🔋🔋🔋🛢🛢🛢

Node1  [🔋🔋🔋🔋🔋🔋🛢🛢🛢🛢🛢🛢]
Node2  [🔋🔋🔋🔋🔋🔋🛢🛢🛢🛢🛢🛢]
Node3  [🔋🔋🔋🔋🔋🔋🛢🛢🛢🛢🛢🛢]
```

Pod 1은 CPU 2개와 메모리 1개 필요

스케줄러는 사용 가능한 리소스가 충분하기 때문에 2번 노드에 배치

<br/>

Pod 생성 시, 필요한 CPU와 메모리의 양을 지정할 수 있음

<br/>

_pod-definition.yml_

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: simple-webapp-color
  labels:
    name: simple-webapp-color
spec:
  containers:
  - name: simple-webapp-color
    image: simple-webapp-color
    port: 
      - containerPort: 8080
    resources:
      requests:
        memory: "4Gi"
        cpu: 2
```

예를 들어, 컨테이너에 대한 리소스 요청으로 CPU 2개와 4 Gi 메모리로 설정할 수 있음

스케줄러가 Pod를 노드에 놓으려 할 때 이 수치를 감안하여 사용 가능한 충분한 양의 리소스가 있는 노드에 배치

Pod는 사용 가능한 양의 자원을 보장받음 

---

`0.1`와 같이 수치를 지정해서 CPU 설정 가능한데, 참고로 동일한 수치로써 Milli를 의미하는 m과 함께 100m로 표현될 수 있음

`1m`가 최소

CPU 1 카운트 = AWS로 vCPU 한 대 = GCP나 Azure의 코어 하나  = 다른 시스템의 Hyperthread 하나


<br/><br/>

### Resource - Memory

작성할 수 있는 값:

```
1 G (Gigabyte) = 1,000,000,000 bytes
1 M (Megabyte) = 1,000,000 bytes
1 K (Kilobyte) = 1,000 bytes

1 Gi (Gigibyte) = 1,073,741,824 bytes
1 Mi (Mebibyte) = 1,048,576 bytes
1 Ki (Kibibyte) = 1,024 bytes
```

**노드에서 실행되는 컨테이너**

기본적으로, 컨테이너는 노드 리소스 사용에 한계가 없음

하지만, Pod에 리소스 제한 지정 가능

예를 들어 컨테이너에 vCPU를 하나로 제한하면, 컨테이너는 해당 노드에서 vCPU 한 대만 사용하도록 제한되며,
메모리도 동일하게 컨테이너에 512 mebibyte로 지정 가능

Resource Limits은 Pod 파일에서 지정 가능

_pod-definition.yml_

<br/><pre><code lang="yaml">
apiVersion: v1
kind: Pod
metadata:
  name: simple-webapp-color
  labels:
    name: simple-webapp-color
spec:
  containers:
  - name: simple-webapp-color
    image: simple-webapp-color
    port: 
      - containerPort: 8080
    resources:
      requests:
        memory: "4Gi"
        cpu: 2
      <b>limits:
        memory: "2Gi"
        cpu: 2</b>
</code></pre>

<br/>

메모리와 CPU에 대한 새로운 한도를 지정 후,
Pod가 만들어질 때 쿠버네티스는 컨테이너에 제한을 설정

```
⚠️ 이 때, Pod 내 **모든 컨테이너마다** 제한과 요청이 설정된 것 명심
컨테이너가 여러 개면 각 컨테이너는 개별적으로 요청이나 제한을 설정할 수 있음
```

<br/>

Pod가 지정된 제한을 초과하면?

**CPU**는 시스템이 CPU를 조절해 지정된 한도를 넘지 않음. 즉, 컨테이너가 한계점 이상으로 CPU 리소스를 쓸 수 없음

**Memory는 다름**

컨테이너는 한도보다 많은 메모리 리소스를 쓸 가능성이 있음

무리가 계속해서 한계보다 많은 메모리를 소모하면 그 무리는 종료 (terminated)

그래서, 로그나 실행하면 설명된 명령의 출력 결과에서 Pod가 OOM 에러로 종료되는 걸 확인할 수 있음

<br/>

#### Default Behavior

```
⭐️ 기본적으로 쿠버네테스는 CPU나 메모리 요청, 한계점이 없음
즉, 어떤 Pod든 어떤 노드에서 필요한 만큼의 자원을 소비할 수 있고,
해당 노드에서 실행 중인 다른 Pod나 프로세스를 질식(suffocate)시킬 수 있음
```

<br/><br/>

### Behavior - CPU

```
CPU: 🔋🔋🔋🔋🔋🔋
```

클러스터에서 CPU 리소스를 위해 경쟁하는 두 개의 포드가 있을 때,
어떻게 설정하는 게 가장 이상적일까?

<br/>

**CASE 1. NO REQUESTS / NO LIMITS**

리소스 한계점이 없으면, 
Pod 하나가 노드의 모든 CPU 리소스를 소비하고 두 번째 리소스 요구도 막을 수 있음

```
CPU: X

Limits = X
Requests = X
1️⃣: 🔋🔋🔋🔋🔋🔋
2️⃣: X
```

→ 이상적인 상황 X

<br/>

**CASE 2. NO REQUESTS / LIMITS**

구체적인 요청은 없지만 한계가 구체적으로 명시된 경우,
이 경우 쿠버네티스는 자동으로 요청을 한계와 동일하게 설정

```
CPU: X

Limits = 3 vCPU
Requests = limits
1️⃣: 🔋🔋🔋
2️⃣: 🔋🔋🔋
```

예를 들어, limit을 3을 지정하면 request를 3개로 추정함

Pod마다 vCPU가 3개 이상 보장

<br/>

**CASE 3. REQUESTS / LIMITS**

구체적인 요청과 한계가 모두 구체적으로 명시된 경우,
각 Pod에는 vCPU 하나인 CPU 요청이 보장되고, vCPU 3 까지만 사용 가능


```
CPU: 🔋🔋

Limits = 3 vCPU
Requests = 1 vCPU
1️⃣: 🔋🔋🔋
2️⃣: 🔋
```

가장 이상적으로 보이지만, 비효율이 존재

그런데, Pod 1은 CPU 사이클이 더 필요하고,
Pod 2는 CPU 사이클을 그만큼 소비하지 않는다면, CPU 1의 제한을 두고 싶지 않을 수 있음

그래서 1번 포드는 CPU 사이클을 사용할 수 있게 하고 싶다면, 
CPU 사이클의 리소스를 불필요하게 제한하지 않는게 나음

<br/>

**CASE 4. REQUESTS / NO LIMITS**

제한 없이 요청 설정

요청이 정해져 있어서, 각 구역마다 vCPU를 사용을 보장받음

```
CPU: 🔋

Limits = X
Requests = 1 vCPU
1️⃣: 🔋🔋🔋🔋
2️⃣: 🔋
```

사용 가능 시에는 한계가 설정되지 않기 때문에 Pod마다 CPU 사이클을 최대한 많이 사용할 수 있음

또, 어떤 시점에서든 Pod 2의 CPU 사용이 요구되면 요청된 CPU 사이클이 보장됨

→ 가장 이상적인 환경

<br/>

요청 설정이나 다른 포드에 대한 제한이 없는 포드가 있다면,
어떤 포드든 노드에서 사용 가능한 메모리와 CPU를 모두 소모하고 정의되지 않은 포드는 제한할 수 있음

Pod 다른 컨테이너마다 요구나 제한을 정할 수 있음

<br/><br/>

### Behavior - Memory

```
Memory: 🛢️🛢️🛢️🛢️🛢️🛢️
```

메모리도 CPU와 동일한데, 클러스터에서 메모리 리소스를 두고 두 포드가 경쟁하는 것을 예시로 들어보자

<br/>

**CASE 1. NO REQUESTS / NO LIMITS**

```
Memory: X

Limits = X
Requests = X
1️⃣: 🛢️🛢️🛢️🛢️🛢️🛢️
2️⃣: X
```

Limit이 없어 Pod 하나가 노드의 메모리 리소스 전체를 소비할 수 있고,
다른 Pod가 필요한 리소스 사용을 막을 수 있음

<br/>

**CASE 2. NO REQUESTS / LIMITS**

Request가 구체적으로 명시되지 않고 한계는 명시되어 있는 경우,
컨테이너는 자동으로 요청을 한계로 설정

```
Memory: X

Limits = 3 Gi
Requests = Limits
1️⃣: 🛢️🛢️🛢️
2️⃣: 🛢️🛢️🛢️
```

위 예시에서는, Request과 Limit는 둘 다 `3Gi`로 설정 됨

<br/>

**CASE 3. REQUESTS / LIMITS**

Request가 구체적으로 명시되지 않고 한계는 명시되어 있는 경우,
컨테이너는 자동으로 요청을 한계로 설정

```
Memory: 🛢️🛢️

Limits = 3 Gi
Requests = 1 Gi
1️⃣: 🛢️🛢️🛢️🛢️
2️⃣: 🛢️
```

Request과 Limit을 설정한 경우, 각 포드마다 메모리가 보장됨

<br/>

**CASE 4. REQUESTS / LIMITS**

Request을 정하되 Limit은 지정하지 않음

```
Memory: 🛢️🛢️

Limits = 3 Gi
Requests = 1 Gi
1️⃣: 🛢️🛢️🛢️🛢️
2️⃣: 🛢️
```


이 경우, 각 Pod에 정해진 Request 만큼의 Gi를 보장

하지만, 한계가 설정되지 않기 때문에 어떤 포드든 사용 가능한 메모리를 소모할 수 있음

CPU와는 달리 메모리를 조절할 수 없기 때문에 Pod 2가 제공된 메모리 보다 더 요청하면 죽음

Memory를 되찾을 유일한 방법은 Pod를 죽이고 Pod에 사용한 Memory를 해제(free)시켜주는 것

---

기본적으로 Pod에 구성된 리소스 요청이나 한계가 없다고 했는데,
그렇다면 Pod 내 Container에 Default로 설정할 Resource Limit을 지정할 수 있을까

→ LimitRange 정의를 설정해서 가능


Pod 정의 파일에 Resource 나 특정 Limit 없이 생성된 컨테이너에 한해, Pod 내 모든 컨테이너에 적용됨

- Namespace 레벨에서 생성됨
- LimitRange는 하나의 쿠버네티스 오브젝트


<br/>

_limit-range-cpu.yaml_

```yaml
apiVersion: v1
kind: LimitRange
metadata:
  name: cpu-resource-constraint
spec:
  limits:
    - default:           # limit
        cpu: 500m
      defaultRequest:    # request
        cpu: 500m
      max:               # limit
        cpu: "1"
      min:               # request
        cpu: 500m
      type: Container
```
_👆🏻 예제 값, 추천 X.
각자의 앱에 가장 적합한 걸 설정해야함_

<br/>

`default` Limit 을 500m으로 지정하고, `defaultRequest`도 동일하게 500m로 지정

`max`는 Pod 내 Container에 지정할 수 있는 Maximum limit, 
`min` 은 Pod 내 Container가 만들 수 있는 Minimum request 의미

✔️ **적용한 Pod에 명시되는 정보**

```Bash
Name:             myapp-pod
Namespace:        default
Priority:         0
Service Account:  default
Node:             kind-control-plane/172.21.0.2
Start Time:       Sat, 30 Mar 2024 17:04:30 +0900
Labels:           app=myapp
                  type=front-end
Annotations:      kubernetes.io/limit-ranger: LimitRanger plugin set: cpu request for container nginx-container; cpu limit for container nginx-container
Status:           Running
```

<br/>

_limit-range-memory.yaml_

```yaml
apiVersion: v1
kind: LimitRange
metadata:
  name: memory-resource-constraint
spec:
  limits:
  - default:
      memory: 1Gi
    defaultRequest:
      memory: 1Gi
    max:
      memory: 1Gi
    min:
      memory: 500Mi
    type: Container
```


메모리도 동일

⭐️위 Limitation은 Pod가 생성될 때 강제로 설정됨 
만약, `LimitRange`를 생성하거나 수정한다면,  기존 Pod에는 적용되지 않고 새로 생성되는 Pod에 한해서만 적용

<br/>

#### 클러스터 Limitation

쿠버네티스 클러스터에 배포된 앱이 사용할 수 있는 전체 리소스를 제한할 방법?

Resource Quota는 네임스페이스 레벨에서, Request와 Limit의 Hard Limit 값을 설정할 수 있음

_resource-quota.yaml_

```yaml
apiVersion: v1
kind: ResourceQuota
metadata:
  name: my-resource-quota
spec:
  hard:
    request.cpu: 4
    request.memory: 4Gi
    limit.cpu: 10
    limit.memory: 10Gi
```


Reference: 
- [Manage Memory, CPU, and API Resources](https://kubernetes.io/docs/tasks/administer-cluster/manage-resources/)
- [LimitRange for CPU](https://kubernetes.io/docs/tasks/administer-cluster/manage-resources/cpu-default-namespace/)
- [LimitRange for Memory](https://kubernetes.io/docs/tasks/administer-cluster/manage-resources/memory-default-namespace/)


---

**⚠️ Caution**

`Terminated` 된 이유를 확인해보면, 현재 상태(`State`)와 마지막 상태 (`Last State`)를 확인할 수 있고,
각 `Reason` 를 통해 이유를 확인할 수 있음

```Bash
controlplane ~ ➜  k get pods
NAME     READY   STATUS             RESTARTS        AGE
rabbit   0/1     CrashLoopBackOff   5 (2m24s ago)   5m20s

controlplane ~ ✖ k describe pods elephant
Name:             elephant
Namespace:        default
  ...
    State:          Waiting
      Reason:       CrashLoopBackOff
    Last State:     Terminated
      Reason:       OOMKilled
      Exit Code:    1
      Started:      Sat, 30 Mar 2024 08:40:20 +0000
      Finished:     Sat, 30 Mar 2024 08:40:20 +0000
    Ready:          False
    Restart Count:  2
    Limits:
      memory:  10Mi
    Requests:
      memory:     5Mi
    ...
```
