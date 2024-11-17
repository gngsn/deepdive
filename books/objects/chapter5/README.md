# CHAPTER 05. 책임 할당하기

**TL;DR**
- GRASP Pattern: General Responsibility Assignment Software Pattern, 책임 할당을 위한 소프트웨어 패턴
  - : 책임을 수행하는 데 필요한 메시지를 결정하고, 책임을 수행할 정보 전문가에게 책임을 할당하라
  - **INFORMATION EXPERT 패턴**: 책임을 정보 전문가(책임을 수행하는 데 필요한 정보를 가지고 있는 객체)에게 할당하라
  - **LOW COUPLING 패턴**: 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라
  - **HIGH COHESION 패턴**: 높은 응집도를 유지할 수 있게 책임을 할당하라
  - **CREATOR 패턴**: 연결되거나 관련될 필요가 있는 객체에게 객체 생성 책임을 할당하라 (잘 알고 있거나/어차피 사용해야 하는 객체)
  - **POLYMORPHISM 패턴**: 타입을 명시적으로 정의하고 각 타입에 다형적으로 행동하는 책임을 할당하라
  - **PROTECTED VARIATIONS 패턴**: 변화가 예상되는 불안정한 지점들을 식별하고 그 주위에 안정된 인터페이스를 형성하도록 책임을 할당하라
- 리팩터링을 고려할 시점 2가지
    - 클래스의 속성이 서로 다른 시점에 초기화되거나 일부만 초기화된다는 것은 응집도가 낮다는 증거
    - 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다는 증거
- 주석을 추가하는 대신 메서드를 작게 분해해서 각 메서드의 응집도를 높여라
    - Benefit: 재활용될 확률 증가 / 메소드 이름으로 주석을 읽는 느낌을 줌 / 오버라이딩하기 용이
- 처음부터 책임 주도 설계 방법을 따르는 것보다 동작하는 코드를 작성한 후, 리팩터링하는 것이 더 훌륭한 결과물을 낳을 수도 있음


## 01. 책임 주도 설계를 향해

### GRASP Pattern

배경
: 책임에 초점을 맞춰서 설계할 때 어려운 점: 어떤 객체에게 어떤 책임을 할당할지를 결정하기가 쉽지 않음. 책임 할당 과정은 일종의 트레이드오프 활동

목적

: - 책임 할당의 어려움을 해결하기 위한 답을 제시 
: - 응집도와 결합도, 캡슐화 같은 다양한 기준에 따라 책임을 할당하고 결과를 트레이드오프할 수 있는 기준을 배울 수 있음

<br/><br/>

## 02. 책임 할당을 위한 GRASP 패턴

#### GRASP
 
_General Responsibility Assignment Software Pattern_. 

- 책임 할당을 위한 소프트웨어 패턴
- 객체에게 책임을 할당할 때, 지침으로 삼을 수 있는 **원칙들의 집합을 패턴 형식으로 정리**한 것

<br/>

### 02-1. 도메인 개념에서 출발하기

- 설계 출발점: 개념 정리를 빠르게 마치고 설계와 구현을 진행하라
- 개념들의 의미와 관계가 정확하거나 완벽할 필요는 없음
- 책임을 할당받을 객체들의 종류와 관계에 대한 유용한 정보를 제공할 수 있으면 충분

<pre>
<b>📌 올바른 도메인 모델이란 존재하지 않는다.</b> 

도메인 모델이 구현을 염두에 두고 구조화되는 것이 바람직하다.
반대로 코드의 구조가 도메인을 바라보는 관점을 바꾸기도 한다.

필요한 것은 도메인을 그대로 투영한 모델이 아니라 구현에 도움이 되는 모델이다. 
실용적이면서도 유용한 모델이 답이다.
</pre>

<br/>

### 02-2. 정보 전문가에게 책임을 할당하라

**영화 예매 시스템의 책임은?**

👉🏻 영화를 예매하는 것: 애플리케이션은 영화를 예매할 책임이 있음

<br/>

#### [ 책임을 수행하는 데 필요한 메시지를 결정 ]

메시지는 메시지를 수신할 객체가 아니라 **메시지를 전송할 객체의 의도를 반영해서 결정**.
즉, 아래와 같은 질문을 던질 수 있음

> 메시지를 전송할 객체는 무엇을 원하는가?
> 
> 👉🏻 **①** "영화를 예매하라"

<br/>

#### [ 책임을 수행할 정보 전문가에게 책임을 할당 ]

<pre>
<b>📌 INFORMATION EXPERT 패턴</b> 
책임을 정보 전문가(책임을 수행하는 데 필요한 정보를 가지고 있는 객체)에게 할당하라

