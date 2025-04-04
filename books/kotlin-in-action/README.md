## Kotlin in Action 2/e

**Kotlin in Action, Second Edition**

<small><i>2024-03-10 ~ Present</i></small>

<br><img src="../img/kotlin-in-action-second-edition.jpg" alt="Kotlin in Action, Second Edition" width="40%" /><br>

<br>

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