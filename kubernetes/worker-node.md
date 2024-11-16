## Worker Node
The Worker nodes are responsible for running containerized applications.
The worker Node has the following components.

- kubelet
- kube-proxy
- Container runtime

## Kubernetes Worker Node Components
이제 각각의 워커 노드 구성요소를 살펴보도록 하겠습니다.


# 1. Kubelet

- Kubelet은 클러스터의 모든 노드에서 실행되는 에이전트 컴포넌트
- 컨테이너로 실행되지 않고, systemd에 의해 관리되는 데몬으로 실행
- API 서버에 워커 노드를 등록하고 주로 API 서버에서 podSpec(팟 사양 – YAML 또는 JSON)으로 작업하는 역할
  - podSpec: pod 내부에서 실행되어야 하는 컨테이너와 해당 컨테이너의 리소스(예: CPU 및 메모리 제한)나 환경 변수, 볼륨, 레이블 등을 정의
  - 정의 후 컨테이너를 생성하여 podSpec을 **원하는 상태** desired state 로 조정

### 역할

- Pod의 컨테이너 생성, 수정 및 삭제
- liveliness, readiness, startup 프로브 처리
- Pod 구성을 읽고 볼륨 마운팅을 담당하고 볼륨 마운트를 위한 호스트에 각각의 디렉토리 생성
- `CAdvisor` 및 `CRI` 같은 구현체로 API 서버 호출을 통해 노드 및 Pod 상태를 수집 및 보고

Kubelet은 Pod 변경을 지켜보고, 노드의 컨테이너 런타임을 활용하여 이미지를 pull 받거나, 컨테이너를 실행하는 등의 작업을 수행하는 컨트롤러이기도 함

- API 서버의 PodSpec 외에 kubelet은 파일, HTTP endpoint, HTTP 서버의 podSpec 수용 가능

파일과 관련된 podSpec의 좋은 예가 바로 Kubernetes static pods.
정적 Pod 들은 API 서버가 아닌 Kubelet에 의해 관리됨.
Kubelet 컴포넌트에 Pod YAML 위치를 제공하면 Pod를 생성할 수 있음을 의미.
하지만, Kubelet이 생성하는 정적 Pod는 API 서버에서 관리하지 않음

실제 정적 Pod 예시를 확인해보면,
컨트롤 플레인을 부트스트래핑하는 동안 kubelet은 `/etc/kubernetes/manifest`에 있는 podSpecs에서 `api-server`, `scheduler` 및 `Controller Manager`를 정적 포드로 시작.

Kubelet의 몇 가지 주요 사항을 확인해보면,

- Kubelet은 container runtime interface(CRI) gRPC 인터페이스를 사용하여 컨테이너 런타임과 통신
- 또한 HTTP 엔드포인트를 노출하여 로그를 스트리밍하고 클라이언트에 대한 실행 세션 exec session 을 제공
- CSI(Container Storage Interface) gRPC를 사용하여 블록 볼륨을 구성
- 클러스터에 구성된 CNI 플러그인을 사용하여 Pod IP 주소를 할당하고 Pod 에 필요한 네트워크 경로 및 방화벽 규칙을 설정



## 2. Kube proxy

Kube 프록시를 이해하려면 Kubernetes Service & Endpoint 개체에 대한 기본 지식 필요

### Kubernetes Service & Endpoint

Kubernetes Service:
- 일련의 포드들을 내부적으로 혹은 외부 트래픽에 노출시키는 방법
- 서비스 객체가 생성될 때, 해당 객체는 가상 IP 인 **ClusterIP**를 할당 받음
  - ClusterIP는 Kubernetes 클러스터 내에서만 접근 가능

Kubernetes Endpoint:
- Service 객체 아래에 **모든 IP 주소들과 pod 그룹의 Port**들

- Endpoint Controller: Pod IP 주소 (endpoint) 목록 유지 관리 역할
- Service Controller: Endpoint를 서비스에 구성하는 역할

Pod IP의 경우에는 ping이 가능하지만, ClusterIP는 Service Discovery에만 사용되기 때문에 ping할 수 없음


### Kube Proxy 란?

- Kube-proxy는 데몬셋으로 모든 노드에서 실행되는 데몬
- Pod에 대한 Kubernetes Services 개념을 구현하는 프록시 구성 요소 (로드 밸런싱 되는 Pod 세트의 단일 DNS).
- 주로 UDP, TCP, SCTP를 프록시 함 (HTTP 인식 불가)

