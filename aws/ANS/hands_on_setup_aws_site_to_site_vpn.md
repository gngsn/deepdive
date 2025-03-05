# Hands On: Setup AWS Site-to-Site VPN

- **목표**: 회사 내의 데이터센터에서 AWS EC2 인스턴스으로 프라이빗 접근
- **데모**: 회사 데이터센터가 없기 때문에 N.Virginia Region 리전의 VPC-DC로 Customer Network 대체
- **데모 순서**
  1. AWS Network의 VGW (= VPN Gateway)
  2. Data Center
    - Internet Gateway를 설치할 Public Subnet 과 
    - EC2-VPN: SSH를 통한 VPN 소프트웨어 설치
  3. VPN Gateway 와 VPN 소프트웨어가 설치된 장치(EC2)를 IPSec VPN으로 연결
  4. Customer Network에서 AWS EC2의 Private IP로 `ping` 시도 후 연결 확인

<br/>

### Configure VPN Connectivity

**1. AWS Network**

| Destination    | Target     |
| -------------- | ---------- |
| 10.0.0.0/16    | Local      |
| 192.168.0.0/16 | vgw-xxxxxx |

⭐️ **중요**: VGW 로 향하는 라우트 설정

<br/>

**2. Customer Data Center**

| Destination    | Target     |
| -------------- | ---------- |
| 192.168.0.0/16 | Local      |
| 0.0.0.0/0      | igw-xxxxxx |

사용자의 데이터센터에서 시작하는 트래픽은 인터넷 게이트웨이를 통해 라우팅 되어야 함

이 때, 트래픽은 암호화된 상태

### Configure EC2 Security Group

**1. EC2 in AWS Network**

| Protocol      | Port Range | Source         |
| ------------- | ---------- | -------------- |
| ICMP IPv4 All | All        | 192.168.0.0/16 |

사용자 데이터 센터에서 들어올 `ping` 통신을 허용할 ICMP 프로토콜 추가

<br/>

**2. EC2 in Customer Data Center**

| Protocol      | Port Range | Source      |
| ------------- | ---------- | ----------- |
| SSH           | 22         | MyIP        |
| ICMP IPv4 All | All        | 10.0.0.0/16 |

<br/>

## High level steps

### 1. VPC 생성
- 다이어그램에 표시된 것처럼 두 개의 다른 리전에서 VPC 생성. 
- AWS 네트워크
  - **VPC**
    - **Name**: VPC-AWS
    - **CIDR**: 10.0.0.0/16
  - **Private Subnet**
    - **Name**: VPC-AWS-Private-1
    - **CIDR**: 10.0.0.0/16
  - **Route Table**
    - **Name**: VPC-AWS-Private-RT
    - **Association**: VPC-AWS-Private-1
    - Main Route Table이 자동 생성되지만, 항상 전용 Route Table을 따로 생성하는게 좋음
  - **EC2**
    - **Name**: EC2-A
    - **VPC**: VPC-AWS
    - **Subnet**: VPC-AWS-Private-1
    - **Security Group**: EC2-A-SG
      - **Inbound**: ICMP - 192.168.0.0/16
- 온프레미스 DC 네트워크
  - **VPC**
    - **Name**: VPC-DC
    - **CIDR**: 192.168.0.0/16
  - Internet Gateway
    - **Name**: VPC-DC-IGW
    - Attach to VPC-DC
  -  **Public Subnet**
    - **Name**: VPC-DC-Public-1
    - **CIDR**: 192.168.0.0/24
    - **Route Table**
      - **Name**: VPC-DC-Public-RT
      - **Route**: (0.0.0.0/0, igw-xxxxxx) 추가
      - **Association**: VPC-DC-Public-1

- VGW 생성
  - **Name**: VPC-AWS-VGW

1. 두 VPC에 EC2 인스턴스 생성. VPC-DC의 경우 Amazon Linux 2023 AMI를 사용하여 Libreswan을 설치할 수 있도록 함. 두 EC2 인스턴스의 보안 그룹에서 다른 네트워크에서 ICMP 트래픽을 허용. EC2-VPN은 Public IP를 가짐. 또한 MyIP 또는 0.0.0.0/0에서 SSH를 열어야함.

2. Mumbai 리전에 Virtual Private Gateway (VGW)를 생성하고 VPC-AWS에 연결. VPC-AWS의 Private 서브넷 라우팅 테이블에 모든 트래픽 (0.0.0.0/0) 경로를 VGW를 통해 라우팅하는 라우트를 추가. EC2-VPN Public IP를 사용하여 Customer gateway를 생성.

3. Mumbai 리전에 VPN 연결 생성. VPC-DC CIDR을 사용하여 정적 라우팅과 함께 EC2-VPN Public IP를 사용.

4. VPN 연결 콘솔에서 Openswan을 위한 VPN 구성 파일을 다운로드.
VPN 소프트웨어는 AMI에 따라 다른데, 이 경우 Amazon Linux 2023 AMI를 사용하여 Libreswan을 사용.

1. EC2-VPN에 Libreswan을 설치. Libreswan fedora 저장소를 추가해야함. libreswan을 설치한 후 VPN 구성 파일의 지침을 따름. 환경에 맞게 IP 및 CIDR 범위와 같은 다양한 필드의 값을 바꿔야함. IPSec VPN 서비스를 시작.

2. EC2-VPN에서 AWS 측 EC2-A 인스턴스의 Private IP에 접근 후 성공 확인.


EC2-VPN에 할당된 Public IP 확인: `3.93.37.75`
