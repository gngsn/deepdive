## GoF Design Patterns

> **Design Patterns?** 
> - 많은 사람이 겪은 문제점과 해결 방법을 정리해둔 것. 
> - 디자인 패턴은 객체지향 문제를 해결하기 위한 일련의 코드 스타일. 
> - 객체 지향 개발에서 디자인 패턴이 해결하는 주요 문제는 객체 간 관계와 소통 처리 부분.

<pre>
<b>GoF?</b>
Gang of Four - Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides, Grady Booch.
처음으로 소프트웨어 공학에서 사용되는 패턴을 정리한 사람들의 별칭.
</pre>

<br>
<details>
<summary><b>설계 원칙; SOLID</b></summary>

✔️**SRP, Single Responsibility Principle. 단일 책임 원칙**

\: 하나의 클래스는 하나의 책임만을 가져야 한다

<br>

✔️**OCP, Open/Closed Principle. 개방 폐쇄 원칙**

\: 소프웨어 요소는 확장에는 열려있으나, 변경에는 닫혀 있어야 한다

<br>

✔️**LSP, Liskov Substitution Principle. 리스코브 치환 원칙**

\: 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다

<br>

✔️**ISP, Interface Segregation Principle. 인터페이스 분리의 원칙**

\: 특정 클라이언트를 위한 인터페이스 여러개가 범용 인터페이스 하나보다 낫다

<br>

✔️**DIP, Dependency Inversion Principle. 의존 관계 역전의 원칙**

\: 추상화에 의존해야지 구체화에 의존하면 안된다

*cf. 의존성 주입*

<br>

각각의 원칙을 코드의 목적에 맞게 적절히 사용할줄 알아야 함.

</details>
<br>

###  

<br><br>


## Pattern

|         | Creational Pattern                                                  | Structural Pattern                                                            | Behavioral Pattern                                                                                                          |
|---------|---------------------------------------------------------------------|-------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Class   | Factory Method                                                      | Adapter                                                                       | Interpreter<br />Template Method                                                                                            |
| Object  | Singleton<br />Builder <br />Abstract <br />Factory <br />Prototype | Bridge<br />composite<br />decorator <br />Facade <br />Flyweight <br />Proxy | Chain of Responsibility <br />Command <br />Iterator <br />Mediator <br />Observer <br />State <br />Strategy <br />Visitor |

<br><br>

## Creational Pattern

<br>

생성 패턴은 선언된 클래스로 객체를 생성하는 방법에 대한 문제점과 해결책을 제안

생성 패턴에서는 객체 생성을 위임하여 별개의 클래스로 분리하고, 객체 생성 과정을 담당할 별도의 클래스를 선언. 프로그램 내에서 필요한 객체를 생성하고 관리하는 캡슐화된 클래스를 선언하는 것.

<br>

**[Singleton](./singleton)**

선언된 클래스로 복수의 객체를 생성할 수 없도록 제한. 제한된 단일 객체는 공유와 충돌을 방지

<br>

**Factory**

객체의 생성 동작을 별도 클래스로 분리하여 처리하거나 별도의 메서드를 호출하여 객체의 생성 동작을 처리

- **[Abstract Factory](./abstractFactory)**: 팩토리 메서드를 확장 - 팩토리를 팩토리의 군(family)으로 변경
- **[Factory Method](./factoryMethod)**: 팩토리 패턴에 추상화를 결합

<br>

**[Builder](./builder)**

추상 팩토리를 확장한 패턴. 복잡한 구조의 복합 객체를 빌더 패턴으로 생성. 복합 객체를 생성하기 위한 단계를 정의하고 각 단계별 수행 동작을 변경할 수 있음.

<br>

**[Prototype](./prototype)**

새로운 객체를 생성하지 않고 기존의 객체를 복제. 복잡한 구조의 객체를 새로 생성하는 것은 많은 자원을 소모. 객체를 새로 생성하지 않고 자원을 절약하는 패턴

<br><br>

## Structual Pattern

<br>

