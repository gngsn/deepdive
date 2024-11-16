# Nodes


쿠버네티스는 노드 위에서 실행되는 Pod 들 내 컨테이너를 배치시키면서 워크로드를 실행합니다.
<small> _Workload: 쿠버네티스에서 동작하는 하나의 애플리케이션_ </small>

하나의 노드는 클러스터에 따라 가상 머신이나 물리 머신일 수 있습니다.
각각의 노드는 컨트롤 플레인에 의해 관리되고 Pod를 싱행시키기 위한 필요 서비스들을 포함합니다.

일반적으로, 한 클러스터 내 여러 개의 노드를 가지는데, 
학습이나 자원이 제한된 상황이라면 하나의 노드를 사용하기도 합니다.

노드에 포함되는 컴포넌트로는 kubelet, 하나의 container runtime, 그리고 the kube-proxy 가 있습니다.

### Management

API 서버에 노드를 추가하는 주요 방법은 두 가지가 있습니다.

1. 노드 위 `kubelet`이 컨트롤 플레인에 스스로 등록
2. 개발자가 직접 노드 객체를 추가

위 방식을 통해 노드가 생성되거나 등록되면, 컨트롤 플레인은 새로운 노드 객체가 유효한지 확인합니다.

예를 들어, 아래와 같이 JSON 매니페스트로 부터 노드를 생성한다고 해봅시다.

```yaml
{
  "kind": "Node",
  "apiVersion": "v1",
  "metadata": {
    "name": "10.240.79.157",
    "labels": {
      "name": "my-first-k8s-node"
    }
  }
}
```

쿠버네티스는 내부적으로 노드(표현) 객체를 생성합니다.
쿠버네티스는 `kubelet`이 (`metadata.name` 필드에 해당하는) API 서버에 등록되었는지 확인합니다.
만약, 노드가 모든 필수 서비스가 실행중인지 확인(health check)한 후, Pod를 실행할 수 있습니다.
그렇지 않으면 노드는 healthy 상태가 될 때까지 어떤 클러스터 활동이든 무시합니다.

<table><tr><td>
<b>📌 Note</b>

쿠버네티스는 계속해서 유효하지 않은 Node를 갖고 있으면서, 해당 상태가 동작할 수 있는 상태(healthy)가 될 때까지 계속해서 확인합니다.
헬스 체크를 멈추고 싶다면, 개발자 또는 컨트롤러가 노드 객체를 명시적으로 삭제해야 합니다.

</td></tr></table>

노드 객체의 이름은 반드시 유효한 DNS Subdomain 이름을 따라야 합니다.

<table><tr><td>

### 📌 Names

