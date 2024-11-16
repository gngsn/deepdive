# Authentication

배포된 클러스터 내에서, 접근 가능한 경우의 수를 따져보면 네 가지의 주체를 생각해볼 수 있음

- Admins
- Developers
- Bots
- Application End Users: 애플리케이션 내부 관리

쿠버네티스 클러스터의 운영 관리 목적으로 바라보면 'User (Admin & Developer)' 와 'Service Account (Bot)' 를 구분할 수 있음   

쿠버네티스는 사용자 계정을 자체에서 개발하지는 않지만, 
사용자 상세 정보나 인증서(Certification) 등 외부 자원을 통해 인증거나,
사용자 관리를 위한 LDAP 같은 외부 인증 서비스를 사용 

<br>

### Service Account

쿠버네티스는 User 자체를 관리하지는 않지만 Service Account는 관리하고 있음

<table>
<tr><th>Create</th><th>List</th></tr>
<tr><td>

```Bash
❯ kubectl create serviceaccount sa1
Service Account sa1 Created
```

</td><td>

```Bash
❯ kubectl create serviceaccount sa1
Service Account
sa1
```

</td></tr>
</table>


`kubectl` 명령어를 통해, 혹은 `curl https://kube-server-ip:6443/` Curl 명령어를 통해 kube-apiserver에 접근할 수 있는데,
두 방식 모두 프로세싱 전, 사용자 인증을 먼저 진행함

<br><img src="./img/authentication_img1.png" /><br>


## Auth Mechanisms

여러 인증 방식을 사용할 수 있음

- Static Password File
- Static Token File
- Certificates
- Identity Services (ex. LDAP, Kerberos ...)

<br>

### 1. Basic Authentication: Static Password / Token File


> **⚠️ Text / Token 인증 메커니즘 비권장**
> - 고정 파일에 사용자 이름, 암호, 토큰을 단순 텍스트로 저장하기 때문에 불안정
> - 단순 개념 이해 목적의 소개
>
> **⚠️ Auth 파일을 가져올 볼륨 마운트 고려**


비밀번호나 토큰을 사용한 방식으로는, 인증 정보를 담은 파일을 쿠버네티스 시작 명령어의 옵션으로 추가해줄 수 있음 

<br>

간략히 살펴보면 아래와 그림과 같음

<br><img src="./img/authentication_img2.png" /><br>

한 단계씩 알아보자

<br>

##### ✔️ First. Create User Detail File 

<i>user-details.csv</i>

```Bash
password123,user1,u00001
password456,user2,u00002
password789,user3,u00003
password101,user4,u00004
```

<br>

##### ✔️ Second. Set Volumes Mount to pass the file 

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  name: kube-apiserver
  namespace: kube-system
spec:
  containers:
  - command:
    - kube-apiserver
      <content-hidden>
    image: k8s.gcr.io/kube-apiserver-amd64:v1.11.3
    name: kube-apiserver
    <b>volumeMounts:
    - mountPath: /tmp/users
      name: usr-details
      readOnly: true
  volumes:
  - hostPath:
      path: /tmp/users
      type: DirectoryOrCreate
    name: usr-details</b>
</code></pre>

<br>

##### ✔️ Third. Edit kube-apiserver Startup Option

만약 kubeadm 툴을 통해 시작했다면, 반드시 kube-api-server Pod 정의 파일을 수정해야 함

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  name: kube-apiserver
  namespace: kube-system
spec:
  containers:
  - command:
    - kube-apiserver
    - --authorization-mode=Node,RBAC
      <content-hidden>
    - <b>--basic-auth-file=/tmp/users/user-details.csv</b>
</code></pre>

<br>

##### ✔️ Fourth. Create the necessary roles & role bindings 

<pre><code lang="yaml">---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
namespace: default
name: pod-reader
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["get", "watch", "list"]

---
# This role binding allows "jane" to read pods in the "default" namespace.
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
name: read-pods
namespace: default
subjects:
- kind: User
  name: user1 # Name is case sensitive
  apiGroup: rbac.authorization.k8s.io
  roleRef:
  kind: Role #this must be Role or ClusterRole
  name: pod-reader # this must match the name of the Role or ClusterRole you wish to bind to
  apiGroup: rbac.authorization.k8s.io
</code></pre>

<br>

##### ✔️ Fifth. Authenticate into the kube-api server

이후, API Server에 접근하고 싶다면 curl 명령어 시 아래 옵션을 추가하면 됨

<pre><code>
❯ curl -v -k https://master-node-ip:6443/api/v1/pods -u <b>"user1:password123"</b>
</code></pre>

<br>

**📌 with Token**

토큰도 동일한데, 파일과 인증 방법을 살펴보면 아래와 같음

<br>
<table>
<tr>
    <th>user-token-details.csv</th>
    <th>Cluster Start Option</th>
    <th>Authentication</th>
</tr>
<tr><td>

```Bash
y4JcjGQbKfJ3o4tWPCHWzMLD4We8OcHVxO4Q63TDXgSF5eDLblLsztA7Lvm0fxbZ,user1,u00001
IYUDhgVf4ImovpVtdCFFoY4Dzbs88t1ZYCa3uxyqlITduC9KbqYjwmvWtTulvw7e,user2,u00002
aPHklIfOLZcG4sim3HfpPYePU0UZgdQ2BbJ4d5leALv6X23vO09jsjp5T7XFqVhA,user3,u00003
4qET6UOJQJu3HtKHQuBtKqHjkkQrAwxFOP65SsJ2AhFAmIuGXEYFmesh9sZ9WGrx,user4,u00004
```

</td><td>

```Bash
ExecStart=/usr/local/bin/kube-apiserver
  ...
  --token-auth--file=user-token-details.csv
```

</td><td>

```Bash
❯ curl -v -k https://master-node-ip:6443/api/v1/pods <b>--header "Authorization: Bearer y4JcjGQbKfJ3o4tWPCHWzMLD4We8OcHVxO4Q63TDXgSF5eDLblLsztA7Lvm0fxbZ"</b>
```

</td></tr>
</table>
<br>