- 객체는 자율적인 존재이며, 정보를 알고 있는 객체만이 책임을 어떻게 수행할지 결정할 수 있다.
- 필요한 정보를 가진 객체들로 책임이 분산 
 👉🏻 높은 응집도, 이해도 상승
 👉🏻 결과적으로 결합도가 낮아져서 간결하고 유지보수하기 쉬운 시스템을 구축
</pre>

**②** 영화 예매를 위한 정보 전문가 → 상영 (Screening)

**Screening**

- "예매하라" 메시지를 처리하기 위해 필요한 절차와 구현을 고민
- 스스로 처리할 수 없다면 외부에 도움을 요청 → 외부로 전송해야 하는 새로운 메시지가 되고, 최종적으로 이 메시지가 새로운 객체의 책임으로 할당
  - 영화의 가격 계산 필요: 예매 가격을 계산하는 작업이 필요. => 영화 한 편 가격 X 인원수 
  - Screening은 가격을 계산 시 필요한 정보를 모름 → 외부 객체에게 도움을 요청
  - > 새로운 메세지: **③** 가격을 계산하라

**👉🏻 위의 연쇄적인 메시지 전송과 수신을 통해 협력 공동체가 구성됨**


가격을 계산할 **④ Movie 객체에 책임 할당**

> **⑤** 할인 여부를 판단하라

할인이 가능한지 확인할 **⑥ Discount Condition 객체에게 책임 할당**

<br/><br/>

### 02-3. 높은 응집도와 낮은 결합도

- 트레이드 오프의 일환으로, 책임 할당의 대안들이 다양하게 존재한다면 **응집도**와 **결합도**의 측면에서 더 나은 대안을 선택하라.
- GRASP에서는 LOW COUPLING / HIGH COHESION 패턴이라고 부른다.
- LOW COUPLING / HIGH COHESION는 책임과 협력의 품질을 검 토하는 데 사용할 수 있는 중요한 평가 기준이다.

<pre>
<b>📌 LOW COUPLING 패턴</b> 
: 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라
</pre>

- Movie와 DiscountCondition은 이미 결합 (Movie가 조건 목록을 속성으로 포함)
- Screening과 DiscountCondition은 협력 시 새로운 결합도 추가 필요

👉🏻 LOW COUPLING 패턴의 관점에서 Screening보다는 Movie가 DiscountCondition과 협력하는 것이 더 나은 설계 대안이다.


<pre>
<b>📌 HIGH COHESION 패턴</b> 
: 높은 응집도를 유지할 수 있게 책임을 할당하라
</pre>


할인 여부 책임을 Screening이 갖는다면
- DiscountCondition의 할인 여부를 판단 + Movie가 이 할인 여부를 필요
- 할인 여부에 대한 Screening의 두 가지 책임이 부여
=> 계산하는 방식이 변경될 경우 Screening 도 함께 변경해야 한다. 

할인 여부 책임을 Screening이 갖는다면
- Movie의 주된 책임은 영화 요금을 계산
- Movie의 할인 조건 판단은 응집도에 아무런 해도 끼치지 않음

👉🏻 HIGH COHESION 패턴의 관점에서 Screening보다는 Movie가 DiscountCondition과 협력하는 것이 더 나은 설계 대안이다.

<br/><br/>

### 02-4. 창조자에게 객체 생성 책임을 할당하라

<pre>
<b>📌 CREATOR 패턴</b> 
: 객체 A를 생성할 때 아래 조건을 최대한 많이 만족하는 B에게 객체 생성 책임을 할당하라
- B가 A 객체를 포함하거나 참조한다.
- B가 A 객체를 기록한다.
- B가 A 객체를 긴밀하게 사용한다.
- B가 A 객체를 초기화하는 데 필요한 데이터를 가지고 있다(이 경우 B는 A에 대한 정보 전문가)

CREATOR 패턴의 의도는 생성되는 객체와 연결되거나 관련될 필요가 있는 객체에 해당 객체를 생성할 책임을 맡기는 것
-> 연결되거나 관련될 필요가 있는 객체? 잘 알고 있거나 / 사용해야 하는 객체
</pre>

✔️ 이미 결합돼 있는 객체에게 생성 책임을 할당하는 것은 설계의 전체적인 결합도에 영향을 미치지 않기 때문에 **설계가 낮은 결합도를 유지**할 수 있게 한다.

- 영화 예매 협력의 최종 결과물인 Reservation 인스턴스를 생성해야 함 (객체 B)
- 예매 정보를 생성하는 데 필요한 정보 전문가 -> Screening: Screening을 Reservation의 CREATOR로 선택하는 것이 적절

✔️ 협력과 책임이 제대로 동작하는지 확인할 수 있는 유일한 방법은 **코드를 작성하고 실행해 보는 것 뿐**

