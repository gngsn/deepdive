**1. Node의 Internal IP address**

```
➜  k get node controlplane -o wide
NAME           STATUS   ROLES           AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION   CONTAINER-RUNTIME
controlplane   Ready    control-plane   26m   v1.29.0   192.25.111.6   <none>        Ubuntu 22.04.4 LTS   5.4.0-1106-gcp   containerd://1.6.26
```

**2. `controlplane` node 와 연결된 network interface**

`192.25.111.6` 와 연결된 network interface 조회  →  `10823: eth0@if10824`

```
➜  ip addr
...
10823: eth0@if10824: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1450 qdisc noqueue state UP group default 
    link/ether 02:42:c0:19:6f:06 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 192.25.111.6/24 brd 192.25.111.255 scope global eth0
       valid_lft forever preferred_lft forever
...
```

Mac Address `02:42:c0:19:6f:06` 도 확인할 수 있음

참고로, `ip addr show eth0` 로 특정 지어 볼 수도 있음

```Bash
➜  ip addr show eth0
10823: eth0@if10824: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1450 qdisc noqueue state UP group default 
    link/ether 02:42:c0:19:6f:06 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 192.25.111.6/24 brd 192.25.111.255 scope global eth0
       valid_lft forever preferred_lft forever
```

**3. 특정 타입의 Network Interface 만 조회**

```Bash
➜  ip addr show type bridge
3: cni0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1400 qdisc noqueue state UP group default qlen 1000
link/ether 7a:4b:9e:e9:e3:a8 brd ff:ff:ff:ff:ff:ff
inet 10.244.0.1/24 brd 10.244.0.255 scope global cni0
valid_lft forever preferred_lft forever
```

**4. If you were to ping google from the controlplane node, which route does it take?**
default 인 `172.25.0.1` 를 통해 이동

```Bash
➜  ip route
default via 172.25.0.1 dev eth1 
10.244.0.0/24 dev cni0 proto kernel scope link src 10.244.0.1 
10.244.1.0/24 via 10.244.1.0 dev flannel.1 onlink 
172.25.0.0/24 dev eth1 proto kernel scope link src 172.25.0.71 
192.25.111.0/24 dev eth0 proto kernel scope link src 192.25.111.6 
```

**kube-scheduler 가 사용하는 Port 확인하는 쉬운 방법**

```Bash
➜  netstat -npl | grep -i scheduler
tcp        0      0 127.0.0.1:10259         0.0.0.0:*               LISTEN      3734/kube-scheduler 
```

---

## CNI

1. Container runtime endpoint 알아보는 법

<pre><code>➜  ps -aux | grep kubelet | grep --color container-runtime-endpoint
root        4313  0.0  0.0 4148720 86204 ?       Ssl  16:30   0:07 /usr/bin/kubelet --bootstrap-kubeconfig=/etc/kubernetes/bootstrap-kubelet.conf --kubeconfig=/etc/kubernetes/kubelet.conf --config=/var/lib/kubelet/config.yaml <b>--container-runtime-endpoint=unix:///var/run/containerd/containerd.sock</b> --pod-infra-container-image=registry.k8s.io/pause:3.9
</code></pre>

Inspect the kubelet service and identify the  value is set for Kubernetes

What is the CNI plugin configured to be used on this kubernetes cluster?

```
# The CNI binaries are located under /opt/cni/bin by default
controlplane /etc/cni/net.d ➜  cd /opt/cni/bin/

controlplane /opt/cni/bin ➜  ls
bandwidth  dhcp   firewall  host-device  ipvlan    macvlan  ptp  static  tuning  vrf
bridge     dummy  flannel   host-local   loopback  portmap  sbr  tap     vlan

# `/etc/cni/net.d/` is default file to identify the name of the plugin.
controlplane /opt/cni/bin ➜  cd /etc/cni/net.d/

controlplane /etc/cni/net.d ➜  ll
10-flannel.conflist

controlplane /etc/cni/net.d ➜  cat 10-flannel.conflist 
{
  "name": "cbr0",
  "cniVersion": "0.3.1",
  "plugins": [
    {
      "type": "flannel",
      "delegate": {
        "hairpinMode": true,
        "isDefaultGateway": true
      }
    },
    {
      "type": "portmap",
      "capabilities": {
        "portMappings": true
      }
    }
  ]
}
```