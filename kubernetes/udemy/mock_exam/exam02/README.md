### Q1. Take a backup of the etcd cluster and save it to `/opt/etcd-backup.db`.


<br>

#### Answer

Kubernetes 공식 문서에서 `etcd backup` 검색 후 가장 상단의 `Operating etcd clusters for Kubernetes | Kubernetes` 참고

https://kubernetes.io/docs/tasks/administer-cluster/configure-upgrade-etcd/#snapshot-using-etcdctl-options

```Bash
controlplane ~ ✖ cat /etc/kubernetes/manifests/etcd.yaml | grep '\-\-'
    - --advertise-client-urls=https://192.23.197.8:2379
    - --cert-file=/etc/kubernetes/pki/etcd/server.crt
    - --client-cert-auth=true
    - --data-dir=/var/lib/etcd
    - --experimental-initial-corrupt-check=true
    - --experimental-watch-progress-notify-interval=5s
    - --initial-advertise-peer-urls=https://192.23.197.8:2380
    - --initial-cluster=controlplane=https://192.23.197.8:2380
    - --key-file=/etc/kubernetes/pki/etcd/server.key
    - --listen-client-urls=https://127.0.0.1:2379,https://192.23.197.8:2379
    - --listen-metrics-urls=http://127.0.0.1:2381
    - --listen-peer-urls=https://192.23.197.8:2380
    - --name=controlplane
    - --peer-cert-file=/etc/kubernetes/pki/etcd/peer.crt
    - --peer-client-cert-auth=true
    - --peer-key-file=/etc/kubernetes/pki/etcd/peer.key
    - --peer-trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
    - --snapshot-count=10000
    - --trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt

controlplane ~ ✖ ETCDCTL_API=3 etcdctl --endpoints=https://127.0.0.1:2379 \
 --cacert=/etc/kubernetes/pki/etcd/ca.crt \
 --cert=/etc/kubernetes/pki/etcd/server.crt \
 --key=/etc/kubernetes/pki/etcd/server.key \
 snapshot save /opt/etcd-backup.db
Snapshot saved at /opt/etcd-backup.db
```

<br><br>

### Create a Pod called `redis-storage` with image: `redis:alpine` with a Volume of type `emptyDir` that lasts for the life of the Pod.

Specs on the below.

- Pod named 'redis-storage' created
- Pod 'redis-storage' uses Volume type of emptyDir
- Pod 'redis-storage' uses volumeMount with mountPath = /data/redis

<br>

#### Answer


```Bash
controlplane ~ ➜  k run redis-storage --image=redis:alpine --dry-run=client -o yaml > redis-storage.yaml 
controlplane ~ ➜  vi redis-storage.yaml
```

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
name: redis-storage
spec:
  containers:
  - image: redis:alpine
    name: redis-storage-container
    volumeMounts:
    - mountPath: /data/redis
      name: redis-volume
  <b>volumes:               # ← 추가
    - name: redis-volume
      emptyDir: {}</b>
</code></pre>


<br><br>

---

#### Q3. Create a new pod called `super-user-pod` with image `busybox:1.28`. Allow the pod to be able to set `system_time`.

The container should sleep for 4800 seconds.

- **Pod**: `super-user-pod`
- **Container Image**: `busybox:1.28`
- Is `SYS_TIME` capability set for the container?

<br>

### Answer

**Security Context** 관련 문제

→ Kubernetes 공식 문서에 `Security Capabilities` 검색

[Security Context: Set capabilities for a Container](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-capabilities-for-a-container) 내용 확인

```Bash
controlplane ~ ➜  k run super-user-pod --image=busybox:1.28 --dry-run=client -o yaml --command -- sleep 4800 > super-user-pod.yaml 
controlplane ~ ➜  vi super-user-pod.yaml
```

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: super-user-pod
  name: super-user-pod
spec:
  containers:
  - command:
    - sleep
    - "4800"
    image: busybox:1.28
    name: super-user-pod
    resources: {}
    <b>securityContext:           # ← 추가
      capabilities:
        add: ["SYS_TIME"]</b>
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
</code></pre>

---

#### Q4. A pod definition file is created at `/root/CKA/use-pv.yaml`. Make use of this manifest file and mount the persistent volume called `pv-1`. Ensure the pod is running and the PV is bound.

- mountPath: `/data`
- persistentVolumeClaim Name: `my-pvc`
- persistentVolume Claim configured correctly
- pod using the correct mountPath
- pod using the persistent volume claim?

<br>

### Answer

**1. PV 스펙 확인**

```Bash
controlplane ~ ➜  k get pv -o yaml
apiVersion: v1
items:
- apiVersion: v1
  kind: PersistentVolume
  metadata:
    creationTimestamp: "2024-07-27T04:50:58Z"
    finalizers:
    - kubernetes.io/pv-protection
    name: pv-1
    resourceVersion: "4521"
    uid: 879b96c9-470e-4d60-89ff-211e8a9c485f
  spec:
    accessModes:
    - ReadWriteOnce
    capacity:
      storage: 10Mi
    hostPath:
      path: /opt/data
      type: ""
    persistentVolumeReclaimPolicy: Retain
    volumeMode: Filesystem
  status:
    lastPhaseTransitionTime: "2024-07-27T04:50:58Z"
    phase: Available
kind: List
metadata:
  resourceVersion: ""
```

<br>