<br/><br/>

## 03. 구현을 통한 검증

_코드 추가: version 1_

### 03-1. DiscountCondition 개선하기

- **새로운 할인 조건 추가**
  - if ~ else 문 수정 필요
  - 새로운 할인 조건이 요구되면 DiscountCondition 속성 변경 필요
- **순번 조건을 판단하는 로직 변경**
  - isSatisfiedBySequence 내부 구현 변경 필요
  - 새로운 순번 조건이 요구되면 DiscountCondition 속성 변경 필요
- **기간 조건을 판단하는 로직이 변경된 경우**
  - isSatisfiedByPeriod 내부 구현 변경 필요
  - 새로운 기간 조건이 요구되면 DiscountCondition의 dayOfWeek, startTime, endTime 속성 변경 필요

<br/>

#### 변경의 이유에 따라 클래스를 분리
: 설계를 개선할 때에는 변경의 이유가 하나 이상인 클래스를 찾는 것으로부터 시작하는 것이 좋다.

**코드를 통해 변경의 이유를 파악할 수 있는 방법**
1. 인스턴스 변수가 초기화되는 시점을 살펴보는 것
   - 일부만 초기화하고 일부는 초기화되지 않은 상태로 남겨진다
   - 클래스의 속성이 서로 다른 시점에 초기화되거나 일부만 초기화된다는 것은 응집도가 낮다는 증거
   - **함께 초기화되는 속성을 기준으로 코드를 분리**
2. 메서드들이 인스턴스 변수를 사용하는 방식 을 살펴보는 것
   - 모든 메서드가 객체의 모든 속성을 사용한다면 클래스의 응집도는 높다
   - 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다
     - isSatisfiedBySequence(): sequence만 사용 
     - isSatisfiedByPeriod()  : dayOfWeek, startTime, endTime만 사용
   - **속성 그룹과 해당 그룹에 접근하는 메서드 그룹을 기준으로 코드를 분리**

<br/>

### 03-2. 타입 분리하기

_코드 수정: version 2_

1. 위의 두 문제를 DiscountCondition -> PeriodCondition, SequenceCondition으로 분리
2. 하지만, Movie의 결합도가 그만큼 높아짐
3. 추상화를 사용하자 DiscountCondition Interface 생성 & 구현
-> 이 변화에 Movie는 아무런 변화가 없음
4. Movie는 어떤 DiscountCondition 구현체이든 isSatisfiedBy() 만 호출 -> Movie와 DiscountCondition 사이의 협력은 다형적

<br/>

### 03-3. 다형성을 통해 분리하기

GRASP 에서의 POLYMORPHISM (다형성) 패턴: 객체의 타입에 따라 변하는 행동이 있다면 타입을 분리하고 변화하는 행동을 각 타입의 책임으로 할당

<pre>
<b>📌 POLYMORPHISM 패턴</b> 
: 타입을 명시적으로 정의하고 각 타입에 다형적으로 행동하는 책임을 할당하라

- 객체의 타입을 검사해서 타입에 따라 여러 대안들을 수행하는 조건적인 논리를 사용하지 말라고 경고
- 다형성을 이용해 새로운 변화를 다루기 쉽게 확장하라고 권고
</pre>

<br/>

### 03-4. 변경으로부터 보호하기

DiscountCondition이라는 역할이 Movie 로부터 PeriodCondition과 SequenceCondition 의 존재를 감춤

GRASP 에서의 PROTECTED VARIATIONS(변경 보호) 패턴: 변경을 캡슐화하도록 책임을 할당하는 것

<pre>
<b>📌 PROTECTED VARIATIONS 패턴</b> 
: 변화가 예상되는 불안정한 지점들을 식별하고 그 주위에 안정된 인터페이스를 형성하도록 책임을 할당하라

- "설계에서 변하는 것이 무엇인지 고려하고 변하는 개념을 캡슐화하라 - GOF"
- 변경이 될 가능성이 높은가? 그렇다면 캡슐화하라
</pre>

<br/>

### 03-5. Movie 클래스 개선하기

_코드 수정: version 3_
 
- 금액 할인 정책과 관련된 클래스: AmountDiscountMovie
- 비율 할인 정책과 관련된 클래스: PercentDiscountMovie
- 할인 정책을 적용하지 않는 클래스: NoneDiscountMovie

<br/>

### 03-5. 변경과 유연성

**문제점**
- 할인 정책을 구현하기 위해 Movie를 상속 받음 -> 영화의 할인 정책을 변경을 위해서 새로운 인스턴스를 생성 & 필요한 정보를 복사해야 함
- 변경 전후의 인스턴스가 개념적으로는 동일한 객체를 가리키지만 물리적으로 서로 다른 객체

