## Network Troubleshooting 

> ### Question 1.
> Do the services in triton namespace have a valid endpoint? If they do, check the kube-proxy and the weave logs.
Does the cluster have a Network Addon installed?

> Install Weave using the link: https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/#pod-network
> 
> **For example**: `curl -L https://github.com/weaveworks/weave/releases/download/latest_release/weave-daemonset-k8s-1.11.yaml | kubectl apply -f -`

```bash
root@controlplane ~ ➜  k logs kube-proxy-x92wc
I1212 03:47:15.257067       1 server_linux.go:66] "Using iptables proxy"
I1212 03:47:15.697232       1 server.go:677] "Successfully retrieved node IP(s)" IPs=["192.168.121.74"]
I1212 03:47:15.699677       1 conntrack.go:121] "Set sysctl" entry="net/netfilter/nf_conntrack_max" value=131072
I1212 03:47:15.699815       1 conntrack.go:60] "Setting nf_conntrack_max" nfConntrackMax=131072
I1212 03:47:15.700122       1 conntrack.go:121] "Set sysctl" entry="net/netfilter/nf_conntrack_tcp_timeout_established" value=86400
I1212 03:47:15.700258       1 conntrack.go:121] "Set sysctl" entry="net/netfilter/nf_conntrack_tcp_timeout_close_wait" value=3600
E1212 03:47:15.700364       1 server.go:234] "Kube-proxy configuration may be incomplete or incorrect" err="nodePortAddresses is unset; NodePort connections will be accepted on all local IPs. Consider using `--nodeport-addresses primary`"
I1212 03:47:15.787605       1 server.go:243] "kube-proxy running in dual-stack mode" primary ipFamily="IPv4"
I1212 03:47:15.787731       1 server_linux.go:169] "Using iptables Proxier"
I1212 03:47:15.790433       1 proxier.go:255] "Setting route_localnet=1 to allow node-ports on localhost; to change this either disable iptables.localhostNodePorts (--iptables-localhost-nodeports) or set nodePortAddresses (--nodeport-addresses) to filter loopback addresses" ipFamily="IPv4"
I1212 03:47:15.790942       1 server.go:483] "Version info" version="v1.31.0"
I1212 03:47:15.791008       1 server.go:485] "Golang settings" GOGC="" GOMAXPROCS="" GOTRACEBACK=""
I1212 03:47:15.794190       1 config.go:197] "Starting service config controller"
I1212 03:47:15.796826       1 shared_informer.go:313] Waiting for caches to sync for service config
I1212 03:47:15.794458       1 config.go:104] "Starting endpoint slice config controller"
I1212 03:47:15.796856       1 shared_informer.go:313] Waiting for caches to sync for endpoint slice config
I1212 03:47:15.795637       1 config.go:326] "Starting node config controller"
I1212 03:47:15.796864       1 shared_informer.go:313] Waiting for caches to sync for node config
I1212 03:47:15.897829       1 shared_informer.go:320] Caches are synced for node config
I1212 03:47:15.897889       1 shared_informer.go:320] Caches are synced for service config
I1212 03:47:15.898111       1 shared_informer.go:320] Caches are synced for endpoint slice config
```

```
root@controlplane ~ ➜  curl -L https://github.com/weaveworks/weave/releases/download/latest_release/weave-daemonset-k8s-1.11.yaml | kubectl apply -f -
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0
100  6183  100  6183    0     0  15449      0 --:--:-- --:--:-- --:--:-- 15449
serviceaccount/weave-net created
clusterrole.rbac.authorization.k8s.io/weave-net created
clusterrolebinding.rbac.authorization.k8s.io/weave-net created
role.rbac.authorization.k8s.io/weave-net created
rolebinding.rbac.authorization.k8s.io/weave-net created
daemonset.apps/weave-net created
```

```
root@controlplane ~ ➜  k get pods -A
NAMESPACE     NAME                                   READY   STATUS    RESTARTS   AGE
kube-system   coredns-6f6b679f8f-qnpfk               1/1     Running   0          7m21s
kube-system   coredns-6f6b679f8f-xwdz2               1/1     Running   0          7m21s
kube-system   etcd-controlplane                      1/1     Running   0          7m30s
kube-system   kube-apiserver-controlplane            1/1     Running   0          7m29s
kube-system   kube-controller-manager-controlplane   1/1     Running   0          7m29s
kube-system   kube-proxy-x92wc                       1/1     Running   0          7m22s
kube-system   kube-scheduler-controlplane            1/1     Running   0          7m29s
kube-system   weave-net-jf6t6                        2/2     Running   0          31s
triton        mysql                                  1/1     Running   0          5m7s
triton        webapp-mysql-d89894b4b-sxhjx           1/1     Running   0          5m7s
```

---

> **Troubleshooting Test 2:** 
The same 2 tier application is having issues again. It must display a green web page on success. Click on the app tab at the top of your terminal to view your application. It is currently failed. Troubleshoot and fix the issue.


Stick to the given architecture. Use the same names and port numbers as given in the below architecture diagram. Feel free to edit, delete or recreate objects as necessary.


```agsl


root@controlplane ~ ➜  kubectl -n kube-system edit ds kube-proxy
daemonset.apps/kube-proxy edited

root@controlplane ~ ➜  k get pods -A
NAMESPACE     NAME                                   READY   STATUS    RESTARTS        AGE
kube-system   coredns-6f6b679f8f-qnpfk               1/1     Running   0               13m
kube-system   coredns-6f6b679f8f-xwdz2               1/1     Running   0               13m
kube-system   etcd-controlplane                      1/1     Running   0               13m
kube-system   kube-apiserver-controlplane            1/1     Running   0               13m
kube-system   kube-controller-manager-controlplane   1/1     Running   0               13m
kube-system   kube-proxy-n7p84                       1/1     Running   0               47s
kube-system   kube-scheduler-controlplane            1/1     Running   0               13m
kube-system   weave-net-jf6t6                        2/2     Running   0               6m45s
triton        mysql                                  1/1     Running   2 (3m39s ago)   5m29s
triton        webapp-mysql-d89894b4b-4fvjj           1/1     Running   0               5m29s
```