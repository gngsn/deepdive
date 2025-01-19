# VPC interface endpoint features

### Interface Endpoint

- Interface endpoints 가 VPC 내에 (ENI를 사용한) local IP 주소 생성

- HA(High Availability) 를 위해 AZ 당 하나의 Interface endpoint 생성

- 시간 당 비용과 데이터 처리 비용이 부과됨
  - Per hour cost (`~$0.01/hr` per AZ)
  - Data processing cost (~`$0.01/GB`) ← 데이터 처리량이 많을 때 고려해 봐야함
  - \+ **AZ 전송 비용 주의**: EC2와 Interface endpoint를 동일한 AZ에 위치시키는 것을 추천 
    - 가령, Interface endpoint를 다른 AZ (ex. AZ-2)에 위치 시키면, 
      내 EC2를 통해 다른 AWS 서비스를 사용 시 **AZ 데이터 전송 비용**을 지불해야함

- Security Groups – inbound rules 놓치지 않고 추가해야 함


- AWS가 Interface endpoint의 Private IP address를 리졸브할 Regional/Zonal DNS 엔트리를 생성
  - **Zonal**: Availability Zone Level. 해당 AZ 내 모든 IP 주소를 리졸브함
  - **Regional**: Zonal 보다 더 넓은 범위
  - **Regional / Zonal DNS entry**
    - 사용자가 'Regional' 혹은 'Zonal' DNS entry 중 하나를 고를 수 있음
    - 특정 AZ에만 Interface endpoint를 사용하고 싶다면, Zonal DNS entry를 설정하면 되고, 이를 통해 Cross zone data transfer 요금을 방지할 수 있음

- Interface endpoint 오직 IPv4만 지원 (IPv6 지원 X)

- Interface VPC endpoints 오직 TCP만 지원 
