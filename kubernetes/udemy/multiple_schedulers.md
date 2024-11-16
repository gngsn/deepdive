## Multiple Schedulers


기본 Scheduler는 여러 노드에 걸쳐 Pod를 고르게 분배하는 알고리즘이 존재

\+ Taint & Toleration, Affinity 등 다양한 조건을 고려

하지만 이중에 만족스러운 게 없다면?

가령, 특정 조건을 추가하여, 특정 앱을 추가적인 판단 후 특정 노드에 배치하고자 할 때

쿠버네티스 클러스터는 한 번에 여러 스케쥴러를 가질 수 있고, 확장성이 굉장히 좋음

쿠버네티스 스케줄러를 생성해 기본 스케줄러, 혹은 부가적인 스케줄러로 패키지하고 배포할 수 있음

Pod를 만들거나 배치할 때 쿠버네티스에게 특정 일정 관리자가 Pod를 지정하도록 지시할 수 있음

Scheduler가 여럿일 경우, 각 Scheduler의 구분을 위해 반드시 이름이 달라야 함

<br/>

1. default-scheduler

__scheduler-config.yaml__
 
```yaml
apiVersion: kubescheduler.config.k8s.io/v1
kind: KubeSchedulerConfiguration
profiles:
  - schedulerName: default-scheduler
```

이름을 지정하지 않으면 기본 Scheduler로 지정하기 때문에, Default Schdeuler 는 사실 반드시 필요한 것은 아님

굳이 정의하자면 위와 같음 

<br/>

2.  Another scheduler

__my-scheduler-config.yaml__

```yaml
apiVersion: kubescheduler.config.k8s.io/v1
kind: KubeSchedulerConfiguration
profiles:
  - schedulerName: my-scheduler
```

추가적인 스케줄러는 위와 같이 설정할 수 있음

<br/>

### Deploy Additional Scheduler

```Bash
wget https://storage.googleapis.com/kubernetes-release/release/v1.12.0/bin/linux/amd64/kube-scheduler
```

kube-scheduler 바이너리를 다운로드해 여러 옵션과 함께 서비스로 실행하는 것

추가적인 스케줄러를 배포하기 위해서는 동일한 kube-scheduler 바이너리를 이용할 수 있음

```Bash
# kube-scheduler.serivce
ExecStart=/usr/local/bin/kube-scheduler \\
      --config=/etc/kubernetes/config/kube-scheduler.yaml
```

```Bash
# my-scheduler.serivce
ExecStart=/usr/local/bin/kube-scheduler \\
      --config=/etc/kubernetes/config/my-scheduler-config.yaml
```

혹은 직접 구축한 것을 이용할 수 있음

스케줄러가 다르게 작동하기를 원할 때 할 수 있는 것이죠

이 경우엔 같은 바이너리를 이용해 추가적인 스케쥴러를 배포하죠


---

#### Deploy Additional Scheduler as a Pod

99%의 경우 사용자 지정 스케쥴러를 이렇게 배포하진 않음

kubeadm 배포에선, 모든 컨트롤 플레인의 구성 요소가 쿠버네티스 클러스터 안에서 Pod로 또는 배포로 실행되기 때문

**스케줄러를 Pod로 배포한 경우 어떻게 작동?**

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-custom-scheduler
  namespace: kube-system
spec:
  containers:
    - command:
        - kube-scheduler
        - --address=127.0.0.1
        - --kubeconfig=/etc/kubernetes/scheduler.conf   # Kubernetes API 서버에 연결하기 위한 인증 정보를 가지고 있는 스케줄러 conf 파일의 경로
        - --config=/etc/kubernetes/my-scheduler-config.yaml
          
      image: k8s.gcr.io/kube-scheduler-amd64:v1.11.3
      name: kube-scheduler
