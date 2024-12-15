# Worker Node Failure

Node 의 상태를 확인

```Bash
❯ kubectl get nodes
NAME        STATUS      ROLES   AGE     VERSION
worker-1    Ready       <none>  8d      v1.13.0
worker-2    NotReady    <none>  8d      v1.13.0
```

위 `NotReady` 상태 처럼 비정상 적일 때 원인을 찾아야 함

먼저, 

```Bash
❯ kubectl describe node worker-1
Conditions:
Type            Status  LastHeartbeatTime                   Reason                      Message
----            ------  -----------------                   ------                      -------
OutOfDisk       False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientDisk    kubelet has sufficient disk space available
MemoryPressure  False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientMemory  kubelet has sufficient memory available
DiskPressure    False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasNoDiskPressure    kubelet has no disk pressure
PIDPressure     False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientPID     kubelet has sufficient PID available
Ready           True    Mon, 01 Apr 2019 14:30:33 +0000     KubeletReady                kubelet is posting ready status. AppArmor enabled
```

Status 는 'True', 'False' 혹은 'Unknown' 으로 표시될 수 있음

### Check kubelet status

먼저 kubelet 의 상태를 확인해보고, 

```
❯ service kubelet status
```

만약, kubelet 실행에 문제가 있다면, `journalctl` 명령어를 통해 문제를 살펴볼 수 있음

```Bash
node01 ~ ➜  journalctl -u kubelet -f
Dec 15 08:30:09 node01 kubelet[10831]: I1215 08:30:09.837977   10831 server.go:206] "--pod-infra-container-image will not be pruned by the image garbage collector in kubelet and should also be set in the remote runtime"
Dec 15 08:30:09 node01 kubelet[10831]: E1215 08:30:09.839403   10831 run.go:72] "command failed" err="failed to construct kubelet dependencies: unable to load client CA file /etc/kubernetes/pki/WRONG-CA-FILE.crt: open /etc/kubernetes/pki/WRONG-CA-FILE.crt: no such file or directory"
Dec 15 08:30:20 node01 kubelet[10937]: Flag --container-runtime-endpoint has been deprecated, This parameter should be set via the config file specified by the Kubelet's --config flag. See https://kubernetes.io/docs/tasks/administer-cluster/kubelet-config-file/ for more information.
Dec 15 08:30:20 node01 kubelet[10937]: Flag --pod-infra-container-image has been deprecated, will be removed in a future release. Image garbage collector will get sandbox image information from CRI.
Dec 15 08:30:20 node01 kubelet[10937]: I1215 08:30:20.086368   10937 server.go:206] "--pod-infra-container-image will not be pruned by the image garbage collector in kubelet and should also be set in the remote runtime"
Dec 15 08:30:20 node01 kubelet[10937]: E1215 08:30:20.087844   10937 run.go:72] "command failed" err="failed to construct kubelet dependencies: unable to load client CA file /etc/kubernetes/pki/WRONG-CA-FILE.crt: open /etc/kubernetes/pki/WRONG-CA-FILE.crt: no such file or directory"
Dec 15 08:30:30 node01 kubelet[11043]: Flag --container-runtime-endpoint has been deprecated, This parameter should be set via the config file specified by the Kubelet's --config flag. See https://kubernetes.io/docs/tasks/administer-cluster/kubelet-config-file/ for more information.
Dec 15 08:30:30 node01 kubelet[11043]: Flag --pod-infra-container-image has been deprecated, will be removed in a future release. Image garbage collector will get sandbox image information from CRI.
Dec 15 08:30:30 node01 kubelet[11043]: I1215 08:30:30.339660   11043 server.go:206] "--pod-infra-container-image will not be pruned by the image garbage collector in kubelet and should also be set in the remote runtime"
Dec 15 08:30:30 node01 kubelet[11043]: E1215 08:30:30.341960   11043 run.go:72] "command failed" err="failed to construct kubelet dependencies: unable to load client CA file /etc/kubernetes/pki/WRONG-CA-FILE.crt: open /etc/kubernetes/pki/WRONG-CA-FILE.crt: no such file or directory"
```

### Case 1. `/var/lib/kubelet/config.yaml` 설정 파일 오류

가령, 위처럼 `open /etc/kubernetes/pki/WRONG-CA-FILE.crt: no such file or directory` 오류를 발견할 수 있음

PKI 설정 오류인데, 이는 `/var/lib/kubelet/config.yaml` 설정 파일에서 수정해야 함

설정 파일은 기억해둘 필요가 있음

<details>
<summary>FYI. 어디서 설정되었는지 확인</summary>

kubeadm 설정 파일에 지정된 Drop-in 파일로, 아래와 같이 설정되어 있음

```
controlplane ~ ➜  cat /usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf
# Note: This dropin only works with kubeadm and kubelet v1.11+
[Service]
Environment="KUBELET_KUBECONFIG_ARGS=--bootstrap-kubeconfig=/etc/kubernetes/bootstrap-kubelet.conf --kubeconfig=/etc/kubernetes/kubelet.conf"
Environment="KUBELET_CONFIG_ARGS=--config=/var/lib/kubelet/config.yaml"
# This is a file that "kubeadm init" and "kubeadm join" generates at runtime, populating the KUBELET_KUBEADM_ARGS variable dynamically
EnvironmentFile=-/var/lib/kubelet/kubeadm-flags.env
# This is a file that the user can use for overrides of the kubelet args as a last resort. Preferably, the user should use
# the .NodeRegistration.KubeletExtraArgs object in the configuration files instead. KUBELET_EXTRA_ARGS should be sourced from this file.
EnvironmentFile=-/etc/default/kubelet
ExecStart=
ExecStart=/usr/bin/kubelet $KUBELET_KUBECONFIG_ARGS $KUBELET_CONFIG_ARGS $KUBELET_KUBEADM_ARGS $KUBELET_EXTRA_ARGS
```
</details>

