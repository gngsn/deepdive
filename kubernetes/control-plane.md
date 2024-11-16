## What is Kubernetes architecture?
[🔗 Kubernetes Architecture and Components with Diagram](https://www.simform.com/blog/kubernetes-architecture/)
[🔗 Kubernetes Architecture Explained](https://devopscube.com/kubernetes-architecture-explained/)


Kubernetes 아키텍처는 컨테이너화된 워크로드에 안정적이고 적응 가능한 환경을 제공하기 위해,
함께 동작하는 서로 다른 서버와 클러스터에 분산된 컴포넌트들의 집합입니다.
각각의 Kubernetes 클러스터는 컨트롤 플레인 노드들 Control PLane Nodes 과 워커 노드들 Worker Nodes 로 구성됩니다.

해당 글은 Kubernetes 아키텍처 다이어그램의 자세한 중요 요소들을 이해하도록 자세한 내용을 다룹니다.

## Kubernetes architecture components

Kubernetes 아키텍처는 **master-worker 모델** 구조를 따르며, control plane에 해당하는 master는 워커 노드들을 관리합니다.
반면, 파드들로 캡슐화된 컨테이너들은 워커 노드들 내에 배포되어 실행됩니다.
이러한 노드들은 가상 머신이나 물리 서버를 가질 수 있습니다.

이 아키텍처의 중요한 구성 요소에 대해 알아보겠습니다:

### Control Plane

Control Plane *제어 평면*은 컨테이너 오케스트레이션을 담당하며 클러스터의 상태를 유지합니다.

| Component               | Role                                                     |
|-------------------------|----------------------------------------------------------|
| kube-apiserver          | Kubernetes API를 노출하는 컴포넌트 kubectl 로부터 리소스를 조작하라는 지시를 받음  |
| etcd                    | 고가용성을 갖춘 분산 key-value 스토어, 쿠버네티스 클러스터의 백킹 스토어로 사용됨       |
| kube-scheduler          | 노드를 모니터링하고 컨테이너를 배치할 적절한 노드를 선택                          |
| kube-controller-manager | 리소스를 제어하는 컨트롤러를 실행                                       |

각 구성 요소들은 함께 작동하여 **각 Kubernetes 클러스터의 상태가 미리 정의된 원하는 상태 _desired state_ 와 일치하는지 확인**

#### ✔️ kube-apiserver

kube-API 서버는 사용자들과 컴포넌트들이 클러스와 쉽게 통신할 수 있도록 함

몇몇의 모니터링 시스템과 써드파티 서비스들은 아마 (매우 드물게) 클러스터들 간 통신하기 위해 사용할 수도 있음
kubectl과 같은 CLI를 사용하여 클러스터를 관리할 때는, API 서버와 통신하기 위해 HTTP REST API를 사용.

- 하지만, 스케줄러 및 컨트롤러와 같은 **내부 클러스터 구성 요소**는 gRPC 방식으로 통신.
- API 서버는 TLS를 통한 안전성을 위해 다른 컴포넌트와 암호화 통신.
  - API 객체를 검증
  - 사용자 인증 및 권한 부여 _authenticate & authorize_
  - 컨트롤 플레인과 워커 노드 컴포넌트들 사이에서 위치 조정  
  - etc

API 서버는 etcd에서만 작동하며 built-in bastion API 서버 프록시를 포함, 
이를 통해 Cluster IP 서비스에 대한 외부 액세스를 가능하게 합니다.

<br/>

#### ✔️ etcd

key-value 데이터 저장소.


- Kubernetes는 분산형 시스템으로 분산형 특성을 지원하는 etcd와 같은 효율적인 분산형 데이터베이스가 필요. 
- 백엔드 서비스 디스커버리 *backend service discovery*와 데이터베이스 역할을 동시에 수행.
(Kubernetes 클러스터의 브레인이라고도 불림)
- Kubernetes 클러스터애서 저장되는 데이터를 위해 설계되었는데, 가령 Pod 들이나 각 Pod들의 상태, 그리고 Namespace 들이 있음.
- etcd는 보안을 위해 오직 API 서버에서만 접근할 수 있음.

- etcd는 일관성과 가용성을 엄격하게 지키기 위해 [raft consensus algorithm](https://raft.github.io/) 을 사용.
- 고가용성과 노드 장애에 견딜 수 있도록 leader-member 방식으로 작동.

간단히 말하면, 만약 당신이 `kubectl`을 사용하여 쿠버네티스 오브젝트 세부 정보를 얻으려고 한다면, 바로 etcd에서 해당 데이터를 가져오는 것.
또한 Pod과 같은 객체를 배포할 때도 `etcd`에서 엔트리가 생성됨.

Kubernetes는 key-value API를 관리하기 위해서 gRPC 통신으로 etcd와 통신.
모든 객체는 `/registry` 디렉토리 key 아래에 key-value 형식으로 저장.

Kubernetes의 api-server는 `etcd`의 watch 기능을 사용하여 객체의 상태에 대한 변경 사항을 모니터링.


- `etcd`는 Kubernetes 객체의 모든 구성, 상태 및 메타데이터(pods, secrets, daemonsets, deployments, configmaps, statefulsets, etc))를 저장합니다.
- `etcd`는 클라이언트가 `Watch()` API를 이용해 이벤트를 subscribe 할 수 있도록 해줍니다. Kubernetes API-server는 `etcd`의 watch 기능을 이용해 객체의 상태 변화를 추적합니다.
- `etcd`는 gRPC를 이용한 key-value API를 제공합니다. 또한 gRPC 게이트웨이는 모든 HTTP API 호출을 gRPC 메시지로 변환하는 RESTful 프록시입니다. 따라서 Kubernetes에 이상적인 데이터베이스입니다.
- `etcd`는 모든 개체를 '/registry' 디렉토리 키 아래에 key-value 형식으로 저장합니다. 예를 들어 기본 네임스페이스의 Nginx라는 이름의 Pod 정보는 '/registry/pods/default/nginx' 아래에 있습니다

또한, etcd 는 오직 컨트롤 플레인의 **Statefulset** 컴포넌트입니다.

<br/>

#### ✔️ kube-scheduler

kube-scheduler는 **워커 노드에서 Kubernetes pod의 스케줄링을 담당**

<br/>

한 Pod를 배포할 때 CPU, memory, affinity, taints or tolerations, priority, persistent volumes (PV) 등과 같은 포드 요구 사항을 지정.
<small><i>CPU, 메모리, 선호도, 얼룩 또는 허용, 우선순위, 영구 볼륨(PV) 등</i></small> 

스케줄러의 주요 작업은 생성 요청을 식별하고 요구 사항을 충족하는 포드에 가장 적합한 노드를 선택하는 것.
다음 이미지는 스케줄러 작동 방식을 보여줌.

하나의 Kubernetes 클러스 내에는 하나 이상의 워커 노드가 존재할 수 있음.

**스케줄러는 모든 워커 노드들 중에서 하나를 지정하는 방법**

- 최적의 노드를 선택하기 위해, Kube-scheduler는 filtering 과 scoring을 작업합니다.
- filtering 단계: 스케줄러는 pod를 스케줄 할 수 있는 가장 적합한 노드를 찾습니다. 예를 들어, pod를 실행할 자원이 있는 워커 노드가 5개인 경우에는 5개의 노드를 모두 선택합니다. 노드가 없으면 pod는 스케줄이 불가능하기 때문에, 스케줄링 큐 _scheduling queue_ 로 이동합니다. 만약 대규모 클러스터일 때, 100개의 워커 노드가 있고, 스케줄러는 모든 노드를 순회하지 않는다고 가정해 보겠습니다. `percentageOfNodesToScore` 라는 스케줄러 구성 파라미터가 있습니다. 기본값은 일반적으로 `50%`입니다. 따라서 round-robin 방식으로 먼저 50% 이상의 노드에 대해 순회를 시도합니다. 워커 노드가 여러 영역 *zone*에 분산되어 있으면 스케줄러는 다른 영역 *zone*의 노드에서 순회합니다. 매우 큰 클러스터의 경우에는, `percentageOfNodesToScore` 의 기본값은 5%입니다.
- scoring 단계: 스케줄러가 필터링된 워커 노드들에 **점수를 부여하여 노드들의 순위를 매깁**니다. 스케쥴러는 여러 [스케쥴링 플러그인](https://kubernetes.io/docs/reference/scheduling/config/#scheduling-plugins)들을 호출하여 스코어링을 합니다. 그중, 가장 높은 순위를 가지는 워커 노드가 포드 스케쥴링을 위해 선택됩니다. 만약 모든 노드들이 동일한 순위를 가지면, 한 노드가 임의로 선택됩니다.
- 노드가 선택되면, 스케줄러는 API서버에 binding 이벤트를 생성합니다. 이때 binding 이벤트란, pod와 node를 binding하는 이벤트를 의미합니다.

<br/>

#### ✔️ Kube Controller Manager

controller의 동작을 실행시키는 컨트롤 플레인 구성 요소.

논리적으로 각 컨트롤러는 별개의 프로세스이지만, 복잡성을 줄이기 위해, 모두 하나의 바이너리로 컴파일되어 하나의 프로세스로 실행.

<table><tr><td>

**Controller?**
로보틱스(robotics)나 자동화 장치(automation)에서는, 제어 루프 _control loop_ 는 시스템의 상태를 조절하기 위해 무한 루프입니다.

제어 루프의 예시를 들자면, '온도 조절기'가 있습니다
온도를 설정하면 온도 조절기는 원하는 상태를 알려줍니다.
실제 실내 온도는 *현재 상태*이며, 이 현재 상태를 원하는 상태에 가깝게 만들도록 온도 조절기는 장치를 켜거나 끄는 역할을 합니다.

Kubernetes 에서 컨트롤러는 클러스터의 상태를 관찰한 다음, 필요한 곳에 변경시키거나 변경을 요청하는 컨트롤 루프입니다.
각 컨트롤러는 현재 클러스터 상태를 원하는 상태에 가까워지도록 조절합니다.
</td></tr></table>


컨트롤러는 무한한 컨트롤 루프를 실행하는 프로그램으로,
지속적으로 실행되면서 원하는 물체의 상태를 실제와 비교하며 관찰하며 둘이 최대한 가까워지도록 조정.

**Kube controller manager**는 Kubernetes의 모든 Controller를 관리하는 컴포넌트. 
Kubernetes resources/objects (pod, namespace, job, replicaset 등)은 각각의 Controller에 의해 관리됨.
또, 위에서 살펴본 `Kube scheduler`도 `Kube controller manager`에 의해 관리되는 Controller이기도 함.

**Kubernetes가 기본적으로 지원 몇 가지**

- Node controller: 노드가 다운되었을 때 이를 인지하고 응답하는 역할을 합니다.
- Job Controller (Kubernetes Jobs): 일회성 작업을 나타내는 작업 객체를 관찰한 다음, Pods를 생성하여 해당 작업을 완료하도록 실행합니다.
- Endpoints controller: EndpointSlice 개체를 채웁니다 _Populates_. (Service 들과 Pod 들 사이의 링크 제공).
- Service Accounts controller: 새로운 Namespace 에 대한 기본 ServiceAccounts를 생성합니다.
- Deployment controller
- Replicaset controller
- DaemonSet controller
- CronJob Controller
- namespace controller
- ...

<br/>

#### ✔️ Cloud Controller Manager

_cloud-controller-manager, CCM_

쿠버네티스 컨트롤 플레인 컴포넌트 중 하나로, 클라우드 컨트롤 로직을 처리.
Kubernetes가 클라우드 환경에 배포되면 cloud-controller-manager 는 Cloud Platform API와 Kubernetes 클러스터 사이의 브리지 역할을 합니다.
쿠버네티스 핵심 구성 요소가 독립적으로 작동할 수 있으며, 클라우드 공급자가 플러그인을 사용하여 쿠버네티스와 통합할 수 있습니다.
(예를 들어, 쿠버네티스 클러스터와 AWS 클라우드 API 간의 인터페이스)

클라우드 컨트롤러 통합을 통해 Kubernetes 클러스터는 instances (for nodes), Load Balancers (for services), and Storage Volumes (for
persistent volumes)과 같은 클라우드 리소스를 프로비저닝할 수 있습니다.

온프레미스 환경에서 쿠버네티스를 실행시킨다면, 클러스터는 cloud-controller-manager 를 갖지 않습니다.

kube-controller-manager와 마찬가지로, cloud-controller-manager도 논리적으로 여러개의 독립된 제어 루프를 하나의 바이너리로 결합시켜 하나의 프로세스로 실행시킵니다.
성능을 향상시키거나 장애를 견딜 수 있도록 (tolerate failures) 하기 위해, 수평 확장(하나 이상의 복사본 실행)이 가능합니다.

다음 컨트롤러는 클라우드 클라우드 제공자 dependency 들을 가질 수 있습니다:

- Node controller: 응답 중단 후, 클라우드에서 노드가 삭제되었는지 확인하기 위해 클라우드 제공자를 확인. 클라우드 제공자 API와 대화하여 노드 관련 정보를 업데이트
    - 예를 들어, node labeling & annotation, hostname 가져오기, CPU & memory 사용 가능 여부, nodes health 등
- Route controller: 기본*underlying* 클라우드 인프라스트럭처에서 Route를 설정
    - 클라우드 플랫폼의 네트워크 라우팅을 설정하는 책임을 가지며, 이를 통해 Pod들이 서로 통신할 수 있음
- Service controller: 클라우드 제공자 로드밸런서 생성, 업데이트 및 삭제
    - 쿠버네티스 서비스를 위한 로드 밸런서 배포나 IP 주소 할당을 책임짐

다음은 클라우드 컨트롤러 관리자의 고전적인 예입니다.

- 로드 밸런서 유형의 Kubernetes Service 배포. 여기서 Kubernetes는 Cloud-specific Loadbalancer를 프로비저닝하고 Kubernetes Service와 통합됩니다.
- 클라우드 스토리지 솔루션의 지원을 받는 Pod를 위한 스토리지 볼륨(PV) 프로비저닝.

전반적으로 Cloud Controller Manager는 kubernetes가 사용하는 클라우드를 위한 리소스의 라이프사이클을 관리합니다.



---


### Components running on the worker nodes
The task of running your containers is up to the components running on each worker node:

### The Kubelet
The Kubernetes Service Proxy (kube-proxy)
The Container Runtime (Docker, rkt, or others)


### Add-on components
Control Plane 컴포넌트와 노드에서 실행되는 컴포넌트 외에도, 아래와 같은 추가 기능(add-on) 컴포넌트가 필요합니다.



### The Kubernetes DNS server

- The Dashboard
- An Ingress controller
- Heapster
- CNI: The Container Network Interface network plugin


#### Control Plane 구성 요소의 상태 확인
API 서버는 ComponentStatus라는 API 리소스를 노출하며, 이는 각 Control Plane 구성 요소의 상태를 보여줍니다.
kubectl을 사용하여 컴포넌트와 그 상태를 확인할 수 있습니다.


```
$ kubectl get componentstatuses
NAME                 STATUS    MESSAGE              ERROR
scheduler            Healthy   ok
controller-manager   Healthy   ok
etcd-0               Healthy   {"health": "true"}
```

컴포넌트 간 통신
Kubernetes 시스템의 컴포넌트들은 오로지 API Server 와만 통신합니다.

서로 직접 통신하지 않습니다.

일례로, etcd와 통신하는 구성 요소는 API Server가 유일합니다.

다른 컴포넌트들은 etcd와 직접 통신하지 않으며, API Server와 통신하며 클러스터 상태를 업데이트합니다.

그림

그림과 같이, API 서버와 다른 구성 요소들은 대부분 항상 동일한 구성 요소들로 초기화됩니다.

하지만 kubectl을 사용하여 로그를 가져오거나 kubectl attach를 사용하여 실행 중인 컨테이너에 연결하거나 kubectl port-forward 명령을 사용하면 API 서버가 kublet에 연결됩니다.


### Kubelet

Kubelet과 서비스 프록시는 모두 실제 포드 컨테이너가 실행되는 워커 노드에서 실행됩니다.



Kubelet 동작?
한마디로, Kubelet은 Worker 노드에서 실행되는 모든 것을 담당하는 컴포넌트입니다.



가장 처음으로, API 서버에 Node 리소스를 생성하여 실행 중인 노드를 등록합니다.

그런 다음, 노드에 예약된 Pods에 대해 API 서버를 지속적으로 모니터링하고 Pod의 컨테이너를 시작합니다.

특정 컨테이너 이미지에서 컨테이너를 실행하기 위해 구성된 컨테이너 런타임(Docker, CoreOS의 rkt 또는 다른 것)을 알려줌으로써 이 작업을 수행합니다.

그러면 Kubelet은 실행 중인 컨테이너를 지속적으로 모니터링하고 상태, 이벤트 및 리소스 소비량을 API 서버에 보고합니다.

Kubelet은 컨테이너 라이브니스 프로브를 실행하는 구성 요소이기도 하며, 프로브가 실패하면 컨테이너를 다시 시작합니다. 마지막으로 API 서버에서 Pod가 삭제되면 컨테이너를 종료하고 Pod가 종료되었음을 서버에 알립니다.