**2. PVC 작성**

<pre><code lang="yaml">controlplane ~ ➜  vi my-pvc.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-pvc 
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Mi
</code></pre>

```Bash
controlplane ~ ➜  k apply -f my-pvc.yaml
persistentvolumeclaim/my-pvc created
```

<br><br>

**3. Pod 정의 스펙에 명시**

<pre><code lang="yaml">controlplane ~ ➜  vi /root/CKA/use-pv.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: use-pv
  name: use-pv
spec:
  containers:
  - image: nginx
    name: use-pv
    resources: {}<b>
    volumeMounts:
      - mountPath: "/data"
        name: my-pvc-volume</b>
  dnsPolicy: ClusterFirst
  restartPolicy: Always
  <b>volumes:
  - name: my-pvc-volume
    persistentVolumeClaim:
      claimName: my-pvc</b>
status: {}
</code></pre>

생성된 객체 확인 

<pre><code lang="yaml">controlplane ~ ➜  k apply -f /root/CKA/use-pv.yaml 
controlplane ~ ➜  k get pods
NAME             READY   STATUS    RESTARTS   AGE
use-pv           1/1     Running   0          5m35s
...
</code></pre>

생성된 Pod 정보 확인

<pre><code lang="yaml">controlplane ~ ➜  k describe pod use-pv 
Name:             use-pv
...
Containers:
  use-pv:
    Container ID:   containerd://6ae4315a1264d84a0e8b5b488adc9369c199f481ccfa7f7ad03a8a2c177cb74d
    ...
    <b>Mounts:
      /data from my-pvc-volume (rw)</b>     # ← /data 에 mount 되었고 my-pvc-volume 사용
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-cjlpd (ro)
  ...
<b>Volumes:                                 # ← PersistentVolumeClaim 객체 my-pvc-volume 사용 중
  my-pvc-volume:
    Type:       PersistentVolumeClaim (a reference to a PersistentVolumeClaim in the same namespace)
    ClaimName:  my-pvc</b>
    ReadOnly:   false
  ...
</code></pre>

<br><br>

---

### Q5. Create a new deployment called `nginx-deploy`, with image `nginx:1.16` and `1` replica. Next upgrade the deployment to version `1.17` using rolling update.

- **Deployment** : `nginx-deploy`. Image: `nginx:1.16`
- **Image**: `nginx:1.16`
- **Task**: Upgrade the version of the deployment to `1:17`
- **Task**: Record the changes for the image upgrade

<br>

#### Answer

`nginx:1.16` 버전의 Pod 생성

```Bash
controlplane ~ ➜  kubectl create deployment nginx-deploy --image=nginx:1.16 --replicas=1
deployment.apps/nginx-deploy created

controlplane ~ ➜  k get deploy -o wide
NAME           READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS   IMAGES       SELECTOR
nginx-deploy   1/1     1            1           19s   nginx        nginx:1.16   app=nginx-deploy
```

`nginx:1.17` 버전으로 image setting

```Bash
controlplane ~ ➜  kubectl set image deployment/nginx-deploy nginx=nginx:1.17
deployment.apps/nginx-deploy image updated

controlplane ~ ➜  k get pods -o wide -w
NAME                            READY   STATUS              RESTARTS   AGE    IP             NODE     NOMINATED NODE   READINESS GATES
nginx-deploy-58f87d49-5ml4b     0/1     ContainerCreating   0          3s     <none>         node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   1/1     Running             0          20s    10.244.192.2   node01   <none>           <none>
use-pv                          1/1     Running             0          7m7s   10.244.192.1   node01   <none>           <none>
nginx-deploy-58f87d49-5ml4b     1/1     Running             0          3s     10.244.192.3   node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   1/1     Terminating         0          20s    10.244.192.2   node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   0/1     Terminating         0          21s    <none>         node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   0/1     Terminating         0          21s    10.244.192.2   node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   0/1     Terminating         0          21s    10.244.192.2   node01   <none>           <none>
nginx-deploy-858fb84d4b-v4926   0/1     Terminating         0          21s    10.244.192.2   node01   <none>           <none>


controlplane ~ ✖ k get pods -o wide -w
NAME                          READY   STATUS    RESTARTS   AGE   IP             NODE     NOMINATED NODE   READINESS GATES
nginx-deploy-58f87d49-9vdgl   1/1     Running   0          60s   10.244.192.5   node01   <none>           <none>
redis-storage                 1/1     Running   0          24m   10.244.192.1   node01   <none>           <none>
super-user-pod                1/1     Running   0          20m   10.244.192.2   node01   <none>           <none>
use-pv                        1/1     Running   0          10m   10.244.192.3   node01   <none>           <none>

controlplane ~ ✖ k get deploy -o wide
NAME           READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS   IMAGES       SELECTOR
nginx-deploy   1/1     1            1           79s   nginx        nginx:1.17   app=nginx-deploy
```

<br><br>

---

### Q6. Create a new user called `john`. Grant him access to the cluster. John should have permission to `create`, `list`, `get`, `update` and `delete` pods in the `development` namespace . The private key exists in the location: `/root/CKA/john.key` and csr at `/root/CKA/john.csr`.

**Important Note**: As of kubernetes `1.19`, the CertificateSigningRequest object expects a `signerName`.

Please refer the documentation to see an example. The documentation tab is available at the top right of terminal.

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










