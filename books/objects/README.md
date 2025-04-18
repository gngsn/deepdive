## Objects

**Objects - 코드로 이해하는 객체지향 설계, 조영호**

<small><i>2023-04-12 ~ 2023-07-19 (15 weeks)</i></small>

<br><img src="../img/Objects.jpeg" alt="Objects" width="40%" /><br>

<br>

<details>
<summary><b>CHAPTER 01. 객체, 설계</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter1"> 🔗 link </a>
<br>

**TL;DR**

- 소프트웨어 모듈 목적은 '제대로된 실행 동작', '변경 용이성', '코드를 읽는 사람과의 의사소통' 이다.
- 객체는 자신의 데이터를 스스로 처리하는 자율적인 존재여야 한다.
- 객체는 캡슐화를 이용해 의존성을 적절히 관리하여 결합도를 낮추는 것이다.
- 설계는 여러 방법이 될 수 있는, 트레이드오프의 산물이다.
- 훌륭한 객체지향 설계는 모든 객체들이 자율적으로 행동하며, 내일의 변경을 매끄럽게 수용할 수 있는 설계이다.

**QUESTION**

- 이해하기 쉬운 코드를 위해서라면, 아래와 같이 수정하는 게 낫지 않을까?
    1. Theater -> TicketOffice
    2. TicketOffice -> TicketBox (TicketSeller가 TicketBox를 속성으로 포함)
    3. Ticket에 Theater 위치 속성 추가 -> Audience에 moveTo 메소드 추가

- TicketSeller가 TicketOffice를 가지고 있다는 사실이 어색함
- Theater과 TicketOffice는 개별된 공간이라고 했으니, Theater를 TicketOffice라고 바꾸는 게 낫지 않을까?

<br>
</details>
<details>
<summary><b>CHAPTER 02. 객체지향 프로그래밍</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter2"> 🔗 link </a>
<br>

**TL;DR**
- 객체지향 패러다임 특징: 요구사항과 프로그램을 객체를 동일한 관점에서 바라볼 수 있기 때문에 도메인 개념이 프로그램 객체와 클래스로 매끄럽게 연결될 수 있음
- 프로그래머의 역할을 클래스 작성자 (class creator)와 클라이언트 프로그래머 (client programmer)로 구분
  - 클라이언트 프로그래머: 필요한 클래스들을 엮어서 애플리케이션을 빠르고 안정적으로 구축
  - 클래스 작성자: 구현 은닉 - 클라이언트 프로그래머가 내부에게 필요한 부분 만을 공개
- 객체지향 프로그램을 작성할 때는 협력의 관점에서 어떤 객체가 필요한지 결정하고, 객체들의 공통 상태와 행위를 구현하기 위해 클래스를 작성
- 객체가 다른 객체와 상호작용할 수 있는 유일한 방법은 **객체 간 메시지 전송** 뿐
- **유연한 설계**와 **이해하기 쉬운 코드 및 디버깅**은 트레이드 오프 관계: 항상 유연성과 가독성 사이에서 고민해야 함
- 추상화는 요구사항의 정책을 높은 수준에서 서술할 수 있고, 상위 정책을 쉽고 간단하게 표현함으로써 더 유연한 설계를 하게 해줌

**Impression**

- 프로그래머의 역할을 클래스 작성자 (class creator)와 클라이언트 프로그래머 (client programmer)로 구분하라
- BigDecimal: 금액이나 복잡한 숫자 계산에는 BigDecimal을 사용하는 게 분명히 좋음. 가령, 부동 소수점 관련 문제

<br>
</details>

<details>
<summary><b>CHAPTER 03. 역할, 책임, 협력</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter3"> 🔗 link </a>
<br>

**TL;DR**
- 객체지향 패러다임의 관점에서 핵심: 역할(role), 책임(responsibility), 협력(collaboration)
- 메시지 전송: 객체 사이의 협력을 위해 사용할 수 있는 유일한 커뮤니케이션 수단
- 협력 > 행동 > 상태: 협력은 객체 설계의 문맥(context)을 제공하며 행동, 상태를 결정
- CRC 카드 활용: 역할 식별, 책임 할당, 협력을 명시적이고 구체적인 실용적인 설계 기법
- Information Expert (정보 전문가) 패턴: 책임을 수행하는 데 필요한 정보를 가장 잘 알고 있는 전문가에게 그 책임을 할당하는 것
- 역할 / 객체: 객체가 항상 하나의 역할을 수행한다면 둘은 동일한 것, 하지만 협력에서 **하나 이상의 객체가 동일한 책임을 수행**할 수 있으면 **역할**(서로 다른 방법으로 실행할 수 있는 책임의 집합)
- 협력 (Collaboration) -- _reference_ → 역할 (Role) -- _select from_ → 객체 (Object) -- _instance of_ → 클래스 (Class)

