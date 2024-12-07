## Question 8. 

> ### A `kubeconfig` file called `super.kubeconfig` has been created under `/root/CKA`. There is something wrong with the configuration. Troubleshoot and fix it.

### Solution

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

### Check

```
controlplane ~ ➜  kubectl cluster-info --kubeconfig=/root/CKA/super.kubeconfig
Kubernetes control plane is running at https://controlplane:6443
CoreDNS is running at https://controlplane:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.

```

