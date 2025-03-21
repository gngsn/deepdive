# CloudFront Origins

- **S3 Bucket**
    - 파일 배포
    - CloudFront Origin Access Control (OAC)을 사용한 보안 강화 – 이전 OAI
    - CloudFront를 사용하여 S3로 파일 업로드
- **웹사이트 설정**
    - 버킷에서 **Static Website hosting** ⎯ 정적 웹사이트 호스팅 ⎯ 활성화
- **MediaStore Container & MediaPackage Endpoint**
    - AWS 미디어 서비스를 사용하여 비디오 온 디맨드 (VOD) 또는 라이브 스트리밍 비디오 제공
- **사용자 정의 오리진 (HTTP)**
    - EC2 인스턴스
    - Elastic Load Balancer (CLB 또는 ALB)
    - API 게이트웨이 (더 많은 제어를 위해, 혹은 API 게이트웨이 엣지 사용)
    - 원하는 모든 HTTP 백엔드

<br/>

### CloudFront Origins – S3 as an Origin

CloudFront는 콘텐츠 전송 네트워크(CDN)으로 여러 종류의 기원을 제공.

<img src=""/>

- 첫 번째는 S3 버킷으로, 다양한 파일 배포에 사용됨.
- **S3 버킷과 CloudFront 간의 보안**: S3 버킷 정책에 OAC(Origin Access Control)를 묶어 CloudFront에서만 S3 버킷에 액세스할 수 있도록 보장.
- 파일 업로드를 가속화하기 위해 S3로 파일을 인그레스할 때도 사용할 수 있음.
- **웹사이트 호스팅:** S3에서 정적 웹사이트 호스팅을 활성화하고 CloudFront를 사용하여 전 세계적으로 웹사이트 콘텐츠를 배포.

<br/>

### CloudFront Origins – ALB or EC2 as an origin

- **사용자 정의 HTTP 호환 오리진:**
    - 가령, EC2 인스턴스, Elastic Load Balancer(CLB 또는 ALB), API Gateway 또는 온프레미스 서버가 있음.

<img src=""/>
