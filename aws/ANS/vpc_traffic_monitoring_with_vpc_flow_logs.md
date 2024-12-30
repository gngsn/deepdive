# VPC Traffic Monitoring with VPC Flow logs

## VPC Flow logs

- 인터페이스로 인/아웃 IP 트래픽 정보 캡처:
  - VPC Flow Logs
  - Subnet Flow Logs
  - Elastic Network Interface Flow Logs
- Monitor & Troubleshoot connectivity 문제 해결에 도움
  - 가령, EC2 인스턴스 연결에 실패할 때, Flow logs 를 통해 어떤 ENI가 해당 트래픽을 거부했는지 확인 가능. 이후 Security Group 이나 NACL Role 을 확인해가면서 Connectivity 이슈 처리.
  - 악의적인 IPs가 당신의 IP 주소에 접근하려고 하는 지 확인할 수 있음. e.g., ENI 혹은 IP 주소에 얼마나 많은 요청이 받았는지 확인. 이후 해당 IP를 필터링 할 수 있음
- Flow Logs 데이터는 S3 / CloudWatch Logs / Kinesis Data Firehose 로 전송 가능
- AWS에서 관리되는 인터페이스의 네트워크 정보도 캡처 가능: ELB, RDS, ElastiCache, Redshift, Amazon WorkSpaces
- VPC Flow Logs를 활성화해도 네트워크 성능에는 영향 없음

<br>
