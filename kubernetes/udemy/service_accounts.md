# Service Accounts

쿠버네티스에는 두 가지 종류의 계정이 존재

- **User** Accounts
  - ex. Admin, Developer
- **Service** Accounts
  - ex. Prometheus, Jenkins

<br>

### Service Account → Kube API Server Authorization

#### ✔️ STEP 1. Create a service account

Create

```Bash
# Create a new service account named my-sa
❯ kubectl create serviceaccount my-sa
serviceaccount/my-sa created
```

List

```Bash
❯ kubectl get serviceaccount
NAME                 SECRETS   AGE
default              0         20h
my-sa   0         5s
```

Describe

```Bash
❯ kubectl describe serviceaccount my-sa
Name:                my-sa
Namespace:           default
Labels:              <none>
Annotations:         <none>
Image pull secrets:  <none>
Mountable secrets:   <none>
Tokens:              <none>
Events:              <none>
```

<br>

#### ✔️ STEP 2. Assign the right permissions

_using role-based access control mechanisms_

Service Account 는 생성 시 토큰 생성 필요

이후, 쿠버네티스 클러스터에 토큰과 함께 요청 - 특정 시스템에서 접근하는 방식은 모두 인증/인가 과정이 필요하기 때문 

토큰은 Secret 객체로 저장됨

즉, Token 을 위한 Secret Object을 생성한 후, ServiceAccount에 해당 Secret 객체 식별자를 명시해 연결

```Bash
❯ kubectl describe secret my-sa
```

<br>

#### ✔️ STEP 3. Export your service account tokens 

and use it to configure your third-party application to authenticate to the Kubernetes API

생성한 Token 을 쿠버네티스 API 요청 시 함께 요청

```Bash
curl https://192.168.56.70:6443/api insecure \
    --header "Authorization: Bearer eyJhGcd2sWc...RsD2S"
```

---

인증 요청자가 Kubernetes 클러스터에 의해 운영되는 앱이라면, 
토큰을 따로 추출해서 외부 애플리케이션에 설정하여 인증을 요청하는 과정을, 
서비스 토큰 Secret 파일을 마운팅하여 사용하도록 자동화할 수 있음 

가령, Prometheus 앱을 사용하고자 할 때, Pod 내부에 Service Token Secret을 자동으로 마운팅되어 사용할 수 있음

---

모든 namespace에는 default service account 가 존재

Pod가 생성될 때면 항상, default service account 와 토큰이 해당 Pod에 자동으로 Volume 마운팅되어

실제로, Pod 를 생성하면 default service account에 마운팅되어 실행되는 것을 확인할 수 있음

```Bash
❯ kubectl run nginx --image=nginx
pod/nginx created

❯ kubectl describe pod nginx
Name:             nginx
...
Containers:
  nginx:
    Container ID:   containerd://f4c634b320cadc4bdf1a6d368d22e0a3c639c78f671373247538da22603a3207
    ...
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-588gz (ro)
...
Volumes:
  kube-api-access-588gz:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ...
```

Mount 된 `/var/run/secrets/kubernetes.io/serviceaccount` 데이터를 확인해보면 세 개의 파일이 위치한 것을 확인할 수 있음 

```Bash
❯ kubectl exec -it nginx -- ls /var/run/secrets/kubernetes.io/serviceaccount
ca.crt	namespace  token
```

이 중, token 파일을 확인해보면, 실제 사용되는 토큰을 확인할 수 있음 

```Bash
❯ k exec -it nginx -- cat /var/run/secrets/kubernetes.io/serviceaccount/token
eyJhbGciOiJSUz...rXk3IcBbfdaDZoVdJPVEdw
```

만약, Service Account 를 변경하고 싶다면 정의 스펙에 `serviceAccountName`를 수정

⚠️ 이미 생성된 Pod의 serviceAccount는 수정할 수 없음


하지만, Deployment의 경우에는 ServiceAccount를 수정할 수 있는데, 자동으로 새로운 Pod 정의로 Rollout 됨

즉, Deployment는 변경된 ServiceAccount를 취하기 위해 Pod를 새로 생성하고 제거

정리하자면 `serviceAccountName`를 명시하지 않은 경우, Default ServiceAccount를 설정해 토큰을 마운팅하는 과정을 자동으로 수행

만약 토큰을 자동으로 마운팅하는 것을 원치 않다면, `automountServiceAccountToken`을 `false`로 지정할 수 있음


---

### 1.22 → 1.24 변경 사항 

위 `/var/run/secrets/kubernetes.io/serviceaccount/token` 파일의 JWT 토큰의 데이터를 확인해보면, 

```Bash
❯ jq -R 'split(".") | select(length > 0) | .[0],.[1] | @base64d | fromjson' <<< eyJhbGciOiJSU...rXk3IcBbfdaDZoVdJPVEdw
{
  "alg": "RS256",
  "kid": "a2Q0bmEcSjeUUqzwGC08a3Ds3mLFpX52bAP2wjG6zzk"
}
{
  "aud": [
    "https://kubernetes.default.svc.cluster.local"
  ],
  "exp": 1747046637,
  "iat": 1715510637,
  "iss": "https://kubernetes.default.svc.cluster.local",
  "kubernetes.io": {
    "namespace": "default",
    "pod": {
      "name": "nginx",
      "uid": "9030aee4-9e53-49b5-8c7e-8e36fc143d52"
    },
    "serviceaccount": {
      "name": "default",
      "uid": "7fa3f327-4b81-4162-8146-ec8cb9bb1a2c"
    },
    "warnafter": 1715514244
  },
  "nbf": 1715510637,
  "sub": "system:serviceaccount:default:default"
}
```

