# Appendix B. A Collection of Best Practices for Production Services

<i><small>운영 서비스를 위한 권장 사례 모음</small></i>

분별있게 실패하기 설정 입력값에 대해서는 확인 및 검증을 반드시 수행하고,
의심쩍은 입력에 대해서는, 이전의 상태를 계속 유지하는 동시에, 잘못된 입력에 대한 알림을 수행

### Fail Sanely

**✔️ 부정확한 데이터**

가능한 문법과 의미를 모두 검증하는 것. 

빈 데이터, 일부만 제공되는 데이터 혹은 잘려진 데이터도 확인해야 함 (예를 들어 설정 값이 이전 버전보다 N% 이상 작은 경우 등).

**✔️ 지연된 데이터**

이런 데이터는 타임아웃으로 인해 현재 데이터를 무효화시킬 수 있음. 

이 데이터가 현재 데이터를 덮어쓰기 전에 제대로 알림을 발송하는 것

이전 기능을 유지하는 방향 유지하는 것. 

새로운 데이터가 잘못된 것을 발견했을 때는 이전 설정값으로 시스템이 계속 동작하도록 하는 것이 안전

<br>

### 점진적 배포

급하지 않은 배포는 반드시 단계적으로 진행하는 것

- 서비스나 배포의 규모
- 위험 분석을 통해 배포에 영향을 받을 운영 환경의 수용량의 비율 확인
- 각 단계 사이의 적절한 시간을 배분하는 것

매일의 트래픽 주기와 지리적 트래픽이 혼합되었을 때의 차이점과 관련된 문제 확인

→ 각 단계를 지리적으로 다른 위치에서 진행하는 것도 좋은 것

**배포는 반드시 관리·감독 하에 실행하는 것**

배포를 수행하는 동안 예상치 못한 상황이 발생하지 않도록 하기 위해, 배포를 수행하는 엔지니어나 신뢰할 수 있는 모니터링 시스템이 모두 모니터링해야 함

만일 예상치 못한 동작이 발견되면, MTTR (Mean Time to Recovery)을 최소화하기 위해 우선 롤백을 수행한 후, 그다음에 분석을 수행하는 것

<br>

## 사용자 입장에서의 SLO 정의

**SLO?** [Chapter 4 - slo 참고](https://github.com/gngsn/deepdive/tree/main/books/sre/chapter04#2-slo-%EB%AA%A9%ED%91%9C)

### 에러 예산

에러 예산([Chapter 3 - Motivation for Error Budgets](https://github.com/gngsn/deepdive/blob/main/books/sre/chapter03/README.md#motivation-for-error-budgets--%EC%97%90%EB%9F%AC-%EC%98%88%EC%82%B0-%ED%99%9C%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0)) 을 이용해 신뢰성과 혁신의 속도 사이의 균형을 맞추는 것. 

이를 통해 일정 기간 동안 허용 가능한 서비스의 장애 수준을 정의할 수 있음. (구글은 주로 월 단위로 정의) 

**계산법**

이 예산은 간단히 1에서 서비스의 SLO를 빼면 구할 수 있음. 

즉, 서비스가 99.99%의 신뢰성을 목표로 하는 경우에는, 서비스가 사용 불가능 상태로 남아 있을 수 있는 예산이 0.01%인 것. 


한 달 동안 서비스의 에러율에 다운타임을 더한 값이 이 에러 예산을 초과하지 않는다면 개발팀은 (합당한 이유를 바탕으로) 얼마든지 새로운 기능, 업데이트 등을 배포할 수 있음. 

만일 에러 예산이 소진되면 서비스가 추가 예산을 마련하거나 아니면 한 달이 지나 에러 예산이 복구되기 전까지는 어떠한 변경도 추가할 수 없음.

(다만, 에러율을 높일 수 있는 긴급한 보안 및 버그의 수정은 여기에 해당되지 않음).

SLO 목표가 99.99% 이상인 성숙한 서비스의 경우에는 허용 가능한 다운타임이 훨씬 적으므로 월별 예산보다는 분기별 예산 관리가 더 적절함.

에러 예산은 배포의 위험을 평가하기 위한 데이터 중심의 메커니즘을 제공함으로써 SRE와 제품개발팀 사이에 발생할 수 있는 구조적 긴장을 완화함. 

게다가 SRE와 제품개발팀 모두에게 더 빠른 혁신과 '예산을 하루아침에 날려버리는' 일 없이 제품을 배포할 수 있는 선례와 기술을 개발하는 공통의 목표를 제시하는 것

<br>

### 모니터링

모니터링은 다음의 세 가지 출력 결과만을 갖는 것

✔️ **호출**: 반드시 지금 누군가가 개입해야 하는 경우

✔️ **티켓**: 누군가 며칠 내로 대처를 해야 하는 경우

✔️ **로깅(logging)**: 이 결과를 지금 당장 누군가 살펴야 하는 것은 아니지만 나중에 분석을 위해 필요한 경우

<br>

### Postmortems

포스트모텀([Chapter 15](https://github.com/gngsn/deepdive/blob/main/books/sre/chapter15/README.md#chapter-15-postmortem-culture-learning-from-failure) 참고)은 누군가를 비난하기 위한 것이 아니며, 사람이 아닌 절차와 기술에 집중하는 것. 

장애 대응에 참여했던 사람들이 매우 똑똑하고, 악의가 없었으며 당시 상황에서 주어진 정보를 토대로 최선의 선택을 했다고 가정하는 것

사람을 '고칠' 수는 없으니, 그들의 환경을 고치는 것

<br>

### Capacity Planning

사용자에게 피해가 가지 않으면서도 계획한 장애와 계획하지 않은 장애를 동시에 처리할 수 있도록 준비하는 것

- 수요 예측이 지속적으로 꾸준히 일치할 때까지 계속해서 검증
- 어림짐작하지 말고 부하 테스트를 수행
- 서비스 개시 첫날의 부하가 계속될 것이라는 보장은 없음

<br>

### 과부하와 장애

검색 SRE들은 웹 검색 클러스터가 계획된 수용량을 넘어서는 상황에 대한 테스트를 진행해서 트래픽이 과도한 상황에서도 적절한 검색을 수행하는지를 확인하는 것

부하가 그보다 더 심각해서 모든 종류의 질의 수행이 어려운 상황이 되면, 잘 동작하는 큐와 동적 타임아웃을 이용해 자연스럽게 부하를 분산하는 것.

재시도는 낮은 에러율을 높은 트래픽으로 증폭시켜서 결과적으로 연속 장애가 발생할 수 있음

<br>

### SRE 팀

많은 서비스들이 초과 운영 업무가 없는 상황에서도 제품 개발자들이 비상 대기 순환 업무와 티켓의 처리에 참여하도록 하고 있음.

이를 통해 제품 개발자들이 서비스의 운영적 측면을 이해할 수 있도록 할 수 있을 뿐만 아니라 잡다한 운영 업무를 최소화할 수 있는 방향으로 시스템을 디자인할 수 있음.

SRE와 개발팀 간의 정기적인 제품 회의(제31장 참고) 역시 상당히 유용함

가상의 장애를 처리하는 연습을 정기적으로 수행하고(468페이지의 "장애 상황을 가정한 역할 수행"), 

이를 수행하는 동안 장애 처리 문서 역시 개선할 수 있도록 하는 것