# CHAPTER 08. 의존성 관리하기

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


<br/>

## 01. 의존성 이해하기

### 변경과 의존성

- **실행 시점**: 의존하는 객체가 정상적으로 동작하기 위해서는 실행 시에 의존 대상 객체가 반드시 존재해야 함.
- **구현 시점**: 의존 대상 객체가 변경될 경우 의존하는 객체도 함께 변경됨

두 요소 사이의 의존성은 의존되는 요소가 변경 될 때 의존하는 요소도 함께 변경될 수 있다는 것을 의미

<br/>

### 의존성 전이
:: 의존하는 대상의 의존성에 대해서도 연쇄적으로 의존하게 되는 것

- 의존성은 함께 변경될 수 있는 가능성을 의미하기 때문에 모든 경우에 의존성이 전이되는 것은 아님
- 의존성이 실제로 전이될지 여부는 변경의 방향과 캡슐화의 정도에 따라 달라짐

**의존성의 종류**
- **직접 의존성**(direct dependency): 한 요소가 다른 요소에 직접 의존하는 경우
- **간접 의존성**(indirect dependency): 직접적인 관계는 존재하지 않지만 의존성 전이에 의해 영향이 전파되는 경우

<br/>

### 런타임 의존성과 컴파일타임 의존성

**다루는 주제**
- **컴파일타임 의존성**: 클래스(작성한 코드)의 구조
- **런타임 의존성**: 객체 사이의 의존성

**-> 런타임 의존성과 컴파일타임 의존성이 다를 수 있음**


**유연하고 다양한 실행 구조** 

- 인스턴스가 다양한 클래스의 인스턴스와 협력하기 위해서는 협력할 **인스턴스의 구체적인 클래스를 알아서는 안됨**
- 실제로 협력할 객체가 어떤 것인지는 **런타임에 해결**해야 함
- 컴파일타임 구조와 런타임 구조 사이의 거리가 멀면 멀수록 설계가 유연 해지고 재사용 가능해짐

<br/>

### 컨텍스트 독립성
:: 각 객체가 해당 객체를 실행하는 시스템에 관해 아무것도 알지 못한다는 의미

- 클래스가 사용 될 **특정 문맥**에 **최소한의 가정만**으로 이뤄져 있다면 **다른 문맥에서 재사용하기가 더 수월**
- 클래스는 자신과 협력할 객체의 구체적인 클래스에 대해 알아서는 안됨

> 시스템을 구성하는 객체가 컨텍스트 독립적이라면 해당 시스템은 변경하기 쉽다. 
> 여기서 **컨텍스트 독립적**이라는 말은 **각 객체가 해당 객체를 실행하는 시스템에 관해 아무것도 알지 못한다는 의미**다. 
> 
> _- Freeman._

<br/>

### 의존성 해결하기
:: 컴파일타임 의존성을 실행 컨텍스트에 맞는 적절한 런타임 의존성으로 교체

**의존성 해결을 위한 방법**

1. 객체를 생성하는 시점에 생성자를 통해 의존성 해결
   - 추상형을 받아서, 런타임 (객체 생성) 시에 실제 구현체를 생성자를 통해 넘겨줌
2. 객체 생성 후 setter 메서드를 통해 의존성 해결
   - 객체를 생성한 이후에도 의존하고 있는 대상을 변경할 수 있는 가능성을 열어 놓고 싶은 경우에 유용
   - 객체를 생성하고 의존 대상을 설정하기 전까지 객체의 상태가 불완전할 수 있음 (NPE 발생 가능)
   - `setter`를 단일 사용하는 것보다 좋은 방법은 **생성자 방식과 `setter` 방식을 혼합**
3. 메서드 실행 시 인자를 이용해 의존성 해결
   - 항상 의존성을 알 필요가 없더 특정 계산을 진행할 때만 일시정적으로 알아도 된다면 메서드의 인자로 해결 가능
   - 메서드가 실행 될 때마다 의존 대상이 매번 달라져야 하는 경우에도 유용


<br/>

## 2. 유연한 설계

### 의존성과 결합도

- 의존성은 객체들의 협력을 가능하게 만드는 매개체라는 관점에서는 바람직한 것, 하지만 의존성이 과하면 문제가 될 수 있음.
- **의존성 자체가 나쁜 것이 아니다. 의존성은 협력을 위해 반드시 필요한 것이다. 단지 바람직하지 못한 의존성이 문제일 뿐이다.**


> 바람직한 의존성이란? 

- 어떤 의존성이 다양한 환경에서 클래스를 재사용할 수 없도록 제한 - 바람직하지 못한 의존성
- 어떤 의존성이 다양한 환경에서 재사용할 수 있음 - 바람직한 의존성

**결합도**

- 느슨한 결합도(loose coupling) or 약한 결합도(weak coupling): 두 요소 사이에 존재하는 의존성이 바람직할 때
- 단단한 결합도(tight coupling) 또는 강한 결합도(strong coupling): 두 요소 사이의 의존성이 바람직하지 못할 때

<br/>

### 지식이 결합을 낳는다

- 결합도의 정도: 한 요소가 자신이 의존하고 있는 **다른 요소에 대해 알고 있는 정보의 양**으로 결정

<small> 더 많이 알수록 더 많이 결합, 더 많이 알고 있다는 것은 더 적은 컨텍스트에서 재사용 가능하다는 것을 의미 </small>

