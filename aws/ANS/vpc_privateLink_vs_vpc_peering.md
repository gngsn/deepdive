# VPC PrivateLink vs VPC Peering

|                       | **AWS PrivateLink**                                                       | **VPC Peering**                                                                                                      |
|-----------------------|---------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------|
| **Use Case**          | When there are many resources that should communicate between peered VPCs | When you want to allow access to only a single application hosted in your VPC to other VPCs without peering the VPCs |
| **Overlapping CIDRs** | Cannot be created                                                         | Supports overlapping CIDR                                                                                            |
| **Connections**       | Maximum of 125 peering connections                                        | No limit                                                                                                             |
| **Traffic Direction** | Enables bidirectional traffic origin                                      | Allows only the consumer to originate the traffic                                                                    |

<br><hr><br>

# VPC PrivateLink Summary

- VPC Peering - 같거나 다른 Region 내 두 VPC 간의 통신을 가능하게 함
- VPC endpoint: VPC 와 특정 AWS 서비스 간의 Private connection 지원
    - 특정 조건의 AWS 서비스
        - 지원되는 AWS 서비스
        - PrivateLink로 연결된 서비스 (Internet gateway / NAT device / VPN connection / AWS Direct Connect connection 없이 연결)
- VPC 내의 인스턴스는 서비스와 통신하기 위해 Public IP 주소가 필요하지 않음
- VPC와 다른 서비스 간의 트래픽은 Amazon 네트워크를 통해서만 전달됨
- Gateway VPC endpoint: 서비스에 연결할 수 있게 하지만, VPC 내에서 네트워킹 컴포넌트로 존재하지 않음
- Interface VPC endpoint: VPC 내부에서 AWS Cloud 서비스에 Private하게 접근할 수 있게 하는 Elastic Network Interface, Private IP 주소, DNS 이름
- AWS PrivateLink은 Interface VPC endpoint의 확장으로, 자체 endpoint를 생성하거나 다른 사람이 생성한 endpoint를 사용할 수 있음

<details>
<summary>원본</summary>

- VPC peering enables the communication between 2 VPCs in same region or across regions
- A VPC endpoint enables you to privately connect your VPC to supported AWS services and Services powered by PrivateLink
  without requiring an internet gateway, NAT device, VPN connection, or AWS Direct Connect connection.
- Instances in your VPC do not require public IP addresses to communicate with resources in the service.
- Traffic between your VPC and the other service does not leave the Amazon network.
- Gateway VPC endpoints allow you to connect to services but do not exist as a networking component in your VPC
- Interface VPC endpoints are elastic network interfaces, private IP addresses, and DNS names that allow you to access AWS Cloud services privately from within your VPC
- AWS PrivateLink is an extension of interface VPC endpoints, and it allows you to create your own endpoints or consume endpoints that others have created

</details>

<br><hr><br>

# Exam Essentials

- VPC peering은 Transitive routing을 지원하지 않음. 따라서 다른 VPC의 IGW, NAT에 대한 접근은 불가능
- VPC peering은 다른 VPC의 Security group을 사용하여 inbound rule을 설정할 수 있음
- 최대 125개의 VPC peering 연결을 생성할 수 있음
- VPC gateway endpoint은 IGW나 NAT 없이 동일한 AWS region의 S3나 DynamoDB에 대한 private connectivity를 제공
- Gateway endpoint로 트래픽을 라우팅하려면 해당 서브넷의 route table을 수정해야 함
- Gateway endpoint는 Direct Connect/VPN이나 VPC Peering VPC interface endpoint를 통해 접근할 수 없음
- Interface endpoint는 VPC에 ENI를 생성함
- Interface endpoint는 Regional과 zonal DNS 이름을 받음
- Route53 private hosted zone을 사용하여 Alias record를 interface DNS에 사용할 수 있음
- Interface endpoint는 Direct Connect connection, AWS managed VPN, VPC peering connection을 통해 접근할 수 있음
- 트래픽은 VPC 내의 리소스에서 발생하며, endpoint 서비스는 요청에 응답할 수 있음. Endpoint 서비스는 요청을 시작할 수 없음


<details>
<summary>원본</summary>

- VPC peering does not support transitive routing. Hence you can not access IGW, NAT of other VPC through peering connection
- VPC peering allows using Security group as source for inbound rules from other VPC
- You can create maximum 125 VPC peering connections
- VPC gateway endpoint enables private connectivity to S3 or DynamoDB in same AWS region without requiring IGW or NAT
- To route traffic through gateway endpoint, you should modify the route table of the required subnet
- Gateway endpoint is not accessible over Direct Connect/VPN or VPC Peering VPC interface endpoint create an ENI into your subnets
- Interface endpoint receives the Regional and zonal DNS name.
- You can also use Route53 private hosted zone to use your custom DNS with Alias record to interface DNS
- Interface endpoint can be accessed over Direct Connect connection AWS managed VPN and VPC peering connection
- Traffic originates from the resources in the VPC and endpoint service can only respond to the request. Endpoint service cannot initiate the request

</details>



