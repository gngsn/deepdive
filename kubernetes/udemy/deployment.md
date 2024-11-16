# Deployment

<small> 

> **TOC**
<br/> Deployment?
<br/> Use Case
<br/> Deployment yaml
<br/> Deployment command

</small>

## Deployment?

> A Deployment provides declarative updates for Pods and ReplicaSets.

Deployment는 선언형 방식으로 Pod와 ReplicaSet를 업데이트할 수 있습니다.

Deployment에서 원하는 상태를 설명하면 Deployment Controller가 실제 상태를 원하는 상태로 제어된 속도로 변경합니다. Deployment를 정의하여 새 ReplicaSet을 생성하거나 기존 Deployment를 제거하고 모든 리소스를 새 Deployment로 채택할 수 있습니다.

쿠버네티스는 이전의 애플리케이션 배포 시 필요했던 조건 들을 만족하도록 구성되어 있습니다.
가령, 아래와 같은 조건을 고려하여 안정적인 서버 운영을 위한 배포 기능을 가집니다.

- 배포된 파드 업데이트
- 새로운 애플리케이션 릴리즈
- 무중단 배포

배포된 파드를 업데이트하거나 새로운 릴리즈를 배포할 때,
사용자의 트래픽이 막히지 않게 중단되지 않는 서버를 운영하는 것이 당연시 되었는데요.
쿠버네티스에서는 안정적인 배포 뿐만 아니라, 
배포 과정을 파일에 한 번 선언해두면 항상 동일한 방식으로 배포가 진행되도록 합니다. 

애플리케이션을 업데이트하고 싶을 때, 모든 Pod에 변경 사항을 쉽게 반영하기 위해서,
Pod 상위 계층의 ReplicaSet 에 변경 사항을 적용할 수 있습니다. 

> ReplicaSets 를 통해 안정적으로 파드 개수를 유지시키고, 
한 파드에 장애가 발생했을 때, 트래픽을 정상 동작하는 파드로 조정하여 안정적인 파드 운영을 도와줍니다.  

업데이트를 반영 시킬 때, 여러 개의 ReplicaSet 모두에 반영을 하려고 할 땐 어떨까요?

가령, 공통 모듈 내 3개의 애플리케이션에 해당하는, _ex. DB·서버·프론트_, ReplicaSet들을 하나씩 업데이트 하는 방법도 있을 텐데요.
문제는, 서버 다음 프론트를 업데이트 한다고 했을 때, 
서버가 v2인데 DB와 프론트는 아직 v1인 내용을 반영한다면 장애가 발생할 수도 있습니다. 

위 방식이 아닌, 공통 모듈의 **모든 구성요소가 한 번에 업데이트**되고,
**항상 동일한 조건의 배포를 통해 진행**되는 것을 선호할 것입니다.

ReplicaSet보다 더 높은 계층인 Deployment 하나를 배포하여 안정적인 업데이트를 진행할 수 있습니다.

---

## Use Case

실제로 Deployment 를 사용하는 예시를 알아봅시다.

✔️ ReplicaSet 배포
ReplicaSet는 내부에서(background) Pod를 생성하며, 배포 과정 상태를 체크할 수 있습니다.

✔️ Pod의 새로운 상태를 선언.
Deployment의 PodTemplateSpec를 업데이트해서 새로운 상태를 선언합니다.
새로운 ReplicaSet가 생성되고 Deployment는 속도를 제어하며,
Pod가 이전 ReplicaSet에서 새로운 ReplicaSet 하위로의 이동을 관리합니다.
각각의 새로운 ReplicaSet은 Deployment의 수정을 반영합니다.

