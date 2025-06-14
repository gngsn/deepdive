## Kotlin in Action 2/e

**Kotlin in Action, Second Edition**

<small><i>2024-03-10 ~ Present</i></small>

<br><img src="../img/kotlin-in-action-second-edition.jpg" alt="Kotlin in Action, Second Edition" width="40%" /><br>

<br>

### Part 1. Introducing Kotlin

<details>
<summary><b>CHAPTER 05. Programming with lambdas</b></summary>

<br>

<a href="./chapter05"> 🔗 link </a>
<br>

**TL;DR**

- **Lambda**: 다른 함수에 넘길 수 있는 작은 코드 조각
- **코틀린의 람다 문법**
  - 함수 인자로 전달할 경우, 괄호 밖으로 람다 표현 가능 → 코드 간결화
  - 인자가 하나일 경우, `it` 사용 가능 → 짧고 간단한 코드 작성 가능
- **람다와 외부 변수 캡처**
  - 외부 변수 캡처 가능
  - 자바와 달리, 바깥 함수의 변수를 읽거나 수정 가능
- **함수 참조**
  - `::메서드이름`, `::생성자이름`, `::프로퍼티이름` 사용 → 참조 생성 가능
  - 참조를 함수 인자로 전달 가능
- **컬렉션 함수 (`filter`, `map`, `all`, `any`)** 내에서 직접 원소 이터레이션 없이 컬렉션 연산 수행 가능
- SAM 인터페이스 구현 시, SAM 인터페이스 객체 생성 없이 람다를 전달해서 구현 가능
- **수신 객체 지정 람다**: 수신 객체의 메서드 직접 호출 가능
- 기존 코드와 다른 컨텍스트에서 동작 → 코드 구조화할 때 유용
- **표준 라이브러리 함수 활용**
  - **`with`** : 객체 참조 반복 없이 메서드 호출 가능
  - **`apply`** : 빌더 스타일 API로 객체 생성 및 초기화 가능
  - **`also`** : 객체에 대한 추가 작업 수행 가능

<br>
</details>

<details>
<summary><b>CHAPTER 06. Working with collections and sequences</b></summary>

<br>

<a href="./chapter06"> 🔗 link </a>
<br>

**TL;DR**

- **표준 라이브러리 함수**와 **람다**를 활용해 컬렉션을 효율적으로 처리할 수 있음
  - `filter`: Boolean 값이 결과인 함수로 컬렉션의 원소를 걸러내고 싶을 때 사용
    - `filterIndexed`: `filter`와 인덱스를 함께 필요할 때 사용
  - `map`: 입력 컬렉션의 원소를 입력한 람다 함수로 처리한 값으로 변환
    - `mapIndexed`: `map`와 인덱스를 함께 필요할 때 사용
  - `reduce`: 람다(누적기, accumulator)는 각 원소에 별로 호출되며 새로운 누적 값을 반환
    - `runningReduce`: `reduce` 연산의 모든 중간 누적 값을 포함해서 반환
  - `fold`: 람다에 컬렉션의 각 값과 이전 누적기를 적용하면서 누적기로 점차 결과를 만들어나감
    - `runningFold`: `fold` 연산의 모든 중간 누적 값을 포함해서 반환
  - `all`: 컬렉션의 모든 원소가 특정 조건을 만족하는지 판단
  - `any`: 컬렉션의 원소가 하나라도 있는지 판단 (= `!all`)
  - `none`: 컬렉션의 조건을 만족하는 원소가 전혀 없는지 판단 (= `!any`)
  - `count`: 조건을 만족하는 원소의 개수를 반환
  - `find`: 조건을 만족하는 첫 번째 원소를 반환
  - `partition`: 술어를 만족하는 그룹과 그렇지 않은 그룹으로 나눌 때 사용 (= `filter` + `filterNot`)
  - `groupBy`: 컬렉션의 원소를 어떤 특성에 따라 여러 그룹으로 나눌 때 사용
  - `associate`: **컬렉션으로부터 맵을 만들어내고 싶을 때** 사용
    - `associateWith`: **컬렉션 원소**를 **키**로 사용하고, **맵의 값**을 **생성하는 람다** 입력
    - `associateBy`: **컬렉션 원소**를 **맵의 값**으로 하고, **입력한 람다가 만들어내는 값**을 **맵의 키**로 사용
  - `replaceAll`: `MutableList` 에 적용하면 지정한 람다의 결과로 컬렉션의 모든 원소를 변경
  - `fill`: 가변 리스트의 모든 원소를 똑같은 값으로 바꾸는 특별한 경우에는 함수를 쓸 수 있음
  - `ifEmpty`: **컬렉션이 비어있을 때 기본값을 생성하는 람다를 제공**할 수 있음
    - `ifBlank`: **문자열**에서 **'공백(`" "`)'과 '비어있음(`""`)'일 때, 기본값을 지정**
  - `windowed`: 데이터를 연속적인 시간의 값들로 처리하고 싶을 경우, 슬라이딩 윈도우를 생성
  - `chunked`: 컬렉션을 주어진 크기의 서로 겹치지 않는 (서로소) 부분으로 나누고 싶을 때 사용
  - `zip`: 각 리스트의 값들이 서로의 인덱스에 따라 대응되는 경우, 두 컬렉션에서 같은 인덱스에 있는 원소들의 쌍으로 이뤄진 리스트 생성
  - `flatMap`: 컬렉션의 각 원소를 파라미터로 주어진 함수를 사용해 매핑 한 후, 변환한 결과를 하나의 리스트로 펼침
  - `flatten`: 변환할 것이 없고 단지 컬렉션의 컬렉션을 평평한 컬렉션으로 만들 경우 사용
