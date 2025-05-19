# Route 53 CNAME vs Alias
## Route 53 – Alias Records

- AWS 리소스에 대한 호스트 이름을 매핑
- DNS 기능의 확장
- 리소스의 IP 주소 변경을 자동으로 인식
- CNAME과 달리 DNS 네임스페이스의 최상위 노드(Zone Apex)에서 사용할 수 있음
- AWS 리소스에 대해 항상 A/AAAA 유형 (IPv4 / IPv6)
- **TTL 설정 불가**


## Route 53 – Alias Records Targets

- Elastic Load Balancers
- CloudFront Distributions
- API Gateway
- Elastic Beanstalk environments
- S3 Websites (S3 Bucket X)
- VPC Interface Endpoints
- Global Accelerator accelerator
- Route 53 record in the same hosted zone

**EC2 인스턴스에 대한 ALIAS 레코드 설정 불가**