<br>

### Case 2. `/etc/kubernetes/kubelet.conf` 설정 파일 오류

혹은 `NotReady` 상태의 노드의 상태가 정상으로 뜰 때도 있음

kubelet 서비스 자체는 잘 떴지만, 런타임에서 실행되는 동작에 오류가 생길 수도 있음

전형적으로 kube-apiserver 와 통신할 때

```bash
node01 ~ ➜ journalctl -u kubelet -f
Dec 15 08:50:49 node01 kubelet[17841]: E1215 08:50:49.273831   17841 controller.go:145] "Failed to ensure lease exists, will retry" err="Get \"https://controlplane:6553/apis/coordination.k8s.io/v1/namespaces/kube-node-lease/leases/node01?timeout=10s\": dial tcp 192.168.28.14:6553: connect: connection refused" interval="7s"
Dec 15 08:50:49 node01 kubelet[17841]: I1215 08:50:49.523085   17841 kubelet_node_status.go:72] "Attempting to register node" node="node01"
Dec 15 08:50:49 node01 kubelet[17841]: E1215 08:50:49.524307   17841 kubelet_node_status.go:95] "Unable to register node with API server" err="Post \"https://controlplane:6553/api/v1/nodes\": dial tcp 192.168.28.14:6553: connect: connection refused" node="node01"
Dec 15 08:50:53 node01 kubelet[17841]: W1215 08:50:53.391100   17841 reflector.go:561] k8s.io/client-go/informers/factory.go:160: failed to list *v1.Node: Get "https://controlplane:6553/api/v1/nodes?fieldSelector=metadata.name%3Dnode01&limit=500&resourceVersion=0": dial tcp 192.168.28.14:6553: connect: connection refused
Dec 15 08:50:53 node01 kubelet[17841]: E1215 08:50:53.391161   17841 reflector.go:158] "Unhandled Error" err="k8s.io/client-go/informers/factory.go:160: Failed to watch *v1.Node: failed to list *v1.Node: Get \"https://controlplane:6553/api/v1/nodes?fieldSelector=metadata.name%3Dnode01&limit=500&resourceVersion=0\": dial tcp 192.168.28.14:6553: connect: connection refused" logger="UnhandledError"
Dec 15 08:50:53 node01 kubelet[17841]: E1215 08:50:53.700130   17841 eviction_manager.go:285] "Eviction manager: failed to get summary stats" err="failed to get node info: node \"node01\" not found"
```

이 때는, `/etc/kubernetes/kubelet.conf` 설정 파일 오류로 수정 후 서비스를 재시작 해주어야 함 



<br><br>

### Check Certificates

```Bash
❯ openssl x509 -in /var/lib/kubelet/worker-1.crt -text
Certificate:
Data:
Version: 3 (0x2)
Serial Number:
ff:e0:23:9d:fc:78:03:35
Signature Algorithm: sha256WithRSAEncryption
Issuer: CN = KUBERNETES-CA
Validity
Not Before: Mar 20 08:09:29 2019 GMT
Not After : Apr 19 08:09:29 2019 GMT
Subject: CN = system:node:worker-1, O = system:nodes
Subject Public Key Info:
Public Key Algorithm: rsaEncryption
Public-Key: (2048 bit)
Modulus:
00:b4:28:0c:60:71:41:06:14:46:d9:97:58:2d:fe:
a9:c7:6d:51:cd:1c:98:b9:5e:e6:e4:02:d3:e3:71:
58:a1:60:fe:cb:e7:9b:4b:86:04:67:b5:4f:da:d6:
6c:08:3f:57:e9:70:59:57:48:6a:ce:e5:d4:f3:6e:
b2:fa:8a:18:7e:21:60:35:8f:44:f7:a9:39:57:16:
4f:4e:1e:b1:a3:77:32:c2:ef:d1:38:b4:82:20:8f:
11:0e:79:c4:d1:9b:f6:82:c4:08:84:84:68:d5:c3:
e2:15:a0:ce:23:3c:8d:9c:b8:dd:fc:3a:cd:42:ae:
5e:1b:80:2d:1b:e5:5d:1b:c1:fb:be:a3:9e:82:ff:
a1:27:c8:b6:0f:3c:cb:11:f9:1a:9b:d2:39:92:0e:
47:45:b8:8f:98:13:c6:4d:6a:18:75:a4:01:6f:73:
f6:f8:7f:eb:5d:59:94:46:d8:da:37:75:cf:27:0b:
39:7f:48:20:c5:fd:c7:a7:ce:22:9a:33:4a:30:1d:
95:ef:00:bd:fe:47:22:42:44:99:77:5a:c4:97:bb:
37:93:7c:33:64:f4:b8:3a:53:8c:f4:10:db:7f:5f:
2b:89:18:d6:0e:68:51:34:29:b1:f1:61:6b:4b:c6
```
