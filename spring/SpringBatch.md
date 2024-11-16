[우테코 강의](https://www.youtube.com/watch?v=1xJU8HfBREY)를 듣고 정리한 내용입니다.

<br>

## 배치 애플리케이션 

batch : 집단, 무리, 함께 묶다.

-> 개발자가 정의한 작업을 한번에 일괄 처리하는 애플리케이션

<br>

**배치를 적용한 애플리케이션**

- 매출데이터를 이용한 일매출 집계

- 매우 큰 데이터를 활용한 보험급여 결정

- 트랜잭션 방식으로 포맷, 유효성 확인 및 처리가 필요한 내부 및 외부 시스템에서 수신한 정보를 기록 시스템으로 통합 등

<br>

**예시: 대용량의 기반 데이터를 활용한 크롤링**

-> 사용자의 접속이 거의 없는 시간대에 주기적으로 시도

<br>

**예시: 메일 구독 서비스**

- 정해진 시간에 구독을 신청한 회원에게 일괄 전송 (e.g. 미디엄의 뉴스레터 구독)

- 다른 정보를 열람하는 등 다른 서비스에 영향을 주지 않음

<br>

### 필요한 상황

- 일정 주기로 실행해야할 때

- 실시간 처리가 어려운 대량의 데이터를 처리해야 할 때

-> 이런 작업을 하나의 애플리케이션에서 수행하면 성능 저하를 유발할 수 있으니 배치 애플리케이션을 구현한다

<br>

### 배치 애플리케이션의 조건

대용량 데이터 - 배치 어플리케이션은 대량의 데이터를 가져오거나, 전달하거나, 계산하는 등의 처리를 할 수 ​​있어야 합니다.

자동화 - 배치 어플리케이션은 심각한 문제 해결을 제외하고는 사용자 개입 없이 실행되어야 합니다.

견고성 - 배치 어플리케이션은 잘못된 데이터를 충돌/중단 없이 처리할 수 있어야 합니다.

신뢰성 - 배치 어플리케이션은 무엇이 잘못되었는지를 추적할 수 있어야 합니다. (로깅, 알림)

성능 - 배치 어플리케이션은 지정한 시간 안에 처리를 완료하거나 동시에 실행되는 다른 어플리케이션을 방해하지 않도록 수행되어야합니다.

<br>

**스케쥴링** : 매 시간/지정한 시간에 지정한 동작을 수행하는 것.

> Spring Batch is not a scheduling framework!

<br>

배치 애플리케이션의 절대적인 목적은 대용량 데이터 처리이다.

배치 프레임워크에서 스케쥴링 기능을 제공하지 않는다.

스케쥴링 프레임워크는 배치를 도와주는 보완제 역할

<br>

### Job 

- Job 이름을 정의

- Step을 정의하고 순서를 정의

- Job의 재사용 가능성을 정의

<br>

### JobInstance

- 논리적으로 Job을 실행

- JobParameters를 이용하여 구분

- JobInstance = Job * identifying JobParameters

<br>

### JobExecution

- Job을 실행하는 단일 시도

- 실패했던 JobInstance에 대해 새로운 실행을 하면 새로운 JobExecution이 생성

<br>

### JobExecution Properties

**BatchStatus**: 실행 상태를 나타낸다. 실행중이면 started, 실패하면 failed, 성공하면 completed

**ExitStatus**: 실행 결과를 나타낸다. ExitCode를 포함한다.

<br>

### Step

- 배치 작업의 독립적이고 순차적인 단계를 캡슐화하는 도메인 객체

- 모든 Job은 하나 또는 그 이상의 Step으로 구성된다.

- Step의 내용은 개발자의 재량이므로 복잡하거나 단순하게 구현 가능하다.

<br>

### StepExecution

**Status**: 실행 상태를 나타낸다.

**ExitStatus**: 실행의 결과를 나타낸다

ReadCount, WriteCount, CommitCount, RollbackCount, FilterCount 등 실행에 대한 다양한 정보를 담고 있다.

<br>

**JobRepository**: Job, Step 구현을 위한 CRUD 작업을 제공 (Job Execution, Step Execution이 갖는 데이터들을 DB에 저장하고 불러오는 작업)

**JobLauncher**: Job을 시작하기 위한 간단한 인터페이스. 구현 시 JobRepository에서 유효한 JobExecution을 획득하고 Job을 실행한다. -> Spring Batch 사용시 제공한다

<br>

**Item**: 작업에 사용하는 데이터

**ItemReader**: Step에서 한 항목씩 검색. 모든 항목이 소진된 경우 null을 반환

**ItemWriter**: 여러 출력 항목을 나타낸다. DB에 저장 과 같은 작업을 정의

**ItemProcessor**: 비즈니스 처리를 담당. 항목이 유효하지 않다고 판단되는 경우 null을 반환

<br>

## Chunk-oriented Processing

https://docs.spring.io/spring-batch/docs/current/reference/html/step.html

**chunk**: 각 커밋 사이에 처리될 row(item)의 수

Chunk-oriented Processing은 성공 시 chunk만큼 커밋하고 실패 시 chunk 만큼 롤백

<br>

```
List items = new Arraylist();
for(int i = 0; i < commitInterval; i++){
    Object item = itemReader.read();
    Object processedItem = itemProcessor.process(item);
    items.add(processedItem);
}

itemWriter.write(items);
```

<br>

### ItemReader

Cursor와 Paging 기능을 제공

**Cursor**: DB와 커넥션을 계속 유지한채 하나씩 가져옴

**Paging**: 한 번 커넥션을 할 때 정의한 페이지 사이즈 만큼의 아이템을 한 번에 가져오는 역할

<br>

페이징을 사용한 ItemReader를 자주 사용할텐데

1. 메모리의 page 사이즈만큼 데이터를 가져온다 (chunk size = page size 권장)

2. chunk를 하나의 트랜잭션에서 처리 -> JPA의 영속성 컨텍스트 사용 가능

<br>

chuck size > page size일 때의 문제점: 하나의 트랜잭션 처리를 위해 여러번의 조회를 해야 한다.

```
Setting a fairly large page size and using a commit interval that matches the page size should provide better performance.
```


chunk 단위 만큼 한번에 작업 수행. chunk 단위만큼의 List를 다룬다

<br>

### ItemProcessor(Optional)

Chunk-oriented Tasklet을 구성할 때 선택 요소이다

데이터를 가공/필터링하는 역할을 한다 -> writer에서도 구현 가능한 역할 -> 비즈니스 코드가 섞이는 것을 방지 (유지보수 용이)

Step에 여러 로직이 필요할 때 도입을 고려해 유지보수성을 증가시킨다

데이터 처리에 실패했을 때 null을 반환해 writer에 전달되지 않는다

<br>

## 배치 애플리케이션 운영

- 테스트 코드를 반드시 작성

    -> QA를 하기 어렵기 대문에 테스트 코드가 필요

    복잡한 쿼리를 실행한 결과를 처리하고 다시 데이터베이스에 저장하는 작업이기 때문에 통합 테스트를 실행한다

    보통 단위 테스트를 이용해서 내부 작업을 검사하고 전체 테스트 코드를 반드시 작성한다

<br>

### 관리 도구

- Cron: 리눅스 작업 스케줄러

- Spring MVC + API Call: 권장 X

- Spring Batch Admin: Deprecated

- Quartz + Admin: 스케줄러 프레임워크 + 관리자 페이지 구현

- CI Tool (Jenkins) -> 많이 운영하는 편


<br>

### 관리도구로 Jenkins를 사용할 때 장점

- Integration (Slack, Email 등과의 연동 가능)

- 실행 이력/ 로그 관리

- 다양한 실행 방법(Rest API/스케줄링/수동 실행)

- 파이프라인

- 공통 설정 관리하기 쉬움 

    ```
    java -jar -XX: +UseG1GC -DSpring.profiles.active=dev Application.jar -- job.name=job이름 jobParameter1=jobParameterValue1 jobParameter2=jobParameterValue2
    ```
    global properties를 통해 위의 공통 설정을 쉽게 주입할 수 있다.


- 파이프라인으로 Job을 관리해 유지보수에 용이

    ```
    +---------- Jenkins Pipeline -----------+
    |                                       |
    |  +- Job -+    +- Job -+    +- Job -+  |
    |  |       |--->|       |--->|       |  |
    |  +-------+    +-------+    +-------+  |
    +---------------------------------------+
    ```

    Job 내부에 Step을 여러개 설계하는 것보다 권장된다

    -> Job을 단독으로 실행할 수 있도록 설계하는 것이 유지보수에 더 좋다



### Chunk 최적화

- 구체적인 가이드는 없음

- 설계한 비즈니스 로직에 대해서 가장 효율적인 단위를 설정해야 한다.

- 여러개의 배치 작업을 구성ㅎ하는 경우 다른 배치 작업에 영향을 주면 안된다.

    -> 스프링 배치에서는 chunk 사이즈 만큼 메모리에 데이터를 적재해야하기 때문에 다른 배치에서 사용할 수 있는 메모리가 줄어들 수 있다.









