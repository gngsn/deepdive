# CHAPTER 02. The Production Environment at Google, from the Viewpoint of an SRE

<i>SRE 관점에서 바라본 구글의 프로덕션 환경</i>

## 01. Hardware

구글의 대부분의 데이터센터는 동일한 유형의 하드웨어를 사용하며, 클러스터 운영 시스템인 Borg가 자원을 할당

**구글의 데이터 센터 토폴로지(Topology) 구조의 특징**

- 여러 대의 머신이 하나의 랙(rack)에 장착됨
- 여러 랙이 클러스터를 구성함
- 여러 클러스터가 모여 하나의 데이터센터 건물을 이룸
- 인근의 여러 데이터센터 건물이 모여 캠퍼스를 형성함
- 구글은 수백 대의 자체 제작 스위치를 연결하여 "주피터(Jupiter)" 라는 네트워크 구축

<br/>

## 02. System Software That "Organizes" the Hardware

우리가 보유한 하드웨어는 반드시 엄청난 규모의 하드웨어를 조정할 수 있는 소프트웨어에 의해 제어 및 관리 되어야 한다.
우리는 하드웨어 결함도 소프트웨어로 관리할 수 있다는 것을 알아냈다.
클러스터에 많은 수의 하드웨어 컴포넌트가 내장된 경우, 하드웨어 상의 문제는 매우 빈번하게 나타난다.
한해 동안 대략 한 클러스터에서 수천 건의 머신 장애와 수천 건의 하드디스크 결함이 발생

하드웨어는 소프트웨어에 의해 제어 및 관리되며, 이를 통해 하드웨어 결함도 관리할 수 있음

Borg는 분산 클러스터 운영 시스템으로, 쿠버네티스(Kubernetes)의 전신

### Storage

- 구글은 러스터(Lustre)와 하둡 분산 파일 시스템(Hadoop Distributed File System; HDFS)과 같은 오픈소스 기반의 파일 시스템을 사용
- 이 위에 Colossus - 구글파일시스템(Google File System, GFS) 후속 제품 - 라는 계층을 추가해 복제와 암호화를 지원함
- 데이터베이스 시스템으로는 Bigtable과 Spanner, Blobstore 등이 있음
- 네트워크는 오픈플로우(OpenFlow) 기반의 소프트웨어 정의 네트워크(SDN)를 사용하며, GSLB(Global Software Load Balancer)를 통해 여러 수준의 로드 밸런싱을 수행함

<br>

**콜로서스를 기반으로 다양한 데이터베이스와 유사한 서비스들이 존재**:

**빅테이블 (Bigtable)**:
 
- 페타바이트 규모의 데이터를 처리할 수 있는 NoSQL 데이터베이스 시스템
- 로우키(Row Key), 칼럼키(Column Key), 타임스탬프(Timestamp) 등을 통해 데이터를 인덱싱할 수 있으며, 데이터는 다차원 정렬 맵(Multidimensional Sorted Map) 형태로 분산 저장됨
- 빅테이블에 저장된 값은 해석되지 않은 바이트 배열로, 결과적 일관성(Eventual Consistency)과 데이터센터 간 복제를 지원

**스패너 (Spanner)**:

- 실시간 일관성을 제공하는 글로벌 데이터베이스 시스템으로, 전 세계의 사용자에게 SQL과 유사한 인터페이스를 제공

**블롭스토어 (Blobstore)**:

- 다른 데이터베이스 시스템으로, 특정 사용 사례에 따라 다양한 옵션을 제공 (26장 참조)


### Networking

- **오픈플로(OpenFlow) 기반 소프트웨어 네트워크:**
    - 구글은 스마트한 라우팅 하드웨어 대신, 비교적 저렴하고 기능이 간단한 스위칭 컴포넌트와 이중화된 중앙 컨트롤러를 조합하여 사용
    - 네트워크에서 가장 적합한 경로를 사전에 계산해두고, 복잡한 경로 계산을 라우터 대신 간단한 스위칭 하드웨어에서 수행

- **글로벌 소프트웨어 로드 밸런서(GSLB):**
    - GSLB는 세 가지 수준에서 로드 밸런싱을 수행
        1. **DNS 요청에 대한 지역적 로드 밸런싱:** 예를 들어, `www.google.com`에 대한 요청을 처리
        2. **사용자 서비스 수준의 로드 밸런싱:** 유튜브나 구글 지도와 같은 서비스에 대해 로드 밸런싱을 수행
        3. **원격 프로시저 호출(Remote Procedure Call, RPC) 수준의 로드 밸런싱:** 이 로드 밸런싱은 RPC 호출에 대해 이루어짐


## 03. Show More Items

### Lock Service

**Chubby**: 파일 시스템과 유사한 API를 제공하여 잠금을 관리하며, Paxos 프로토콜을 통해 비동기 합의 (asynchronous consensus)를 통해 마스터 노드를 결정하는데 중요한 역할을 담당 

처비에 저장될 데이터는 일관성을 확보해야하는 데이터가 적합: 때문에 BNS는 처비를 이용해 BNS경로와 IP 주소 및 포트의 매핑 정보를 저장

### Monitoring and Alerting

**Borgmon**: 모니터링 시스템을 사용해 다양한 지표를 수집하고, 이를 실시간 알림과 향후 분석에 활용

• 치명적인 문제점에 대한 알림 설정
• 행동 비교: 소프트웨어 업데이트 이후 서버가 빨라졌는가?
• 수용 계획을 위한 가장 기본적인 지표인 시간의 흐름에 따른 자원 소비 행위의 개선 여부 확인

## 04. Our Software Infrastructure

**스튜비(Stubby)**: 원격 프로시저 호출(RPC) 인프라를 사용하여 통신

- 스튜비의 오픈소스버전인 gRPC도 사용할 수 있음
- gRPC 호출은 종종 로컬 프로그램이 수행 해야 하는 서브루틴을 호출할 때도 생성
- 데이터 전송은 프로토콜 버퍼(Protocol Buffers)를 통해 이루어짐 (`protobufs` 라고도 부름)

## 05. Our Development Environment

개발속도(development velocity)

- 외부에서 발생한 문제를 해결 후 변경 사항을 소유자에게 보내 검토를 요청한 후 코드를 제출
- 모든 소스 코드 변경은 반드시 리뷰를 거쳐야 함

<br/><br/>