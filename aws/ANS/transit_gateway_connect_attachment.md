# Transit Gateway Connect Attachment

# Transit Gateway Connect Attachment

VPC Attachment 뿐만 아니라, 

Direct Connect Gateway를 사용한 Direct Connect ~ Transit Gateway 연결을 위한 Direct Connect Attachment를 가질 수 있음

또, Transit Gateway 위에 IPsec VPN tunnels를 종료시킬 수 있는 VPN Attachment를 가질 수 있음

> **IPsec VPN tunnel**: IPsec (인터넷 프로토콜 보안) VPN 터널은 인터넷과 같은 보안되지 않은 네트워크를 통해 두 끝점 간의 데이터 트래픽을 암호화하는 연결

이 Attachment는 Transit Gateway를 가상 Appliances에 연결하는 데 사용됨 (ex. SD-WAN routers)

기존의 SD-WAN 네트워크를 확장하여 Transit Gateway를 사용하여 AWS로 연결할 때 필수적임

## Transit Gateway – Connect attachment

- Transit Gateway Connect Attachment: VPC 혹은 온프레미스 내에, Transit Gateway 와 third-party virtual appliances 사이의 연결 생성 (SD-WAN appliances)
- Connect Attachment는 '기존의 VPC' 혹은 'AWS Direct Connect Attachment'를 기반 전송 메커니즘로써 사용
- 고성능을 위해 Generic Routing Encapsulation (GRE) 터널 프로토콜을 지원하며, 동적 라우팅을 위해 Border Gateway Protocol (BGP)을 지원

사실, Connect Attachment에서는 BGP Peering이 반드시 필요함

Connect Attachment를 먼저 생성하고,
GRE tunnel 연결을 만들기 위해 (establish) BGP Peering를 갖게 되는데,
궁극적으로, Transit Gateway와 Network Appliances 사이에 BGP 세션이 설정될 것임

그리고 Transit Gateway는 내부 IP 주소와 외부 IP 주소를 가질 것임 (BGP Configuration)

Transit Gateway는 고유한 IP 주소를 가질 것이며, Transit Gateway를 생성할 때 이 IP 주소를 할당할 수 있음 

또는 나중에 이 주소를 수정할 수도 있음