```

우리는 포드 정의 파일을 만들고 kubeconfig 속성을 지정

그 다음, 사용자 지정 kube-scheduler 구성 파일을 스케쥴러에 대한 구성 옵션으로 넘김

지정한 옵션 파일에 스케줄러 이름이 명시되어 있기 때문에 Scheduler가 커스텀 Scheduler를 선택할 수 있게 됨 


__my-scheduler-config.yaml__

```yaml
apiVersion: kubescheduler.config.k8s.io/v1
kind: KubeSchedulerConfiguration
profiles:
  - schedulerName: my-scheduler
leaderElection:
  leaderElect: true
  resourceNamespace: kube-system
  resourceName: lock-object-my-scheduler
```

### leaderElection

또 다른 중요한 옵션은, 이건 kube-scheduler YAML 설정 파일에 지정할 수 있는 지도자 선출 옵션 (`leaderElection`)

리더 선별 옵션은 **동일한 스케줄러 여러 대**가 **서로 다른 여러 대의 마스터 노드**에서 실행될 때, 리더를 선출하기 위해 활성화될 수 있음

새롭게 정의된 Scheduler를 선출 과정에 구분을 주기 위한 `log name` 옵션을 줄 수도 있음 

<pre>
이 모든 과정을 수행하기 위해서 필수 조건이 있는데, 
가령 인증을 위한 ServiceAccount 와 ClusterRoleBinding 등이 있음 
나중에 더 알아볼 필요가 있음
</pre>

**1. 로컬 --볼륨마운트-→ 클러스터** 

🔗 [Configure Multiple Schedulers](https://kubernetes.io/docs/tasks/extend-kubernetes/configure-multiple-schedulers/)

<br/>

파일을 로컬에서 만들어 볼륨 마운트로 넘길 수도 있음 (`--config=/etc/kubernetes/my-scheduler/my-scheduler-config.yaml`)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    component: scheduler
    tier: control-plane
  name: my-scheduler
  namespace: kube-system
spec:
  selector:
    matchLabels:
      component: scheduler
      tier: control-plane
  replicas: 1
  template:
    metadata:
      labels:
        component: scheduler
        tier: control-plane
        version: second
    spec:
      serviceAccountName: my-scheduler
      containers:
      - command:
        - /usr/local/bin/kube-scheduler
        - --config=/etc/kubernetes/my-scheduler/my-scheduler-config.yaml
        image: gcr.io/my-gcp-project/my-kube-scheduler:1.0
        livenessProbe:
        ...
      volumes:
        - name: config-volume
          configMap:
            name: my-scheduler-config
```

이때, `my-scheduler-config.yaml` 는 Volume 을 통해 전달됨

<br/>

**2. ConfigMap**

혹은, ConfigMap을 생성할 수 있음

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-scheduler-config
  namespace: kube-system
data:
  my-scheduler-config.yaml: |
    apiVersion: kubescheduler.config.k8s.io/v1beta2
    kind: KubeSchedulerConfiguration
    profiles:
      - schedulerName: my-scheduler
    leaderElection:
      leaderElect: false  
```

**✔️ 여기서 주목할 점은 생성한 Scheduler를 어떻게 넘겨주는 지에 대한 이해** 

<br/><br/>

### View Schedulers

```Bash
kubectl get pods --namespace=kube-system
```

namespace 지정 필수 

<br/><br/>

### Use Custom Scheduler

<pre><code lang="yaml">
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
  - name: nginx
    image: nginx
    
  <b>schedulerName: my-custom-scheduler</b>
</code></pre>

schedulerName 필드에 사용하고자 하는 Scheduler 이름을 명시하면 됨

만약, 명시된 Scheduler가 제대로 동작하고 있지 않으면 위 Pod는 계속 `Pending` 상태로 남음

<br/><br/>

### View Events

모든 동작이 제대로 이뤄지고 있는지 확인하려면 아래 명령어로 확인할 수 있음

```Bash
kubectl get events -o wide
```

혹은 `kubectl logs` 명령어로 로그를 확인할 수 있음

```Bash
kubectl logs my-custom-scheduler --name-space=kube-system
```

---



```Bash
kubectl create configmap my-scheduler-config --from-file=/root/my-scheduler-config.yaml -n kube-system
```
