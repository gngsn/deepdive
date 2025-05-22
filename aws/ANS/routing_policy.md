# Routing Policy - Simple

## Route 53 – Routing Policies

- DNS 쿼리의 응답 정의
- DNS가 트래픽을 "Routing"하는게 아니라, **DNS 쿼리에 응답**함
    - 로드 밸런서 라우팅과 다름
    - "Routing"이라는 단어에 혼동하면 안됨
- Route 53 지원 라우팅 정책
    - Simple (단순)
    - Weighted (가중치)
    - Failover (장애 조치)
    - Latency based (지연 시간 기반)
    - Geolocation (지리적 위치)
    - Multi-Value Answer (다중 값 응답)
    - Geoproximity (지리적 근접성) (Route 53 Traffic Flow 기능 사용)

<br/>

## Routing Policies – Simple

- 전형적으로, 트래픽을 단일 리소스로 라우팅
- 헬스 체크와 연결할 수 없음
- 같은 레코드에 여러 값을 지정할 수 있음
    - **여러 값 반환 시, '클라이언트'가 무작위로 선택**
    - Alias 활성화 시, 단일 AWS 리소스만 지정 가능

<br/>
<table>
  <tr>
    <th>Single Value</th>
    <th>Multiple Value</th>
  </tr>
  <tr>
    <td><img src="./img/routing_policy_simple_img1.png" alt="Routing Policies - Simple" /></td>
    <td><img src="./img/routing_policy_simple_img2.png" alt="Routing Policies - Multiple" /></td>
  </tr>
</table>

<br/>


# Routing Policy - Weighted

- 특정 리소스에 대한 요청 비율을 제어
- 각 레코드에 상대적 가중치를 할당
  - 트래픽 (%) = 특정 레코드의 가중치 / 모든 레코드의 가중치 합계
  - 가중치는 100%로 합칠 필요 없음
- DNS 레코드는 동일한 **이름**과 **유형**을 가져야 함
- **헬스 체크**와 연결할 수 있음
- 사용 사례: 지역 간 로드 밸런싱, 새로운 애플리케이션 버전 테스트 등
- 리소스에 대한 **트래픽을 중지**하려면 **해당 레코드에게 가중치 `0` 할당**
- **모든 레코드의 가중치가 `0` 이면, 모든 레코드가 동일하게 반환됨**


<br/><img src="./img/routeing_policy_weighted_img1.png" alt="Routing Policies - Weighted" width="80%" /><br/>

가령, 위와 같이 가중치를 70%, 20%, 10%로 설정하면, 각각 70%, 20%, 10%의 비율로 요청이 분배됨

**Demo**
- Subdomain `weighted.example.com` 라고 지정
- Routing Policy를 'Weighted' 선택
- Weight를 각각 70
-
'Add another record' 버튼 클릭 후, (2번 반복)
- Subdomain 를 모두 동일하게 입력 (e.g. `weighted.example.com`)
- Routing Policy를 'Weighted' 선택
- Weight를 각각 70, 20, 10으로 설정

<br/>

# Routing Policy - Latency-based

- 사용자에게 가장 낮은 지연 시간<sup>Latency</sup>으로 리소스에 요청을 라우팅
- 사용자의 지연 시간을 우선순위로 할 때 굉장히 유용
- 지연 시간은 사용자와 AWS 리전간의 트래픽을 기반으로 결정됨
- 단, 독일에서 발생한 요청이 미국으로 갈 수 있음 - 만약 지연 시간이 최소인 경우
- 헬스 체크 연동 가능 (장애 조치 기능<sup>failover capability</sup>를 가짐)

<br/><img src="./img/routeing_policy_latency_img1.png" alt="Routing Policies - Latency" width="80%" /><br/>

가령, 위와 같이 ALB를 `us-east-1` 와 `ap-southeast-1`에 각각 배포하고,
여러 국가에서 요청을 보낼 때, Route53은 지연 시간이 가장 짧은 리전으로 요청을 보냄

**Demo**
- Subdomain 를 latency.example.com으로 입력
- Routing Policy를 'Latency' 선택
- Region을 각각Asia Pacific(Singapore)
- Record ID를`ap-southeast-1`로 설정

