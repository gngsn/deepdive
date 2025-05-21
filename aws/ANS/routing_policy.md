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

