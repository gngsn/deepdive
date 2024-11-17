# CHAPTER 3. 단위테스트의 구조

**TL;DR**
- **AAA 패턴**: Arrange, Act, Assert Pattern. _준비 · 실행 · 검증_
  - **① 준비 구절**: SUT과 해당 의존성을 원하는 상태로 만듦
  - **② 실행 구절**: SUT에서 메서드를 호출 · 준비된 의존성을 전달하며 · 출력 값 캡처
  - **③ 검증 구정**: 결과 검증
  - ‘준비 or 실행 or 검증’ 중 여러 번 실행해야 한다면: **여러 동작 단위를 검증한다는 표시** → 여러 개로 분리
  - **구절 표기 주석 제거**: 준비 및 검증 구절에 빈 줄을 추가해야 할 때라면 주석을 유지하고, 아니라면 주석 제거
- AAA 패턴 코드의 크기: **준비 구절 ≥ 실행 구절 + 검증 구절**
  - 준비 구절이 너무 크면 `private method` 나 `factory method`로 도출 가능
- **`if` 문이 있는 단위 테스트**는 안티 패턴
- 실행 구절이 **한 줄 이상인 경우**를 경계하라
  - **불변 위반** invatiant violation: ex. 각기 다른 메서드를 실행하는데 서로의 결과에 의존해 이상한 결과값을 도출되는 것 → 단일한 공개 메서드여야 하는 메서드
  - **캡슐화**: 잠재적인 불변 위반으로부터 코드를 보호하는 것
- SUT의 이름을 <b>`sut`</b>로 지정해 구별해라
- **Test Fixture 재사용 방법**
  1. 안티 패턴: 생성자에서 Test Fixture 초기화
  2. 테스트 클래스에 비공개 팩토리 메서드 _private factory method_ 를 두는 것
- **읽기 쉬운 테스트 이름**: 간단하고 쉬운 영어 명명
  - 엄격한 테스트 명명 정책을 피해라
  - 도메인 전문가에게 시나리오를 설명하는 것처럼 지어라
  - 단어를 밑줄(`_`)로 구분하라
  - 테스트 명 내 테스트 대상 메서드 이름을 포함하지 마라
  - *`should be`* 는 또 다른 안티 패턴 네이밍
- 매개변수화된 테스트 _parameterized test_ 를 통해 유사한 사실을 단일한 메서드로 묶을 수 있음
  - **긍정 케이스 vs. 부정 케이스를 분리**하되, 동작이 너무 복잡하면 사용 금지
- **검증문 라이브러리** 사용: Java에는 `assertJ` 가 대표적

<br/><br/>

---

<br/>


## 3.1 단위 테스트를 구성하는 방법

### 3.1.1 AAA 패턴 사용

AAA 패턴

:: _3A 패턴, 준비-실행-검증._

<br/>

```java
public class CalculatorTests 
{
	public void Sum(double first, double second)
	{
		return first + second;
	}
}
```

```java
public class CalculatorTests {
	[Fact] // 테스트를 나타내는 xUnit 속성
	public void Sum_of_two_numbers()
	{
		// 준비
		double first = 10;
		double second = 20;
		var calculator = new Calculator();

		// 실행
		double result = calculator.Sum(first, second); // 실행 구절

		// 검증
		Assert.equal(30, result);
	}
}
```

- 준비 구절: SUT (테스트 대상 시스템, System Under Test)과 해당 의존성을 원하는 상태로 만듦
- 실행 구절: SUT에서 메서드를 호출하고 준비된 의존성을 전달하며 출력 값을 캡처(있을 때)
- 검증 구정: 결과 검증.
    - 결과: 반환 값 / SUT와 협력자의 최종 상태 / SUT가 협력자에 호출한 메서드 등으로 표시될 수 있음

<br/>

<pre>
📌 <b>Given-When-Then 패턴</b>

- Given: 준비 구절
- When: 실행 구절
- Then: 검증 구절

세 부분으로 나누는 구성 면에서 차이는 없음.
다만, Given-When-Then 구조가 비개발자들이 더 읽기 쉬움.
</pre>

