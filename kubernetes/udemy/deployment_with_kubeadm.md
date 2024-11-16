# Introduction to Deployment with Kubeadm

## Virtual VM

[Multipass - on Apple Silicon](https://github.com/kodekloudhub/certified-kubernetes-administrator-course/tree/master/kubeadm-clusters/apple-silicon)

VirtualBox가 Mac Apple Silicon 지원을 활발하게 하지 않기 때문에, Multipass 를 이용해서 설치

**Multipass**는 단 하나의 명령어로 Cloud-like Ubuntu VM 을 생성할 수 있음. Multipass는 가상 머신을 실행하고 public cloud 처럼 설정해줌 


```bash
❯ brew install multipass
```

<details>
<summary>Installation <code>multipass</code></summary>

```
❯ brew install multipass
controlplane
Deleting controlplane
Launching controlplane
Launched: controlplane
controlplane booted!
Launching node01
Launched: node01
node01 booted!
Launching node02
Launched: node02
node02 booted!
Setting hostnames
++ netstat -rn -f inet
++ grep '^default.*en0'
++ awk '{print $2}'
++ awk 'BEGIN { FS="." } { printf "%s.%s.%s", $1, $2, $3 }'
+ network=192.168.0
+ '[' -f /tmp/hostentries ']'
+ for node in controlplane '$workers'
+ '[' BRIDGE = BRIDGE ']'
++ multipass info controlplane --format json
++ jq -r --arg nw 192.168.0 'first( .info[] )| .ipv4  | .[] | select(startswith($nw))'
+ ip=192.168.0.250
+ echo '192.168.0.250 controlplane'
+ for node in controlplane '$workers'
+ '[' BRIDGE = BRIDGE ']'
++ multipass info node01 --format json
++ jq -r --arg nw 192.168.0 'first( .info[] )| .ipv4  | .[] | select(startswith($nw))'
+ ip=192.168.0.251
+ echo '192.168.0.251 node01'
+ for node in controlplane '$workers'
+ '[' BRIDGE = BRIDGE ']'
++ multipass info node02 --format json
++ jq -r --arg nw 192.168.0 'first( .info[] )| .ipv4  | .[] | select(startswith($nw))'
+ ip=192.168.0.252
+ echo '192.168.0.252 node02'
+ for node in controlplane '$workers'
+ multipass transfer /tmp/hostentries controlplane:/tmp/
+ multipass transfer /Users/gyeongsun/git/kodekloud/certified-kubernetes-administrator-course/kubeadm-clusters/apple-silicon/scripts/01-setup-hosts.sh controlplane:/tmp/
+ multipass exec controlplane -- /tmp/01-setup-hosts.sh BRIDGE 192.168.0
192.168.0.250 controlplane
192.168.0.251 node01
192.168.0.252 node02
^@+ for node in controlplane '$workers'
+ multipass transfer /tmp/hostentries node01:/tmp/
+ multipass transfer /Users/gyeongsun/git/kodekloud/certified-kubernetes-administrator-course/kubeadm-clusters/apple-silicon/scripts/01-setup-hosts.sh node01:/tmp/
+ multipass exec node01 -- /tmp/01-setup-hosts.sh BRIDGE 192.168.0
192.168.0.250 controlplane
192.168.0.251 node01
192.168.0.252 node02
+ for node in controlplane '$workers'
+ multipass transfer /tmp/hostentries node02:/tmp/
+ multipass transfer /Users/gyeongsun/git/kodekloud/certified-kubernetes-administrator-course/kubeadm-clusters/apple-silicon/scripts/01-setup-hosts.sh node02:/tmp/
+ multipass exec node02 -- /tmp/01-setup-hosts.sh BRIDGE 192.168.0
192.168.0.250 controlplane
192.168.0.251 node01
192.168.0.252 node02
+ echo -e '\033[1;32mDone!\033[0m'
Done!
+ '[' '' = -auto ']'
```

</details>

Get an instant Ubuntu VM with a single command. 

Multipass can launch and run virtual machines and configure them with cloud-init like a public cloud.


```Bash
❯ multipass shell controlplane
Welcome to Ubuntu 22.04.4 LTS (GNU/Linux 5.15.0-113-generic aarch64)
...
Last login: Sun Jun 30 16:21:10 2024 from 192.168.65.1
ubuntu@controlplane:~$
```


### Verify the MAC address and product_uuid are unique for every node

It is very likely that hardware devices will have unique addresses, although some virtual machines may have identical values. 

Kubernetes uses these values to uniquely identify the nodes in the cluster. 

If these values are not unique to each node, the installation process may fail.

```Bash
ubuntu@controlplane:~$ ip link
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN mode DEFAULT group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
2: enp0s1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP mode DEFAULT group default qlen 1000
    link/ether 52:54:00:72:c4:2b brd ff:ff:ff:ff:ff:ff
3: enp0s2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP mode DEFAULT group default qlen 1000
    link/ether 52:54:00:3f:28:2c brd ff:ff:ff:ff:ff:ff
```

<br>

### Check network adapters
If you have more than one network adapter, and your Kubernetes components are not reachable on the default route, 
we recommend you add IP route(s) so Kubernetes cluster addresses go via the appropriate adapter.

<br>

### Check required ports

These required ports need to be open in order for Kubernetes components to communicate with each other. 
You can use tools like netcat to check if a [port](https://kubernetes.io/docs/reference/networking/ports-and-protocols/) is open. 
For example:


```Bash
ubuntu@controlplane:~$ for ports in 6443 2379 2380 10250 10259 10257; do nc 127.0.0.1 $ports -v; done;
nc: connect to 127.0.0.1 port 6443 (tcp) failed: Connection refused
...
nc: connect to 127.0.0.1 port 10257 (tcp) failed: Connection refuse
```

<br>

## Installing a container runtime

[🔗 Container Runtimes](https://kubernetes.io/docs/setup/production-environment/container-runtimes/)

<br>

### 📌 Prerequisites

#### Network configuration

```Bash
# sysctl params required by setup, params persist across reboots
ubuntu@controlplane:~$ cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.ipv4.ip_forward = 1
EOF

# Apply sysctl params without reboot
ubuntu@controlplane:~$ sudo sysctl --system

# Verify that net.ipv4.ip_forward is set to 1
ubuntu@controlplane:~$ sysctl net.ipv4.ip_forward
net.ipv4.ip_forward = 1
```

<br>

### 📌 Container-d

[🔗 kubernetes.io - containerd](https://kubernetes.io/docs/setup/production-environment/container-runtimes/#containerd)
[🔗 docker - install engine](https://docs.docker.com/engine/install/ubuntu/)

#### STEP1. Set up Docker's apt repository

```Bash
ubuntu@controlplane:~$ sudo apt-get update
...
Reading package lists... Done

ubuntu@controlplane:~$ sudo apt-get install ca-certificates curl
...
0 upgraded, 0 newly installed, 0 to remove and 2 not upgraded.

ubuntu@controlplane:~$ sudo install -m 0755 -d /etc/apt/keyrings
ubuntu@controlplane:~$ sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
ubuntu@controlplane:~$ sudo chmod a+r /etc/apt/keyrings/docker.asc
ubuntu@controlplane:~$ echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
Get:1 https://download.docker.com/linux/ubuntu jammy InRelease [48.8 kB]
Get:2 https://download.docker.com/linux/ubuntu jammy/stable arm64 Packages [34.6 kB]
Hit:3 http://ports.ubuntu.com/ubuntu-ports jammy InRelease
Hit:4 http://ports.ubuntu.com/ubuntu-ports jammy-updates InRelease
Hit:5 http://ports.ubuntu.com/ubuntu-ports jammy-backports InRelease
Hit:6 http://ports.ubuntu.com/ubuntu-ports jammy-security InRelease
Fetched 83.4 kB in 2s (50.9 kB/s)
Reading package lists... Done
```

<br>

#### STEP2. Install the Docker packages

```Bash
ubuntu@controlplane:~$ sudo apt-get install containerd.io
...

# Check
ubuntu@controlplane:~$ systemctl status containerd
● containerd.service - containerd container runtime
     Loaded: loaded (/lib/systemd/system/containerd.service; enabled; vendor preset: enabled)
     Active: active (running) since Sun 2024-06-30 17:39:14 KST; 9min ago
       Docs: https://containerd.io
    Process: 5146 ExecStartPre=/sbin/modprobe overlay (code=exited, status=0/SUCCESS)
   Main PID: 5148 (containerd)
      Tasks: 7
     Memory: 12.1M
        CPU: 833ms
     CGroup: /system.slice/containerd.service
             └─5148 /usr/bin/containerd
```

동일한 작업을 `node01`, `node02` 에서도 진행

<br>

### C group driver

리눅스 시스템에는 Control Group 이라는 것이 있음

Control Group 은 실행 중인 여러 프로세스에 자원 할당을 제한하는 데 사용함

Pod와 Container 리소스 관리하고 CPU/memory 의 requests 혹은 limits 과 같은 리소스를 설정함

Control Group을 인터페이스하기 위해, `Kubelet`과 `Container Runtime`은 cgroup 드라이버를 사용할 필요가 있음

`Kubelet`과 `Container Runtime`은 반드시 동일한 cgroup driver를 사용해야 하고, 동일하게 구성되어야 함 

두 가지 드라이버가 있음: 

- `cgroupfs`
- `systemd`

일반적으로 `cgroupfs`가 기본값으로 설정됨

현재 호스트에서 사용하는 cgroup driver 를 먼저 확인해볼 필요가 있음

확인하는 방법은 `ps -p 1` 명령어로 확인 가능

```Bash
ubuntu@controlplane:~$ ps -p 1
    PID TTY          TIME CMD
      1 ?        00:00:01 systemd 
```

아래 containerd 가 systemd cgroup driver 를 사용하기 위해서 모든 노드 ⎯ controlplane과 node01, node02 ⎯ 에 아래 설정이 필요

```Bash
ubuntu@controlplane:~$ sudo vi /etc/containerd/config.toml 
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
    SystemdCgroup = true
ubuntu@controlplane:~$ sudo systemctl restart containerd
```

---

### 📌 Installing kubeadm, kubelet and kubectl

[Creating a cluster with kubeadm](https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)

위 문서 과정을 거쳐 `kubelet`, `kubeadm`, `kubectl` 설치

#### 1.  api-get 업데이트

```Bash
ubuntu@controlplane:~$ sudo apt-get update
...
0 upgraded, 0 newly installed, 0 to remove and 4 not upgraded.
```

<br>

#### 2. Kubernetes 패키지 리포지토리의 공개 서명 키 다운로드

apt 명령어 사용 시, Kubernetes 패키지 저장소를 신뢰하고 해당 저장소에서 패키지를 설치할 수 있도록 하는 데 사용

모든 리포지토리에 동일한 서명 키가 사용되므로 URL의 버전을 무시할 수 있음

```Bash
# If the directory `/etc/apt/keyrings` does not exist, it should be created before the curl command, read the note below.
# sudo mkdir -p -m 755 /etc/apt/keyrings
ubuntu@controlplane:~$ curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
```

✔️ `curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.30/deb/Release.key`: Kubernetes 패키지 저장소의 GPG 키가 위치한 URL, GPG 공개 키 다운로드

✔️ `sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg`: 변환된 GPG 키를 `/etc/apt/keyrings/kubernetes-apt-keyring.gpg` 파일에 저장


- `curl`: URL에서 데이터를 가져오는 명령어
  - `-f`: 실패 시 curl 명령 조용히 종료
  - `-s`: 진행률과 오류 메시지를 숨김
  - `-S`: 오류가 발생하면 메시지를 출력
  - `-L`: 리디렉션을 따라감

- `gpg`: GNU Privacy Guard, 파일을 암호화하고 서명하는 데 사용하는 도구
  - `--dearmor`: ASCII로 인코딩된 GPG 키를 바이너리 형식으로 변환
  - `-o <file>`: 변환된 GPG 키를 지정된 경로에 출력 (저장)

<br>

#### 3. Kubernetes apt 리포지토리 추가

아래 리포지토리에는 Kubernetes `1.30` 버전의 패키지에만 존재하기 때문에, minor 버전의 경우 원하는 minor 버전과 일치하도록 URL의 버전을 다르게 명시

```
ubuntu@controlplane:~$ echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /' | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.30/deb/ /
```

- `deb`: Ubuntu/Debian 패키지 설치 명령어

- `tee`: 출력 결과를 파일과 화면에 동시에 출력할 수 있도록 해주는 명령어

<br>

#### 4. `kubelet`, `kubeadm`, `kubectl` 설치

```
ubuntu@controlplane:~$ sudo apt-get update
ubuntu@controlplane:~$ sudo apt-get install -y kubelet kubeadm kubectl
ubuntu@controlplane:~$ sudo apt-mark hold kubelet kubeadm kubectl
ubuntu@controlplane:~$ sudo systemctl enable --now kubelet
```

<br/>

### Initializing `kubeadm`

#### STEP1. `kubeadm init` 으로 초기화 

초기화 전, Control Plane (조금 더 정확히는 `kubeapi-server`) 가 실행될 `ip addr` 로 IP 정보 먼저 확인 

<pre><code lang="bash">
ubuntu@controlplane:~$ ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host
       valid_lft forever preferred_lft forever
2: enp0s1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    link/ether 52:54:00:72:c4:2b brd ff:ff:ff:ff:ff:ff
    inet <b>192.168.65.2/24</b> metric 100 brd 192.168.65.255 scope global dynamic enp0s1
       valid_lft 73704sec preferred_lft 73704sec
    inet6 fdb4:8252:e9ce:b35f:5054:ff:fe72:c42b/64 scope global dynamic mngtmpaddr noprefixroute
       valid_lft 2591975sec preferred_lft 604775sec
    inet6 fe80::5054:ff:fe72:c42b/64 scope link
       valid_lft forever preferred_lft forever
3: enp0s2: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP group default qlen 1000
    link/ether 52:54:00:3f:28:2c brd ff:ff:ff:ff:ff:ff
    inet 192.168.0.43/24 metric 200 brd 192.168.0.255 scope global dynamic enp0s2
       valid_lft 5994sec preferred_lft 5994sec
    inet6 fe80::5054:ff:fe3f:282c/64 scope link
       valid_lft forever preferred_lft forever
</code></pre>

이후 `kubeadm init` 명령어 입력

```Bash
ubuntu@controlplane:~$ sudo kubeadm init --pod-network-cidr=10.244.0.0/16 --apiserver-advertise-address=192.168.65.2
[init] Using Kubernetes version: v1.30.2
...
[addons] Applied essential addon: kube-proxy

Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.65.2:6443 --token zre3m8.bvz...afj \
	--discovery-token-ca-cert-hash sha256:0de9...e81b8
```

이 때, 두 가지 옵션 추가

✔️ `--pod-network-cidr`: IP prefix for all pods in the Kubernetes cluster

✔️ `--apiserver-advertise-address`: to set the advertise address for this particular control-plane node's API server


```Bash
ubuntu@controlplane:~$ kubectl get pods -A
NAMESPACE     NAME                                   READY   STATUS    RESTARTS       AGE
kube-system   coredns-7db6d8ff4d-hwbdf               0/1     Pending   0              67s
kube-system   coredns-7db6d8ff4d-z9wg5               0/1     Pending   0              67s
kube-system   etcd-controlplane                      1/1     Running   1 (116s ago)   35s
kube-system   kube-apiserver-controlplane            1/1     Running   1 (86s ago)    84s
kube-system   kube-controller-manager-controlplane   1/1     Running   1 (116s ago)   31s
kube-system   kube-proxy-4zmtp                       1/1     Running   1 (54s ago)    67s
kube-system   kube-scheduler-controlplane            1/1     Running   2 (40s ago)    40s
```

### 📌 Network: Weave Net

**Weave Net**
: provides networking and network policy, will carry on working on both sides of a network partition, and does not require an external database.

[🔗 github - Using Weave Net on Kubernetes](https://github.com/rajch/weave#using-weave-on-kubernetes)

```Bash
ubuntu@controlplane:~$ kubectl apply -f https://reweave.azurewebsites.net/k8s/v1.29/net.yaml
serviceaccount/weave-net created
clusterrole.rbac.authorization.k8s.io/weave-net created
clusterrolebinding.rbac.authorization.k8s.io/weave-net created
role.rbac.authorization.k8s.io/weave-net created
rolebinding.rbac.authorization.k8s.io/weave-net created
daemonset.apps/weave-net created
```

[🔗 kube-addon](https://rajch.github.io/weave/kubernetes/kube-addon)

<pre><code lang="bash">
ubuntu@controlplane:~$ kubectl edit ds weave-net -n kube-system
... 
name: weave-net
...
spec:
    ...
    spec:
      containers:
      - command:
        - /home/weave/launch.sh
        env:
        <b>- name: IPALLOC_RANGE
          value: 10.244.0.0/16</b>
        - name: INIT_CONTAINER
          value: "true"
          ...
</code></pre>


이후 Worker Node 에 위에서 출력된

```Bash
kubeadm join 192.168.65.2:6443 --token zre3m8.bvz...afj \
	--discovery-token-ca-cert-hash sha256:0de9...e81b8
```

설정 후 kubeadm 에 join 하기 