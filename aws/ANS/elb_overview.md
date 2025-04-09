# AWS Elastic Load Balancer

## What is load balancing?

로드 밸러스는 다운스트림 서버로 트래픽을 포워팅하는 서버들

사용자는 여러 서버에 접근하지 않고, 그 앞단의 Load Balancer 에 접근하면 됨

더 많은 사용자가 있을수록 더 많은 부하가 EC2 인스턴스에 분산됨

<br/><img src="./img/elb_overview_img1.png" alt="ELB Overview Health Check" width="100%"/><br/>

<br/>

## Why use a load balancer?

- 여러 다운스트림 인스턴스에 부하를 분산
- 애플리케이션에 대한 단일 액세스 포인트(DNS)를 노출
- 다운스트림 인스턴스의 장애를 원활하게 처리
- 인스턴스에 대한 정기적인 헬스 체크 수행
- 웹사이트에 대한 SSL 종료(HTTPS) 제공
- 쿠키로 Stickiness 강제 적용
- 가용 영역 간 고가용성
- 공용 트래픽과 사설 트래픽 분리

<br/>

## Why use an Elastic Load Balancer?

- Elastic Load Balancer는 AWS에서 제공하는 로드 밸런서
    - AWS는 로드 밸러스가 정상 동작하도록 보장
    - AWS는 업그레이드, 유지 관리, 고가용성을 처리
    - AWS는 몇 가지 configuration knob만 제공
    - AWS는 몇 가지 구성 옵션<sup>configuration knobs</sup>만 제공 (조절 가능한 파라미터나 세팅 값 등)
- AWS는 자체 로드 밸런서를 설정하는 것이 더 저렴하지만, 많은 노력이 필요
- AWS는 많은 AWS 제공/서비스와 통합됨
    - EC2, EC2 Auto Scaling Groups, Amazon ECS
    - AWS Certificate Manager (ACM), CloudWatch
    - Route 53, AWS WAF, AWS Global Accelerator

## Health Checks

- Health Checks는 로드 밸런서에 매우 중요함
- 로드 밸런서가 트래픽을 전달하는 인스턴스가 요청에 응답할 수 있는지 알 수 있도록 함
- 헬스 체크는 포트와 경로(/health가 일반적)에서 수행됨
- 응답이 200(OK)이 아니면 인스턴스는 비정상으로 간주됨

<br/><img src="./img/elb_overview_img2.png" alt="ELB Overview Health Check" width="100%"/><br/>










