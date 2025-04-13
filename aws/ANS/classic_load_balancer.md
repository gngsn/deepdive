# Classic Load Balancer

<br/><img src="./img/classic_load_balancer_img1.png" width="40%"/><br/>

- Layer 4 (TCP) and Layer 7 (HTTP)에서 작동
- 지원 프로토콜: HTTP, HTTPS, TCP, SSL
- EC2 인스턴스가 Target Group 없이 CLB에 직접 등록됨
- 헬스 체크는 HTTP, HTTPS 또는 TCP일 수 있음
- EC2-Classic 네트워크 지원

<br/>

<br/><img src="./img/classic_load_balancer_img2.png" width="30%"/><br/>

| Listener                                                                           | Internal                                                            |
|------------------------------------------------------------------------------------|---------------------------------------------------------------------|
| HTTP (L7)                                                                          | HTTP or HTTPS<br/><small>(Must install certificate on EC2)</small>  |
| HTTPS (L7) – SSL Termination<br/><small>(Must install certificate on CLB)</small>  | HTTP or HTTPS<br/><small>(Must install certificate on EC2)</small>  |
| TCP (L4)                                                                           | TCP or SSL<br/><small>(Must install certificate on EC2)</small>     |
| SSL (L4)<br/><small>(Must install certificate on CLB)</small>                      | TCP or SSL<br/><small>(Must install certificate on EC2)</small>     |


<br/>

## Classic Load Balancer – SSL considerations

- EC2에 HTTPS 혹은 SSL을 설정하는 것을 "**Backend Authentication**"이라고 함 (CLB와 백엔드 EC2 인스턴스 간의 인증)
- TCP => TCP는 모든 트래픽을 EC2 인스턴스로 전달함 (종료<sub>termination</sub> 없음):
  - 2-way Mutual SSL Authentication을 수행하는 유일한 방법

<br/><img src="./img/classic_load_balancer_img3.png" width="100%"/><br/>

<small>시험에 나올 수 있는 내용</small>

<br/>