- 결합도를 느슨하게 만들기 위해서는 협력하는 대상에 대해 필요한 정보 외에 는 최대한 감추는 것이 중요 -> **추상화**


<br/>

### 추상화에 의존하라

- 아래로 갈수록 결합도가 느슨해짐

  - **구체** 클래스 의존성(concrete class dependency)
  - **추상** 클래스 의존성(abstract class dependency)
  - **인터페이스** 의존성(interface dependency)

- **-> 클라이언트가 알아야 하는 지식의 양이 적어지기 때문**

<br/>

### 명시적인 의존성

```java
public class Movie {
    private DiscountPolicy discountPolicy;
    public Movie(String title, Duration runningTime, Money fee) {
        // ...
        this.discountPolicy = new AmountDiscountPolicy( /* ... */);
    }
 }
```

추상 클래스인 DiscountPolicy 타입으로 선언되어 있지만, 생성자에서 구체 클래스인 AmountDiscountPolicy 의 인스턴스를 직접 생성해서 대입
-> 추상 클래스인 DiscountPolicy와 구체 클래스인 AmountDiscountPolicy를 동시에 의존


- **명시적인 의존성 (explicit dependency)**: 생성자의 인자로 선언하는 방법, 의존성은 명시적으로 퍼블릭 인터페이스에 노출됨
- **숨겨진 의존성(hidden dependency)**: 특정 객체의 내부에서 다른 객체의 인스턴스를 직접 생성하는 방식, 퍼블릭 인터페이스에 표현되지 않음

**경계해야 할 것은 의존성 자체가 아니라 의존성을 감추는 것**

<br/>

### new는 해롭다

**new가 해로운 이유**
- 추상화가 아닌 구체 클래스에 의존: new 연산자를 사용하기 위해서는 구체 클래스의 이름을 직접 기술해야 함
- 알아야 하는 지식의 양이 늘어남: 생성하려는 구체 클래스뿐만 아니라, 생성자 호출 인자도 알아야 해서 결합도가 높아짐

**해결 방법**
- 인스턴스를 생성하는 로직과 생성된 인스턴스를 사용하는 로직을 분리
- 외부로부터 이미 생성된 인스턴스를 전달받자
- 객체를 생성하는 책임을 객체 내부가 아니라 클라이언트로 옮기는 것에서 시작

<br/>

### 가끔은 생성해도 무방하다

**클래스 안에서 객체의 인스턴스를 직접 생성하는 방식이 유용한 경우 존재**

- 중복 코드나 사용성의 문제
- 해결 방법: 기본 객체를 생성하는 생성자를 추가하고 이 생성자에서 인스턴스를 인자로 받는 생성자를 체이닝하는 것 (Kerievsky)

<br/>

```java
public class Movie {
      private DiscountPolicy discountPolicy;
      
      // ①
      public Movie(String title, Duration runningTime, Money fee) {
            this(title, runningTime, fee, new AmountDiscountPolicy(/*...*/));
      }

      // ②
      public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
            // ...
            this.discountPolicy = discountPolicy;
      }
}
```

- 첫 번째 생성자 (①) 의 내부에서 두 번째 생성자 (②) 를 호출 → 생성자가 체인처럼 연결

<br/>

```java
public class Movie {
      public Money calculateMovieFee(Screening screening) {
            return calculateMovieFee(screening, new AmountDiscountPolicy(...)));
      }
      
      public Money calculateMovieFee(Screening screening,
                                     DiscountPolicy discountPolicy) {
            return fee.minus(discountPolicy.calculateDiscountAmount(screening));
      }
}
```

- 메서드를 오버로딩하는 경우에도 사용할 수 있음

<br/>

### 표준 클래스에 대한 의존은 해롭지 않다

```java
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();
}
```

- `ArrayList`의 코드가 수정될 확률은 0에 가깝기 때문에 인스턴스를 직접 생성하더라도 문제가 되지 않음

<br/>

### 컨텍스트 확장하기

```java
public class Movie {
      private DiscountPolicy discountPolicy;
      
      public Movie(String title, Duration runningTime, Money fee) {
            this(title, runningTime, fee, null);
      }

      public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
            // ...
            this.discountPolicy = discountPolicy;
      }
}
```

- 메서드 내부에서 discountPolicy 의 값이 null 인지 여부를 체크
- 이처럼 코드 내부를 직접 수정하는 것은 버그의 발생 가능성을 높임
- 해결책은 할인 정책이 존재하지 않는다는 사실을 예외 케이스로 처리하지 말고, 존재하지 않는 할인 정책을 할인 정책의 한 종류로 간주하자. 
  - 할인할 금액으로 0 원을 반환하는 NoneDiscountPolicy 클래스를 추가

<br/>

### 조합 가능한 행동

- 유연하고 재사용 가능한 설계: 작은 객체들의 행동을 조합함으로써 새로운 행동을 이끌어낼 수 있는 설계
- 훌륭한 객체지향 설계: 객체들의 조합을 선언적으로 표현함으로써 객체들이 무엇을 하는지를 표현하는 설계

> 시스템에 포함된 객체의 구성을 변경해(절차적인 코드를 작성하기보다는 인스턴스 추가나 제거 또는 조합을 달리해서) 시스템의 작동 방식을 바꿀 수 있다.
> 시스템을 이런 방식으로 구축하면 방법(how)이 아니라 목적(what)에 집중할 수 있어 시스템의 행위를 변경하기가 더 쉽다.
> 
> - Freeman