- Service (Cluster IP)를 통해 Pod를 노출시킬 때, Kube-proxy는 네트워크 규칙 생성
  - 네트워크 규칙: Service 객체 아래에 그룹화된 백엔드 Pod(Endpoint)으로 트래픽을 전송
- 즉, 모든 로드 밸런싱 및 서비스 검색은 Kube 프록시에서 처리 됨

### Kube-proxy 작동 원리?

Kube 프록시는 API 서버와 통신하면서,
서비스(ClusterIP)와 각 포드 IP 및 포트(endpoint)에 대한 세부 정보를 얻고 **서비스 및 엔드포인트의 변경을 모니터링**합니다.

그런 다음 Kube-proxy는 아래 모드 중 하나를 사용하여 트래픽을 서비스 뒤의 Pod로 라우팅하기 위한 규칙을 생성/업데이트합니다

✔️ **IPTables**
_default_

- IPTables 모드에서 트래픽은 IPtables 규칙에 의해 처리 → 때문에, 각 서비스 당 IPtables 규칙이 생성됨
- 이 규칙은 ClusterIP로 오는 트래픽을 캡처한 다음 백엔드 Pod로 전달
- 또, kube-proxy는 로드 밸런싱을 위해 백엔드 포드 랜덤으로 선택
- 한 번 연결이 설정되면, 연결이 종료될 때까지 요청은 동일한 Pod로 전송

✔️ **IPVS**
- IP Vertual Server
- 서비스가 1000개를 초과한 클러스터를 위한 모드, IPVS가 성능 향상 제공
- 아래 로드 밸런싱 알고리즘 지원
  - `rr`: round-robin (default)
  - `lc`: least connection. 최소 연결 (가장 적은 오픈 커넥션 수)
  - `dh`: destination hashing. 대상 해시
  - `sh`: source hashing. 소스 해시
  - `sed`: shortest expected delay. 최단 예상 지연
  - `nq`: never queue. 대기열 없음

✔️ **Userspace** (legacy & not recommended)
✔️ **Kernelspace**: only for Windows System

