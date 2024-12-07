## Question 8.
> A `kubeconfig` file called `super.kubeconfig` has been created under `/root/CKA`. 
> There is something wrong with the configuration. Troubleshoot and fix it.

---

### Solution

#### STEP 1. 이슈 파악 & 로그로 확인

```bash
controlplane ~ ➜  k get node --kubeconfig /root/CKA/super.kubeconfig
E1207 08:43:08.432751   11203 memcache.go:265] "Unhandled Error" err="couldn't get current server API group list: Get \"https://controlplane:9999/api?timeout=32s\": dial tcp 192.5.34.3:9999: connect: connection refused"
E1207 08:43:08.434237   11203 memcache.go:265] "Unhandled Error" err="couldn't get current server API group list: Get \"https://controlplane:9999/api?timeout=32s\": dial tcp 192.5.34.3:9999: connect: connection refused"
E1207 08:43:08.435676   11203 memcache.go:265] "Unhandled Error" err="couldn't get current server API group list: Get \"https://controlplane:9999/api?timeout=32s\": dial tcp 192.5.34.3:9999: connect: connection refused"
E1207 08:43:08.437037   11203 memcache.go:265] "Unhandled Error" err="couldn't get current server API group list: Get \"https://controlplane:9999/api?timeout=32s\": dial tcp 192.5.34.3:9999: connect: connection refused"
E1207 08:43:08.438351   11203 memcache.go:265] "Unhandled Error" err="couldn't get current server API group list: Get \"https://controlplane:9999/api?timeout=32s\": dial tcp 192.5.34.3:9999: connect: connection refused"
The connection to the server controlplane:9999 was refused - did you specify the right host or port?
```

#### STEP 2. Config 파일 수정

```Bash
controlplane ~ ➜  cat /root/CKA/super.kubeconfig 
apiVersion: v1
clusters:
- cluster:
    certificate-authority-data: LS0tLS1CR...S0K
    # 9999 → 6443
    server: https://controlplane:9999 
  name: kubernetes
contexts:
- context:
    cluster: kubernetes
    user: kubernetes-admin
  name: kubernetes-admin@kubernetes
current-context: kubernetes-admin@kubernetes
kind: Config
preferences: {}
users:
- name: kubernetes-admin
  user:
    client-certificate-data: LS0tLS1CRU...DdHV
```

#### STEP3. 해결 확인

```
controlplane ~ ➜  kubectl cluster-info --kubeconfig=/root/CKA/super.kubeconfig
Kubernetes control plane is running at https://controlplane:6443
CoreDNS is running at https://controlplane:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```

