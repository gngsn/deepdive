## Kotlin in Action 2/e

**Kotlin in Action, Second Edition**

<small><i>2024-03-10 ~ Present</i></small>

<br><img src="../img/kotlin-in-action-second-edition.jpg" alt="Unit Testing" width="40%" /><br>

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