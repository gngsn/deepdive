# CHAPTER 22. Addressing Cascading Failures

연속적 장애 다루기


📌 **Cascading Failure**
: 연속적 장애. 정상적인 것처럼 보여지는 응답 때문에 시간이 지나면서 장애가 계속해서 가중되는 현상

<br /><img src="./img/figure22-2.png" width="70%" />
<br /><small><b>Example production configuration for the Shakespeare search service</b></small><br />

## Causes of Cascading Failures and Designing to Avoid Them

<small><i>연속적 장애의 원인과 그 대책</i></small>

연속적 장애를 유발할 수 있는 상황:

### Server Overload

<small><i>서버 과부하</i></small>

연속적 장애를 유발하는 가장 일반적인 원인.

#### Example.

클러스터 A 의 프런트엔드가 초당 1,000 개의 요청 (Query Per Second, QPS) 을 처리한다고 가정

<br /><img src="./img/figure22-2.png" width="70%" />
<br /><small><b>Normal server load distribution between clusters A and B</b></small><br />

만일 클러스터 B 에서 장애가 발생하면(아래) 클러스터 A 에 전달되는 요청은 1,200 QPS 로 증가

<br /><img src="./img/figure22-3.png" width="70%" />
<br /><small><b>Cluster B fails, sending all traffic to cluster A</b></small><br />

- 클러스터 A 의 프런트엔드는 1,200 QPS 의 요청을 처리할 수 없으므로 자원이 부족하게 되어 충돌이 발생하거나 지연응답 혹은 오동작이 발생
- 그 결과 성공 처리된 요청 건 수는 1,000 QPS 이하로 감소


<br />

### Resource Exhaustion

<br />

### Service Unavailability

<br />

## Preventing Server Overload

<br />

## Slow Startup and Cold Caching

<br />

## Triggering Conditions for Cascading Failures

<br />

## Testing for Cascading Failures

<br />

## Immediate Steps to Address Cascading Failures

<br />

## Closing Remarks





