[Objects In Kubernetes > Object Names and IDs](https://kubernetes.io/docs/concepts/overview/working-with-objects/names/#dns-subdomain-names">)

Resource URL 형식의 특정 객체를 언급하기 위해 사용자가 지정한 문자열 (가령, `/api/v1/pods/some-name`)

주어진 종류의 개체는 한 번에 하나의 이름을 가질 수 있으며,
이미 삭제된 객체와 같은 이름을 가진 새로운 객체를 만들 수는 있다.

이름은 동일한 리소스의 모든 API 버전에 걸쳐 고유한 값이어야 한다.
API 리소스는 API 그룹, 리소스 유형, 네임스페이스(이름공간 리소스용) 및 이름으로 구분된다.
즉, API 버전은 이러한 맥락에서 무관하다.

<pre>
참고: 물리적 호스트를 나타내는 Node와 같이 객체가 물리적 개체를 나타내는 경우, 
Node를 삭제 및 재작성하지 않고 동일한 이름으로 호스트를 재작성하는 경우, 
Kubernetes는 새로운 호스트를 기존 호스트로 취급하여 불일치가 발생할 수 있다.
</pre>

하기의 일반적으로 사용되는 리소스를 위한 네 타입의 이름 규칙(constraint)이 존재

#### DNS Subdomain Names
대부분의 리소스 타입은 RFC 1123에 정의된 DNS subdomain 서브도메인으로 사용될 수 있는 이름으로 지정되어야 한다.
즉, 아래 사항들을 반드시 지켜야 한다.

- 253자 이내
- 영소문자, 숫자, '-', '.' 기호만 포함
- 영숫자로 시작
- 영숫자로 끝

#### RFC 1123 Label Names
일부 리소스 타입은 [RFC 1123](https://datatracker.ietf.org/doc/html/rfc1123)에 정의된 DNS subdomain label 표준으로 사용될 수 있는 이름으로 지정되어야 한다.
즉, 아래 사항들을 반드시 지켜야 한다.

- 최대 63자
- 영소문자, 숫자, '-' 기호만 포함
- 영숫자로 시작
- 영숫자로 끝

#### RFC 1035 Label Names
일부 리소스 타입은 [RFC 1035](https://datatracker.ietf.org/doc/html/rfc1035)에 정의된 DNS subdomain label 표준으로 사용될 수 있는 이름으로 지정되어야 한다.
즉, 아래 사항들을 반드시 지켜야 한다.

- 최대 63자
- 영소문자, 숫자, '-' 기호만 포함
- 영문자로 시작
- 영숫자로 끝

#### Path Segment Names

일부 자원 타입들은 그들의 이름들이 path segment 로서 안전하게 인코딩될 수 있도록 요구한다. 
즉, 이름은 "." 또는 "."가 아닐 수 있고, 이름은 "/" 또는 "%"를 포함하지 않을 수 있다.

가령, `nginx-demo` 라는 이름의 Pod 예시를 들면 아래와 같다.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-demo
spec:
  containers:
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
```

### 📌 UIDs
객체를 고유하게 식별하기 위해 Kubernetes 시스템에서 생성된 문자열입니다.
쿠버네티스 클러스터의 전체 생애에 걸쳐 생성된 모든 객체는 고유한 UID를 가진다.
유사한 개체들의 역사적 발생을 구별하기 위한 것이다.
쿠버네티스 UID는 범용 고유 식별자(UUID라고도 함)이다. UUID는 ISO/IEC 9834-8과 ITU-TX.667로 표준화되어 있다.

</td></tr></table>

### Node name uniqueness

이름은 노드를 식별합니다.
한 번에 두 노드가 동일한 이름을 가질 수는 없습니다.
쿠버네트스는 동일한 이름을 동일한 객체로 간주합니다.
노드의 경우, 동일한 이름을 사용하는 한 인스턴스는 동일한 상태 (가령, 네트워크 세팅, 루트 디스크 컨텐츠)과 속성(가령, 노드 레이블)을 갖을 것라고 암묵적인 가정을 한다.
이는 인스턴스가 수정되면 불일치의 문제를 발생시킬 수 있습니다.
만약, 노드가 대체되야 하거나 중요하게 업데이트되어야 하면, 기존의 노드 객체는 API 서버에서 가장 먼저 제거되어야 하고 업데이트가 된 이후에 다시 추가될 수 있습니다.

<br/><br/>

## Node status

노드 상태는 다음의 정보를 포함합니다:

- Addresses
- Conditions
- Capacity and Allocatable
- Info


### `kubectl describe`

kubectl 노드들의 상태나 상세 사항들을 확인하려면 `kubectl`을 사용할 수 있습니다.

```Bash
kubectl describe node <insert-node-name-here>
```

FYI. [Node Status](https://kubernetes.io/docs/reference/node/node-status/)

### Node heartbeats
Kubernetes 노드에서 보내는 Heartbeat 는 클러스터에서 각 노드의 가용성을 확인하고 장애가 감지되면 조치를 취할 수 있도록 도와줍니다.
노드에는 두 가지 형태의 Heartbeat 이 있습니다:

- 노드의 `.status`로 업데이트
- `kube-node-lease` 네임스페이스 내의 개체를 Lease. 각 노드에는 연결된 리스 개체 존재


### Node controller

노드 컨트롤러는 노드의 다양한 측면을 관리하는 쿠버네티스 컨트롤 플레인 컴포넌트입니다.
노드 컨트롤러는 노드의 생애 동안에 다양한 역할을 합니다.

가장 먼저, 노드가 등록될 때 CIDR 블록을 노드에 할당합니다. (만약 CIDR 할당 옵션이 켜있는 상태라면)
두 번째는, 노드 컨트롤러의 내부 노드 목록을 클라우드 제공자의 이용 가능한 머신 목록과 함께 최신으로 유지하는 것 입니다.
클라우드 환경에서 실행될 때 그리고 노드가 건강하지 않을 때마다,
노드 컨트롤러는 클라우드 제공자에게 해당 노드의 VM이 여전히 사용 가능한 상태인지 묻고,
아니라면, 노드 컨트롤러는 노드 목록으로부터 노드를 삭제합니다.

세 번째는 노드의 상태를 모니터링하는 것입니다.
노드 컨트롤러는 다음과 같은 역할을 한다:

- 노드에 도달할 수 없게 된 경우 노드의 `.status` 필드에 `Ready` 조건 업데이트: `Ready` 조건을 `Unknown`으로 설정
- 노드가 도달 불가능한 상태로 남아있는 경우: 도달 불가능한 노드의 모든 Pods에 대해  API-initiated eviction 를 트리거한다. 기본적으로, 노드 컨트롤러는 노드를 `Unknown`으로 마킹하고, 첫 번째 퇴거 요청을 제출할 때까지 5분을 기다린다.

기본적으로 노드 컨트롤러는 5초 간격으로 각 노드의 상태를 확인합니다. 
이 주기는 `kube-controller-manager` 컴포넌트의 `--node-monitor-period` 플래그를 사용하여 구성할 수 있다.


---


---

## Lease
리ː스, lease [명사] 기계·설비·기구 따위를 임대하는 제도. 일반적으로는 장기간의 임대를 말함. 순화어는 `임대'.