**Impression**

- Information Expert (정보 전문가) 패턴
- 상태를 우선시 하는 게 아니라 행동이 우선시 되어 상태를 결정 (DDD와 반대되는 개념)
- Spring에서의 협력은 **DI**, 협력을 위해 DI를 사용한다.
</details>
<details>
<summary><b>CHAPTER 04. 설계 품질과 트레이드오프</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter4"> 🔗 link </a>
<br>

**TL;DR**
- 좋은 설계란 오늘의 기능을 수행하면서 내일의 변경을 수용할 수 있는 설계이다.
- 객체지향 프로그램을 통해 전반적으로 얻을 수 있는 장점은 오직 설계 과정 동안 캡슐화를 목표로 인식할 때만 달성될 수 있다.
- 추측의 의한 설계 전략은 접근자와 수정자에 과도하게 의존하게 하는 설계 방식이다.
- 결론: 데이터 중심의 설계는 **너무 이른 시기에 데이터에 대해 고민**하기 때문에 **캡슐화에 실패**
- 객체의 구현을 먼저 결정하고 협력을 고민하기 때문에 이미 구현된 객체의 인터페이스를 억지로 끼워맞출 수 밖에 없다.

<br>
</details>
<details>
<summary><b>CHAPTER 05. 책임 할당하기</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter5"> 🔗 link </a>
<br>

**TL;DR**
- **GRASP Pattern**: General Responsibility Assignment Software Pattern, 책임 할당을 위한 소프트웨어 패턴
  - : 책임을 수행하는 데 필요한 메시지를 결정하고, 책임을 수행할 정보 전문가에게 책임을 할당하라
  - **INFORMATION EXPERT 패턴**: 책임을 정보 전문가(책임을 수행하는 데 필요한 정보를 가지고 있는 객체)에게 할당하라
  - **LOW COUPLING 패턴**: 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라
  - **HIGH COHESION 패턴**: 높은 응집도를 유지할 수 있게 책임을 할당하라
  - **CREATOR 패턴**: 연결되거나 관련될 필요가 있는 객체에게 객체 생성 책임을 할당하라 (잘 알고 있거나/어차피 사용해야 하는 객체)
  - **POLYMORPHISM 패턴**: 타입을 명시적으로 정의하고 각 타입에 다형적으로 행동하는 책임을 할당하라
  - **PROTECTED VARIATIONS 패턴**: 변화가 예상되는 불안정한 지점들을 식별하고 그 주위에 안정된 인터페이스를 형성하도록 책임을 할당하라
- **리팩터링을 고려할 시점 2가지**
  - 클래스의 속성이 서로 다른 시점에 초기화되거나 일부만 초기화된다는 것은 응집도가 낮다는 증거
  - 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다는 증거
- 주석을 추가하는 대신 **메서드를 작게 분해**해서 각 메서드의 **응집도를 높여라**
  - Benefit: 재활용될 확률 증가 / 메소드 이름으로 주석을 읽는 느낌을 줌 / 오버라이딩하기 용이
- 처음부터 책임 주도 설계 방법을 따르는 것보다 동작하는 **코드를 작성한 후, 리팩터링하는 것이 더 훌륭한 결과물을 낳을 수도 있음**

<br>
</details>
<details>
<summary><b>CHAPTER 06. 메시지와 인터페이스</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter6"> 🔗 link </a>
<br>

**TL;DR**
- **Law of Demeter**: 디미터 법칙. 객체의 내부 구조에 강하게 결합되지 않도록 **협력 경로를 제한**하라
  - Use only one dot.
  - 특정 조건의 클래스에게만 메시지 전송: **① 메서드의 인자**로 전달된 클래스, **② 해당 메서드를 가진 클래스** 자체, **③ 해당 메서드를 가진 클래스의 인스턴스 변수 클래스**
  - Shy Code: 부끄럼타는 코드, 디미터 법칙에서 보이는 패턴, 불필요한 어떤 것도 다른 객체에게 보여주지 않으며, 다른 객체의 구현에 의존하지 않는 코드
  - Train Wreck: 기차 충돌, 디미터 법칙 위반 패턴, 여러 대의 기차가 한 줄로 늘어진 것처럼 보이는 코드로 내부 구현이 외부로 노출됐을 때 나타나는 전형적인 형태.