- **시퀀스**를 활용하면 중간 결과 없이 연산을 지연 계산하여 성능을 최적화할 수 있음.
  - `asSequence()`: 컬렉션에 `asSequence()`를 호출해서 시퀀스로 변경
  - `generateSequence`: 주어진 이전의 원소로, 다음 원소를 계산

<br>
</details>

<details>
<summary><b>CHAPTER 07. Working with nullable values</b></summary>

<br>

<a href="./chapter07">🔗 link</a>
<br>

**TL;DR**

- 코틀린은 널이 될 수 있는 타입을 지원해 `NullPointerException` 오류를 컴파일 시점에 감지할 수 있음
- **안전한 호출 (`?.`)**: 널이 될 수 있는 객체의 메서드를 호출하거나 프로퍼티에 접근할 수 있음
- **엘비스 연산자 (`?:`)**: 어떤 식이 null 일 때 대신할 값을 지정할 수도 있고, 실행을 반환시키거나 예외를 던질 수도 있음
- **널 아님 단언 (`!!`)**: 컴파일러에게 주어진 값이 null 이 아니라고 약속하는 것
  - null 값에 대한 책임은 개발자에게 있음
- **`let` 함수**: 자신이 호출된 수신 객체를 람다에게 전달
  - 안전한 호출 연산자와 `let`을 함께 사용하면 널이 될 수 있는 타입의 객체를 널이 될 수 없는 타입으로 변환하는 효과가 있음
- **`as?` 연산자**: 값을 다른 타입으로 변환하는 것과 변환이 불가능한 경우를 처리하는 것을 한꺼번에 편리하게 처리할 수 있음

<br>
</details>

<details>
<summary><b>CHAPTER 08. Basic types, collections, and arrays</b></summary>

<br>

<a href="./chapter08">🔗 link</a>
<br>

**TL;DR**

- 기본적인 수를 표현하는 타입은 일반 클래스같지만 보통 자바의 원시 타입으로 컴파일됨
  - e.g. Kotlin `Int` → Java `int`
  - **코틀린의 부호 없는 수 클래스**
    - JVM 에는 상응하는 타입이 없음
    - 인라인 클래스를 통해 변환되며 원시 타입과 마찬가지 성능을 냄
- 널이 될 수 있는 원시 타입은 자바의 박싱된 원시 타입에 대응
  - e.g. Kotlin `Int` → Java `java.lang.Integer`
- `Any` 타입: 모든 다른 타입의 상위 타입. 자바 `Object` 타입에 대응.
- `Unit` 타입: 자바 `void`에 대응
- `Nothing` 타입은 함수가 정상적으로 끝나지 않는다는 것을 나타내는 타입
- 자바에서 온 타입은 코틀린에서 플랫폼 타입
- 코틀린 컬렉션은 표준 자바 클래스를 사용하지만, '읽기 전용'과 '변경 가능'한 컬렉션을 구분
- 코틀린에서 자바 클래스를 확장하거나 자바 인터페이스를 구현해야 한다면, 파라미터의 널 가능성과 변경 가능성을 주의 깊게 생각해야 함
- 코틀린에서도 배열을 사용할 수 있음. 하지만 컬렉션 권장
- 코틀린 `Array` 는 일반적 제네릭 클래스처럼 보이지만 **자바 배열**로 컴파일 됨
  - e.g. Kotlin `intArrayOf(0, 0, 0, 0, 0)` → Java `int[]`

