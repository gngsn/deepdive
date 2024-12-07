## Question 8.

> Create a static pod on `node01` called `nginx-critical` with image `nginx` and make sure that it is `recreated/restarted` automatically in case of a failure.
>
> Use `/etc/kubernetes/manifests` as the Static Pod path for example.

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










