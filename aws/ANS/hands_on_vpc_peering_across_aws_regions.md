# Hands On: VPC Peering across AWS regions

- Create **VPC-A** in Mumbai (`ap-south-1`) region (CIDR: `10.10.0.0/16`)
- Create **Internet Gateway** and Attach to VPC-A
- Create **Public Subnet** in VPC-A (10.10.0.0/24)
- Launch EC2 instance and assign Public IP. **Open inbound port 22** for your IP.
    - Auto-assign Public IP 가 Disabled (default) 라면, **Enable**로 변경

- Create **VPC-B** in North Virginia (us-east-1) region (CIDR: 10.20.0.0/16)
- Create **Private Subnet** in VPC-B (10.20.0.0/24)
- Launch EC2 instance in private subnet. Open inbound port 22 and ICMP for VPC-A CIDR range
- Create **VPC peering connection** request from VPC-A to VPC-B
    - Requester: VPC-A, Accepter: VPC-B
- Accept the connection request in VPC-B
- Modify route tables on both sides for traffic on other VPC
- Login To EC2-A instance and try to connect to EC2-B (ping or SSH)