- TDD를 실천할 때,
    - 일반적으로는 순서대로 작성하지만, 검증 구절을 먼저 작성하는 것도 가능
    - 테스트를 통해 기대하는 동작으로 윤곽을 잡은 다음, 제품 코드를 개발할지 아는 것이 좋음
- 테스트 전 제품 코드를 작성한다면,
    - 테스트를 작성할 때 무엇을 예상하는지 이미 알음 → 준비 구절부터 시작하는 것이 좋음

<br/>

### 3.1.2 여러 개의 준비, 실행, 검증 구절 피하기

> 테스트 준비 > 실행 > 검증 > 좀 더 실행 > 다시 검증

여러 개의 실행 구절은 여러 개의 동작 단위를 검증하는 테스트를 의미

이런 테스트는 더 이상 **단위 테스트**가 아닌 **통합 테스트**

<br/>

실행이 하나일 경우,

- 테스트가 단위 테스트 범주에 있음을 보장
- 간단 / 빠름 / 이해하기 쉬움

<br/>

### 3.1.3 테스트 내 if 문 피하기

- `if` 문이 있는 단위 테스트는 안티 패턴
- 단위 테스트나 통합 테스트는 분기가 없는 간단한 일련의 단계여야 함

<br/>

### 3.1.4 각 구절은 얼마나 커야 하는가?

#### 테스트 구절의 크기에 따른 지침

✔️ **준비 구절이 가장 큰 경우**
- `if` 문이 있는 단위 테스트는 안티 패턴
- 준비 구절 ≥ 실행 + 검증 구절
    - 너무 크면 `private method` 나 `factory method`로 도출 가능
- 준비 구절 - 코드 재사용에 도움이 되는 두 패턴: Object Mother, Test Data Builder

<br/>

✔️ **실행 구절이 한 줄 이상인 경우를 경계하라**

실행 구절은 보통 한 줄 → 두 줄 이상인 경우 SUT의 공개 API 문제를 의심하라

<br/>

_한 줄인 경우:_

```java
[Fact]
public void Purchase_succeeds_when_enough_inventory()
{
    // Arrange
    var store = new Store();
    store.AddInventory(Product.Shampoo, 10);
    var customer = new Customer();

    // Act
    **bool success = customer.Purchase(store, Product.Shampoo, 5);**

    // Assert
    Assert.True(success);
    Assert.Equal(5, store.GetInventory(Product.Shampoo));
}
```

<br/>

_두 줄인 경우:_

```java
[Fact]
public void Purchase_succeeds_when_enough_inventory()
{
    // Arrange
    var store = new Store();
    store.AddInventory(Product.Shampoo, 10);
    var customer = new Customer();

    // Act
    **bool success = customer.Purchase(store, Product.Shampoo, 5);
    store.RemoveInventory(success, Product.Shampoo, 5);**

    // Assert
    Assert.True(success);
    Assert.Equal(5, store.GetInventory(Product.Shampoo));
}
```

- **불변 위반** invatiant violation: ex. 각기 다른 메서드를 실행하는데 서로의 결과에 의존해 이상한 결과값을 도출되는 것 → 단일한 공개 메서드여야 하는 메서드
- **캡슐화**: 잠재적인 불변 위반으로부터 코드를 보호하는 것

<br/>

### 3.1.5 검증 구절에는 검증문이 얼마나 있어야 하는가

가능한 가장 작은 코드를 목표로 하는 전제를 기반함

단위 테스트는 ‘코드’의 단위가 아니라 ‘동작’의 단위이기 때문에 도출하는 모든 결과에 대해서 평가하는 것이 좋음.

But 검증 구절이 너무 커지는 것은 경계해야 함

<br/>

### 3.1.6 종료 단계는 어떤가

- SUT는 애플리케이션에서 호출하는 동작에 대한 진입점 제공
- 진입점은 오직 하나만 존재할 수 있음
- SUT를 의존성과 구분하는 것이 좋음
- SUT의 이름을 `sut`로 지정해 구별해라

<br/>