[//]: # ( TODO )
✔️ Rollback to an earlier Deployment revision if the current state of the Deployment is not stable.
Each rollback updates the revision of the Deployment.

✔️ Scale up the Deployment to facilitate more load.

✔️ Pause the rollout of a Deployment to apply multiple fixes to its PodTemplateSpec and then resume it to start a new rollout.

✔️ Use the status of the Deployment as an indicator that a rollout has stuck.

✔️ Clean up older ReplicaSets that you don't need anymore.

---

## Deployment yaml


```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment          # ❶
  labels:
    app: nginx
spec:
  replicas: 3                     # ❷
  selector:                       # ❷
    matchLabels:                  # ❷
      app: nginx                  # ❷
  template:
    metadata:
      labels:
        app: nginx                # ❸.①
    spec:                         # ❸.②
      containers:                 # ❸.②
      - name: nginx               # ❸.③
        image: nginx:1.14.2       # ❸.②
        ports:
        - containerPort: 80
```

<br/>

#### ❶ `Deployment` 이름
Kubernetes는 `.metadata.name`를 참고하여 `nginx-deployment`를 생성합니다.
이 이름은 나중에 생성될 `ReplicaSets` 나 `Pods` 의 기초가 됩니다.
자세한 내용은 이후 [Deployment Spec](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#writing-a-deployment-spec) 을 설명할 때 다루겠습니다.

<br/>

#### ❷ `ReplicaSet` 정의

`Deployment`는 `.spec.replicas` 를 참고하여 `ReplicaSet`이 생성·관리할 Pod의 개수를 설정합니다.
`.spec.selector`는 생성된 `ReplicaSet`이 어떤 Pod을 관리할지 지정합니다.
위 경우, 파드의 템플릿 `app: nginx` 로 정의된 레이블을 찾습니다.
파드 템플릿 규칙을 살펴보면, 조금 더 정교하게 규칙을 설정할 수 있습니다.

<table><tr><td>
<b>참고</b> 

`.spec.selector.matchLabels` 필드는 `{key, value}`의 쌍으로 정의합니다.
비슷하게, `matchExpressions`를 사용할 수 있는데요.
`matchExpressions` 는 정교한 조건을 담아 `selector`를 설정할 수 있습니다. 

```yaml
selector:
  matchLabels:
    component: redis
  matchExpressions:
    - { key: tier, operator: In, values: [cache] }
    - { key: environment, operator: NotIn, values: [dev] }
```

이 때, `matchLabels` 에 정의된 `{key, value}` 쌍은,
`matchExpressions`에 `{ key: ≪key≫, operator: In, values: ≪value≫}` 를 입력하는 것과 동일합니다.
즉, key 필드는 `matchLabels`의 "key"값을, `operator`는 "In", `values` 는 `matchLabels`의 "value"를 명시하는 것과 동일합니다.


</td></tr></table>
<br/>

#### ❸

템플릿의 필드는 다음과 같은 하위 필드들을 포함합니다:

- ❸.①: Pod는 `.metadata.labels`를 통해 `app: nginx`로 라벨링됩니다.
- ❸.②: 파드 템플릿을 정의하거나 `.spec.template.spec` 필드에 명시한 내용들은 하나의 컨테이너에서 동작한다는 것을 의미합니다.
  위 예시에서는 파드가 `nginx:1.14.2` 이미지를 사용하여, `nginx` 이름으로 Port 80에서 생성시키라는 설정입니다.
- ❸.③: `.spec.template.spec.containers[0].name`에 `nginx`을 설정함으로써,`nginx` 명의 컨테이너가 생성됩니다.


## Deployment command


| Goal                                                                                          | Command                                                                                                     |
|-----------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| Create an NGINX Pod                                                                           | kubectl run nginx --image=nginx                                                                             |
| Generate POD Manifest YAML file (-o yaml). Don't create it(--dry-run)                         | kubectl run nginx --image=nginx --dry-run=client -o yaml                                                    |
| Create a deployment                                                                           | kubectl create deployment --image=nginx nginx                                                               |
| Generate Deployment YAML file (-o yaml). Don't create it(--dry-run)                           | kubectl create deployment --image=nginx nginx --dry-run=client -o yaml                                      |
| Generate Deployment YAML file (-o yaml). Don’t create it(–dry-run) and save it to a file.     | kubectl create deployment --image=nginx nginx --dry-run=client -o yaml > nginx-deployment.yaml              |
| Make necessary changes to the file (ex, adding more replicas) and then create the deployment. | kubectl create -f nginx-deployment.yaml                                                                     |
| Create a deployment with 4 replicas by specifying `--replicas` option (In k8s version 1.19+)  | kubectl create deployment --image=nginx nginx --replicas=4 --dry-run=client -o yaml > nginx-deployment.yaml |

