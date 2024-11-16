# Choosing Kubernetes Infrastructure

## Development / Testing / Education purpose

쿠버네테스는 다양한 시스템에 다양한 방식으로 배포되며, 노트북이나 컴퓨터에서 시작하는 몇 가지 방법이 있음

### on Linux 

**방법 1.** 수동으로 바이너리 파일을 설치하고 로컬 클러스터를 설정할 수 있음

→ 하지만, 너무 번거롭고 시간이 오래 걸림

모든 걸 자동화하는 솔루션을 사용하면 빠르고 간편하게 클러스터 설정할 수 있음

### on Windows

Window 용 바이너리 파일이 없기 때문에 반드시 Linux VM 을 위한 가상 컴퓨터를 통해서 설치해야 함

: Hyper-V, VMware Workstation, VirtualBox 등

Docker 이미지로 실행하는 다른 방법이 있지만, 결론적으로는 Hyper-V 위 Linux 에서 실행되는 것  

### Other Solutions

Minikube 이나 Kubadm 같은 솔루션 사용할 수 있음

**Minikube**
- VMs 까지 함께 배포
- 단일 클러스터만 제공

**Kubeadm**
- 단일 노드나 다중 노드 클러스터를 아주 빨리 배포 가능
- 하지만 필요 구성과 요구되는 호스트를 직접 프로비전해야 함

<br>

## Production purpose

<table>
<tr>
<th></th>
<th>Turnkey Solutions</th>
<th>Hosted Solutions</th>
</tr>
<tr>
<td>Introduction</td>
<td>
요구되는 VM 프로비전할 때나 호스트 혹은 관리 솔루션
이 위에 쿠버네티스 클러스터를 설정하기 위한 툴이나 스크립트를 사용
</td>
<td>
Kubernetes as a service 와 같은 개념으로, VM은 제공자에 의해 관리됨
가령, AWS의 kOps 도구를 이용해 쿠버네티스 클러스터를 배포
</td>
</tr>
<tr>
<td>Role</td>
<td>

- You Provision VMs
- You Configure VMs
- You Use Scripts to Deploy Cluster
- You Maintain VMs yourself
- Eg: Kubernetes on AWS using KOPS

VM을 프로비전하고, VM을 구성하고, 스크립트로 Cluster를 배포하고, VM를 직접 유지 보수함

(가령, Kubernetes on AWS using kOps 등)
</td>
<td>

- Kubernetes-As-A-Service
- Provider provisions VMs
- Provider installs Kubernetes
- Provider maintains VMs
- Eg: Google Container Engine (GKE)

</td>
</tr>
<tr>
<td>Role</td>
<td>

- **OpenShift**
  - Red Hat 의 온프레미스 쿠버네티스 플랫폼
  - 오픈 소스 컨테이너 앱 플랫폼으로 쿠버네티스에 기반하여 만들어짐
- **Cloud Foundry Container Runtime**
  - 오픈 소스 프로젝트로 고가용성 쿠버네티스 클러스터를 배포 및 관리하는 걸 도움
- **VMware Cloud PKS**
  - 기존 VMware 환경을 활용하고 싶을 때 사용
- **Vagrant**
  - 유용한 스크립트들을 제공해 서로 다른 클라우드 서비스 공급자에 쿠버네티스 클러스터를 배포

</td>
<td>

- **Google Container Engine (GKE)**
- **OpenShift Online**
  - Red Hat의 제품으로 온라인에서 완전히 작동하는 쿠버네티스 클러스터에 접속할 수 있음
- **Azure Kubernetes Service**
- **Amazon Elastic Container Service for Kubernetes (EKS)**

</td>
</tr>
</table>