<pre><code lang="java">
public class CalculatorTests 
{
	[Fact]
	public void Sum_of_two_numbers()
	{
		// 준비
		double first = 10;
		double second = 20;
		<b>var sut = new Calculator();</b>

		// 실행
		double result = sut.Sum(first, second);

		// 검증 
		Assert.Equal(30, result);
	}
}
</code></pre>

<br/>

### 3.1.8 준비, 실행, 검증 주석 제거하기

- 어떤 구절에 속한지 ‘주석’을 통해 명시 → 빈 줄로 분리
    - 간결성, 가독성의 균형
    - 준비 및 검증 구절에 빈 줄을 추가해야 할 때라면 주석을 유지하고, 아니라면 주석 제거

<br/>

```java
public class CalculatorTests
{
    [Fact]
    public void Sum_of_two_numbers()
    {
        double first = 10;                        ①
        double second = 20;                       ①
        var sut = new Calculator();               ①

        double result = sut.Sum(first, second);   ②

        Assert.Equal(30, result);                 ③
    }
}
```

① **Arrange**

② **Act**

③ **Assert**

<br/>

## 3.2 xUnit 테스트 프레임워크 살펴보기

```java
public class CalculatorTests : IDisposable
{
    private readonly Calculator _sut;

    public CalculatorTests()       ①
    {                              ①
        _sut = new Calculator();   ①
    }                              ①

    [Fact]
    public void Sum_of_two_numbers()
    {
        /* ... */
    }

    public void Dispose()          ②
    {                              ②
        _sut.CleanUp();            ②
    }                              ②
}
```

① **클래스 내 각 테스트 전 호출**

② **클래스 내 각 테스트 후 호출**

<br/>

단위 테스트를 작성할 때 추천하는 사고방식:

> ‘각 테스트는 이야기가 있어야 한다’


<br/>

## 3.3 테스트 간 테스트 픽스처 재사용

언제 어떻게 테스트에서 코드를 재사용해야 올바른 방법인가

<pre>
<b>📌 Test Fixture</b>
1. <b>테스트 실행 대상 객체</b> : 정규 의존성. <b>SUT로 전달되는 인수</b>.
    - ex. 데이터베이스에 있는 데이터 or 하드 디스크의 파일

2. NUnit 테스트 프레임워크에서 비롯됨
    - NUnit 에서 TestFixture 는 <b>테스트가 포함된 클래스를 표시</b>하는 특성

</pre>

<br/>

<small><i>유지비를 증가시키는 -</i></small>

**Test Fixture 재사용 방법 1.** 생성자에서 Test Fixture 초기화

- **장점**: 테스트 코드 감소. 테스트 픽스처 구성의 대부분 혹은 전부 제거 가능
- **단점** : 테스트 간 결합도 증가 / 테스트 가독성 저하

<br/>

### 3.3.1 테스트 간의 높은 결합도는 안티 패턴

서로 결합된 준비 로직 → 다른 테스트에 영향을 줌

**Before :**

```java
_store.addInventory(Product.shampoo, 10);
```

<br/>

**After :**

```java
_store.addInventory(Product.shampoo, 15);
```

<br/>

테스트 클래스에 **공유 상태**를 두지 말아야 함

```java
// 공유 상태의 예
private readonlu Store _store;
private readonlu Customer _sut;
```

<br/>

### 3.3.2 테스트 가독성을 떨어뜨리는 생성자 사용

테스트만 보고 전체 그림을 보지 못함 → 다른 부분도 봐야 함

<br/>

### 3.3.3 더 나은 테스트 픽스처 재사용법

**Test Fixture 재사용 방법 2.** 테스트 클래스에 비공개 팩토리 메서드 _private factory method_ 를 두는 것

테스트 코드를 짧게 하면서, 테스트 진행 상황에 대한 전체 맥락을 유지할 수 있음

<br/>

테스트 픽스처 재사용 규칙의 한 가지 예외

: 테스트 전부 또는 대부분에 사용되는 생성자에 픽스처를 인스턴스화할 수 있음

<br/>

ex. 종종 DB 연결된 통합 테스트의 경우

but, 기초 생성자 case class를 둬서 개별 클래스 생성자에서 DB 연결을 초기화하는 것이 더 합리적

