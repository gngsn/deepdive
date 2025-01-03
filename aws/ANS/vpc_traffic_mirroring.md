# VPC Traffic Mirroring

**Traffic Mirroring**
: 어떤 트래픽이 오고/가던 간에, 혹은 특별히 ENI를 통해 흐르는 트래픽이라면, 
실시간으로 동일한 트래픽을 다른 타겟으로 복사할 수 있음.

그 후 복사된 트래픽을 모니터링 시스템에 연결하여, 악의적인 행위를 감지할 수 있음.

AWS VPC 에는 Traffic Mirroring 기능이 있는데,

여기에는 Mirror Targets, Mirror Filters, Mirror Sessions 등의 옵션이 있음.

이러한 세션을 시작하면, ENI에 도착하는 모든 트래픽이 다른 타겟으로 복사되어 모니터링이 가능함.

- EC2 인스턴스의 ENI에 도착하는 네트워크 트래픽 복사
- 복사된 트래픽을 외부 보안 및 모니터링 장비로 전송
- 컨텐츠 검사, 위협 모니터링, 문제 해결을 위함
- Traffic Mirroring 설정 방법 (AWS VPC 콘솔을 통해)
  - Mirror Target 생성
  - Traffic Filter 정의
  - Mirror Session 생성

### VPC Traffic Mirroring – ENI as Target

EC2에 부착된 ENI에 도착하는 모든 트래픽을 복사하여, 다른 타겟으로 전송하는 방법

<img src="">

Subnet A 내에 위치하는 EC2 Traffic Monitoring 에서 
Subnet B 내에 위치한 ENI (Traffic Destination)로 트래픽을 복사할 때,
두 ENI 사이에 Session을 정의할 수 있음

### VPC Traffic Mirroring – NLB as Target

타겟은 EC2 뿐만 아니라 NLB로도 설정 가능함.

<img src="">

### VPC Traffic Mirroring Filters

원하는 트래픽 만을 미러링 하도록 필터 설정 가능.

여러 개의 인스턴스를 정의할 수도 있음.

가령, Instance A 와 Instance B 를 통해 타겟 ENI 혹은 NLB로 트래픽을 복사.  

<img src="">

Traffic Filter Parameters
- Traffic Direction: Inbound or Outbound
- Action: Accept or Reject
- Protocol: L4
- Source Port Range/ Destination Port range
- Source CIDR block/ Destination CIDR block

### VPC Traffic Mirroring – Good to know

- VPC 오고/가는 트래픽을 복사하여, 네트워크 분석 시스템에 전송 - 잠재적인 네트워크/보안 이상 감지
- Mirror Source: ENI
- Mirror Target: 다른 ENI 혹은 NLB (UDP - 4789)
- Mirror Filter: 관심 있는 트래픽만 캡쳐
- 프로토콜, Source/Destination port 범위, CIDR 블록을 지정하여 트래픽 필터링
- 숫자로 된 규칙을 정의하여 트래픽을 각각의 목적지로 보냄
- Source와 Destination은 같은 VPC에 있을 수도 있고, 다른 VPC에 있을 수도 있음 - intra-Region VPC peering 혹은 transit gateway로 연결
- Source와 Destination은 다른 AWS 계정에 있을 수도 있음