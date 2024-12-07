## Question 9. 

> ### Use the command kubectl scale to increase the replica count to `3`.

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
