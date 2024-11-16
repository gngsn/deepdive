#### Q1. Create a new service account with the name `pvviewer`. Grant this Service account access to `list` all PersistentVolumes in the cluster by creating an appropriate cluster role called `pvviewer-role` and `ClusterRoleBinding` called `pvviewer-role-binding`. Next, create a pod called `pvviewer` with the `image: redis` and `serviceAccount: pvviewer` in the `default` namespace.

<br>

#### Solution

1. create serviceaccount

```Bash
controlplane ~ ➜  kubectl create serviceaccount pvviewer
serviceaccount/pvviewer created
```

2. create role

```Bash
controlplane ~ ➜  kubectl create clusterrole pvviewer-role --resource=persistentvolumes --verb=list
clusterrole.rbac.authorization.k8s.io/pvviewer-role created
```

3. create role-binding

```Bash
controlplane ~ ➜  kubectl create clusterrolebinding pvviewer-role-binding --clusterrole=pvviewer-role --serviceaccount=default:pvviewer
clusterrolebinding.rbac.authorization.k8s.io/pvviewer-role-binding created
```

4. create pod & configure serviceAccount by specifying `serviceAccountName`

```Bash
controlplane ~ ➜  k run pvviewer --image=redis -o yaml --dry-run=client > pvviewer.yaml

controlplane ~ ➜  vi pvviewer.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: pvviewer
  name: pvviewer
spec:
  containers:
  - image: redis
    name: pvviewer
    resources: {}
  serviceAccountName: pvviewer 
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
```

```Bash
controlplane ~ ➜  k apply -f pvviewer.yaml 
pod/pvviewer created
```

check permission

```
controlplane ~ ➜  kubectl auth can-i list pv
Warning: resource 'persistentvolumes' is not namespace scoped

yes
```

<br><br>

---

#### Q2. List the `InternalIP` of all nodes of the cluster. Save the result to a file `/root/CKA/node_ips`.

Solution should be in the format: `InternalIP of controlplane<space>InternalIP of node01` (in a single line)

<br>

#### Solution