- **Tell, Don't Ask**: 묻지 말고 시켜라. **객체의 내부 구조를 묻는 메시지**가 아니라 **수신자에게 무언가를 시키는 메시지**를 강조하는 법칙
- **의도를 드러내는 선택자(Intention Revealing Selector)**: 무엇을 하느냐에 따라 메서드의 이름을 짓는 패턴
- **명령-쿼리 분리**
  - Command: 객체의 **상태를 수정**하는 오퍼레이션
  - Query: 객체와 관련된 **정보를 반환**하는 오퍼레이션
- 원칙을 맹신하지 마라, **원칙이 적절한 상황과 부적절한 상황을 판단할 수 있는 안목을 길러라.**
- Design By Contract: 계약에 의한 설계. 협력을 위해 클라이언트와 서버가 준수해야 하는 제약을 코드 상에 명시적으 로 표현하고 강제할 수 있는 방법

<br>
</details>
<details>
<summary><b>CHAPTER 07. 객체 분해</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter7"> 🔗 link </a>
<br>

**TL;DR**
- 추상화: 불필요한 정보를 제거하고 현재의 문제 해결에 필요한 핵심만 남기는 작업
- 분해(decomposition): 큰 문제를 해결 가능한 작은 문제로 나누는 작업
- 소프트웨어는 **데이터**를 이용해 정보를 표현하고 **프로시저**를 이용해 데이터를 조작
  - 프로시저 추상화: = 기능 분해, 알고리즘 분해. 소프트웨어가 무엇을 해야 하는지를 추상화.
  - 데이터 추상화: 소프트웨어가 무엇을 알아야 하는지를 추상화
- 모듈은 퍼블릭 인터페이스를 외부에 제공해서 복잡성과 변경 가능성을 감춰야 한다.
- 클래스는 절차를 추상화(procedural abstraction), 추상 데이터 타입은 타입을 추상화(type abstraction).
- 추상 데이터 타입과 객체지향 설계의 유용성은 설계에 요구되는 변경의 압력이 '타입 추가'에 관한 것인지, 아니면 '오퍼레이션 추가'에 관한 것인지에 따라 달라짐
  - 변경의 압력이 **타입 추가하는 것**이라면 더 강한 경우에는 객체지향
  - 변경의 압력이 **오퍼레이션을 추가하는 것**이라면 추상 데이터 타입

<br>
</details>
<details>
<summary><b>CHAPTER 08. 의존성 관리하기</b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter8"> 🔗 link </a>
<br>

**TL;DR**

- 의존성 전이: 의존하는 대상의 의존성에 대해서도 연쇄적으로 의존하게 되는 것
  - **직접 의존성**(direct dependency): 한 요소가 다른 요소에 직접 의존하는 경우
  - **간접 의존성**(indirect dependency): 직접적인 관계는 존재하지 않지만 의존성 전이에 의해 영향이 전파되는 경우
- **컴파일타임 의존성**: 클래스(작성한 코드)의 구조
- **런타임 의존성**: 객체 사이의 의존성
- 컨텍스트 독립성: 각 객체가 해당 객체를 실행하는 시스템에 관해 아무것도 알지 못하도록 함
  - 클래스가 사용 될 **특정 문맥**에 **최소한의 가정만**으로 이뤄져 있다면 **다른 문맥에서 재사용하기가 더 수월**
- 의존성 해결을 위한 방법: 생성자, setter 메서드, 메서드 실행 인자
- 결합도의 정도: 한 요소가 자신이 의존하고 있는 **다른 요소에 대해 알고 있는 정보의 양**으로 결정
- 경계해야 할 것은 의존성 자체가 아니라 의존성을 감추는 것
- new는 해롭다: 외부로부터 이미 생성된 인스턴스를 전달받자
- 선언적인 정의: 작은 객체들의 행동을 조합함으로써 새로운 행동을 이끌어낼 수 있는 설계
  - 방법(how)이 아니라 목적(what)에 집중할 수 있어 시스템의 행위를 변경에 용이

<br>
</details>

<details>
<summary><b>CHAPTER 09. 유연한 설계 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter9"> 🔗 link </a>
<br>

**TL;DR**

- **개방-폐쇄 원칙**
  - 정의: 소프트웨어 개체는 확장에 대해 열려 있어야 하고, 수정에 대해서는 닫혀 있어야 한다.
  - 개방-폐쇄 원칙을 따르는 설계: **컴파일타임 의존성은 유지**하면서, **런타임 의존성의 가능성을 확장하고 수정할 수 있는 구조**.
