## Question 6.

> Create a new user called `john`. Grant him access to the cluster. John should have permission to `create`, `list`, `get`, `update` and `delete` pods in the `development` namespace . The private key exists in the location: `/root/CKA/john.key` and csr at `/root/CKA/john.csr`.
>
> **Important Note**: As of kubernetes `1.19`, the CertificateSigningRequest object expects a `signerName`.
>
> Please refer the documentation to see an example. The documentation tab is available at the top right of terminal.

<br>

### Answer

> **_TOC_**
> 1. CertificateSigningRequest 생성
> 2. Role 생성
> 3. RoleBinding 생성
> 4. auth can-i 확인

#### 1. CertificateSigningRequest 생성

[🔗 Create a CertificateSigningRequest](https://kubernetes.io/docs/reference/access-authn-authz/certificate-signing-requests/#create-certificatessigningrequest) 참고 해서 CSR 생성

**1.1. john csr 파일 `request` 에 추가할 base64 인코딩한 데이터 추출** 

```Bash
controlplane ~ ➜  cat /root/CKA/john.csr | base64 | tr -d "\n"
LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJQ1ZEQ0NBVHdDQVFBd0R6RU5NQXNHQTFVRUF3d0VhbTlvYmpDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRApnZ0VQQURDQ0FRb0NnZ0VCQUwyS2w0ZjlvSHVVSXhBU1JweC8vWERmSW95MDFMeitFQXRvbDJBVHdOeERCZEt3CkhxZ3BYUVhCdkQ2S2JtWGluUkZGVXpjNXdrRUlqSnp6UUIvbWV6cjhGTjVaMGtnblhFbXlBeHdteGNFNWJYM3YKVVFoWEZLcFdkenoreEY2MFRiaGl4ekhydVowaE9XejIzWFQrUExiaTVEc1k3ZVpZR2VXUEc2MmU1KzJkWDk4bApPMVBRdUdvaUgzRFo4VTBCTENzTWhVRTU3TUJUMEp1Q29EbEhKYjgzY1lUQnEwSnJpWmlPenN6VHVjOEVYZlFCCkJIVERQNm9JMkFYNVhsMW5vVWxJZ0FIa2FrTXpyMzJxTCs3UDVGSTlBdUMwa2VzMDFXM1VpWjVLOVdaYVdVclkKTjVsQXpSNk1NUlZGOVNQOUpUU1k4WGNlLzN5LzM3dUExdDRKR29FQ0F3RUFBYUFBTUEwR0NTcUdTSWIzRFFFQgpDd1VBQTRJQkFRQVlnVGFCZFp0eDZySzNkZEYrUEpIeVBEa1IyZktkT01jR3YwSzJBS3hBd05GTUJoM0pMOXNSCmcxQ0JqOWhUT0xMMDkyV3hFSzdKQ0lra2taSTltdklLeW5yYzd3ZDVUeUhWTVQwTXI4dTBSV1JjWDl5MFBkbU4KdnNtcVJYNFBZOVNYY2QrdFRRR1NOSjJWSkx4aWFMaWhEcEQ5NmFRaVN0S0ZJZ2lMNFhDWVYzdk14YXBhOVYwUwpCVG1GS2ZvRUlsd3IwMmJ1NlVyTE9wVjdON29PdHlhcXc5K2o1a08zbFpLcVpXWTBFOTJrSTV0aDFhRmRaTzZ6ClpmcXYzd1lZbkI5azNLZFNRRVpVWFpNSTRtc0VLTFpQYlJGd0MyNHJMaW5GTXN4TVR4Zzl4alZlM09SSHVBZ0EKUGpYaUFxQlh3YkxxTk85UVViTGRkblpoSDBWK0Vick4KLS0tLS1FTkQgQ0VSVElGSUNBVEUgUkVRVUVTVC0tLS0tCg==
```

**1.2. `CertificateSigningRequest` 객체 생성**

```Bash
controlplane ~ ➜  cat <<EOF | kubectl apply -f -
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: john-developer
spec:
  request: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJQ1ZEQ0NBVHdDQVFBd0R6RU5NQXNHQTFVRUF3d0VhbTlvYmpDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRApnZ0VQQURDQ0FRb0NnZ0VCQUwyS2w0ZjlvSHVVSXhBU1JweC8vWERmSW95MDFMeitFQXRvbDJBVHdOeERCZEt3CkhxZ3BYUVhCdkQ2S2JtWGluUkZGVXpjNXdrRUlqSnp6UUIvbWV6cjhGTjVaMGtnblhFbXlBeHdteGNFNWJYM3YKVVFoWEZLcFdkenoreEY2MFRiaGl4ekhydVowaE9XejIzWFQrUExiaTVEc1k3ZVpZR2VXUEc2MmU1KzJkWDk4bApPMVBRdUdvaUgzRFo4VTBCTENzTWhVRTU3TUJUMEp1Q29EbEhKYjgzY1lUQnEwSnJpWmlPenN6VHVjOEVYZlFCCkJIVERQNm9JMkFYNVhsMW5vVWxJZ0FIa2FrTXpyMzJxTCs3UDVGSTlBdUMwa2VzMDFXM1VpWjVLOVdaYVdVclkKTjVsQXpSNk1NUlZGOVNQOUpUU1k4WGNlLzN5LzM3dUExdDRKR29FQ0F3RUFBYUFBTUEwR0NTcUdTSWIzRFFFQgpDd1VBQTRJQkFRQVlnVGFCZFp0eDZySzNkZEYrUEpIeVBEa1IyZktkT01jR3YwSzJBS3hBd05GTUJoM0pMOXNSCmcxQ0JqOWhUT0xMMDkyV3hFSzdKQ0lra2taSTltdklLeW5yYzd3ZDVUeUhWTVQwTXI4dTBSV1JjWDl5MFBkbU4KdnNtcVJYNFBZOVNYY2QrdFRRR1NOSjJWSkx4aWFMaWhEcEQ5NmFRaVN0S0ZJZ2lMNFhDWVYzdk14YXBhOVYwUwpCVG1GS2ZvRUlsd3IwMmJ1NlVyTE9wVjdON29PdHlhcXc5K2o1a08zbFpLcVpXWTBFOTJrSTV0aDFhRmRaTzZ6ClpmcXYzd1lZbkI5azNLZFNRRVpVWFpNSTRtc0VLTFpQYlJGd0MyNHJMaW5GTXN4TVR4Zzl4alZlM09SSHVBZ0EKUGpYaUFxQlh3YkxxTk85UVViTGRkblpoSDBWK0Vick4KLS0tLS1FTkQgQ0VSVElGSUNBVEUgUkVRVUVTVC0tLS0tCg==
  signerName: kubernetes.io/kube-apiserver-client
  expirationSeconds: 86400  # one day
  usages:
  - client auth
EOF
```

생성된 CSR 확인 

```Bash
controlplane ~ ➜  kubectl get csr
NAME             AGE   SIGNERNAME                                    REQUESTOR                  REQUESTEDDURATION   CONDITION
john-developer   6s    kubernetes.io/kube-apiserver-client           kubernetes-admin           24h                 Pending
...
```

<br>

#### 2. Role 생성

```
controlplane ~ ➜  kubectl create role developer --resource=pods --verb=create,list,get,update,delete -n development
role.rbac.authorization.k8s.io/developer created

controlplane ~ ➜  k get roles -n development
NAME        CREATED AT
developer   2024-07-28T07:02:33Z

controlplane ~ ➜  k describe role developer -n development
Name:         developer
Labels:       <none>
Annotations:  <none>
PolicyRule:
  Resources  Non-Resource URLs  Resource Names  Verbs
  ---------  -----------------  --------------  -----
  pods       []                 []              [create list get update delete]        # ← 추가된 권한 확인
```

#### 3. RoleBinding 생성

```
controlplane ~ ➜  kubectl create rolebinding developer-role-binding --role=developer --user=john -n development
rolebinding.rbac.authorization.k8s.io/developer-role-binding created

controlplane ~ ➜  k get rolebinding -n development
NAME                     ROLE             AGE
developer-role-binding   Role/developer   18s

controlplane ~ ✖ k describe rolebinding.rbac.authorization.k8s.io developer -n development        # ← rolebinding 이랑 동일
Name:         developer-role-binding
Labels:       <none>
Annotations:  <none>
Role:
  Kind:  Role
  Name:  developer
Subjects:
  Kind  Name  Namespace
  ----  ----  ---------
  User  john
```


#### 4. auth can-i 확인

```Bash
controlplane ~ ➜  kubectl auth can-i update pods --as=john -n development
yes

controlplane ~ ➜  kubectl auth can-i create pods --as=john -n development
yes
```

<br><br>

---

#### Q7. Create a nginx pod called `nginx-resolver` using image nginx, expose it internally with a service called `nginx-resolver-service`. Test that you are able to look up the service and pod names from within the cluster. Use the `image: busybox:1.28` for dns lookup. Record results in `/root/CKA/nginx.svc` and `/root/CKA/nginx.pod`

<details>
<summary>Shortcut</summary>

Use the command `kubectl run` and create a nginx pod and `busybox` pod. 
Resolve it, nginx service and its pod name from `busybox` pod.

To create a pod `nginx-resolver` and expose it internally:

```bash
kubectl run nginx-resolver --image=nginx
kubectl expose pod nginx-resolver --name=nginx-resolver-service --port=80 --target-port=80 --type=ClusterIP
```

To create a pod `test-nslookup`. 
Test that you are able to look up the service and pod names from within the cluster:

```bash
kubectl run test-nslookup --image=busybox:1.28 --rm -it --restart=Never -- nslookup nginx-resolver-service
kubectl run test-nslookup --image=busybox:1.28 --rm -it --restart=Never -- nslookup nginx-resolver-service > /root/CKA/nginx.svc
```

Get the IP of the `nginx-resolver` pod and replace the dots(`.`) with hyphon(`-`) which will be used below.

```bash
kubectl get pod nginx-resolver -o wide
kubectl run test-nslookup --image=busybox:1.28 --rm -it --restart=Never -- nslookup <P-O-D-I-P.default.pod> > /root/CKA/nginx.pod
```

</details>

<br>

#### Answer

**1. `nginx-resolver` Pod 생성**

```Bash
controlplane ~ ➜ k run nginx-resolver --image=nginx
pod/nginx-resolver created
```

**2. `nginx-resolver-service` Service 생성: `kubectl expose`**

```Bash
controlplane ~ ➜  kubectl expose pod nginx-resolver --port=80 --name=nginx-resolver-service
service/nginx-resolver-service exposed
```


생성된 Pod & Service 확인

```Bash
controlplane ~ ➜  k describe svc nginx-resolver-service 
Name:              nginx-resolver-service
Namespace:         default
Labels:            run=nginx-resolver
Annotations:       <none>
Selector:          run=nginx-resolver
Type:              ClusterIP
IP Family Policy:  SingleStack
IP Families:       IPv4
IP:                10.107.164.1
IPs:               10.107.164.1
Port:              <unset>  80/TCP
TargetPort:        80/TCP
Endpoints:         10.244.192.1:80
Session Affinity:  None
Events:            <none>
```

**3. 네트워크 통신을 위한 `busybox` Pod 생성**

```Bash
controlplane ~ ➜  k run busybox --image=busybox:1.28 -- sleep 4800
pod/busybox created
```

```Bash
controlplane ~ ➜  k get pods
NAME             READY   STATUS              RESTARTS   AGE
busybox          0/1     ContainerCreating   0          1s
nginx-resolver   1/1     Running             0          118s
```

**3. 연결 체크를 위한 `busybox` Pod 생성**

```Bash
controlplane ~ ➜  k exec busybox -- nslookup nginx-resolver-service
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      nginx-resolver-service
Address 1: 10.107.164.1 nginx-resolver-service.default.svc.cluster.local
```

**4. `/root/CKA/nginx.svc` 저장**

```Bash
controlplane ~ ➜  k exec busybox -- nslookup nginx-resolver-service > /root/CKA/nginx.svc
```

**5. `/root/CKA/nginx.pod` 저장**

```Bash
controlplane ~ ✖ k get pods -o wide
NAME             READY   STATUS    RESTARTS   AGE     IP             NODE     NOMINATED NODE   READINESS GATES
busybox          1/1     Running   0          81s     10.244.192.2   node01   <none>           <none>
nginx-resolver   1/1     Running   0          3m18s   10.244.192.1   node01   <none>           <none>
```

[🔗 DNS for Services and Pods: Services](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/#services) 참고

```Bash
controlplane ~ ✖ k exec  busybox -- nslookup 10-244-192-1.default.pod.cluster.local
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      10-244-192-1.default.pod.cluster.local
Address 1: 10.244.192.1 10-244-192-1.nginx-resolver-service.default.svc.cluster.local

controlplane ~ ➜  k exec  busybox -- nslookup 10-244-192-1.default.pod.cluster.local > /root/CKA/nginx.pod
```

생성한 파일 확인

```Bash
controlplane ~ ➜  cat /root/CKA/nginx.svc
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      nginx-resolver-service
Address 1: 10.107.164.1 nginx-resolver-service.default.svc.cluster.local

controlplane ~ ➜  cat /root/CKA/nginx.pod
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      10-244-192-1.default.pod.cluster.local
Address 1: 10.244.192.1 10-244-192-1.nginx-resolver-service.default.svc.cluster.local
```


---

#### Q8. Create a static pod on `node01` called `nginx-critical` with image `nginx` and make sure that it is `recreated/restarted` automatically in case of a failure.

Use `/etc/kubernetes/manifests` as the Static Pod path for example.

<br>

### Answer

**1. `controlplane`: `node01` IP 확인**

```Bash
controlplane ~ ➜  k get nodes -o wide
NAME           STATUS   ROLES           AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION   CONTAINER-RUNTIME
controlplane   Ready    control-plane   54m   v1.30.0   192.9.236.9    <none>        Ubuntu 22.04.4 LTS   5.4.0-1106-gcp   containerd://1.6.26
node01         Ready    <none>          53m   v1.30.0   192.9.236.12   <none>        Ubuntu 22.04.4 LTS   5.4.0-1106-gcp   containerd://1.6.26
```


**2. `node01` 접속**

```Bash
controlplane ~ ➜  ssh 192.9.236.12
```

**3. nginx Static Pod 생성**

```
controlplane ~ ➜  kubectl run nginx-critical --image=nginx --restart=Always --dry-run=client -o yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: nginx-critical
  name: nginx-critical
spec:
  containers:
  - image: nginx
    name: nginx-critical
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
controlplane ~ ➜  kubectl run nginx-critical --image=nginx --restart=Always --dry-run=client -o yaml > /etc/kubernetes/manifests/nginx-critical.yaml
```

생성 확인

```
controlplane ~ ➜  kubectl get pods -o wide
node01 ~ ➜  kubectl get pods -o wide
NAME                    READY   STATUS    RESTARTS   AGE    IP             NODE     NOMINATED NODE   READINESS GATES
nginx-critical-node01   1/1     Running   0          4m3s   10.244.192.1   node01   <none>           <none>
```

<details>
<summary><code>[ERROR] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s"</code></summary>

```bash
node01 ~ ➜  kubectl get pods
E0728 08:12:42.669295    6101 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0728 08:12:42.669770    6101 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0728 08:12:42.671288    6101 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0728 08:12:42.671575    6101 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0728 08:12:42.673133    6101 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
The connection to the server localhost:8080 was refused - did you specify the right host or port?
```

#### 원인 

쿠버네티스 컨피그 파일이 $HOME/.kube 디렉토리 아래 존재하지 않을 때

현재 유저정보가 쿠버네티스 컨피그 파일에 반영되지 않은 경우에 발생

#### 해결 방법

```Bash
node01 ~ ➜  cp /etc/kubernetes/kubelet.conf .kube/config
```



</details>