위 데이터에는 Expire 데이터가 없는 것을 확인할 수 있음 


KEP 1205 - Bound Service Account Token 을 살펴보면,
응집된 Service Account Token을 만들기 위한 Kubernetes 개선 제안서에서는 
이러한 형태의 JWT가 보안 및 확장성과 관련된 문제를 안고 있음을 설명

즉, 당시 JWT는 특정 Audience 와 시간 제약을 갖고 있지 않음  

따라서 JWT는 Service Account가 존재하면 항상 유효했음

게다가 모든 JWT는 각각의 Service Account 마다 분리된 Secret Object를 필요로 하며, 이는 확장성에 문제가 됨 

이후, 1.22 버전에서 TokenRequestAPI 가 소개되었는데,
더 안전하고 확장 가능한 쿠버네티스 서비스 계정 토큰을 프로비전하는 메커니즘이 도입된 것을 알 수 있음

TokenRequestAPI 가 생성한 토큰은 Audience, Time, Object 가 바인딩되면서 더 안전함

1.22 버전 이후에는 Service Account Secret Token 이 생성될 때, 더 이상 토큰이 발급되지 않음

---

### v1.24

Before:

이전에는 Service Account 를 생성하면 Secret이 자동으로 생성되고, 만료기간이 없는 Token이 함께 포함되어 있었음

또, 자동으로 Token 파일이 마운트되어 사용되어 왔음

하지만, 1.22에서 부터 수명을 가진 토큰이 TokenRequestAPI를 통해 생성되었고,
서비스 계정 승인 컨트롤러가 포드가 생성될 때 이 토큰은 Pod에 볼륨 마운트가 됨

v1.24에서는 이제 더 이상 ServiceAccount 를 생성할 때, Secret 은 물론 Token 또한 생성되지 않음

```
❯ kubectl create serviceaccount my-sa
```

위 명령에 해당하는 버전 별 Service Account 은 아래와 같음

```
 +----- [v1.22] Serivce Account -----+   +----- [v1.24] Serivce Account -----+
 |                                   |   |                                   |
 |     +----- 🔐 Secret ------+      |   |                                   |
 |     |                      |      |   |                                   |
 |     |       🎫 Token       |      |   |            ( nothing )            |
 |     |                      |      |   |                                   |
 |     +----------------------+      |   |                                   |
 |                                   |   |                                   |
 +-----------------------------------+   +-----------------------------------+
```

v1.24 부터는 아래와 같이 따로 토큰을 생성해야 함

```
❯ kubectl create token my-sa
```

그럼 토큰이 출력되고, 이 때에는 `exp` 필드를 확인할 수 있음


만약, 예전 방식인 - 만료되지 않는 토큰을 가진 Secret - 으로 ServiceAccount 를 만들고 싶다면
`kubernetes.io/service-account-token` 객체를 만들 수 있음

그리고 `.metadata.annotations` 아래 `kubernetes.io/service-account.name` 필드 값으로 ServiceAccount 를 명시

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: build-robot-secret
  annotations:
    kubernetes.io/service-account.name: my-sa
type: kubernetes.io/service-account-token
```

그럼 이 Secret 이 ServiceAccount에 연결됨


> ServiceAccount token Secrets 
> A kubernetes.io/service-account-token type of Secret is used to store a token credential that identifies a ServiceAccount. This is a legacy mechanism that provides long-lived ServiceAccount credentials to Pods.
> 
> In Kubernetes v1.22 and later, the recommended approach is to obtain a short-lived, automatically rotating ServiceAccount token by using the TokenRequest API instead. You can get these short-lived tokens using the following methods:
> 
> - Call the TokenRequest API either directly or by using an API client like kubectl. For example, you can use the kubectl create token command. 
> - Request a mounted token in a projected volume in your Pod manifest. Kubernetes creates the token and mounts it in the Pod. The token is automatically invalidated when the Pod that it's mounted in is deleted. For details, see Launch a Pod using service account token projection.
> 
> Note: You should only create a ServiceAccount token Secret if you can't use the `TokenRequest` API to obtain a token, and the security exposure of persisting a non-expiring token credential in a readable API object is acceptable to you. For instructions, see Manually create a long-lived API token for a ServiceAccount.
>

[🔗 kubernetes.io - ServiceAccount token Secrets](https://kubernetes.io/docs/concepts/configuration/secret/#serviceaccount-token-secrets)


ServiceAccount token Secrets 생성을 원한다면, 유효 기간이 없는 토큰 자격 증명에 대해 보안상 노출이 허용되는 경우에만 사용해야 함

이제 TokenRequest API를 사용하는 것이 서비스 계정 토큰 비밀 객체보다 권장
