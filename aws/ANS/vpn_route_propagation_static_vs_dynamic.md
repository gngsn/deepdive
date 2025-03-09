# VPN Route Propagation (Static vs Dynamic)

## VPN Static and Dynamic routing
- **정적 라우팅**: VPN 연결 양측에서 사전에 CIDR 범위를 정의해야 함. 새로운 네트워크 범위를 추가하면 라우팅 변경 사항이 자동으로 전파되지 않음.
- **동적 라우팅**: 양측에서 새로운 네트워크 변경 사항을 자동으로 학습. AWS 측에서는 새로운 경로가 자동으로 라우트 테이블에 전파됨.
- AWS 라우트 테이블은  **최대 100개의 전파된 경로**만 가질 수 있음. 따라서 온프레미스 네트워크에서 100개 이상의 경로를 게시하지 않도록 주의해야 함.
- 이를 방지하기 위해, 여러 네트워크 범위를 더 큰 CIDR 범위로 통합하는 방법을 고려할 수 있음.

## Route Propagation in Site-to-Site VPN

- **정적 라우팅:**
    - CGW(Customer Gateway)를 통해 **10.0.0.1/24**에 대한 정적 경로를 기업 데이터 센터에 생성
    - VGW(Virtual Gateway)를 통해 **10.3.0.0/20**에 대한 정적 경로를 AWS에 생성

- **동적 라우팅 (BGP):**
    - BGP(Border Gateway Protocol)를 사용하여 자동으로 경로 공유 (인터넷에서는 eBGP 사용)
    - 라우팅 테이블을 직접 업데이트할 필요 없이 자동으로 처리됨
    - CGW와 VGW의 **ASN(자율 시스템 번호, Autonomous System Number)** 만 지정하면 됨  
