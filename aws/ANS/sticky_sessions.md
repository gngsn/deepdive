# Sticky Sessions (Session Affinity)

- 클라이언트가 로드밸런스 뒤에 항상 동일한 인스턴스로 리다이렉트되도록 하는 Stickiness(이하 스티키니스) 적용 가능
- Classic Load Balancer, Application Load Balancer, Network Load Balancer 모두 동작
- 스티키니스는 CLB & ALB는 Cookie를 사용해 동작하고, 만기 일자를 설정할 수 있음
- 사용 사례: 사용자의 세션 데이터가 잃지 않게 함
- 스티키니스 사용 시, 로드 밸런서의 EC2 인스턴스 부하 조절에 불균형을 가져올 수 있음

## Sticky Sessions – Cookie Names

두 가지 종류의 쿠키: 

#### 1️⃣ Application-based Cookies
- 모든 레이어에 거쳐 스티키 세션이 필요할 때 사용하면 좋음
- **Custom cookie**
  - 타겟에 의해 생성됨
  - 애플리케이션에서 필요한 인자들을 포함할 수 있음
  - 쿠키 이름은 반드시 각 타겟 그룹 별로 개별로 구분되어야 함
  - ELB에서 이미 사용 중인 `AWSALB`, `AWSALBAPP`, `AWSALBTG` 를 사용하면 안됨
- **Application cookie**
  - 로드 밸런서에 의해 생성됨
  - 쿠키 이름은 `AWSALBAPP`

<br/>

#### 2️⃣ Duration-based Cookies
- 쿠키는 로드밸런서에 의해 생성됨
- 쿠키 이름 
  - `ALB`: `AWSALB`
  - `CLB`: `AWSELB`



