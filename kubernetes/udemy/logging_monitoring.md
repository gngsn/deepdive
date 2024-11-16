### Logging Monitoring


자원 소비량을 모니터하는 방식?

관찰하고자 하는 지표의 대상:

- 노드 레벨: 클러스터 내 노드의 수, 정상적인 노드 개수, 노드 성능; CPU, 메모리, 네트워크 디스크 활용도 등
- Pod 레벨: Pod 개수와 각 Pod 성능 지표 CPU와 메모리 소모량 등

메트릭을 모니터링하고 저장하고 데이터에 대한 분석을 제공할 수 있는 솔루션이 필요

쿠버네테스에는 모든 모니터링 기능을 가진 자체 솔루션은 없지만, 함께 사용 가능한 오픈 소스 솔루션은 많음 

가령, Metrics Server, Prometheus, Elastic Stack 등이 있고, 혹은 Datadog 과 Dynatrace 같은 독점 솔루션들이 Metric Server로 사용될 수 있음

FYI. 이전에는 Heapster는 모니터링과 분석 기능을 제공하는 쿠버네티스의 초기 프로젝트 중 하나인데, Deprecated 됨

<br/>

### Metric Server

쿠버네티스 클러스터 당 하나의 Metric Server를 가질 수 있음 

Metric Server는 각 노드와 Pod에서 메트릭을 가져오고, 원하는 데이터를 모아(aggregate) 메모리에 저장

Metric Server는 in-memory 모니터링 솔루션, 디스크에 저장하지 않음 

그래서, 성능 데이터의 히스토리는 볼 수 없음

만약 더 많은 성능을 원한다면, 앞서 다뤘던 고급 모니터링 솔루션에 의지해야 함


이 노드의 Pod에 대한 지표는 어떻게 생성될까?

쿠버네티스는 각 노드에서 kubelet을 실행하는데, kubelet은 또한 cAdvisor 혹은 Container Advisor를 포함함

CAdvisor는 Pod에서 성능 메트릭을 가져오고 kubelet API를 통해 메트릭을 공개해,
Metric Server에서 해당 메트릭을 사용 가능하게 하는 것


로컬 클러스터로 minikube를 사용한다면, 아래 명령 실행 

```Bash
minikube addons enable metrics-server
```

#### 강의 내용 

```Bash
git clone https://github.com/kubernetes-sigs/metrics-server.git
```

다른 모든 환경에서는 GitHub 에서 Metric Server 배포 파일을 복제해서
Metric Server를 배포할 수 있음

그 다음, `kubectl create` 명령어로 구성 요소를 배포

```Bash
kubectl create -f deploy/1.8+/
```

#### 실습

[link](https://github.com/kubernetes-sigs/metrics-server)

```Bash
❯ kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml                                                  ─╯
serviceaccount/metrics-server created
clusterrole.rbac.authorization.k8s.io/system:aggregated-metrics-reader created
clusterrole.rbac.authorization.k8s.io/system:metrics-server created
rolebinding.rbac.authorization.k8s.io/metrics-server-auth-reader created
clusterrolebinding.rbac.authorization.k8s.io/metrics-server:system:auth-delegator created
clusterrolebinding.rbac.authorization.k8s.io/system:metrics-server created
service/metrics-server created
deployment.apps/metrics-server created
apiservice.apiregistration.k8s.io/v1beta1.metrics.k8s.io created
```

이 명령은 Pods, Services, Roles를 배포해 Metrics Server 가 클러스터 내 노드에서 성능 메트릭을 가져올 수 있게 함

배포 된 후, Metrics Server는 데이터를 수집하고 처리할 시간을 줌

처리 된 후, 클러스터 성능을 `kubectl top node` 명령어로 확인할 수 있음

각 노드의 CPU와 메모리 소비량을 보여줌

보다시피 마스터 노드의 CPU 8%가 사용됐습니다 약 166 millicores

`kubectl top pod` 명령어를 통해 Pod의 성능 지표를 확인할 수도 있음

---

### ✍🏻 kodekloud 실습 기록

**1. Metric Server 준비 / Customize**

```Bash
controlplane ~ ➜  git clone https://github.com/kodekloudhub/kubernetes-metrics-server.git
Cloning into 'kubernetes-metrics-server'...
remote: Enumerating objects: 31, done.
remote: Counting objects: 100% (19/19), done.
remote: Compressing objects: 100% (19/19), done.
remote: Total 31 (delta 8), reused 0 (delta 0), pack-reused 12
Receiving objects: 100% (31/31), 8.08 KiB | 8.08 MiB/s, done.
Resolving deltas: 100% (10/10), done.
```

<br/>

**2. Metric Server 배포**

```
controlplane ~ ➜  k create -f kubernetes-metrics-server/
clusterrole.rbac.authorization.k8s.io/system:aggregated-metrics-reader created
clusterrolebinding.rbac.authorization.k8s.io/metrics-server:system:auth-delegator created
rolebinding.rbac.authorization.k8s.io/metrics-server-auth-reader created
apiservice.apiregistration.k8s.io/v1beta1.metrics.k8s.io created
serviceaccount/metrics-server created
deployment.apps/metrics-server created
service/metrics-server created
clusterrole.rbac.authorization.k8s.io/system:metrics-server created
clusterrolebinding.rbac.authorization.k8s.io/system:metrics-server created
```

<br/>

**3. Node Resource 사용량 확인**

```
controlplane ~ ➜  k top node
NAME           CPU(cores)   CPU%   MEMORY(bytes)   MEMORY%   
controlplane   232m         0%     1082Mi          0%        
node01         28m          0%     263Mi           0%
```

<br/>

**4. Pods Resource 사용량 확인**

```
controlplane ~ ➜  k top pods
NAME       CPU(cores)   MEMORY(bytes)   
elephant   14m          32Mi            
lion       1m           18Mi            
rabbit     97m          252Mi