'Add another eecord' 버튼 클릭 후,
- Subdomain 를 latency.example.com으로 입력
- Routing Policy를 'Latency' 선택
- Region을 각각 US East(N. Virginia)
- Record ID를`us-east-1`로 설정

'Add another eecord' 버튼 클릭 후,
- Subdomain 를 latency.example.com으로 입력
- Routing Policy를 'Latency' 선택
- Region을 Europe(Frankfurt)
- Record ID를`eu-central-1`로 설정

VPN을 통해 각 리전으로 요청을 보내면, 가장 짧은 지연 시간(가장 가까운 지역)으로 응답을 받을 수 있음

<br/>

# Route 53 - Health Checks

<br/><img src="./img/routeing_policy_health_check_img1.png" alt="Routing Policies - Health Check" width="80%" /><br/>

- HTTP 헬스 체크는 오직 **퍼블릭 리소스에만** 사용 가능
- 헬스 체크 => 자동화된 DNS 장애 조치
  1. 엔드포인트(애플리케이션, 서버, 기타 AWS 리소스)를 모니터링하는 헬스 체크
  2. 다른 헬스 체크를 모니터링하는 헬스 체크 (Calculated Health Check 라고 불림)
  3. CloudWatch Alarms을 모니터링하는 헬스 체크 (**완전한 제어 가능 ✅**)
    - e.g. DynamoDB의 스로틀<sup>throttles</sup>, RDS의 알람, 사용자 정의 메트릭 등 (프라이빗 리소스에 유용)
- 헬스 체크는 CloudWatch 메트릭과 통합됨


## Health Checks – Monitor an Endpoint

<br/><img src="./img/routeing_policy_health_check_img2.png" alt="Routing Policies - Monitor an Endpoint" width="80%" /><br/>

- 약 15개의 글로벌 헬스 체크가 엔드포인트 헬스를 체크
  - 3번의 헬스 체크가 성공해야 엔드포인트가 Healthy로 간주됨
  - 3번의 헬스 체크가 실패해야 엔드포인트가 Unhealthy로 간주됨
  - 헬스 체크는 30초마다 수행됨 (10초로 설정 가능 - 비용 증가)
  - 헬스 체크는 HTTP, HTTPS, TCP 프로토콜을 지원
  - 만약 18%의 헬스 체크가 엔드포인트를 Healthy로 보고하면, Route 53은 해당 엔드포인트를 Healthy로 간주함
  - 어떤 위치에서 Route 53이 헬스 체크를 수행할지 선택 가능
- 헬스 체크는 엔드포인트가 `2xx` 또는 `3xx` 상태 코드로 응답할 때만 성공으로 간주됨
- 헬스 체크는 응답의 처음 `5120 bytes` 의 텍스트에 따라 성공/실패로 간주될 수 있음
- 헬스 체크를 위해 **Route 53 헬스 체크부터의 요청을 허용하는 라우터/방화벽을 설정** 필수

<br/>

## Route 53 – Calculated Health Checks

<br/><img src="./img/routeing_policy_health_check_img3.png" alt="Routing Policies - Monitor an Endpoint" width="80%" /><br/>

- 여러 헬스 체크 결과를 조합하여 단일 헬스 체크로 결합
- OR, AND, NOT 연산자 사용 가능
- 최대 256개의 자식 헬스 체크 모니터링 가능
- 자식 헬스 체크가 몇 개 통과해야 부모 헬스 체크가 통과하는지 지정 가능
- 사용 예시: 웹사이트 유지보수 시 모든 헬스 체크가 실패하지 않도록 설정 가능

## Health Checks – Private Hosted Zones

<br/><img src="./img/routeing_policy_health_check_img4.png" alt="Routing Policies - Monitor an Endpoint" width="80%" /><br/>

- Route 53 헬스 체커는 VPC 밖에 위치
- 프라이빗 엔드포인트에 접근할 수 없음 (프라이빗 VPC 또는 온프레미스 리소스)
- **CloudWatch Metric**을 생성하고 **CloudWatch Alarm**과 연결한 후, 헬스 체크를 생성하여 알람 자체를 체크 가능