- 생성과 사용을 분리하라 (separating use from creation)
- **FACTORY**: 생성과 사용을 분리하기 위해 객체 생성에 특화된 객체.
- 추가하려는 행동을 책임질만한 도메인 개념이 존재하지 않는다면, PURE FABRICATION을 추가하고 책임을 할당하라.
  - **PURE FABRICATION**: 순수한 가공물. 책임을 할당하기 위해 창조되는 **도메인과 무관한 인공적인 객체**.
  - 도메인 개념의 객체와 순수하게 창조된 가공의 객체들이 모여 자신의 역할과 책임을 다하고 조화롭게 협력하는 애플리케이션을 설계하는 것이 목표여야 한다.
- **SERVICE LOCATOR 패턴**: 의존성을 해결할 객체들을 보관하는 저장소. SERVICE LOCATOR 에게 의존성을 해결해줄 것을 요청. (의존성을 감춘다는 큰 단점, 글쓴이의 지양 패턴)
- **SEPARATED INTERFACE 패턴**: 인터페이스와 그 구현을 별개의 패키지에 위치시키는 패턴.
- 잘 설계된 객체지향 애플리케이션에서는 **인터페이스의 소유권을 서버가 아닌 클라이언트에 위치**시킨다.

<br>
</details>
<details>
<summary><b>CHAPTER 10. 상속과 코드 재사용 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter10"> 🔗 link </a>
<br>

**TL;DR**

- 요구사항이 변경됐을 때 두 코드를 함께 수정해야 한다면 이 코드는 중복 (함께 수정할 필요가 없다면 중복이 아님)
- DRY 원칙: '반복하지 마라'라는 뜻의 Don't Repeat Yourself 의 첫 글자를 모아 만든 용어로 간단히 말해 동일한 지식을 중복하지 말라는 것
- 취약한 기반 클래스 문제: 상속 관계로 연결된 자식 클래스가 부모 클래스의 변경에 취약해지는 현상
- 메서드 오버라이딩의 오작용 문제: 클래스 상속을 위해서는 클래스를 설계하고 문서화해야 하며, 그렇지 않은 경우에는 상속을 금지해야함
- 차이에 의한 프로그래밍: 중복 코드를 제거하고 코드를 재사용하는 것
- 상속의 오용과 남용은 애플리케이션을 이해하고 확장하기 어렵게 만듦, 정말로 필요한 경우에만 상속을 사용.

<br>
</details>
<details>
<summary><b>CHAPTER 11. 합성과 유연한 설계 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter11"> 🔗 link </a>
<br>

**TL;DR**

- **상속**: is-a 관계. 부모 클래스와 자식 클래스 사이의 의존성이 **컴파일타임**에 해결
- **합성**: has-a 관계. 부모 클래스와 자식 클래스 사이의 의존성이 **런타임**에 해결
- **상속의 단점**: ① 불필요한 인터페이스 상속 ② 메서드 오버라이딩의 오작용 ③ 부모 클래스와 자식 클래스의 동시 수정 필요
- **'상속 → 합성'** 변경 방법: 자식 클래스에 선언된 상속 관계를 제거, 부모 클래스의 인스턴스를 자식 클래스의 인스턴스 변수로 선언
- **포워딩 메서드 (forwarding method)**: 동일한 메서드를 호출하기 위해 추가된 메서드
- **몽키 패치(Monkey Patch)**: 현재 실행 중인 환경에만 영향을 미치도록 지역적으로 코드를 수정하거나 확장하는 것
- **훅 메서드(hook method)**: 추상 메서드와 동일하게 자식 클래스에서 오버라이딩할 의도로 메서드를 추가했지만 편의를 위해 기본 구현을 제공하는 메서드
- **클래스 폭발(class explosion)**: 상속의 남용으로 하나의 기능을 추가하기 위해 필요 이상으로 많은 수의 클래스를 추가 해야 하는 경우

<br>
</details>
<details>
<summary><b>CHAPTER 12. 다형성 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter12"> 🔗 link </a>
<br>

**TL;DR**

- **다형성(Polymorphism)**: 그리스어의 'poly(많은)'와 'morph(형태)'의 합성어로 '많은 형태를 가질 수 있는 능력'.
  - **임시 다형성** _Ad Hoc Polymorphism_
    - **오버로딩 다형성** _Overloading Polymorphism_: 하나의 클래스 안에 동일한 이름의 메서드가 존재하는 경우
    - **강제 다형성** _Coercion Polymorphism_: **동일한 연산자를 다양한 타입에 사용**할 수 있는 방식 (ex. `+` 연산자)
  - **유니버설 다형성** _Universal Polymorphism_
    - **매개변수 다형성** _Parametric Polymorphism_: 사용하는 시점에 구체적인 타입을 지정하는 방식.  (ex. 제네릭 프로그래밍)
    - **포함 다형성** _Inclustion Polymorphism_: 흔히 '다형성' 라고 지칭되는 개념. 메시지가 동일하더라도 수신한 객체의 타입에 따라 실제로 수행되는 행동이 달라지는 능력