kube-proxy IPtables와 IPVS 모드 간의 성능 차이를 알고 싶다면 [여기](https://www.tigera.io/blog/comparing-kube-proxy-modes-iptables-or-ipvs/) 참조
또, kube-proxy를 Cilium로 대체할 수 있음


## 3. Container Runtime

- Host에서 자바 프로그램을 실행하기 위해 필요한 소프트웨어인 Java Runtime (JRE)와 같은 방식.
- Container Runtime은 컨테이너를 실행하는 데 필요한 소프트웨어 구성 요소

- 컨테이너 런타임은 Kubernetes 클러스터의 모든 노드에서 실행됨
- 컨테이너 레지스트리에서 이미지를 끌어오고, 컨테이너를 실행하고, 컨테이너에 대한 리소스를 할당하고 격리하며, 호스트에서 컨테이너의 전체 라이프사이클을 관리


모든 컨테이너 런타임은 **CRI 인터페이스를 구현**하고, **gRPC CRI API(runtime 및 image service endpoint)를 노출**

✔️ **CRI (Container Runtime Interface)**
- 쿠버네티스가 다른 컨테이너 런타임과 상호 작용할 수 있도록 하는 API 집합(set)
- 다른 컨테이너 런타임을 쿠버네티스와 상호 교환하여 사용할 수 있도록 해줍니다. CRI는 컨테이너를 생성, 시작, 중지, 삭제할 뿐만 아니라 이미지 및 컨테이너 네트워크를 관리하기 위한 API를 정의합니다.

✔️ **OCI (Open Container Initiative)**
- 컨테이너 형식 및 실행 시간에 대한 표준 집합(set)
- Kubernetes는 CRI(Container Runtime Interface)를 준수하는 여러 컨테이너 런타임(CRI-O, Docker Engine, containerd 등)을 지원


그렇다면 Kubernetes는 컨테이너 런타임을 어떻게 활용

- Kubelet Agent는 CRI API를 사용하여 컨테이너 런타임과 상호 작용하여 컨테이너의 라이프사이클을 관리하는 역할을 함
- 또한 컨테이너 런타임에서 모든 컨테이너 정보를 가져와 컨트롤 플레인에 제공


**CRI-O 컨테이너 런타임 인터페이스의 예시**

컨테이너 런타임이 kubernetes에서 어떻게 작동하는지에 대한 큰 그림

<img src="./img/runtime.png" width="60%" />

1. API 서버로부터 Pod에 대한 새로운 요청이 있을 때, kubelet은 CRI-O 데몬과 대화하여 Kubernetes **Container Runtime Interface**를 통해 필요한 컨테이너를 시작
2. CRI-O는 `containers/image` 라이브러리를 사용하여 구성된 컨테이너 레지스트리에서 필요한 컨테이너 이미지를 확인하고 당겨(pull) 옴
3. 이후, CRI-O는 컨테이너에 대한 OCI 런타임 규격(JSON)을 생성
4. 그런 다음 CRI-O는 OCI 호환 런타임(runc)을 실행하여 런타임 사양에 따라 컨테이너 프로세스를 시작


## Kubernetes Cluster Addon Components

- 쿠버네티스 클러스터는 핵심 구성 요소 외에도 완전히 작동하려면(fully operational) 구성 요소 Addon 필요
- Addon를 선택하는 것은 프로젝트 요구 사항과 사용 사례에 따라 다름

다음은 클러스터에 필요한 일반적인 추가 기능 구성 요소 중 일부입니다.

1. **CNI Plugin (Container Network Interface)**
2. **CoreDNS (For DNS server)**: CoreDNS는 Kubernetes 클러스터 내에서 DNS 서버 역할을 함. 해당 Addon을 활성화하면, DNS 기반 서비스 검색을 활성화할 수 있음
3. **Metrics Server (For Resource Metrics)**: 이 추가 기능을 사용하면 성능 데이터와 클러스터의 노드 및 포드의 리소스 사용량을 수집할 수 있음
4. **Web UI (Kubernetes Dashboard)**: 이 추가 기능을 통해 Kubernetes 대시보드에서 웹 UI를 통해 개체를 관리할 수 있음

### 1. CNI Plugin

CNI(Container Networking Interface)?
- 컨테이너용 네트워크 인터페이스를 만들기 위한 벤더 중립적인 사양(vendor-neutral specifications)과 라이브러리를 갖춘 플러그인 기반 아키텍처
  - 특정 벤터를 위한 사양이 아니라 공통화를 위해 컨테이너를 다루는 모든 기업에서 구현해야 할 스펙
  - 즉, Kubernetes에만을 위한 사양이 아님
- CNI 컨테이너 네트워킹은 Kubernetes, Mesos, CloudFoundry, Podman, Docker 등과 같은 컨테이너 오케스트레이션 도구 전반에 걸쳐 표준화될 수 있음
- 컨테이너 네트워킹 시, 기업마다 네트워크 isolation, security, encryption 등의 요구사항이 다를 수 있음
- CNI-Plugins: 컨테이너 기술이 발전함에 따라 많은 네트워크 provider 들이 다양한 네트워킹 기능을 갖춘 컨테이너용 CNI 기반 솔루션을 개발해옴
  - 이를 통해 사용자는 다양한 공급자의 요구 사항에 가장 적합한 네트워킹 솔루션을 선택할 수 있음

#### Kubernetes에서의 CNI Plugin 작동 방식

- Kube-controller-manager는 각 노드에 pod CIDR을 할당
  - 각 pod는 pod CIDR로부터 고유 IP 주소(unique IP address)를 가져옴
- Kubelet은 컨테이너 런타임과 상호 작용하여 스케줄된 포드를 시작
  - 컨테이너 런타임의 일부인 CRI 플러그인은 CNI 플러그인과 상호 작용하여 포드 네트워크를 구성
- CNI 플러그인을 사용하면 overlay network를 사용하여 동일한 노드 또는 다른 노드에 걸쳐 포드 간 네트워킹을 수행할 수 있음


**CNI 플러그인에서 제공하는 고급 기능**

- Pod Networking
- Pod Network Security & Isolation: 네트워크 정책을 사용하여 pod 와 namespace 간의 트래픽의 흐름을 제어


**많이 사용되는 CNI 플러그인**

- Calico
- Flannel
- Weave Net
- Cilium (Uses eBPF)
- Amazon VPC CNI (For AWS VPC)
- Azure CNI(For Azure Virtual Network) Kubernetes 네트워킹은 큰 topic 이며 호스팅 플랫폼에 따라 다름

## Kubernetes Native Objects

지금까지 살펴본 핵심 쿠버네티스 구성 요소와 각 구성 요소의 작동 방식은 아래와 같은 주요 Kubernetes 개체를 관리하는 데 사용됨

- Pod
- Namespaces
- Replicaset
- Deployment
- Daemonset
- StatefulSet
- Jobs & CronJobs


또한 CRD 및 Custom Controller를 사용하여 Kubernetes를 확장할 수 있음

따라서 클러스터 구성 요소는 Custom Controller 및 Custom Resource Definition을 사용하여 생성된 개체도 관리


