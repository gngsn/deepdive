Spring에 DB를 통한 저장(Persistence)을 위해 JPA(Jakarta Persistence API), Hibernate, Spring Data와 같은 기술들을 사용하곤 합니다.

이번 포스팅에서는 각 기술의 개념과 사용에 대해 알아보도록 하겠습니다.

참고로, 이 계층을 Persistence 라고 부르며, 그 의미인 ‘지속되는 저장’을 본 포스팅에서는 단순히 ‘지속성’ 혹은 ‘저장’이라고 지칭하겠습니다.

JPA(Jakarta Persistence API)

- JPA는 이전의 Java Persistence API
- 객체 저장 및 객체/관계 매핑을 관리하는 API를 정의한 스펙
- 객체를 저장하기 위해 수행해야 할 작업을 지정

이해를 위해 단순히 말해보면, 데이터베이스에서 데이터를 관리하기 위해 사용될 기능들을 정의한 인터페이스 모음이라고 생각할 수 있습니다.

“데이터베이스에서 데이터를 관리하려면 이러한 동작(메소드)들을 만들어!”라는 지침이죠.

Hibernate

- JPA 스펙의 가장 인기있는 구현체
- 실제 Persistence 기능을 구현

Spring Data

- Persistence 계층 구현체가 더욱 효율적이게 동작하도록 만듦
- 스프링 프레임워크 원리를 준수하는 아주 간단한 접근 방식 제공

## Hibernate

### ORM

관계형 데이터베이스를 사용하며 객체 지향을 유지하기란 쉬운 일이 아닙니다.

가령, 성능 상의 문제로 비정규화 해둔 테이블을 객체 형태로 만들기 위해

설계 고민을 시작하고, 약간의 변형을 가진 서로 다른 객체들을 만들고, 매퍼를 만들기 시작합니다.

이렇게 객체 지향을 지향하는 시스템에서 데이터베이스를 연결하기 위한 많은 노력들을 하곤 하는데요.

ORM(Object Relational Mapping)은 객체 지향을 유지하며 이 둘을 연결하기 위한 프로그래밍 기법입니다.

Hibernate는 Java의 데이터 관리 문제에 대한 완벽한 해결책을 제공하자는 목표를 가지는 ORM 입니다.

이제는 ORM 서비스뿐만 아니라,

ORM 기능을 넘어 아래의 도구들을 포함하는 **데이터 관리 도구 모음** 볼 수 있습니다:

✔️ Hibernate ORM

: Hibernate Core Project. 네이티브 전용 API

- SQL Database를 지속적으로 사용하기 위한 Hibernate 코어 서비스
- 다른 Hibernate 프로젝트의 기반이 되는 가장 오래된 Hibernate 프로젝트

프레임워크나 특정 런타임 환경에 상관없이 Hibernate ORM 만을 사용할 수도 있습니다.

데이터베이스에 접근할 수 있는 한, Hibernate로 구성하여 사용할 수 있습니다.

✔️ Hibernate EntityManager

: Hibernate의 표준 JPA (Jakarta Persistence API) 구현체

Hibernate ORM 프로젝트에 선택적으로 추가할 수 있는 모듈입니다.

✔️ Hibernate Validator

https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#preface

: Hibernate의 Bean Validation(JSR 303) 규격의 구현체

Hibernate Validator는 다른 Hibernate 프로젝트들과 별개의 프로젝트로,

클래스에 대한 검증을 위한 Validator을 제공합니다.

흔히, Java에서 객체를 만들고 Validator 조건을 건 경우, Hibernate Validator 구현체들을 통해 값을 검증하곤 합니다.

```kotlin
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Car {

    @NotNull
    private String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;

		...
}
```

✔️ Hibernate Envers

: 다양한 버전의 SQL 데이터베이스의 데이터의 감사 기록을 보관 및 기록

Hibernate Envers를 통해 Subversion 또는 Git와 같이 이미 VCS (Version Control Systems) 유사하게,

데이터를 기록, 감사, 추적을 할 수 있도록 추가할 수 있습니다.

✔️ Hibernate Search

Hibernate Search은 Apache Lucene 데이터베이스에서 도메인 모델 데이터의 인덱스를 최신 상태로 유지

강력하고 자연스럽게 통합된 API를 사용하여 이 데이터베이스를 쿼리할 수 있습니다.

많은 프로젝트에서 최대 절전 모드 ORM 외에도 최대 절전 모드 검색 기능을 사용하고 있습니다.

응용 프로그램의 사용자 인터페이스에 무료 텍스트 검색 양식이 있고 행복한 사용자를 원한다면 최대 절전 모드 검색 기능을 사용할 수 있습니다.

이번 포스팅에서 Hibernate Search는 다루지 않지만, ≪*Hibernate Search in Action* by Emmanuel Bernard (Bernard, 2008)≫ 을 추천드립니다.

✔️ Hibernate OGM

: Object/Grid Mapper. NoSQL 사용 시의 JPA 지원

Hibernate 코어 엔진을 재사용하면서, 매핑된 엔티티를 key/value-, document-, graph-oriented data stores에 사용하도록 지원합니다.

✔️ Hibernate Reactive

: Non-blocking Hibernate Drivers를 지원

Hibernate ORM을 위한 reactive API로, Non-blocking 방식으로 데이터베이스와 상호 작용합니다.
