# Imperative vs. Declarative

## Imperative vs Declarative

- **Imperative**: What to do & How to do
- **Declarative**: How to do 

<br/>

### Infrastructure as Code

IaC에서 인프라 프로비저닝의 접근법 두 가지

- **Imperative**: 단계적으로 작성된 명령어들의 집합
- **Declarative**: 선언적 접근에서 요구 사항을 선언
  - Ansible, Puppet, Chef, Terraform

<br/>

만약, Imperative 접근법에서 처음에 단계의 절반만 실행하거나, 혹은 동일한 명령어를 다시 입력한다면? → 추가 단계가 많이 필요함 (존재 여부를 확인 후 조치를 취함)
(가령, VM을 프로비저닝하는 동안 "웹 서버"라는 이름의 VM이 이미 존재한다거나, 데이터베이스 생성이나 데이터 불러올 때)

가장 이상적인 건 시스템이 똑똑해서 이미 진행된 사항을 파악하고 필요한 수정 사항만 적용하는 것

---

<br/>

### Kubernetes Command

#### Imperative Approach in Kubernetes

새 개체를 생성하기 위한 run, create, expose 명령 혹은 기존 개체를 업데이트하기 위한 edit, scale, set 명령

인프라를 요구 사항에 맞게 객체를 생성, 업데이트, 삭제하며 정확히 어떻게 적용할지를 명령

<br/>

- **Create Objects**
  - `kubectl create -f nginx.yaml`
- **Update Objects**
  - `kubectl edit deployment nginx`
  - `kubectl replace -f nginx.yaml`

<br/>

**이점**
- 이 명령은 개체 생성과 수정을 신속히 반영 ← YAML 파일을 다룰 필요가 없기 때문

**한계**
- 복잡한 사용에 한해 길고 복잡한 명령을 만들어야 함 (ex. 다중 컨테이너 포드나 배포)
- 이 명령은 한 번 실행하면 잊힘
  - 명령어 실행한 세션 히스토리에서만 볼 수 있음 
  - 객체 이력 추적이 어려움

<br/>

→ 크고 복잡한 환경에서 이런 명령을 다루기가 어려움

<br/>

#### Declarative Approach in Kubernetes

YAML 포맷의 정의 파일 (aka. definition file/ configuration file/ manifest file) 을 통해,
개체가 정확히 어떻게 보여야 하는지를 기록하는데 도움

YAML 파일은 Imperative 방식과 달리 휘발성이 아니라, Git 같은 형상 관리될 수 있음

구성을 코드로 관리하며 코드 리뷰와 검토 과정을 통해 production 환경에 안전히 배포될 수 있음

<br/>

- **Create Objects**
  - `kubectl apply -f /path/to/config-files`
- **Update Objects**
  - `kubectl apply -f /path/to/config-files`

<small>
이미 존재하는 객체라면, 새로운 변화에 대해서만 업데이트 (오류 x) <br/>
(FYI. `kubectl create` 나 `kubectl update` 는 이미 존재 / 업데이트가 적용될 수 없다는 오류 발생)
</small>

<br/><br/>

#### 생성 / 수정

Imperative 방법은, 수정 시 명령어를 통해 원하는 사항을 위해 특정 연쇄적인 명령을 내려 기록이 되지 않음

Declarative 방법은, 로컬에 저장된 설정 파일을 새로운 버전으로 편집
→ 요구되는 변화는 여기 이미지 이름을 업데이트하고 kubectl을 실행해 명령을 대체해 개체를 업데이트

따라서, 변경 사항이 기록되고, 리뷰나 배포 로직을 통해 추적할 수 있음

객체 구성 파일이 여러 개일 경우, 디렉터리를 경로로 지정할 수 있어서, 모든 객체가 한 번에 생성


개체를 완전히 삭제하고 재생성하고 싶을 때에는, 동일한 명령이되 `--force` 옵션 적용

<br/><br/>