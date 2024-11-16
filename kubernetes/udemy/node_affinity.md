# Node Affinity

> **Affinity**: a close similarity between two things

Node Affinity 기능은 특정 Pod 들이 특정 Node에 위치시는 것을 보장하기 위한 기능

쿠버네티스의 기능 중에 `nodeSelector`가 있지만, `OR`이나 `NOT` 연산 등을 하지 못함

Node Affinity 에서는 복잡한 구문을 통한 Node 매치가 가능

<pre><code lang="json">apiVersion: v1
kind: Pod
metadata:
  name: core-k8s
spec:
  containers:
  - name: data-processor
    image: data-processor
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: size
            operator: In
            values:
            - Large
</code></pre>

여러 Label 들에 함께 적용하고 싶다면 (가령, `Large` 뿐만 아니라 `Medium`)

**values에 Label 추가**

```yaml
...
- matchExpressions:
    - key: size
      operator: In
      values:
        - Large
        - Medium
...
```

혹은 특정 Label만 제외 시키고 싶다면, 아래와 같이 구문 설정

```yaml
...
- matchExpressions:
    - key: size
      operator: NotIn
      values:
        - Small
...
```

<br/>

---

<br/>

위 처럼 Node Affinity 를 사용해서 Pod를 Node 선호도에 따라 배치.


만약, Node에 `Large`라는 Label을 설정할 수 없다면, 어떻게 될까? 
`matchExpressions`에 해당하는 노드가 존재하지 않는다면? 
즉, 배치할 수 있는 Node가 존재하지 않는다면?

(가령, 누군가 Label을 변경한다면?)

**Node Affinity 타입**을 통해, 이를 이해할 수 있음 

이름 자체가 스케줄러의 기능을 정의

<br/>

**Available:**
- `requiredDuringSchedulingIgnoredDuringExecution`
- `preferredDuringSchedulingIgnoredDuringExecution`

**Planned:**
- `requiredDuringSchedulingRequiredDuringExecution`

Node Affinity를 설정할 때, Pod의 라이프사이클 내 두 가지 상태가 존재: `DuringScheduling` 와 `DuringExecution`.


|        | `DuringScheduling`       | `DuringExecution`                    |
|--------|--------------------------|--------------------------------------|
|        | 실행 중인 Pod ❌, 생성되는 Pod ⭕️ | 생성되는 Pod ❌, 실행 중인 Pod 들에 한해 변경을 원할 때 |
| Type 1 | Required                 | Ignored                              |
| Type 2 | Preferred                | Ignored                              |
| Type 3 | Required                 | Required  (affinity 규칙에 맞지 않다면 퇴출)   |

<i><small>Preferred like ...  saying "Try your best to place the pod on matching node"</small></i>


---

1. Apply a label `color=blue` to node `node01`

```Bash
❯ kubectl label node node01 color=blue 
```