# Transit Gateway VPC Network Patterns

## Transit Gateway - VPC Network patterns

### Flat network

<br><img src="./img/transit_gateway_vpc_network_patterns_img1.png"><br>

모든 VPCs 들이 Transit Gateway에 연결되어 각 VPC에 연결할 수 있는 상태 

### Segmented network

<br><img src="./img/transit_gateway_vpc_network_patterns_img2.png"><br>

모든 VPCs 들이 특정 VPN에만 연결될 목적으로, 
각 VPC 하위의 Subnet Route Table에 다른 VPC로 이동할 수 있는 CIDR를 등록하지 않음

VPC A에서 192.168.0.0/16 하위 IP를 요청하면 VPN으로 이동할 수 있지만,
VPC B에 해당하는 10.2.0.0/16 으로 이동할 경로는 어디에도 정의되어 있지 않음

<br><hr>

# Transit Gateway AZ considerations

## VPC & Subnets design for Transit Gateway

오직 하나의 AZ 하위 Subnet 만 Transit Gateway에 연결할 수 있음

<br><img src="./img/transit_gateway_az_considerations_img1.png"><br>

> 보통 다른 서비스에서는 ENI를 생성하면, 해당 VPC에 있는 모든 서브넷이 해당 ENI를 통해 통신할 수 있었는데, Transit Gateway는 예외임

### Availability zone considerations

- VPC를 Transit Gateway에 연결할 때, Transit Gateway가 VPC 서브넷의 리소스로 트래픽을 라우팅할 수 있도록 하나 이상의 가용 영역(AZ)을 활성화해야 함
- 각 가용 영역을 활성화하려면 정확히 하나의 서브넷을 지정해야 함 (보통 **`/28`** 범위 사용 - 워크로드 서브넷을 위한 IP 절약을 위함)
- Transit Gateway는 지정된 서브넷에서 하나의 IP 주소를 사용하여 네트워크 인터페이스를 생성함
- 가용 영역이 활성화되면, 지정된 서브넷뿐만 아니라, 해당 가용 영역 내의 모든 서브넷으로 트래픽을 라우팅할 수 있음
- ⭐️**Transit Gateway Attachment가 없는 가용 영역에 위치한 리소스는 Transit Gateway에 접근할 수 없음**

Transit Gateway 는 주어진 AZ에 transit gateway Attachment를 생성하면,
해당 AZ에서는 오직 그 Attachment를 통해서만 통신할 수 있음

Transit Gateway Attachment를 특정 AZ(AZ-A)에서 생성하면,
**같은 AZ(AZ-A)**에 있는 서브넷만 해당 Attachment를 통해 Transit Gateway와 통신할 수 있음.

다른 AZ(AZ-B, AZ-C)에 있는 서브넷은 해당 Attachment를 사용할 수 없음.

Transit Gateway Attachment를 생성하면 특정 AZ의 특정 서브넷과 연결됨.

여러 AZ에 있는 서브넷이 Transit Gateway와 통신할 수 있도록 하려면,
각 AZ의 서브넷에 대해 별도의 Transit Gateway Attachment를 생성해야 함

<br><hr>