- **상속의 목적**: 코드 재사용이 아니라 다형성을 위한 **서브타입 계층을 구축**하는 것
  - **데이터 관점**의 상속: 자식 클래스의 인스턴스 안에 부모 클래스의 인스턴스를 포함하는 개념
  - **행동 관점**의 상속: 부모 클래스가 정의한 일부 메서드를 자식 클래스의 메서드로 포함시키는 개념
- **업캐스팅** _upcasting_: 부모 클래스 타입으로 선언된 변수에 자식 클래스의 인스턴스를 할당하는 것이 가능.
- **동적 바인딩** _dynamic binding_: 선언된 변수의 타입이 아니라 메시지를 수신하는 객체의 타입에 따라 실행되는 메서드가 결정. (메시지 처리 메서드를 컴파일 시점이 아니라 실행 시점에 결정하기 때문에 가능)
- **다운캐스팅** _downcasting_: 반대로 부모 클래스의 인스턴스를 자식 클래스 타입으로 변환하기 위해서는 명시적인 타입 캐스팅
- **프로토타입** _prototype_: 클래스가 아닌 객체를 이용해서도 상속을 흉내 낼 수 있음
- 중요한 것은 **클래스 기반의 상속**과 **객체 기반의 위임** 사이에 **기본 개념과 메커니즘을 공유한다는 점**

<br>
</details>
<details>
<summary><b>CHAPTER 13. 서브클래싱과 서브타이핑 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter13"> 🔗 link </a>
<br>

**TL;DR**
- 상속의 목적: ① 타입 계층을 구현하는 것, ② 코드 재사용
- 상속을 사용하는 일차적인 목표는 코드 재사용이 아니라 타입 계층을 구현하는 것이어야 함
- 타입의 구성
  - **심볼(symbol)**: 타입에 이름을 붙인 것
  - **내연(intension)**: 타입의 정의로서 타입에 속하는 **객체들이 가지는 공통적인 속성이나 행동**
  - **외연(extension)**: 타입에 속하는 객체들의 집합
- 상속을 사용할 2가지 조건
  1. is-a 관계를 모델링하는 상속 관계 : **"타입 S는 타입 T다(S is-a T)"** 를 만족할 때 적용 가능
  2. 클라이언트 입장에서 부모 클래스의 타입으로 자식 클래스를 사용 가능할 때
- **행동 호환성**: 타입의 이름 사이에 개념적으로 어떤 연관성이 있다고 하더라도 **행동에 연관성이 없다면 is-a 관계를 사용하지 말아야 함**
- **인터페이스 분리 원칙 (Interface Segregation Principle, ISP)**: 인터페이스를 클라이언트의 기대에 따라 분리함으로써 변경에 의해 영향을 제어하는 설계 원칙
- **서브클래싱 & 서브타이핑**
  - **서브클래싱(subclassing)**: 다른 클래스의 코드를 재사용할 목적으로 상속을 사용하는 경우
  - **서브타이핑(subtyping)**: 타입 계층을 구성하기 위해 상속을 사용하는 경우
- **클래스 상속 vs 인터페이스 상속**
  - **클래스 상속**: 객체의 구현을 정의할 때 이미 정의된 객체의 구현을 바탕으로 함. 코드 공유의 방법
  - **인터페이스 상속**: 서브 타이핑. 객체가 다른 곳에서 사용될 수 있음을 의미
- is-a 관계로 표현된 문장을 볼 때마다 문장 앞에 "**클라이언트 입장에서**"라는 말을 붙여서 생각하라.

<br>
</details>
<details>
<summary><b>CHAPTER 14. 일관성 있는 협력 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter14"> 🔗 link </a>
<br>

**TL;DR**
- 일관성 있는 개발은 **설계 비용을 감소**시키고 **이해하기 쉬운 코드**를 이끌어 냄
- 비일관성은 **새로운 구현을 추가**하거나, **기존의 구현을 이해해야 하는 상황**에서 어려움을 겪게 함
- **일관성 있는 설계를 만드는 방법**
  1. 다양한 설계 경험을 익히는 것
  2. 널리 알려진 디자인 패턴을 학습하고 변경이라는 문맥 안에서 디자인 패턴을 적용해 보는 것
  3. 기본 지침을 따르는 것 (변하는 개념을 변하지 않는 개념으로부터 분리하라, 변하는 개념을 캡슐화하라)
