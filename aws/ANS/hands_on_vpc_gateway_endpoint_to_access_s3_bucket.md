# Hands On: VPC gateway endpoint to access S3 bucket

S3에 접근하기 위해서 아래 두가지가 필요

- Network connectivity: 네트워크 연결성.
- IAM permission: S3 버킷이 Private 이라면, EC2에서 S3에 접근하려면 IAM permission이 필요함

## Steps

들어 가기 전, S3 버킷 먼저 생성
- Name: `aws-vpc-endpoint-demo`

#### 1. VPC 생성 후, Public & Private Subnet 생성
- Name: `EndpointVPC`, IPv4 CIDR block: `10.10.0.0/16`

#### 1-1. Internet Gateway 생성 - `EndpointVPC-IGW` 후 VPC에 연결

#### 2. Subnet 생성

#### 2-1. Public Subnet
- Name: `EndpointVPC-Public`, IPv4 CIDR block: `10.10.0.0/24`

#### 2-2. Private Subnet
- Name: `EndpointVPC-Private`, IPv4 CIDR block: `10.10.1.0/24`

#### 2-3. Public Subnet의 RouteTable 생성 및 설정 후 Public 서브넷에 연결
- Name: `EndpointVPC-Public`
- Route: Destination: `0.0.0.0/0`, Target: `igw-093....`
- Public Subnet `EndpointVPC-Public`에 연결

#### 2-4. Private Subnet의 RouteTable 생성 및 설정 후 Private 서브넷에 연결
- Name: `EndpointVPC-Private`

#### 2-5. EC2 인스턴스 생성
- Public EC2 Instance: Name - BastionHost w/ Public IP
- Private EC2 Instance: Name - EndpointApplication
    - Security Group: 모든 VPC 오픈

Public EC2 Instance 에서 `google.com` 이나 `amazon.com`으로 ping을 보내면 예상한 대로 접근 불가

```
$ aws s3 cp test.text s3://aws-vpc-endpoint-demo/
```

하지만, 위와 같이 Public EC2 Instance 에서 S3에 파일 업로드를 시도하면 **실패**

> **WHY?**
> 
> EC2에 적용할 **IAM Role** 필요

<br>

#### 3. IAM Role 생성 후 Private EC2 Instance에 연결
- Name: `Endpoint-EC2-Role-S3`
- Policy: `AmazonS3FullAccess`

하지만, 아직도 연결 불가

> **WHY?**
> 
> **Network Connectivity** 가 없기 때문

→ **VPC Gateway Endpoint 필요**

<br>

#### 4. VPC Gateway Endpoint 생성
- Service: `s3.amazonaws.com-1.s3`
- VPC: `EndpointVPC`

위처럼 VPC Gateway Endpoint를 생성하면, Subnet Route Table이 자동 설정됨

**Rotue Tables** 탭에서 VPC 내 할당할 수 있는 라우트 테이블이 자동으로 탐지되는데, Private Subnet에 할당된 Rotue Table 확인할 수 있음

해당 Route Table을 확인해보면 **Routes** 탭에  Prefix List가 추가된 것을 확인할 수 있음 `pl-78a54011(com.amazonaws.region.s3), ips...`

Target이 생성한 VPC Gateway Endpoint인 `vpce-0d7...`로 설정되어 있음

<br>

#### 5. Test

리전을 명시하여 S3에 요청하면 파일 업로드 성공

```bash
$ aws s3 cp test.text s3://aws-vpc-endpoint-demo/ --region ap-south-1
```

> **Why wasn’t the connection established?**
> 
> : VPC Endpoint는 S3에 원래 접근할 수 있는 엔드 포인트가 존재했기 때문.