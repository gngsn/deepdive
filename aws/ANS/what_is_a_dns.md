# What is a DNS?

# Route 53 Overview

- 사람이 읽을 수 있는 호스트 이름을 기계가 읽을 수 있는 IP 주소로 변환하는 시스템
- www.google.com => 172.217.18.36
- DNS는 인터넷의 중추
- DNS는 계층적 이름 구조를 사용
- 계층적 도메인 이름
  - `.com`
  - `example.com`
  - `www.example.com`
  - `api.example.com`

<br/>

## DNS Terminologies
http://api.www.example.com.

- **Domain Registrar**: Amazon Route 53, GoDaddy, ...
- **DNS Records**: `A`, `AAAA`, `CNAME`, `NS`, ...
- **Zone File**: contains DNS records
- **Name Server**: resolves DNS queries (Authoritative or Non-Authoritative)
- **Top Level Domain (TLD)**: `.com`, `.us`, `.in`, `.gov`, `.org`, ...
- **Second Level Domain (SLD)**: `amazon.com`, `google.com`, ...


```
                URL
  ——————————————————————————   ↙ (dot) Root
  http://api.www.example.com.
  ————                   ——— TLD
  Protocol       ——————————— SLD
             ——————————————— Sub Domain
         ——————————————————— FQDN (Fully Qualified Domain Name)
```


웹 브라우저가 `example.com`에 액세스하려고 함
1. 로컬 DNS 서버에 요청

"`example.com`이 뭐야?"

이 로컬 DNS 서버는 일반적으로 회사에서 관리하거나 인터넷 서비스 제공업체(ISP)에서 동적으로 할당.

그리고 로컬 DNS 서버가 이 쿼리를 본 적이 없다면, 먼저 루트 DNS 서버에 요청.
ICANN 조직에서 관리하는 루트 DNS 서버.

"`example.com`이 뭐야?"
첫 번째로 요청되는 서버.

루트 DNS 서버는 "나는 그것을 본 적이 없지만 .com을 알고 있다"고 말함

그래서 `.com`은 `NS` 레코드 이름 서버이며 `1.2.3.4`라는 공용 IP를 확인.

이것은 로컬 DNS에게 "이 답변은 모르겠지만, 당신의 답변에 조금 더 가까워질 수 있도록 `.com` 도메인을 알고 있으며, `.com` 도메인 이름 서버는 이 IP 1234를 가지고 있다"고 말하고 있음.
그래서 로컬 DNS 서버는 "좋아, 이제 최상위 도메인에 물어볼 것이다." 그래서 .com 도메인 서버에 1234를 요청합니다.
"내 쿼리의 답변을 알고 있습니까?" 이것은 IANA에서 관리하는 또 다른 도메인입니다.
example.com에 대해 다시 물어보겠습니다.
그리고 DNS 서버는 "예, 물론 example.com에 대해 알고 있습니다."라고 말할 것입니다. "나는 당신의 쿼리에 대한 답변을 즉시 알지는 못하지만, example.com이라는 서버가 있다는 것을 알고 있습니다." 그리고 그 IP는 5.6.7.8 이다. 그래서, 이 공용 IP는 당신의 질문에 대한 답변을 요청해야 합니다. 로컬 DNS 서버는 최종 서버인 2차 도메인 DNS 서버로 이동할 것입니다. 이 서버는 도메인 등록 기관에서 관리합니다. 예를 들어 Amazon Route 53과 같은 것입니다. DNS 서버는 "이봐, example.com에 대해 알고 있습니까?"라고 말할 것입니다. 그리고 DNS 서버는 example.com에 대한 항목을 가질 것입니다. 그래서 "예, 물론 example.com에 대해 알고 있습니다."라고 말할 것입니다. 그리고 예를 들어, example.com은 A 레코드이고 그 결과는 IP 5.6.7.8 이라고 합니다. 그래서 DNS 서버는 이제 DNS 서버에 재귀적으로 요청하여 답변을 알게 되었습니다. 그리고 "좋아, 예. 나는 그 답변을 즉시 캐시할 것이다. 왜냐하면 누군가가 다시 example.com을 요청할 때 즉시 답변을 얻고 싶기 때문이다."라고 말합니다. 그래서 웹 브라우저에 답변을 보낼 것입니다. 이제 웹 브라우저는 답변을 가지고 있으며 이 IP 주소를 사용하여 웹 서버에 액세스할 수 있습니다. 이것이 DNS 작동 방식입니다. 당신은 평생 동안 항상 DNS를 사용해왔습니다. 예를 들어 www.google.com에 액세스할 때 DNS를 사용하고 있습니다. 하지만 이제 DNS 쿼리가 어떻게 작동하는지 알게 되었습니다. 이것은 배경 지식일 뿐입니다. 이제 Route 53으로 이동하여 직접 DNS 서버를 관리하는 방법을 배울 것입니다.


## Route 53 - Registering a domain
## Route 53 - Creating our first records
## Route 53 - EC2 Setup
## Route 53 - TTL
## Route 53 CNAME vs Alias