- 클래스는 단일 책임 원칙에 따라 명확히 단 하나의 이유에 의해서만 변경돼야 하고 클래스 안의 모든 코드는 함께 변경돼야 함
  - 큰 메서드 안에 뭉쳐있던 조건 로직들을 **변경의 압력에 맞춰** 작은 클래스들로 분리하고 나면, 인스턴스들 사이의 협력 패턴에 일관성을 부여하기가 더 쉬워짐
  - **유사한 행동을 수행하는 작은 클래스**들이 역할이라는 추상화로 묶이고, 역할 사이에서 이뤄지는 **협력 방식이 전체 설계의 일관성을 유지**할 수 있기 때문
- 캡슐화란 단지 데이터 은닉을 의미하는 것이 아니라, **코드 수정으로 인한 파급효과를 제어할 수 있는 모든 기법**이 캡슐화의 일종
- 캡슐화의 다양한 종류
  - **데이터 캡슐화**: 내부에 관리하는 데이터를 캡슐화
  - **메서드 캡슐화**: 클래스의 내부 행동을 캡슐화
  - **객체 캡슐화**: 객체와 객체 사이의 관계를 캡슐화 (객체 캡슐화는 합성을 의미)
  - **서브타입 캡슐화**: 서브타입의 종류를 캡슐화 (서브타입 캡슐화는 다형성 기반)
- **일관성 있는 협력을 위한 캡슐화 방법**
  1. 변하는 부분을 분리해서 타입 계층을 만든다
  2. 변하지 않는 부분의 일부로 타입 계층을 합성한다
- 개념적 무결성(Conceptual Integrity): 협력을 설계하고 있다면 항상 기존의 협력 패턴을 따를 수는 없는지 고민하라.
  - 유사한 기능에 대해 유사한 협력 패턴을 적용하는 것은 객체지향 시스템에서 **개념적 무결성(Conceptual Integrity)** 을 유지할 수 있는 가장 효과적인 방법
- 유사한 기능에 대한 변경이 지속적으로 발생하고 있다면 패턴을 찾아라
  - 변경을 캡슐화할 수 있는 적절한 추상화를 찾은 후, 이 추상화에 변하지 않는 공통적인 책임을 할당하라.
  - 현재의 구조가 변경을 캡슐화하기에 적합하지 않다면 코드를 수정하지 않고도 원하는 변경을 수용할 수 있도록 **협력과 코드를 리팩터링하라**.

<br>
</details>
<details>
<summary><b>CHAPTER 15. 디자인 패턴과 프레임워크 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/chapter15"> 🔗 link </a>
<br>

**TL;DR**

- **패턴**
  - 공통으로 사용할 수 있는 역할, 책임, 협력의 템플릿
  - 패턴은 출발점이다: 특정한 설계 이슈를 해결하기 위해 적절한 디자인 패턴을 이용해 설계를 시작하지만, **패턴이 설계의 목표가 돼서는 안 됨**
- **패턴 종류**
  - **아키텍처 패턴 ( Architecture Pattern )**
    - 미리 정의된 서브시스템들을 제공하고, 각 서브시스템들의 책임을 정의 하며, 서브시스템들 사이의 관계를 조직화하는 규칙과 가이드라인을 포함
  - **분석 패턴 ( Analysis Pattern )**
    - 도메인 내 개념적인 문제를 해결하는 데 초점을 두며, 업무 모델링 시에 발견되는 공통적인 구조를 표현하는 개념들의 집합 _- Fowler_
  - **디자인 패턴( Design Pattern )**
    - 다양한 변경을 다루기 위해 반복적으로 재사용할 수 있는 설계의 묶음
  - **이디엄( Idiom )**
    - 특정 언어의 기능을 사용해 컴포넌트, 혹은 컴포넌트 간의 특정 측면을 구현하는 방법을 서술 _- Buschman_
- **패턴 만능주의**: 패턴을 익힌 후에는 모든 설계 문제를 패턴으로 해결하려고 시도하곤 한다. _- 조슈아 케리에브스키, Kerievsky_
  - 패턴 구조를 맹목적으로 따르려 하면 불필요하게 복잡하고, 난해하며, 유지보수하기 어려운 시스템을 낳음
- **프레임워크**
  - 추상 클래스나 인터페이스를 정의하고 인스턴스 사이의 상호작용을 통해 시스템 전체 혹은 일부를 구현해 놓은 재사용 가능한 설계
  - 애플리케이션 개발자가 현재의 요구사항에 맞게 커스터마이징할 수 있는 애플리케이션의 골격 (skeleton)
- **제어 역전 (Inversion of Control) 원리**
  - 할리우드(Hollywood) 원리, 프레임워크가 애플리케이션에 속하는 서브클래스의 메서드를 호출