<br>
</details>

### Part 2. Embracing Kotlin

<details>
<summary><b>CHAPTER 09. Operator overloading and other conventions</b></summary>

<br>

<a href="./chapter09">🔗 link</a>
<br>

**TL;DR**

- 코틀린은 정해진 컨벤션에 따라 함수를 정의해서 수학 연산을 오버로드할 수 있음
- 비교 연산자 (`=`, `!=`, `>`, `<` 등 ) 를 모든 객체에 사용할 수 있음
  - 비교 연산자는 `equals`와 `compareTo` 메서드 호출로 변환됨
- 자신이 정의한 클래스의 인스턴스에 대해 `[]` 와 `in` 연산을 사용할 수 있음
  - 단, 해당 클래스에 `get`, `set`, `contains` 함수 정의 필수
- 미리 정해진 컨벤션을 따라 범위를 만들거나 컬렉션과 배열의 원소를 이터레이션할 수 있음
  - `rangeTo`, `rangeUntil`
- 구조 분해 선언을 통해 한 객체의 상태를 분해해서 여러 변수에 대입할 수 있음
  - 데이터 클래스에 대해 구조 분해를 사용할 수 있음
  - 혹은, 클래스에 `componentN` 함수를 정의하면 구조 분해를 지원할 수 있음
- **위임 프로퍼티**를 통해 프로퍼티 값을 저장하거나 초기화하거나 읽거나 변경할 때 사용하는 로직을 재활용할 수 있음
- 표준 라이브러리 함수인 `lazy` 를 통해 지연 초기화 프로퍼티를 쉽게 구현할 수 있음
- `Delegates.observable` 함수를 사용하면 프로퍼티 변경을 관찰할 수 있는 옵저버를 쉽게 추가할 수 있음
- 맵을 위임 객체로 사용하는 위임 프로퍼티를 통해 다양한 속성을 제공하는 객체를 유연하게 다룰 수 있음

<br>
</details>

<details>
<summary><b>CHAPTER 10. Higher-order functions:Lambdas as parameters and return values</b></summary>

<br>

<a href="./chapter10">🔗 link</a>
<br>

**TL;DR**

<br>
</details>

<details>
<summary><b>CHAPTER 11. Generics</b></summary>

<br>

<a href="./chapter11">🔗 link</a>
<br>

**TL;DR**

- 코틀린 제네릭스는 자바와 아주 비슷해서, 제네릭 함수와 클래스를 자바와 비슷하게 선언할 수 있음
- **타입 소거** <sup>Type Erasure</sup>: 타입 인자가 실행 시점에 지워짐
  - 제네릭 타입의 타입 인자는 컴파일 시점에만 존재 (자바와 동일)
  - 제네릭 타입을 `is` 연산자로 검사할 수 없음
- 인라인 함수의 타입 파라미터를 `reified`로 표시해서 실체화
  - 실행 시점에 그 타입을 `is`로 검사하거나 `java.lang.Class` 인스턴스를 얻을 수 있음
- 변성은 **베이스 클래스가 같고 타입 파라미터가 다른 두 제네릭 타입 사이**의 상하위 타입 관계를 명시하는 방법
- 제네릭 클래스의 타입 파라미터가 **아웃 위치**에서만 사용되는 경우: 타입 파라미터를 `out` 으로 표시해서 공변성 명시 - 생성자
- 제네릭 클래스의 타입 파라미터가 **인 위치**에서만 사용되는 경우: 타입 파라미터를 `in` 으로 표시해서 반공변성 명시 - 소비자
- 공변성의 반대는 반공변성.
  - 코틀린의 읽기 전용 `List` 인터페이스: **공변적** ← `List<String>`은 `List<Any>`의 하위 타입
  - `Function1<in P, out R>` 함수 인터페이스: **첫 번째 타입 파라미터**에 대해서는 **반공변적**, **두 번째 타입 파라미터**에 대해서는 **공변적**
    - `(Animal) -> Int` 는 `(Cat) -> Number` 의 하위 타입
    - 즉, 함수 타입은 함수 파라미터 타입에 대해서는 반공변적이며 함수 반환 타입에 대해서는 공변적