**[Adapter](https://github.com/gngsn/TIL/blob/master/design-pattern/Adapter.md)**

인터페이스를 추상화하여 서로 다른 인터페이스를 통일화. 상속을 통한 어댑터와 합성을 통한 어댑터로 구분.

<br>

**[Bridge](./bridge)**

개념과 추상을 구부하여 처리. 또한 객체의 독립으로 확장과 변형이 가능

<br>

**[composite](./composite)**

객체의 구조를 이용해 객체를 확장. 트리 구조의 특징

<br>

**[decorator](./decorator)**

객체에 기능을 동적으로 추가. 확장 시 객체에 새로운 책임을 부과할 수 있음. 재귀적 합성 방법을 응용하여 객체를 확장

<br>

**[Facade](./facade)**

복잡한 객체의 구조와 접근을 간단하게 표현하는 방법

<br>

**[Flyweight](./flyweight)**

객체를 공유. 공유를 통해 자원의 효율성과 공유된 객체의 일관성 보장

<br>

**[Proxy](./proxy)**

객체의 겁근을 제한. 액세스할 때 추가적인 책임을 부여하는 역할을 수행

<br><br>

## Behavioral Pattern

<br>

**[Template Method](https://github.com/gngsn/TIL/blob/master/design-pattern/TemplateMethod.md)**

객체의 행위를 단계적으로 분리

추상화를 통해 공통된 단계와 공통되지 않은 단계를 분리함으로써 다양한 로직을 처리

<br>

**[Chain of Responsibility](./chainOfResponsibility)**

체인 패턴은 객체의 요청을 사슬 고리 형태로 연결함

객체는 자신의 동작이 완료될 때 내부적으로 연결된 객체에 다음 행위를 위임. 한 번의 객체 호출로 여러 객체의 행위를 수행할 수 있음

<br>

**[Command](./command)**

명령 패턴은 객체의 행위를 직접 요청하지 않으며 캡술화된 행위 객체를 전달하여 동작을 수행

<br>

**[Iterator](./iterator)**

집합체를 통해 객체의 원소를 순회하여 처리. 또한 객체의 반복 접근을 위해 인터페이스를 제공하고 순회방법을 캡슐화

<br>

**[Mediator](https://github.com/gngsn/TIL/blob/master/design-pattern/Mediator.md)**

중재자 패턴은 상호 연결된 객체를 그룹화하고 객체 사이의 프로토콜을 캡슐화하는 등 분산된 객체의 역할을 조정함

<br>

**[Observer](https://github.com/gngsn/TIL/blob/master/design-pattern/Observer.md)**

*발행/구독 모델*. 객체의 상태 변화를 관찰하는 관찰자들, 즉 옵저버들의 목록을 객체에 등록하여 상태 변화가 있을 때마다 메서드 등을 통해 직접 목록의 옵저버들에게 통지하는 패턴.

상태 변화가 있을 때 직접 처리하지 않고, 변화에 대한 행위를 독립된 객체로 분리하고 상채를 통보. 수신자는 전달 받은 상태의 객체를 호출

<br>

**[State](./state)**

조건에 따른 행동을 독립 객체로 캡슐화하고, 상태값에 따라 의존된 객체의 행동을 호출하는 형태로 제어 구조를 개선함

<br>

**[Strategy](./strategy)**

전략 패턴은 객체의 행위를 독립적인 알고리즘 객체로 분리함. 추상화를 통해 공통된 단계와 공통되지 않은 단계를 분리함으로써 다양한 로직을 처리

<br>

**[Visitor](./visitor)**

방문자 패턴은 객체의 데이터 구조와 처리를 분리. 양방향 참조를 통해 분리된 객체 행위를 수행

<br>

**[Memento](./memento)**

메멘토 패턴은 스냅숏을 활용해 객체 상태를 스택에 저장하고, 스택에 저장된 객체는 복구를 통해 이전 상태로 되돌릴 수 있음

메멘토 : 기억의 증표


<br><br>

<details>
<summary><b>Reference</b></summary>

- [GoF Design Patterns](https://www.amazon.com/Design-Patterns-Object-Oriented-Addison-Wesley-Professional-ebook/dp/B000SEIBB8)
- [쉽게 배워 바로 써먹는 디자인 패턴](https://www.hanbit.co.kr/store/books/look.php?p_code=B9696096335)
- [Head First Design Patterns, 2nd Edition](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
- [객체지향 디자인 패턴 1편](https://www.yalco.kr/29_oodp_1/)
- [객체지향 디자인 패턴 2편](https://www.yalco.kr/30_oodp_2/)

</details>
