# Introduction to VPC private connectivity options

<br><img src="./img/introduction_to_vpc_private_connectivity_options_img1.png" width="80%" /><br>

Private Connectivity가 필요한 이유?

## Disadvantages of having Internet based traffic

- Security: 인터넷에 노출되면 보안 리스크가 발생할 수 있음.
- Consistent bandwidth and Latency: 트래픽이 인터넷을 타다보니, 대역폭이나 레이턴시 등을 예상할 수 없음
- Cost: NAT 기기는 시간 당 실행과 데이터 처리에 비용이 발생
- Public 환경이 필요 없는데도 불구하고 서버가 불필요하게 오픈될 수 있음 (가령, 데이터베이스, 앱 서버, 인트라넷 앱 서버 등)

=> 이를 위해 살펴볼만한 내용: VPC Peering, VPC Endpoints, VPC PrivateLink

<br>

# VPC Peering

두 개의 서로 다른 VPC A 와 B가 존재하고, 각각 Private EC2 A와 B 인스턴스를 하나씩 가짐.

이 때 인스턴스 A와 B 만을 private 하게 연결하고자 함.

이 때 VPC Peering을 사용.

- 두 VPC를 AWS 네트워크를 통해 프라이빗하게 연결
- 마치 동일한 네트워크에 있는 것처럼 통신 가능
- 피어링된 VPC는 동일한 AWS 리전일 수도 있고, 다른 리전일 수도 있음
- 서로 다른 계정의 VPC도 피어링 가능

**Caveats:**

- VPC CIDRs 는 겹치면 안됨
- 각 VPC의 Subnet 라우팅 테이블에 피어링 연결 정보를 추가해야 함
