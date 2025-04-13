# Application Load Balancer


- Layer 7 (HTTP)에서 작동
- 지원 프로토콜: HTTP, HTTPS, WebSocket, HTTP/2, gRPC
- 머신들 (타겟 그룹) 간에 여러 HTTP 애플리케이션으로 로드 밸런싱
- 동일한 서버에서 여러 애플리케이션/포트로 로드 밸런싱 (e.g. 컨테이너)
- 사용자 정의 HTTP 응답을 반환하는 지원
- 리디렉션 지원 (e.g. HTTP에서 HTTPS로)

<br/><img src="./img/application_load_balancer_img1.png" alt="ALB - Target Group" width="60%"/><br/>

<pre>
ALB는 여러 개의 리스터를 가질 수 있고, 각 리스너는 하나 이상의 규칙을 가질 수 있음.
위 예시에서, 리스너 1은 하나의 규칙을 가지고 있고, 그 규칙은 트래픽을 타겟 그룹으로 보내는 것일 수 있음.
타겟은 예를 들어 EC2 인스턴스일 수 있고, 헬스 체크도 가질 수 있음.
<small>* 타겟 그룹은 여러 개의 타겟을 가질 수 있고, 때로는 타겟 그룹 간에 타겟이 공유되기도 함. 또, 헬스 체크를 가질 수 있음.</small>

리스너 2는 두 개의 규칙을 가지고 있고, 각 규칙은 서로 다른 타겟 그룹으로 트래픽을 보낼 수 있음.
리스너는 HTTP, HTTPS, WebSocket, HTTP/2 및 gRPC 프로토콜을 지원함.
</pre>

<br/>

# Application Load Balancer

- **Target Groups**
    - EC2 Instances (can be managed by an ASG) – HTTP
    - ECS Tasks (managed by ECS itself) – HTTP
    - Lambda functions – HTTP request를 JSON event로 변환
    - IP Addresses – private IP addresses만 가능 (e.g., EC2 instances in peered VPC, on-premises servers accessed over AWS Direct Connect or VPN connection)

• Target Groups
• EC2 Instances (can be managed by an ASG) – HTTP
• ECS Tasks (managed by ECS itself) – HTTP
• Lambda functions – HTTP request is translated into a JSON event
• IP Addresses – must be private IP addresses (e.g., EC2 instances in peered VPC, on-premises servers accessed over AWS Direct Connect or VPN connection)
• Supports Weighted Target Groups
• Example: multiple versions of your application, blue/green deployment
• Health Checks can be HTTP or HTTPS (WebSocket is not supported)
• Each subnet must have a min of /27 and 8 free IP addresses
• Across all subnets, a maximum of 100 IP addresses will be used per ALB



<br/><br/>


# Network Load Balancer