<pre><code>
public class CustomerTests : IntegrationTests {
	[Fact]
	public void Purchase_succeeds_when_enough_inventory() {
		/* 여기서 _database 사용 */
	}
}

public abstract class IntegrationTests : IDisposable {
	protected readonly Database _database;

	<b>protected IntegrationTests() {
		_database = new Database();
	}</b>

	public void Dispose() {
		_database.Displose();
	}
}
</code></pre>

## 3.4 단위 테스트 명명법

표현력있는 테스트 이름이 중요.

올바른 명칭은 테스트가 검증하는 내용과 기본 시스템의 동작을 이해하는 데 도움이 됨

가장 유명하지만 도움되지 않는 방법 중 하나:

<pre>
📌 <b>[테스트 대상 메서드]_[시나리오]_[예상 결과]</b>

<b>테스트 대상 메서드</b> : 테스트 중인 메서드의 이름
<b>시나리오</b> : 메서드를 테스트 하는 조건
<b>예상 결과</b> : 현재 시나리오에서 테스트 대상 메서드에 기대하는 것
</pre>

- 테스트가 정확히 무엇을 검증하는지, 비즈니스 요구 사항과 어떤 관련이 있는지 파악하려면 머리를 더 써야 함

**간단하고 쉬운 영어 구문이 훨씬 더 효과적**

- 엄격한 구조에 얽매이지 않고 표현력이 뛰어남
- 간단한 문구로 동작 설명 가능

<br/>

```java
public class CalculatorTests {

	[Fact]
	public void Sum_of_two_numbers() {
		double first = 10;
		double second = 20;
		var sut = new Calculator();

		double result = sut.Sum(first, second);

		Assert.Equal(30, result);
	}
}
```

*✍🏻 메서드 명에 Success / Fail 나타내는 게 좋을듯*

**`[테스트 대상 메서드]_[시나리오]_[예상 결과]`** 방식이면?

```java
public void Sum_twoNumbers_ReturnsSum()
```

- **테스트 대상 메서드**: Sum
- **시나리오**: 숫자 두 개가 포함
- **예상 결과**: 두 수의 합

<br/>

### 3.4.1 단위 테스트 명명 지침

**읽기 쉬운 테스트 이름**

- 엄격한 명명 정책을 따르지 마라
    - 복잡한 동작에 대한 설명에 대한 어려움이 있으니, 표현의 자유를 허용하라
- 도메인 전문가에게 시나리오를 설명하는 것처럼 지어라
    - 문제 도메인에 익숙한 비개발자나 비즈니스 분석자 등
- 단어를 밑줄(`_`)로 구분하라
    - 긴 이름에 가독성을 향상시킨다

<br/>

**테스트 클래스 이름**

- `[클래스명]Tests` 으로 짓지만, 해당 클래스만 검증하는 것으로 제한하는 것은 아님
- 단위 테스트는 동작의 단위지 클래스의 단위가 아니기 때문
- 동작 단위로 검증할 수 있는 진입점 또는 API로 여겨라

<br/>

### 3.4.2 예제: 지침에 따른 테스트 이름 변경

_엄격한 정책으로 명명된 테스트 :_

```csharp
[Fact]
public void IsDeliveryValid_InvalidDate_ReturnsFalse()
{
    DeliveryService sut = new DeliveryService();
    DateTime pastDate = DateTime.Now.AddDays(-1);
    Delivery delivery = new Delivery
    {
        Date = pastDate
    };

    bool isValid = sut.IsDeliveryValid(delivery);

    Assert.False(isValid);
}
```

<br/>

쉬운 영어로 변경했을 때의 이름:

```java
public void Delivery_with_invalid_date_should_be_considered_invalid()
```

<br/>

**특징**

- 프로그래머가 더 잘 이해할 뿐만 아니라, 프로그래머가 아닌 사람들에게 납득
- SUT 메서드 이름이 더 이상 포함되지 않음

<br/>

<pre>
📌 <b>테스트 명 내 테스트 대상 메서드 이름을 포함하지 마라</b>

- 코드를 테스트하는 것이 아니라 애플리케이션 동작을 테스트 하는 것
- SUT는 동작을 호출하는 수단인 진입점일 뿐
- 동작 대신 코드를 목표로 하면 해당 코드의 구현 세부 사항과 테스트 간의 결합도가 높아짐 → 테스트 스위트의 유지 보수성에 부정적