- 훅 (hook)
  - 프레임워크에서는 일반적인 해결책만 제공하고 애플리케이션에 따라 달라질 수 있는 특정한 동작은 비워두는데, 그리고 이렇게 완성되지 않은 채로 남겨진 동작

<br>
</details>
<details>
<summary><b>Appendix A. 계약에 의한 설계 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/appendixA"> 🔗 link </a>
<br>

**TL;DR**

- **계약에 의한 설계(Design By Contract, DBC)**
  - > - 협력에 참여하는 각 객체는 계약으로부터 이익을 기대하고 이익을 얻기 위해 **의무를 이행** ①
    >
    > - 협력에 참여하는 각 객체의 이익과 의무는 객체의 **인터페이스 상에 문서화** ②
    - ② 의도를 드러내는 인터페이스: 오퍼레이션이 클라이언트에게 **어떤 것을 제공하려고 하는지를 충분히 설명할 수 있음**
    - ① 계약은 여기서 한걸음 더: 위의 내용과 더불어 **협력하는 클라이언트는 정상적인 상태를 가진 객체와 협력해야 함**
  - 두 계약 당사자들에 대해, **한쪽의 의무가 반대쪽의 권리가 된다**
    - 한쪽이라도 계약서에 명시된 내용을 위반한다면 계약은 정상적으로 완료되지 않음
- **계약에 의한 설계를 구성하는 세 가지 요소**
  - **사전조건(precondition)**
    - 메서드가 정상적으로 실행되기 위해 만족해야 하는 조건
    - 메서드의 요구사항을 명시
    - 사전조건을 만족시키는 것은 메서드를 실행하는 클라이언트의 의무다.
  - **사후조건(postcondition)**
    - 메서드가 실행된 후에 클라이언트에게 보장해야 하는 조건
    - 메서드의 **인스턴스 변수의 상태**, **메서드에 전달된 파라미터의 값이 올바르게 변경됐는지**, **반환값이 올바른지**를 검증
    - 사후조건을 만족시키는 것은 서버의 의무
  - **불변식(invariant)**
    - 항상 참이라고 보장되는 서버의 조건
    - 실행 중에는 불변식을 만족시키지 못할 수도 있지만, 메서드를 실행하기 전이나 종료된 후에 불변식은 항상 참이어야 함
    - 사전조건과 사후조건에 추가되는 공통의 조건
- **리스코프 치환 원칙**을 만족시키기 위해서는 서브타입이 클라이언트와 슈퍼타입 간에 체결된 계약을 준수해야 함
  1. **계약 규칙** _contract rules_: 슈퍼타입과 서브타입 사이의 사전조건, 사후조건, 불변식에 대해 서술할 수 있는 제약에 관한 규칙
    - > - 서브타입에 더 강력한 사전조건을 정의할 수 없다.
      > - 서브타입에 더 완화된 사후조건을 정의할 수 없다.
      > - 슈퍼타입의 불변식은 서브타입에서도 반드시 유지돼야 한다.
  2. **가변성 규칙** _variance rules_: 파라미터와 리턴 타입의 변형과 관련된 규칙
    - > - 서브타입의 메서드 파라미터는 반공변성을 가져야 한다.
      > - 서브타입의 리턴 타입은 공변성을 가져야 한다.
      > - 서브타입은 슈퍼타입이 발생시키는 예외와 다른 타입의 예외를 발생시켜서는 안 된다.
- **진정한 서브타이핑 관계를 만들고 싶다면,**
  - 서브타입에 **더 강화된 사전조건**이나 **더 완화된 사후조건**을 정의해서는 안 되며,
  - 슈퍼타입의 **불변식을 유지**하기 위해 항상 노력해야 하며,
  - 서브타입에서 **슈퍼 타입에서 정의하지 않은 예외를 던져서는 안됨**

<br>
</details><details>
<summary><b>Appendix B. 타입 계층의 구현 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/appendixB"> 🔗 link </a>
<br>

**TL;DR**

- 타입 계층을 구현하는 동시에 다형성을 구현하는 방법임
- 타입 계층을 구현한다고 해서, 서브타이핑 관계가 보장되는 것은 아님
  - 올바른 타입 계층: 리스코프 치환 원칙을 준수 (리스코프 치환 원칙을 준수하는 책임은 본인에게 있음, _13장_.)
- 객체지향 언어에서 클래스를 **사용자 정의 타입 (user-defined data type)** 이라고 부름
  - 타입은 객체의 퍼블릭 인터페이스 - 결과적으로 클래스는 **객체의 타입과 구현을 동시에 정의하는 것**
