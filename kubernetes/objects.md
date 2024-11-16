# Kubernetes

[🔗 Objects In Kubernetes](https://kubernetes.io/docs/concepts/overview/working-with-objects/)

Docker Compose, Swarm, Stack 을 이용한 애플리케이션의 기초를 익힘

<br/>

## Role of Kubernetes

- Docker: 컨테이너를 관리하는 데몬인 `dockerd`와 명령행 도구로 구성
- Swarm: 여러 대의 호스트를 묶어 기초적인 컨테이너 오케스트에리션 기능을 제공하는 도커 관련 기술
- Kubernetes: Compose/Stack/Swarm 보다 더 높은 수준의 기능을 갖춘 컨테이너 오케스트레이션 시스템이자 도커를 비롯해 여러 가지 컨테이너 런타임을 다룰 수 있음

<br/>

#### 로컬 PC에서 Kubernetes 실행

_[Minikube](https://github.com/kubernetes/minikube): minikube implements a local Kubernetes cluster on macOS, Linux, and
Windows._

<br/>

#### 쿠버네티스 연동 설정

1. kubernetes 환경 구축

Docker Desktop > Enable Kubernetes

2. **kubectl** 설치

- `kubectl`: 쿠버네티스를 다루기 위한 명령행 도구
    - Local/Managed 환경에서 모두 사용 가능

<br/>

#### kubectl 설치 - macOS

macOS에서 Homebrew 패키지 관리자를 사용하는 경우, Homebrew로 kubectl을 설치할 수 있다.

설치 명령을 실행한다.

```Bash
❯ brew install kubectl
```

또는

```Bash
❯ brew install kubernetes-cli
```

설치한 버전이 최신 버전인지 확인한다.

```Bash
❯ kubectl version --output=json
{
  "clientVersion": {
    "major": "1",
    "minor": "27",
    "gitVersion": "v1.27.2",
    "gitCommit": "7f6f68fdabc4df88cfea2dcf9a19b2b830f1e647",
    "gitTreeState": "clean",
    "buildDate": "2023-05-17T14:20:07Z",
    "goVersion": "go1.20.4",
    "compiler": "gc",
    "platform": "darwin/arm64"
  },
  "kustomizeVersion": "v5.0.1",
  "serverVersion": {
    "major": "1",
    "minor": "27",
    "gitVersion": "v1.27.2",
    "gitCommit": "7f6f68fdabc4df88cfea2dcf9a19b2b830f1e647",
    "gitTreeState": "clean",
    "buildDate": "2023-05-17T14:13:28Z",
    "goVersion": "go1.20.4",
    "compiler": "gc",
    "platform": "linux/arm64"
  }
}
```

<br/>

<table><tr><td>
<b>Minkube</b>

windows/macOS 용 도커에 쿠버네티스 연동 기능이 추가되기 전까지는 로컬 환경에 쿠버네티스 환경을 구축하기 위해 Minkube를 많이 사용했다.
쿠버네티스 연동 기능이 추가 되면서 쿠버네티스가 여러 환경에서 안정적으로 동작하게 된 만큼 Minikube 는 앞으로 점점 덜 사용하게 될 것이다.
windows/macOS 용 도커 연동 기능은 이미 실행중인 `dockerd` 를 대상으로 쿠버네티스 환경을 구축하지만, Minkube는 로컬에 `dockerd`를 새로 띄워 이를 대상으로 쿠버네티스 환경을 구축하는 방식이다.
로컬에서 dockerd를 2개 다루기 때문에 windows/macOS 용 도커 보다 좀 더 까다로운 면이 있다.
물론 Minkube 에도 이점은 있다.
윈도우 용 도커가 Hyper-V, macOS용 도커가 `Hypervisor.framework`에서 동작하는데 반해,
Minikube는 이런 하이퍼바이저 뿐만아니라 VirtualBox나 VMWare에서도 실행이 가능하므로 지원 플랫폼의 다양성 면에서 이점이 있다.
Minikube는 애드온(확장 기능)도 충실히 갖추고 있다. 예를 들면 조금 전 설명했던 대시보드는 Minikube에는 기본으로
포함 돼 있어 별도로 설치할 필요가 없다. `nginx-ingress-controler`도 애드온을 활성화하는 것만으로 사용할 수 있다
</td></tr></table>

<br/>

## 03. Kubernetes Components

### Objects

_Objects In Kubernetes_

[🔗 official] (https://kubernetes.io/docs/concepts/overview/working-with-objects/#kubernetes-objects)

<br/>

#### Object spec and status

Kubernetes Objects는 Kubernetes 시스템의 persistent 엔터티이다.
Kubernetes는 당신의 cluster의 상태를 나타내기 위해 이러한 엔티티를 사용.

- 어떤 컨테이너화된 애플리케이션이 실행되는지 혹은, 어떤 노드들인지
- 이러한 애플리케이션들이 사용할 수 있는 리소스
- 이러한 애플리케이션이 어떻게 행동하는지에 관련된 정책; 가령, restart 정책, upgrade, 그리고 fault-tolerance.

Kubernetes object 는 "record of intent" 으로,
한 번 object를 생성하면, Kubernetes 시스템은 해당 객체가 존재를 지속적으로 보장한다.
object를 생성하는 것은 Kubernetes 시스템에 당신의 cluster가 어떤 워크로드처럼 동작할지 결정하는 것이다.
(object를 생성함으로써, 당신은 Kubernetes 시스템에 당신이 원하는 cluster의 워크로드이 어떻게 보일지 효과적으로 말하는 것이다.)
-> 이것이 바로 cluster의 목표 상태 (desired state)이다.

Kubernetes object 와 동작하기 위해서는 - 그것들을 생성, 수정, 혹은 삭제하는 것에 상관없이 - Kubernetes API가 사용된다.
가령, `kuberctl` 명령어를 사용하게 되면 CLI는 필요한 Kubernetes API 호출을 당신을 위해 만들 것이다.
또한, Client Library 를 사용해서 당신의 프로그램에서 Kubernetes API를 바로 호출할 수도 있다.

<br/>

#### Required fields
[🔗 official] (https://kubernetes.io/docs/concepts/overview/working-with-objects/#required-fields)

<br/>

생성하려는 Kubernetes 개체의 매니페스트(YAML 또는 JSON 파일)에는 이래와 같은 필드 값을 설정 필요:

- `apiVersion`: 해당 object를 생성하기 위해 사용하는 Kubernetes API 버전
- `kind`: 생성하고자 하는 object의 종류 (kind)
- `metadata`: object 를 구별할 용도의 `name`, `UID`(, 선택적으로 `namespace`) 등을 포함한 object 데이터
- `spec` - object가 가질 상태 정의. 객체 spec의 정확한 형식은 모든 쿠버네티스 객체마다 다르며, 한 객체에 특정된 중첩된 필드를 포함. 쿠버네티스 API 참조를 통해 쿠버네티스를 사용하여 생성할 수 있는 모든 객체의 사양 형식을 찾을 수 있다.

가령, Pod API의 spec 필드 문서를 참고해보면, 각 Pod의 경우, `.spec` 필드는 `Pod` 와 `Pod`가 가질 상태를 지정 (예: 해당 Pod 내의 각 컨테이너에 대한 컨테이너 이미지 이름).
다른 예로, StatefulSet의 경우, `.spec` 필드는 `StatefulSet`와 원하는 상태를 지정.
StatefulSet의 `.spec` 내에는 Pod 개체에 대한 템플릿이 있음. 해당 템플릿은 StatefulSet 컨트롤러가 StatefulSet 규격을 만족시키기 위해 만들 Pods를 설명함.
다른 종류의 개체도 다른 `.status`를 가질 수 있으며, 또 API 참조 페이지는 해당 `.status` 필드의 구조와 각 다른 유형의 개체에 대한 내용을 자세히 설명함


FYI. [Configuration Best Practices] (https://kubernetes.io/docs/concepts/configuration/overview/)


| 리소스                      | 용도                                          |
|--------------------------|---------------------------------------------|
| Node                     | 컨테이너가 배치되는 서버                               |
| Namespace                | 쿠버네티스 클러스터 안의 가상 클러스터                       |
| Pod                      | 컨테이너의 집합 중 가장 작은 단위, 컨테이너의 실행 방법 정의         |
| ReplicaSet               | 같은 스펙을 갖는 파드를 여러 개 생성하고 관리하는 역할             |
| Deployment               | 레플리카 세트의 리비전을 관리                            |
| Service                  | 파드의 집합에 접근하기 위한 경로 정의                       |
| Ingress                  | 서비스를 쿠버네티스 클러스터 외부로 노출                      |
| Config Map               | 설정 정보를 정의하고 파드에 전달                          |
| Persistence Volume       | 파드가 사용할 스토리지의 크기 및 종류 정의                    |
| Persistence Volume Claim | Persistence Volume 을 동적으로 확보                |
| Storage Class            | Persistence Volume 이 확보하는 스토리지의 종류를 정의      |
| Stateful Set             | 같은 스펙으로 모두 동일한 파드를 여러 개 생성하고 관리             |
| Job                      | 상주 실행을 목적으로 하지 않는 파드를 여러 개 생성하고 정상적인 종료 보장  |
| Cron Job                 | 크론 문법으로 스케줄링되는 Job                          |
| Secret                   | 인증 정보 같은 기밀 데이터 정의                          |
| Role                     | 네임스페이스 안에서 조작 가능한 쿠버네티스 리소스의 규칙 정의          |
| Role Binding             | 쿠버네티스 리소스 사용자와 Role 을 연결 지음                 |
| Cluster Role             | 클러스터 전체적으로 조작 가능한 쿠버네티스 리소스의 규칙 정의          |
| Cluster Role Binding     | 쿠버네티스 리소스 사용자와 클러스터 Role 을 연결 지음            |
| Service Account          | 파드가 쿠버네티스 리소스를 조작할 때 사용하는 계정                |

<br/>

### Node

: Kubernetes 리소스 중에서 가장 큰 개념

- 쿠버네티스 클러스터의 관리 대상으로 등록된 도커 (컨테이너의) 호스트
- 컨테이너가 배치되는 대상
- Kubernetes 는 노드의 리소스 사용 현황 및 배치 전략을 근거로 컨테이너를 적절히 배치
- 즉, 클러스터에 배치된 노드의 수, 노드의 사양 등에 따라 배치할 수 있는 컨테이너 수가 결정
- 클러스터의 처리 능력은 노드에 의해 결정
- Master Node: Kubernetes Cluster 전체를 관리하는 서버이며, 최소 하나 이상 필요


- ❯ `kubectl get nodes`
- 현재 클러스터에 소속된 노드의 목록 확인

```Bash
❯ kubectl get nodes
```

<br/>

클라우드에서의 Kubernetes 라면, 아래에 해당

- GCP → GCE Instance
- AWS → EC2 Instance

### Cluster

: Kubernetes 의 여러 리소스를 관리하기 위한 집합체

쿠버네티스를 배포하면 클러스터를 얻음

- **✔️ 쿠버네티스 클러스터**
    - 컨테이너화된 애플리케이션을 실행하는 노드라고 하는 워커 머신의 집합
    - 모든 클러스터는 최소 한 개의 워커 노드를 가짐

- **✔️ 워커 노드**
    - 애플리케이션의 구성요소인 파드를 호스트

- **✔️ 컨트롤 플레인**
    - 워커 노드와 클러스터 내 파드를 관리

프로덕션 환경에서는 일반적으로 컨트롤 플레인이 여러 컴퓨터에 걸쳐 실행되고,
클러스터는 일반적으로 여러 노드를 실행하므로 내결함성과 고가용성이 제공.

<br/><img src="https://kubernetes.io/images/docs/components-of-kubernetes.svg" alt="components-of-kubernetes" width="60%" /><br/>


- Control Plane (Master 구성) Component

| Component               | Role                                                    |
|-------------------------|---------------------------------------------------------|
| kube-apiserver          | Kubernetes API를 노출하는 컴포넌트 kubectl 로부터 리소스를 조작하라는 지시를 받음 |
| etcd                    | 고가용성을 갖춘 분산 key-value 스토어, 쿠버네티스 클러스터의 백킹 스토어로 사용됨      |
| kube-scheduler          | 노드를 모니터링하고 컨테이너를 배치할 적절한 노드를 선택                         |
| kube-controller-manager | 리소스를 제어하는 컨트롤러를 실행                                      |

<br/>


#### kube-apiserver

: 쿠버네티스 주요 API 서버를 노출하는 쿠버네티스 컨트롤 플레인 컴포넌트

- 쿠버네티스 컨트롤 플레인의 프론트 엔드
- 수평으로 확장되도록 디자인 되어, 더 많은 인스턴스를 배포해서 확장할 수 있음

<br/>

#### etcd

- 쿠버네티스 뒷단의 저장소: 모든 클러스터 데이터를 담는 일관성·고가용성 키-값 저장소
- 쿠버네티스 클러스터에서 etcd를 뒷단의 저장소로 사용한다면, [데이터 백업 계획](https://kubernetes.io/docs/tasks/administer-cluster/configure-upgrade-etcd/#backing-up-an-etcd-cluster) 필수 참조
- [etcd 공식 문서](https://etcd.io/docs/)

<br/>

#### kube-scheduler

: 노드가 배정되지 않은 새로 생성된 파드를 감지하고, 실행할 노드를 선택하는 컨트롤 플레인 컴포넌트.

스케줄링 결정을 위해서 고려되는 아래 요소들을 포함

- 리소스에 대한 개별 및 총체적 요구 사항
- 하드웨어/소프트웨어/정책적 제약
- 어피니티(affinity) 및 안티-어피니티(anti-affinity) 명세
- 데이터 지역성
- 워크로드-간 간섭
- 데드라인

<br/>

#### kube-controller-manager

- 컨트롤러 프로세스를 실행하는 컨트롤 플레인 컴포넌트.
- 논리적으로, 각 컨트롤러는 분리된 프로세스이지만, 복잡성을 낮추기 위해 모두 단일 바이너리로 컴파일되고 단일 프로세스 내에서 실행 됨.

이들 컨트롤러는 다음을 포함한다.

- 노드 컨트롤러: 노드가 다운되었을 때 통지와 대응에 관한 책임을 가진다.
- 잡 컨트롤러: 일회성 작업을 나타내는 잡 오브젝트를 감시한 다음, 해당 작업을 완료할 때까지 동작하는 파드를 생성한다.
- 엔드포인트슬라이스 컨트롤러: (서비스와 파드 사이의 연결고리를 제공하기 위해) 엔드포인트슬라이스(EndpointSlice) 오브젝트를 채운다
- 서비스어카운트 컨트롤러: 새로운 네임스페이스에 대한 기본 서비스어카운트(ServiceAccount)를 생성한다.

<br/>

#### cloud-controller-manager

- 클라우드별 컨트롤 로직을 포함하는 쿠버네티스 컨트롤 플레인 컴포넌트.
- 클라우드 컨트롤러 매니저를 통해 클러스터를 클라우드 공급자의 API에 연결하고, 해당 클라우드 플랫폼과 상호 작용하는 컴포넌트와 클러스터와만 상호 작용하는 컴포넌트를 구분할 수 있게 해 준다.
- cloud-controller-manager는 클라우드 제공자 전용 컨트롤러만 실행한다. 자신의 사내 또는 PC 내부의 학습 환경에서 쿠버네티스를 실행 중인 경우 클러스터에는 클라우드 컨트롤러 매니저가 없다.

<br/>

kube-controller-manager와 마찬가지로 cloud-controller-manager는 논리적으로 독립적인 여러 컨트롤 루프를 단일 프로세스로 실행하는 단일 바이너리로 결합한다. 수평으로 확장(두 개 이상의 복제 실행)해서 성능을 향상시키거나 장애를 견딜 수 있다.

다음 컨트롤러들은 클라우드 제공 사업자의 의존성을 가질 수 있다.

노드 컨트롤러: 노드가 응답을 멈춘 후 클라우드 상에서 삭제되었는지 판별하기 위해 클라우드 제공 사업자에게 확인하는 것
라우트 컨트롤러: 기본 클라우드 인프라에 경로를 구성하는 것
서비스 컨트롤러: 클라우드 제공 사업자 로드밸런서를 생성, 업데이트 그리고 삭제하는 것


<br/>

### Namespace

- Namespace: 클러스터 안의 가상 클러스터
    - Kubernetes Cluster 안에 가상 클러스터를 또 만들 수 있음
    - 클러스터 구축 시 `default`, `docker`, `kube-public`, `kube-system`, 네 개의 Namespace 가 이미 생성되어 있음

- ❯ `kubectl get namespace`
- 현재 클러스터 내에 존재하는 네임스페이스 목록 확인
    - 네임스페이스마다 권한을 설정할 수 있어 더욱 견고하고 세세하게 권한을 제어할 수 있음

<br/>

## Container Runtime Interface (CRI)

: Kubelet과 컨테이너 런타임 사이의 통신을 위한 메인 프로토콜 (이하 CRI)

CRI는 노드 컴포넌트 **Kubelet**과 **컨테이너 런타임** 사이의 통신을 위한 메인 gRPC 프로토콜을 정의합니다.

CRI는 클러스터 컴포넌트를 다시 컴파일하지 않아도,
Kubelet이 다양한 컨테이너 런타임을 사용할 수 있도록 하는 플러그인 인터페이스입니다.

클러스터의 모든 노드에 동작 중인 *Container Runtime이 존재해야, kubelet이 파드들과 컨테이너들을 실행할 수 있습니다.

<table><tr><td>

* **Container Runtime**
: 컨테이너 실행을 담당하는 소프트웨어.

- containerd
- CRI-O
- Docker Engine
- Mirantis Container Runtime

</td></tr></table>
### The API
_FEATURE STATE: Kubernetes v1.23 [stable]_

