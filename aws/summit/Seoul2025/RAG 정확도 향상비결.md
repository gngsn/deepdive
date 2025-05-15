
# RAG 정확도 향상 비결: Amazon Bedrock Knowledge Base 완전 정복

RAG: Retrieval Augmented Generation

검색 (Retrieval):
증강 (Augmented): 검색된 정보를 사용자 프롬프트에 추가
생성 (Generation): 모델 응답

### Amazon Bedrock Knowledge Base
- 네이티브 기능을 활요해서 확장 가능한 RAG 애플리케이션 구축

#### 단계
질문 -> Amazon Bedrock Knowledge Base -> 프롬프트 증강 생성 -> LLM (NOVA, Anthropic Claude, Meta Lama, ...) -> 답변


#### 확장 가능한 RAG 앱을 위한 방법론
1. 생성형 AI를 위한 데이터 전략
2. RAG 인프라 (모델 및 벡터 스토어 선택)
3. PoC 구축 (or MVP, 작고 단순하게 시작)
4. 평가 (워크플로우 최적화)
5. 배포 및 모니터링 (가드레일 및 보안)

데이터에서 실질적 가치를 창출하는 시간에 격차가 큼
-> **지능형 연결**

이전에 발견되지 않은 기획 발굴

| 챌린지 | 솔루션 |
| --- | --- |

1. 청킹 최적화 -> 데이터 기반 청킹
2. 데이터 일관성 -> 벡터 데이터 동기화
3. 부하 관리 -> 자동 부하 제어
4. 운영 지속성 -> 안정적 시스템 운영
5. 검색 품질 _. 고급 검색 기능

#### 인스턴트 애널리스트
키워드: 인스턴트 애널리스트

<img src="img1" />

#### 데이터 수집 파이프라인 아키텍처

<img src="img2" />

#### RAG 아키텍처

<img src="img3" />


#### Advanced Rag

Pre-Retrieval
요청 검증 > 쿼리 파싱 > 쿼리 재작성 > 쿼리 확장 -> Retrieval > Post-Pretrival (리랭크, 시간 관련성 vs )


---

#### 도전 과제들

- 정확도 / 데이터 관리/ 비용/ 기술 변동성/ 시장출시속도 / 실현 가능성
  - Accuracy  / data management / cost / mutabiliy

### Rag 정확도 향상을 위한 다양한 데이터 활용

- 비정형 데이터 / 정형 데이터 / 그래프 데이터
- -> 다양한 형태의 데이터를 활용할 수 있어야 함


#### 비정형 데이터

1. 데이터 수집: 다중 데이터 소스, 증분 데이터 업데이트, 메타 데이터, 사용자 지정 커넥터
2. 텍스트 추출
3. 데이터 청킹: 기본 청킹 / 고정 크기 청킹/ 계층척 청킹 / 시멘틱 청킹 / 사용자 지정 청킹
4. 임베딩 (숫자 표현): 임베딩 모델
   - AWS 모델
   - cohere 모델: 언어 특화
   - 바이너리 임베딩: 정확도는 다소 떨어지지만 아주 저렴하게 사용할 수 있는 새모델. 2진수로 변환해서 사용
5. 벡터 스토어

#### 정형 데이터

- 정형 데이터 검색: **NL2SQL** (Natural Language to SQL)

##### NL2SQL


벡터 검색 vs 그래프 검색

- 벡터 검색: 데이터 간 근접성을 분석해서 유사성을 판단
  - Example. 텍스트 임베딩 모델 / 이미지 임베딩 모델
- 그래프 검색: 데이터 간 실제 연결을 분석해서 관계를 판단

=> AWS GraphRag
Neptune Analytics 서비스로 사용 가능



#### 정형 데이터
#### 그래프 데이터