- **인터페이스 상속**의 장점 (↔ 클래스 상속)
  1. _다중 상속의 딜레마에 빠지지 않을 수 있음_
  2. _단일 상속 계층으로 인한 결합도 문제도 피할 수 있음_
- **골격 구현 추상 클래스( skeletal implementation abstract class )**
  - 인터페이스를 이용해 타입을 정의하면 **다중 상속 문제를 해결**할 수 있지만 **중복 코드를 제거하기 어려움**
  - **추상 클래스**: **타입을 정의**하면서 **코드 중복을 방지**하도록 정의
    **인터페이스와 추싱클래스를 결합하여 타입을 정의할 때의 장점**
  - 다양한 구현 방법이 필요할 경우 새로운 추상 클래스를 추가해서 쉽게 해결 가능.
  - 부모 클래스가 이미 있어도, 인터페이스를 통해 새로운 타입으로 쉽게 확장 가능.
- 설계가 복잡할 경우 (상속 계층에 얽매이지 않는 타입 계층을 요구한다면):
- 인터페이스로 타입을 정의하고, 추상 클래스로 기본 구현을 제공해서 중복 코드를 제거하라
  - 복잡하지 않다면: 인터페이스나 추상 클래스 둘 중 하나만 사용
- **덕 타이핑**
  - 어떤 대상의 **행동**이 오리와 같다면 그것을 오리라는 타입으로 취급해도 무방하다는 것
- **디폴트 메서드( default method )**
  - **자바에서 믹스인을 구현할 수 있는 기능**
  - 인터페이스에 메서드의 기본 구현을 추가하는 것을 허용
  - '추상 클래스가 제공하는 코드 재사용성 + 특정한 상속 계층에 얽매이지 않는 인터페이스'의 장점 유지
- **디폴트 메서드가 가지는 한계**
  - 디폴트 메서드에서 호출하는 메서드들이 인터페이스에 정의되어 있음 → 클래스 안에서 **퍼블릭 메서드**로 구현해야 함

<br>
</details><details>
<summary><b>Appendix C. 동적인 협력, 정적인 코드 </b></summary>

<br>
<a href="https://github.com/2mz1/theory/tree/main/objects/gngsn/appendixC"> 🔗 link </a>
<br>

**TL;DR**

- **객체지향 프로그램을 작성하기 위한 두 가지 모델:**
  - **동적 모델(dynamic model)**
    - 프로그램 실행 구조를 표현하는 움직이는 모델
    - **객체**와 **협력**으로 구성
  - **정적 모델 (static model)**
    - 코드의 구조를 담는 고정된 모델
    -  **타입**과 **관계**로 구성
- **도메인 모델 (domain model)**: 사용자가 프로그램을 사용하는 대상 영역에 대한 지식을 선택적으로 단순화하고 의식적으로 구조화한 형태
  - 소프트웨어 제작에 필요한 **개념의 이름과 의미, 그리고 관계에 대한 힌트를 제공하는 역할**로 끝나야 함, 목적이 되면 안됨
- **객체지향 분석 설계**: 소프트웨어의 도메인에 대해 고민하고 **도메인 모델**을 기반으로 소프트웨어를 구축하라
  - 개념과 소프트웨어 사이의 표현적 차이를 줄일 수 있음
  - 이해하고 수정하기 쉬운 소프트웨어를 만들 수 있음
- **수정이 용이한 코드**: 응집도가 높고, 결합도가 낮으며, 단순해서 쉽게 이해할 수 있는 코드 (4장, 5장 참고)
- **유연한 코드**: 동일한 코드를 이용해 다양한 컨텍스트에서 동작 가능한 협력을 만들 수 있는 코드 (객체 사이의 다양한 조합을 지원해야 함, 8장 참고)
- **TYPE OBJECT 패턴**: 어떤 인스턴스가 다른 인스턴스의 타입을 표현하는 방법
- **도메인 모델**
  - 도메인 안에 존재하는 **개념과 관계를 표현**해야 하지만, **최종 모습**은 객체의 **행동과 변경**에 기반해야 하며 **코드의 구조를 반영**해야 함.
  - 중요한 건, 도메인의 개념뿐만 아니라 **코드도 함께 이해될 수 있는 구조**를 찾는 것
- 객체지향 패러다임에 대한 흔한 오해와는 다르게 분석 모델과 설계 모델, 구현 모델 사이에 어떤 차이점도 존재하지 않으며, 모두 **행동과 변경이라는 요소에 영향을 받으며 전체 개발 주기 동안 동일한 모양을 지녀야 함**


<br>
</details>
<br><br>