-> 해결 방법: **합성**

Movie의 할인 정책을 DiscountPolicy 로 분리한 후 Movie에 합성시키면 유연한 설계 완성됨

=> 2 장에서 살펴본 영화 예매 시스템의 전체 구조

<br/>

## 04. 책임 주도 설계의 대안

<br/>

### 04-1. 메서드 응집도

길이가 너무 길고 이해하기도 어려운 ReservationAgency의 `reserve()` 메서드

[ReservationAgency.java](./demo/src/main/java/com/gngsn/chapter5/v1/ReservationAgency.java)

> **몬스터 메서드 _monster method_**
> 
> 긴 메서드는 응집도가 낮기 때문에 이해하기도 어렵고 재사용하기도 어려우며 변경하기도 어렵운 메서드 
> 
> _- Michael Feathers_

<br/>

주석을 추가하는 대신 메서드를 작게 분해해서 각 메서드의 응집도를 높여라

> "**짧고 이해하기 쉬운 이름으로 된 메서드**"를 사용하는 이유
> 
> - 첫째, 다른 메서드에서 사용될 확률이 높아진다.
> - 둘째, 고수준의 메서드에서 볼 때 주석을 읽는 것 같게 한다.
> - 또한, 오버라이딩하기 훨씬 쉽다.
> 
> _- Fowler_

ReservationAgency 를 응집도 높은 메서드들로 잘게 분해

```java
public class ReservationAgency {

    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        boolean discountable = checkDiscountable(screening);
        Money fee = calculateFee(screening, discountable, audienceCount);
        return createReservation(screening, customer, audienceCount, fee);
    }
    
    //...
}
```

<br/>

### 04-2. 객체를 자율적으로 만들자

어떤 데이터를 사용하는지를 가장 쉽게 알 수 있는 방법은 메서드 안에서 어떤 클래스의 접근자 메서드를 사용하는지 파악하는 것

- 처음부터 책임 주도 설계 방법을 따르는 것보다 동작하는 코드를 작성한 후, 리팩터링하는 것이 더 훌륭한 결과물을 낳을 수도 있다.
- 캡슐화, 결합도, 응집도를 이해하고 훌륭한 객체지향 원칙을 적용하기 위해 노력한다면 책임 주도 설계 방법을 단계적으로 따르지 않더라도 유연하고 깔끔한 코드를 얻을 수 있을 것

<br/><br/>

**후기**

책임 할당을 통한 객체들의 협력을 어떻게 구현할 수 있는지 구체적인 예시를 통해 가이드한다. 
가령, 영화 예매 시스템을 고려할 때 어떤 객체가 "정보 책임자"인지, 해당 객체가 왜 정보 책임자인지에 대한 이유부터 시작한다. 
Reservation 을 생성할 책임은 예약에 대한 정보를 가장 많이 담고 있는 Screening에게 할당하는 것이 적절해보이는 타당성을 제시하며 독자를 이해시킨다.
합리적이고 납득가능한 설명으로 객체지향적인 개발을 어떻게 할 수 있는지에 대해 제시하며 책임 주도 개발을 코드로 직접 느낄 수 있었다.



**스터디 논의사항**

- interface와 abstract 차이? 
  - 상수 인터페이스 안티패턴 -> 사용 금지 (인터페이스를 잘못 사용한 예)
  - interface static final 상수 값이 자식 클래스로 구현되었을 경우, 상수가 오염된다.
  - ```java
    public interface Example {
        String message = "STATIC MESSAGE"; 
    }
    ```
  - ```java
    public class ImplementExample implements Example {
    public String message;
    
        public String getMessage() {
            return message;
        }
    
        public void setMax(String message) {
            this.message = message;
        }
    }
    ```
  - 가령, 위의 코드를 보면 Example 에서 message는 interface에 의해 `static final String message = "STATIC MESSAGE";` 로 정의되었음에도, 구현에 의해 message 필드는 `setter`를 통해 오염될 수 있게된다.


<br/><br/>

**후기**

책임 할당을 통한 객체들의 협력을 어떻게 구현할 수 있는지 구체적인 예시를 통해 가이드한다.
가령, 영화 예매 시스템을 고려할 때 어떤 객체가 "정보 책임자"인지, 해당 객체가 왜 정보 책임자인지에 대한 이유부터 시작한다.
Reservation 을 생성할 책임은 예약에 대한 정보를 가장 많이 담고 있는 Screening에게 할당하는 것이 적절해보이는 타당성을 제시하며 독자를 이해시킨다.
합리적이고 납득가능한 설명으로 객체지향적인 개발을 어떻게 할 수 있는지에 대해 제시하며 책임 주도 개발을 코드로 직접 느낄 수 있었다.


