# CHAPTER 23. Managing Critical State: Distributed Consensus for Reliability

신뢰할 수 있는 고가용성 시스템을 효과적으로 구축하기 위해 분산에 대한 합의가 필요

분산 시스템의 여러 프로세스들은 중요한 설정을 일관되게 확인할 수 있어야 함

<br><img src="./img/figure23-1.png" width="80%" /><br>

분산 환경에서 합의 시스템은 충분히 테스트되고 증명된 시스템을 권장

<br/>

**저장소**
- 시스템, 소프트웨어 저장소: ACID 기반
  - Atomicity, Consistency, Isolation, Durability
  - 원자성, 영속성, 격리성, 내구성
- 분산 데이터 저장소: BASE 기반
  - Basically Available, Soft state, Eventual consistency
  - 기본적인 가용성, 유연한 상태, 궁극적 일관성
  - 보통 멀티마스터 복제(multimaster replication) 지원: 여러 프로세스에서 쓰기 작업을 동시에 수행할 수 있음

<br/>

## Motivating the Use of Consensus: Distributed Systems Coordination Failure

<small><i>합의는 왜 필요할까: 분산 시스템 간 협업의 실패</i></small>

### Case Study 1: The Split-Brain Problem

<small><i>사례 연구 1: 스플릿 브레인 문제</i></small>

- 살펴볼 서비스: 여러 사용자들이 협업할 수 있는 콘텐츠 저장소
- 신뢰성을 위해, **서로 다른 랙에 배치된 두 개의 복제된 파일 서버** 사용
- 한 쌍의 파일 서버 중 하나는 **리더**(leader), 다른 하나는 **수행 서버**(follower) 역할 수행
- 장애 발생 시 **STONITH**를 통해 리더십 확보

<pre><b>STONITH</b>
Shoot The Other Node in the Head.
상대 서버를 강제로 종료.
</pre>

#### 💥 **문제점**
- 네트워크 지연이나 패킷 손실 발생 시, 서버들이 서로를 인식하지 못하고 동시에 **STONITH** 명령을 실행할 가능성 존재
- 일부 명령이 네트워크 문제로 전달되지 않아, 두 서버가 동시에 활성 상태이거나 둘 다 종료될 수 있음
  - 데이터 손상(동시 쓰기) 또는 가용성 문제(둘 다 종료됨) 발생하게 됨
- 단순한 타임아웃을 통한 리더 선출 방식은 본질적으로 분산 비동기 합의 문제를 해결할 수 없음

<br/>

### Case Study 2: Failover Requires Human Intervention

<small><i>사례 연구 2: 인간 개입이 필요한 장애 조치</i></small>

- 다중 샤드 데이터베이스 시스템에서 각 샤드의 주(primary) 노드는 다른 데이터센터의 보조(secondary) 노드로 동기 복제 수행
- 외부 시스템이 기본 노드 상태를 점검하고, 문제가 발생하면 보조 노드를 승격
- 기본 노드가 보조 노드 상태를 확인할 수 없을 경우, 스스로 비활성화하고 인간 개입 요청

#### 💥 **문제점**
- 데이터 손실 위험은 없으나, **가용성이 안좋음**
  - 시스템 운영 엔지니어의 업무 증가
  - 확장성 부족
- 네트워크의 심각한 문제로 리더를 선출하지 못하면, 인간 개입이 더 나은 것도 아님

<br/>

### Case Study 3: Faulty Group-Membership Algorithms

<small><i>사례 연구 3: 잘못된 그룹-멤버십 알고리즘</i></small>

- 인덱싱을 수행하고 검색 서비스를 제공하는 시스템 운영
- 노드들은 시작 시 **가십 프로토콜(gossip protocol)** 을 사용해 서로를 인식하고 클러스터에 가입
- 클러스터 내에서 리더를 선출하고 조정 역할 수행

#### 💥 **문제점**
- 네트워크 문제 시, 클러스터가 분리되고, **스플릿 브레인 상태**가 발생하여 데이터 손상 초래
- 그룹 멤버십을 일관되게 유지하는 문제 역시 분산 합의 문제의 한 형태

분산에 대한 합의 알고리즘은 정확성이 입증됨, 그 구현의 **포괄적으로 테스트된 분산에 대한 합의 알고리즘을 통해서만** 해결될 수 있음

<br/>

## How Distributed Consensus Works

<small><i>분산에 대한 합의가 동작하는 방식</i></small>

분산 합의 알고리즘 방식 
- **충돌-실패** (crash-fail): 충돌이 발생한 노드를 시스템에서 영구 제외 
- **충돌-복구** (crash-recover)


### Paxos Overview: An Example Protocol

## System Architecture Patterns for Distributed Consensus
### Reliable Replicated State Machines
### Reliable Replicated Datastores and Configuration Stores
### Highly Available Processing Using Leader Election
### Distributed Coordination and Locking Services
### Reliable Distributed Queuing and Messaging

## Distributed Consensus Performance
### Multi-Paxos: Detailed Message Flow
### Scaling Read-Heavy Workloads
### Quorum Leases
### Distributed Consensus Performance and Network Latency
### Reasoning About Performance: Fast Paxos
### Stable Leaders
### Batching
### Disk Access

## Deploying Distributed Consensus-Based Systems
### Number of Replicas
### Location of Replicas
### Capacity and Load Balancing

## Monitoring Distributed Consensus Systems
## Conclusion









<br />
