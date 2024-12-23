# Network performance Summary

- **높은 네트워크 대역폭 및 처리량**
  - Jumbo Frames: 한 패킷에 대해 9001 byte까지 전송 가능. 더 많은 데이터를 더 적은 수의 패킷으로 전송 가능.
  - EC2 Enhanced Networking, 배치 그룹(Placement Groups), EBS 최적화 인스턴스(EBS Optimized Instances), DPDK를 사용.
- **인스턴스 수준의 네트워크 최적화**
  - Enhanced Networking(SR-IOV, ENA, Intel VF 82599)
  - Placement Groups: EC2 인스턴스들이 물리적으로 근접하게 위치. 저지연으로 더 많은 패킷 전송 가능.
  - EBS Optimized Instances
- **운영 체제 수준의 네트워크 최적화**
  - **DPDK**(Intel Data Plane Development Kit) 활용
- **EFA(Elastic Fabric Adapter)**
  - ENA(Elastic Network Adapter)에 추가적으로 OS-bypass 기능을 제공하며, HPC 워크로드에 대해 더욱 향상된 네트워크 성능을 제공

<br>

# Exam Essentials

**Throughput**
- VPC 내부에서, Jumbo Frames를 사용하면 MTU 크기를 9001 바이트로 설정할 수 있음
- 최대 MTU 크기가 1500 바이트로 제한되는 경우:
  - 인터넷 게이트웨이를 통한 트래픽
  - 서로 다른 리전 간 VPC peering 연결을 통한 트래픽
  - VPN 연결을 통한 트래픽
- ⭐️ PPS(Packet Per Second)가 병목인 경우, MTU를 증가시키면 더 높은 처리량을 제공할 수 있음 
- Enhanced Networking과 Placement Group은 EC2와 하이퍼바이저 간의 지연 시간을 줄이며, **DPDK**는 운영 체제 수준에서 패킷 처리를 향상

**Bandwidth**
- EC2 네트워크 대역폭은 인스턴스 패밀리, 크기, Enhanced Networking 지원 여부 등 다양한 요소에 따라 달라짐
- 인스턴스에 사용할 수 있는 Multi-flow 트래픽의 총 대역폭은 트래픽의 목적지에 따라 달라짐
  - 리전 내 트래픽
    - [EC2 인스턴스](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-network-bandwidth.html)의 전체 대역폭 사용 가능
    - EC2 인스턴스 간 또는 EC2와 S3 간 최대 100 Gbps 대역폭 사용 가능(다중 흐름 사용 시)
  - 리전 외부, 인터넷 또는 Direct Connect 트래픽
    - 최소 32 vCPU를 가진 최신 세대 인스턴스: 사용 가능한 네트워크 대역폭의 50%
    - 32 vCPU 미만의 인스턴스: 5 Gbps로 제한
- 단일 흐름(5-tuple) 트래픽의 대역폭은 5 Gbps로 제한되며, 인스턴스가 클러스터 배치 그룹 내에 있을 경우 최대 10 Gbps까지 가능