[🔗 JSONPath Support](https://kubernetes.io/docs/reference/kubectl/jsonpath/) 참고

```Bash
controlplane ~ ➜  kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="InternalIP")].address}' 
192.12.122.12 192.12.122.3

controlplane ~ ➜  kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="InternalIP")].address}' > /root/CKA/node_ips
```

<br><br>

---

#### Q3. Create a pod called `multi-pod` with two containers.
Container 1: name: `alpha`, image: `nginx`
Container 2: name: `beta`, image: `busybox`, command: `sleep 4800`

Environment Variables:
container 1:
`name: alpha`

Container 2:
`name: beta`

<br>

#### Solution

```Bash
apiVersion: v1
kind: Pod
metadata:
name: multi-pod
spec:
containers:
- image: nginx
  name: alpha
  env:
  - name: name
    value: alpha
- image: busybox
  name: beta
  command: ["sleep", "4800"]
  env:
  - name: name
    value: beta
```

Check

```Bash
controlplane ~ ✖ k describe pods multi-pod 
Name:             multi-pod
Namespace:        default
...
Containers:
  alpha:
    Image:          nginx
    Environment:
      name:  alpha
    ...
  beta:
    Image:         busybox
    Command:
      sleep
      4800
    Environment:
      name:  beta
  ...
```

<br><br>

---

#### Q4. Create a Pod called `non-root-pod`, image: `redis:alpine`

- `runAsUser: 1000`
- `fsGroup: 2000`

<br>

#### Solution

[🔗Configure a Security Context for a Pod or Container](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-the-security-context-for-a-pod) 참고

```Bash
controlplane ~ ➜  k run non-root-pod --image=redis:alpine --dry-run=client -o yaml > non-root-pod.yaml

controlplane ~ ➜  vi non-root-pod.yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: non-root-pod
  name: non-root-pod
spec:
  securityContext:
    runAsUser: 1000
    fsGroup: 2000
  containers:
  - image: redis:alpine
    name: non-root-pod
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

controlplane ~ ➜  k apply -f non-root-pod.yaml 
pod/non-root-pod created
```

<br><br>

---

#### Q5. We have deployed a new pod called `np-test-1` and a service called `np-test-service`. 
Incoming connections to this service are not working. Troubleshoot and fix it.
Create NetworkPolicy, by the name `ingress-to-nptest` that allows incoming connections to the service over port `80`.

Important: Don't delete any current objects deployed.

<br>

#### Solution

[Network Policies](https://kubernetes.io/docs/concepts/services-networking/network-policies/)

```Bash
controlplane ~ ➜  vi ingress-to-nptest.yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: ingress-to-nptest
spec:
  podSelector:
    matchLabels:
      run: np-test-1
  ingress:
  - from:
    ports:
    - protocol: TCP
      port: 80
  policyTypes:
  - Ingress

controlplane ~ ➜  k apply -f ingress-to-nptest.yaml 
networkpolicy.networking.k8s.io/allow-all-ingress created
```

Check

```Bash
controlplane ~ ➜  k describe networkpolicy ingress-to-nptest
Name:         ingress-to-nptest
Namespace:    default
Created on:   2024-07-28 10:03:17 +0000 UTC
Labels:       <none>
Annotations:  <none>
Spec:
  PodSelector:     run=np-test-1
  Allowing ingress traffic:
    To Port: 80/TCP
    From: <any> (traffic not restricted by source)
  Not affecting egress traffic
  Policy Types: Ingress
```

<br><br>

---

#### Q6. Taint the worker node `node01` to be Unschedulable. 
Once done, create a pod called `dev-redis`, image `redis:alpine`, to ensure workloads are not scheduled to this worker node.
Finally, create a new pod called `prod-redis` and image: `redis:alpine` with toleration to be scheduled on `node01`.

key: `env_type`, value: `production`, operator: `Equal` and effect: `NoSchedule`

<br>

#### Solution

```Bash
controlplane ~ ➜  kubectl taint nodes node01 env_type=production:NoSchedule
node/node01 tainted

controlplane ~ ➜  k run dev-redis --image=redis:alpine
pod/dev-redis created

controlplane ~ ➜  k get pods -o wide
NAME           READY   STATUS    RESTARTS   AGE   IP             NODE           NOMINATED NODE   READINESS GATES
dev-redis      1/1     Running   0          6s    10.244.0.4     controlplane   <none>           <none>
...

controlplane ~ ➜  k run prod-redis --image=redis:alpine --dry-run=client -o yaml > prod-redis.yaml

controlplane ~ ➜  vi prod-redis.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: prod-redis
  name: prod-redis
spec:
  containers:
  - image: redis:alpine
    name: prod-redis
    resources: {}
  tolerations:
  - key: "env_type"
    operator: "Equal"
    value: "production"
    effect: "NoSchedule"
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

controlplane ~ ➜  k apply -f prod-redis.yaml 
pod/prod-redis created

controlplane ~ ➜  k get pod -o wide -w
NAME           READY   STATUS    RESTARTS   AGE    IP             NODE           NOMINATED NODE   READINESS GATES
dev-redis      1/1     Running   0          2m7s   10.244.0.4     controlplane   <none>           <none>
prod-redis     1/1     Running   0          6s     10.244.192.6   node01         <none>           <none>
...
```

<br><br>

---

#### Q7. Create a pod called `hr-pod` in `hr` namespace belonging to the `production` environment and `frontend` tier.
image: `redis:alpine`

Use appropriate labels and create all the required objects if it does not exist in the system already

<br>

#### Solution

```Bash
controlplane ~ ➜ k create ns hr
namespace/hr created
```

```Bash
controlplane ~ ➜  kubectl run hr-pod -n hr  --image=redis:alpine --labels="environment=production,tier=frontend"
pod/hr-pod created
```

```Bash
controlplane ~ ➜  k describe pods hr-pod -n hr
Name:             hr-pod
Namespace:        hr
Priority:         0
Service Account:  default
Node:             controlplane/192.12.122.12
Start Time:       Sun, 28 Jul 2024 09:22:06 +0000
Labels:           environment=production
                  tier=frontend
...
```

<br><br>

---

#### Q8. A `kubeconfig` file called `super.kubeconfig` has been created under `/root/CKA`. There is something wrong with the configuration. Troubleshoot and fix it.

#### Solution


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

Check

```
controlplane ~ ➜  kubectl cluster-info --kubeconfig=/root/CKA/super.kubeconfig
Kubernetes control plane is running at https://controlplane:6443
CoreDNS is running at https://controlplane:6443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.

```



#### Q9. Use the command kubectl scale to increase the replica count to `3`. 

```Bash
controlplane ~ ➜  kubectl scale --replicas=3 deploy/nginx-deploy
deployment.apps/nginx-deploy scaled

controlplane ~ ✖ k get pods -w
NAME                            READY   STATUS    RESTARTS   AGE
nginx-deploy-68b454659d-6mfdz   1/1     Running   0          77s
np-test-1                       1/1     Running   0          89s

controlplane ~ ➜  k get pods -A
NAMESPACE     NAME                                   READY   STATUS             RESTARTS      AGE
kube-system   kube-contro1ler-manager-controlplane   0/1     ImagePullBackOff   0             117s
...

controlplane ~ ➜  k describe pod kube-contro1ler-manager-controlplane -n kube-system
Name:                 kube-contro1ler-manager-controlplane
Namespace:            kube-system
...
Events:
  Type     Reason   Age                 From     Message
  ----     ------   ----                ----     -------
  Warning  Failed   50s (x6 over 2m7s)  kubelet  Error: ImagePullBackOff
  Normal   Pulling  39s (x4 over 2m7s)  kubelet  Pulling image "registry.k8s.io/kube-contro1ler-manager:v1.30.0"
  Warning  Failed   39s (x4 over 2m7s)  kubelet  Failed to pull image "registry.k8s.io/kube-contro1ler-manager:v1.30.0": rpc error: code = NotFound desc = failed to pull and unpack image "registry.k8s.io/kube-contro1ler-manager:v1.30.0": failed to resolve reference "registry.k8s.io/kube-contro1ler-manager:v1.30.0": registry.k8s.io/kube-contro1ler-manager:v1.30.0: not found
  Warning  Failed   39s (x4 over 2m7s)  kubelet  Error: ErrImagePull
  Normal   BackOff  28s (x7 over 2m7s)  kubelet  Back-off pulling image "registry.k8s.io/kube-contro1ler-manager:v1.30.0"

controlplane ~ ➜  vi /etc/kubernetes/manifests/kube-controller-manager.yaml 


controlplane ~ ➜  k get deploy
NAME           READY   UP-TO-DATE   AVAILABLE   AGE
nginx-deploy   3/3     3            3           5m57s
```


- vim replace all command
: `:%s/1l/ll/g`