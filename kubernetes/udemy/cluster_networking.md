# Cluster Networking

### IP & FQDN

쿠버네티스 클러스터는 마스터와 작업자 노드로 구성됨

각 노드는 네트워크에 연결된 인터페이스가 최소한 하나는 있어야 하고,
각 인터페이스는 반드시 구성된 주소가 있어야 함 

<br/><img src="./img/cluster_networking_img1.png" width="60%" /><br/>

호스트는 고유한 호스트 이름 세트와 고유한 MAC 주소가 있어야 함

Control Plane의 다양한 컴포넌트 들에 의해 사용될 아래 포트들도 열려있어야 함

<br>

### Control plane Ports

[🔗 Kubernetes Check Required Port](https://kubernetes.io/docs/reference/networking/ports-and-protocols/)

| Protocol	   | Direction | Port Range  | Purpose                   | Used By               |
|-------------|-----------|-------------|---------------------------|-----------------------|
| TCP         | Inbound   | 6443        | Kubernetes API server     | All                   |
| TCP         | Inbound   | 2379-2380   | etcd server client API    | kube-apiserver, etcd  |
| TCP         | Inbound   | 10250       | Kubelet API	              | Self, Control plane   |
| TCP         | Inbound   | 10259       | kube-scheduler	           | Self                  |
| TCP         | Inbound   | 10257       | kube-controller-manager	  | Self                  |


ETCD 포트가 control plane 에 포함되어 있긴 하지만,
ETCD 클러스터를 외부로 혹은 사용자가 지정한 포트(들)로 호스팅할 수 있음

`kube-api-server`는 `6443`로 열려있어서 Worker Node, kube kubelet, 외부 users 들 모두가 해당 포트로 접근할 수 있음

### Worker node(s) Ports

| Protocol	 | Direction | Port Range  | Purpose           | Used By              |
|-----------|-----------|-------------|-------------------|----------------------|
| TCP       | Inbound   | 10250       | Kubelet API       | Self, Control plane  |
| TCP       | Inbound   | 10256       | kube-proxy	       | Self, Load balancers |
| TCP       | Inbound   | 30000-32767 | NodePort Services | All                  |


<br/><img src="./img/cluster_networking_img2.png" width="50%" /><br/>

- Master 과 Worker Node에 있는 `kubelet` 들은 모두 `10250`로 열려 있음

<br/><img src="./img/cluster_networking_img3.png" width="50%" /><br/>

- `kube-scheduler`는 `10259`번 포트에 열려있음

- `kube-controller-manager` 는 `10257`번 포트에 열려 있음

- Worker Node 는 `30000-32767` 범위 포트에 열려 있음

- `etcd`는 `2379`번 포트에 열려 있으며, 만약 여러 Master Node가 있다면 `etcd` 클라이언트끼리 통신할 수 있도록 `2380`가 열려 있어야 함

<br/><img src="./img/cluster_networking_img4.png" width="50%" /><br/>

---

### for CKA Exam

Kubernetes 클러스터에 Network Addon을 배치하는 것에 대한 중요한 팁

클러스터에 네트워크 플러그인을 설치하는 것도 포함

위브넷을 예로 들었지만, 여기에 설명된 모든 플러그인을 사용할 수 있다는 것을 명심

- https://kubernetes.io/docs/concepts/cluster-administration/addons/
- https://kubernetes.io/docs/concepts/cluster-administration/networking/#how-to-implement-the-kubernetes-networking-model

CKA 시험에서 네트워크 Addon을 배포해야 하는 문제에 대해서,
특별한 지시가 없는 한 위 링크에서 설명한 솔루션을 사용할 수 있음

그러나 현재 문서에는 서드파티 네트워크 Addon을 배포하는 데 사용될 정확한 명령어에 대한 직접적인 언급이 포함되어 있지 않음

위의 링크는 third-party/vendor 사이트 또는 GitHub 저장소로 리디렉션되며, 이는 시험에서 사용될 수 없음

(Kubernetes documentation vendor-neutral에 있는 콘텐츠를 유지하기 위함)

참고: 공식 시험에서는 필수 CNI 배치 세부 정보가 모두 제공됨