- 코틀린에서의 제네릭 클래스의 **공변성 정의 지점**:
  - **선언 지점 변성**: 전체적으로 지정
  - **사용 지점 변성**: 구체적인 사용 위치에서 지정
- **스타 프로젝션**: 제네릭 클래스의 타입 인자가 어떤 타입인지 정확히 모르거나 혹은 중요하지 않을 때 사용
- **타입 별명**: 타입에 대해 더 짧은 이름이나 다른 이름을 부여
  - 타입 별명은 컴파일 시점에 원래의 타입으로 치환.

<br>
</details>

<details>
<summary><b>CHAPTER 12. Annotations and reflection</b></summary>

<br>

<a href="./chapter12">🔗 link</a>
<br>

**TL;DR**

- 코틀린에서는 넓은 범위(파일, 식 등)의 타깃에 대해 어노테이션을 붙일 수 있음
- 어노테이션 인자로 기본 타임 값, 문자열, 이넘, 클래스 참조, 다른 어노테이션 클래스의 인스턴스, 배열을 사용할 수 있음
- 어노테이션의 사용 지점 타깃을 명시 가능 (e.g. `@get:JvmName`)
  - 여러 가지 바이트코드 요소를 만들어내는 경우, 정확히 어떤 부분에 어노테이션을 적용할지 지정할 수 있음
- 어노테이션 클래스 정의: `annotation class` 
  - 모든 파라미터를 `val` 프로퍼티로 표시한 주 생성자가 있어야 하고, 본문은 없어야 함
- 메타어노테이션을 사용해 타깃, 어노테이션 유지 모드 등 여러 어노테이션 특성을 지정할 수 있음
- **리플렉션 API**: 실행 시점에 객체의 메서드와 프로퍼티를 동적으로 열거하고 접근할 수 있음. 
  - 리플렉션 API에는 클래스(`KClass`), 함수(`KFunction`) 등 여러 종류의 선언을 표현하는 인터페이스가 있음
- `::class`로 `KClass` 인스턴스 가져오기
  - 클래스는 `ClassName::class`를 사용
  - 객체는 `objName::class`를 사용
- `Function`과 `KProperty` 인터페이스는 모두 `Kcallable` 을 확장
  - `KCallable`은 제네릭 `call` 메서드 제공
  - `KCallable.callBy` 메서드: 메서드 호출 시, 디폴트 파라미터 값을 사용할 수 있음
- `KFunction0`, `KFunction1` 등의 인터페이스는 모두 파라미터 개수가 다른 함수를 표현하며 `invoke` 메서드를 사용해 함수를 호출할 수 있음
- `KProperty`, `KProperty1` 은 수신 객체의 개수가 다른 프로퍼티들을 표현하며 값을 얻기 위한 `get` 메서드를 지원
- `KMutableProperty0` 과 `KMutableProperty1`은 각각 `KProperty0` 과 `KProperty1` 을 확장하며 `set` 메서드를 통해 프로퍼티 값을 변경할 수 있음
- `KType` 의 실행 시점 표현을 얻기 위해 `typeOf<T>()` 함수 사용

<br>
</details>

<details>
<summary><b>CHAPTER 12. Annotations and reflection</b></summary>

<br>

<a href="./chapter12">🔗 link</a>
<br>

**TL;DR**

- **코틀린 내부 DSL**: 여러 메서드 호출 구조를 쉽게 표현할 수 있게 해주는 API 설계 패턴
  - 코틀린 내부 DSL을 사용하면 코드를 추상화하고 재활용할 수 있음
- **람다 수신 객체**<sup>Lambdas with receivers</sup>: 람다 본문 내에서 메소드를 어떻게 실행할지 재정의해서 중첩 구조를 쉽게 구조화함
  - 수신 객체 지정 람다를 파라미터로 받은 경우 그 람다의 타입은 **확장 함수타입**
  - 람다를 파라미터로 받아 사용하는 함수는 **람다를 호출하면서 람다에게 수신 객체를 제공**
- 원시 타입에 대한 확장을 정의하면 상수를 가독성있게 다룰 수 있음 (e.g. 기간)
- **`invoke` 컨벤션**을 사용하면 **임의의 객체를 함수처럼 다룰 수 있음**
- [🔗 Kotest](https://github.com/kotest/kotest), [Exposed](https://github.com/JetBrains/Exposed) 는 각각 단위 테스트, 데이터베이스를 위한 단언문을 지원하는 내부 DSL 제공


<br>
</details>