<b>유일한 예외</b>
유틸리티 코드 → 비즈니스 로직이 없고, 단순한 보조 기능. SUT 메서드 이름을 사용해도 됨
</pre>

<br/>

더 개선해보자

```java
public void Delivery_with_past_date_should_be_considered_invalid()
```

<br/>

Better but still **not ideal**

```java
public void Delivery_with_past_date_should_be_invalid()
```

<br/>

*`should be`* 는 또 다른 안티 패턴: 동작의 단순하고 원자적인 사실일 뿐 소망·욕구가 들어가지 않음

<br/>

```java
public void Delivery_with_past_date_is_invalid()
```

<br/>

관사를 붙이면 이해에 더 도움이 됨. 기초 영문법을 지키지 않을 이유가 없음

```java
public void Delivery_with_a_past_date_is_invalid()
```

<br/>

## 3.5 매개변수화된 테스트 리팩터링하기

대부분의 단위 테스트 프레임워크는 매개변수화된 테스트 parameterized test를 사용해 유사한 테스트를 묶을 수 있는 기능 제공

- 동작의 복잡도가 클수록, 테스트로 표현할 사실들이 그만큼 필요함
- 매개변수화된 테스트를 사용하면 유사한 사실을 단일한 메서드로 묶을 수 있음

<br/>

```java
public void Delivery_with_a_past_date_is_invalid()
public void Delivery_for_today_is_invalid()
public void Delivery_for_tomorrow_is_invalid()
public void The_soonest_delivery_date_is_two_days_from_now()
```

- 위 네 개의 테스트 메서드는 날짜만 다른 동일한 테스트
- 매개변수화된 테스트 parameterized test 로 묶어보자

<br/>

```java
public class DeliveryServiceTests
{
    [InlineData(-1, false)]                            1
    [InlineData(0, false)]                             1
    [InlineData(1, false)]                             1
    [InlineData(2, true)]                              1
    [Theory]
    public void Can_detect_an_invalid_delivery_date(
        int daysFromNow,                               2
        bool expected)                                 2
    {
        DeliveryService sut = new DeliveryService();
        DateTime deliveryDate = DateTime.Now
            .AddDays(daysFromNow);                     3
        Delivery delivery = new Delivery
        {
            Date = deliveryDate
        };

        bool isValid = sut.IsDeliveryValid(delivery);

        Assert.Equal(expected, isValid);               3
    }
}
```

① `InlineData` 는 테스트 메서드에 입력 값 집합을 보냄. 각 라인은 별개의 사실을 나타냄

② `InlineData` 에서 명시한 값 넘어옴

③ 파라미터 사용

<br/>

- **장점**: 코드의 양이 크게 줄어듦
- **단점**: 가독성이 떨어짐

**절충안**

긍정 케이스 vs. 부정 케이스를 분리하여 중요한 부분을 잘 나타내는 이름을 쓰면 좋음

```java
public class DeliveryServiceTests
{
    [InlineData(-1)]
    [InlineData(0)]
    [InlineData(1)]
    [Theory]
    public void Detects_an_invalid_delivery_date(int daysFromNow)
    {
        /* ... */
    }

    [Fact]
    public void The_soonest_delivery_date_is_two_days_from_now()
    {
        /* ... */
    }
}
```

- 입력 매개변수 만으로 테스트 케이스를 판단할 수 있다면 긍정/부정 모두 하나의 메서드로 두는 것이 좋음
- 동작이 너무 복잡하면 사용 금지

<br/>

### 3.5.1 매개변수화된 테스트를 위한 데이터 생성

런타임에 의존하는 데이터를 사용하지 않도록 주의하라

<br/>

## 3.6 검증문 라이브러리를 사용한 테스트 가독성 향상

검증문 라이브러리를 사용

- 장점: 검증문을 재구성해 가독성을 높일 수 있음
- 단점: 프로젝트에 의존성을 추가해야 함

Java에는 `assertJ` 가 대표적

<br/><br/>
