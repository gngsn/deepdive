# Role Based Access Controls

역할을 생성하는 방법

<br>

### STEP1. Create Role

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: pod-reader
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["get", "watch", "list"]
  ```

각 규칙은 세 섹션으로 나뉨 - `apiGroups`, `resources`, `verbs`

`apiGroups` 을 빈칸으로 지정하면 핵심 그룹 - Core API Group 의미

이외 다른 그룹은 이름을 지정해야 함

하나의 Role 객체에는 여러개의 rules 을 가질 수 있음

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: pod-reader
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["get", "watch", "list"]
- apiGroups: [""]
  resources: ["ConfigMap"]
  verbs: ["create"]
```

정의한 역할 `Role`은 아래 명령어로 생성

```Bash
❯ kubectl create -f developer-role.yaml
role.rbac.authorization.k8s.io/developer created
```

<br>

### STEP2. Create RoleBinding

다음은 사용자가 특정 역할을 가질 수 있게 둘을 연결시켜야 함 → `RoleBinding`

```yaml
apiVersion: rbac.authorization.k8s.io/v1
# This role binding allows "jane" to read pods in the "default" namespace.
# You need to already have a Role named "pod-reader" in that namespace.
kind: RoleBinding
metadata:
  name: read-pods
  namespace: default
subjects:
# You can specify more than one "subject"
- kind: User
  name: jane # "name" is case sensitive
  apiGroup: rbac.authorization.k8s.io
roleRef:
  # "roleRef" specifies the binding to a Role / ClusterRole
  kind: Role #this must be Role or ClusterRole
  name: pod-reader # this must match the name of the Role or ClusterRole you wish to bind to
  apiGroup: rbac.authorization.k8s.io
```

[🔗 kubernetes.io - Using RBAC Authorization](https://kubernetes.io/docs/reference/access-authn-authz/rbac/)

정의한 역할 `Role`은 아래 명령어로 생성

```Bash
❯ kubectl create -f devuser-developer-rolebinding.yaml
rolebinding.rbac.authorization.k8s.io/devuser-developer-binding created
```

<br>

가령, `dev-user`가 `developer` 역할을 갖게 하려면 아래와 같이 지정할 수 있음

<br>
<table>
<tr>
  <th>Role</th>
  <th>RoleBinding</th>
</tr>
<tr>
<td>

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: developer
rules:
- apiGroups: [""] 
  resources: ["pods"]
  verbs: ["get", "list", "update", "create", "delete"]
- apiGroups: [""]
  resources: ["ConfigMap"]
  verbs: ["create"]
```

</td>
<td>

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: devuser-developer-binding
subjects:
  - kind: User
    name: dev-user
    apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: developer
  apiGroup: rbac.authorization.k8s.io
```

</td>
</tr>
</table>
<br>

생성된 역할을 확인하려면, kubectl 명령어로 확인할 수 있음

<br>

### Role 확인 

**✔️ Role 확인** 

```Bash
❯ kubectl get roles
NAME        CREATED AT
developer   2024-05-12T03:33:24Z
```

<br>

**✔️ RoleBindning 확인** 

```Bash
❯ kubectl get rolebindings
NAME                        ROLE             AGE
devuser-developer-binding   Role/developer   40s
```

<br>

**✔️ Role 상세 내용** 

```Bash
❯ kubectl describe role developer
Name:         developer
Labels:       <none>
Annotations:  <none>
PolicyRule:
  Resources  Non-Resource URLs  Resource Names  Verbs
  ---------  -----------------  --------------  -----
  ConfigMap  []                 []              [create]
  pods       []                 []              [get list update create delete]
```

<br>

**✔️ RoleBinding 상세 내용** 

```Bash
❯ kubectl describe rolebinding devuser-developer-binding
Name:         devuser-developer-binding
Labels:       <none>
Annotations:  <none>
Role:
  Kind:  Role
  Name:  developer
Subjects:
  Kind  Name      Namespace
  ----  ----      ---------
  User  dev-user
```

---

### Check Access

사용자가 특성 리소스에 대해 권한을 갖고 있는지 확인하고 싶다면,

```Bash
❯ kubectl auth can-i create deployments
yes

❯ kubectl auth can-i delete nodes
no
```

`--as` 옵션을 사용하면 다른 사용자인 것처럼 확인할 수 있음

```Bash
❯ kubectl auth can-i create deployments --as dev-user
yes

❯ kubectl auth can-i delete nodes --as dev-user
no
```

특정 namespace 를 지정할 수도 있음

```Bash
❯ kubectl auth can-i create deployments --as dev-user --namespace test
no
```

---

### Resource Names

특정 리소스의 일부만을 접근하게 하고 싶다면, 이름을 명시해서 제한할 수 있음

가령 아래와 같이 5개의 Pod 가 존재할 때,

```
   🔴     🟠     🟢     🟣      🟤
  red   orange  green  purple  brown
```

오직 `Orage` 와 `Purple` Pod 에만 접근하게 하려면 아애 `resourceNames` 을 작성할 수 있음

<br>
<pre><code>
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: developer
rules:
- apiGroups: [""] 
  resources: ["pods"]
  verbs: ["get", "create", "update"]
  <b>resourceNames: ["red", "purple"]</b>
</code></pre>
