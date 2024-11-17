# CHAPTER 11. 단위 테스트 안티 패턴

**TL;DR**
- 비공개 메서드의 단위 테스트는 세부 구현에 결합되고, 결국 리팩터링 내성이 떨어짐
- 비공개 메서드를 직접 테스트하는 대신, 식별할 수 있는 동작을 통해 간접적으로 테스트하라
- 두 가지 불필요한 비공개 메서드 커버리지
  - **죽은 코드**
    - 코드가 어디에도 사용되지 않는다면 삭제
  - **추상화 누락**
    - 비공개 메서드가 복잡해서 공개 API를 통해 테스트하기 어렵다면, 추상화가 누락됐다는 징후
    - 공개로 변경 하지 말고, **해당 코드를 추상화를 통해 별도 클래스로 추출하라**
- 단위 테스트를 위해 비공개 메서드를 공개로 변경하지 마라
- **비공개 메서드를 절대 테스트하지 말라는 규칙**에도 **예외가 존재**
  - ORM: 공개 생성자가 필요없음, 비공개 생성자로 잘 작동
- 테스트를 작성할 때 **특정 구현을 암시하지 말라**
  - **블랙박스 관점**에서 제품 코드를 검증하라
  - **도메인 지식을 테스트에 유출하지 않도록 하라**
- **코드 오염**: 테스트를 위한 제품 코드를 추가하는 것 → 안티 패턴
  - 테스트 코드와 제품 코드가 섞임
  - 제품 코드 유지비 증가
- 구체 클래스를 목으로 처리해야 하면, 이는 단일 책임 원칙을 위반 하는 결과
  - **해당 클래스를 두 가지 클래스로 분리**: 도메인 로직이 있는 클래스와 프로세스 외부 의존성과 통신하는 클래스
- 현재 시간을 ambient context로 하면 제품 코드가 오염되고 테스트하기가 더 어려워짐
  - 서비스나 일반 값의 명시적인 의존성으로 시간을 주입하라


<br/><br/>

---

<br/>

## 1. 비공개 메서드 단위 테스트

### 1.1 비공개 메서드와 테스트 취약성

비공개 메서드를 노출하면 테스트가 구현 세부사항과 결합되고 결과적으로 리팩터링 내성이 떨어짐

<small>4대 요소 되짚어보기: 회귀방지, 리팩터링내성, 빠른피드백, 유지보수성</small>

비공개 메서드를 직접 테스트하는 대신, 포괄적인 식별할 수 있는 동작으로서 간접적으로 테스트하는 것이 좋음

<br/>

### 1.2 비공개 메서드와 불필요한 커버리지

- **죽은 코드**
  - 테스트에서 벗어난 코드가 어디에도 사용되지 않는다면, 리팩터링 후에도 남아서 관계없는 코드일 수 있음
  - 삭제하는 것이 좋음
- **추상화 누락**
  - 비공개 메서드가 너무 복잡해서 클래스의 공개 API를 통해 테스트하기 어렵다면, 별도의 클래스로 도출해야 하는 추상화가 누락됐다는 징후

```csharp
public class Order {
    private Customer _customer;
    private List<Product> _products;

    public string GenerateDescription() {
        return $"Customer name: {_customer.Name}, " +
            $"total number of products: {_products.Count}, " +
            $"total price: {GetPrice()}";                       // 복잡한 비공개 메서드를 간단한 공개 메서드에서 사용
    }

    private decimal GetPrice() {                                // 복잡한 비공개 메서드
        decimal basePrice = /* Calculate based on _products */;
        decimal discounts = /* Calculate based on _customer */;
        decimal taxes = /* Calculate based on _products */;
        return basePrice - discounts + taxes;
    }
}
```

1. `GenerateDescription()` method

- 매우 간단, 주문 내용 출력
- `GetPrice()` 사용

2. `GetPrice()` method

- 중요한 비즈니스 로직이 있기 때문에 테스트를 철저히 해야함
- 이 로직은 추상화 누락
- GetPrice메서드를 노출하기 보다는 다음 예제와 같이추상화를 별도의 클래스로 도출해서 명시적으로 작성하는 것이 좋음

```csharp
public class Order {
    private Customer _customer;
    private List<Product> _products;

    public string GenerateDescription() {
        var calc = new PriceCalculator();

        return $"Customer name: {_customer.Name}, " +
            $"total number of products: {_products.Count}, " +
            $"total price: {calc.Calculate(_customer, _products)}";
    }
}

public class PriceCalculator {
    public decimal Calculate(Customer customer, List<Product> products) {
        decimal basePrice = /* Calculate based on products */;
        decimal discounts = /* Calculate based on customer */;
        decimal taxes = /* Calculate based on products */;
        return basePrice - discounts + taxes;
    }
}
```

- `Order` 와 별개로 `PriceCalculator`를 테스트할 수 있음
- `PriceCalculator` 에는 숨은 입출력이 없으므로 출력 기반 (함수형) 스타일의 단위테스트 가능

<br/>

### 1.3 비공개 메서드 테스트가 타당한 경우

**비공개 메서드를 절대 테스트하지 말라는 규칙**에도 **예외가 존재**

코드의 공개 여부와 목적의 관계는 아래와 같음

|        | 식별할 수 있는 동작 | 구현 세부 사항 |
| ------ | ------------------- | -------------- |
| 공개   | 좋음                | 나쁨           |
| 비공개 | 해당없음            | 좋음           |

- 식별할 수 있는 동작을 공개로 하고 구현 세부사항을 비공개로 하면 API가 잘 설계됐다고 할 수 있음
- 구현 세부사항이 유출되면 코드 캡슐화를 해침

- 비공개 메서드를 테스트하는 것 자체는 나쁘지 않음
- 비공개 메서드가 구현 세부사항의 프록시에 해당하므로 나쁜 것
- 구현 세부사항을 테스트하면 궁극적으로 테스트가 깨지기 쉽기 때문

<br/>

_신용 조회를 관리하는 시스템_

```csharp
public class Inquiry {
    public bool IsApproved { get; private set; }
    public DateTime? TimeApproved { get; private set; }

    private Inquiry(bool isApproved, DateTime? timeApproved) {  // 비공개 생성자
        if (isApproved && !timeApproved.HasValue)
            throw new Exception();

        IsApproved = isApproved;
        TimeApproved = timeApproved;
    }

    public void Approve(DateTime now) {
        if (IsApproved)
            return;

        IsApproved = true;
        TimeApproved = now;
    }
}
```

ORM은 공개 생성자가 필요하지 않으며, 비공개 생성자로 잘 작동할 수 있음

- 승인 로직은 분명히 중요하므로 단위 테스트를 거쳐야 함 vs 생성자를 공개하는 것은 비공개 메서드를 노출하지 않는 규칙 위반

<br/>

**Inquiry 생성자는** 비공개**이면서** 식별할 수 있는 동작**인 메서드의 예시**

- Inquiry 생성자를 공개한다고해서 테스트가 쉽게 깨지지는 않음
  - 실제로 클래스 API가 잘 설계된 API에 가까워지는 것임은 분명
- 생성자가 캡슐화를 지키는 데 필요한 전제 조건이 모두 포함 돼 있는지 확인하라

<br/>

## 2. 비공개 상태 노출

또 다른 안티 패턴: 단위 테스트 목적으로만 비공개 상태 노출

```csharp
public class Customer {
    private CustomerStatus _status = CustomerStatus.Regular;  // 비공개 상태

    public void Promote() {
        _status = CustomerStatus.Preferred;
    }

    public decimal GetDiscount() {
        return _status == CustomerStatus.Preferred ? 0.05m : 0m;
    }
}

public enum CustomerStatus {
    Regular,
    Preferred
}
```

Customer Class: 고객은 각각 Regular 상태로 생성된 후, 모든 항목에 5% 할인 상태의 Preferred로 업그레이드 가능

Promote() 메서드 테스트 방법: 제품 코드가 이 코드를 어떻게 사용하는지 대신 살펴보는 것

- 새로 생성된 고객은 할인이 없음
- 업그레이드 시 5% 할인율 적용

⚠️ 테스트 유의성을 위해 공개 API 노출 영역을 넓히는 것은 좋지 않은 관습

<br/>

## 3. 테스트로 유출된 도메인 지식

- 도메인 지식을 테스트로 유출하는 것은 또 하나의 흔한 안티 패턴
- 복잡한 알고리즘을 다루는 테스트에서 발생

아래는 제품 코드를 테스트하기 위해 테스트 코드에 복사하는 안티 패턴을 불러오는 예시

```csharp
public static class Calculator {
    public static int Add(int value1, int value2) {
        return value1 + value2;
    }
}
```

위 코드를 테스트하기 위해서 아래와 같은 테스트 코드를 작성할 수 있음

👉🏻 단순히 제품 코드를 복붙

```csharp
public class CalculatorTests {
    [Fact]
    public void Adding_two_numbers()
    {
        int value1 = 1;
        int value2 = 3;
        int expected = value1 + value2;     // 유출

        int actual = Calculator.Add(value1, value2);

        Assert.Equal(expected, actual);
    }
}
```

몇 가지 테스트를 추가하기 위해 아래와 같이 매개변수화 할 수도 있음

```csharp
public class CalculatorTests {
    [Theory]
    [InlineData(1, 3)]
    [InlineData(11, 33)]
    [InlineData(100, 500)]
    public void Adding_two_numbers(int value1, int value2) {
        int expected = value1 + value2;       // 유출

        int actual = Calculator.Add(value1, value2);

        Assert.Equal(expected, actual);
    }
}
```

이는 리팩터링 내성 지표가 거의 0

\[개선\] 도메인 지식을 포함하지 않는 테스트:

```csharp
public class CalculatorTests {
    [Theory]
    [InlineData(1, 3, 4)]
    [InlineData(11, 33, 44)]
    [InlineData(100, 500, 600)]
    public void Adding_two_numbers(int value1, int value2, int expected) {
        int actual = Calculator.Add(value1, value2);
        Assert.Equal(expected, actual);
    }
}
```

단위 테스트에서는 예상 결과를 하드코딩하는 것이 좋음

<br/>

## 4. 코드 오염

✔️ 코드 오염은 테스트에만 필요한 제품 코드를 추가하는 것

```csharp
public class Logger {
    private readonly bool _isTestEnvironment;

    public Logger(bool isTestEnvironment) {    // 스위치
        _isTestEnvironment = isTestEnvironment;
    }

    public void Log(string text) {
        if (_isTestEnvironment)                // 스위치
            return;

        /* Log the text */
    }
}

public class Controller {
    public void SomeMethod(Logger logger) {
        logger.Log("SomeMethod is called");
    }
}
```

운영 환경에서 실행되는지 여부 체크 로직 포함

코드 오염의 문제: 테스트 코드와 제품 코드가 혼재돼 유지비가 증가하는 것

\[개선\] Logger와 ILogger 인터페이스를 구현

```csharp
public interface ILogger {
    void Log(string text);
}


public class Logger : ILogger {      // ① 제품 코드
    public void Log(string text) {   // ①
        /* Log the text */           // ①
    }                                // ①
}                                    // ①

public class FakeLogger : ILogger {  // ② 테스트 코드
    public void Log(string text) {   // ②
        /* Do nothing */             // ②
    }                                // ②
}                                    // ②

public class Controller {
    public void SomeMethod(ILogger logger) {
        logger.Log("SomeMethod is called");
    }
}
```

더 이상 다른 환경을 설명할 필요없이 단순하게 구현 가능

<br/>

## 5. 구체 클래스를 목으로 처리하기

구체 클래스 대신 목으로 처리하는 방식도 때때로 유용함
단, 단일 책임 원칙을 위배함

```csharp
public class StatisticsCalculator {
    public (double totalWeight, double totalCost) Calculate(int customerId) {
        List<DeliveryRecord> records = GetDeliveries(customerId);

        double totalWeight = records.Sum(x => x.Weight);
        double totalCost = records.Sum(x => x.Cost);

        return (totalWeight, totalCost);
    }

    public List<DeliveryRecord> GetDeliveries(int customerId) {
        /* Call an out-of-process dependency
        to get the list of deliveries */
    }
}
```

`StatisticsCalculator`는 특정 고객에게 배달된 모든 배송물의 무게와 비용 같은 고객 통계를 수집하고 계산

👉🏻 외부 서비스에서 검색한 배달 목록 기반으로 계산

```csharp
public class CustomerController {
    private readonly StatisticsCalculator _calculator;

    public CustomerController(StatisticsCalculator calculator) {
        _calculator = calculator;
    }

    public string GetStatistics(int customerId) {
        (double totalWeight, double totalCost) = _calculator.Calculate(customerId);

        return
            $"Total weight delivered: {totalWeight}. " +
            $"Total cost: {totalCost}";
    }
}
```

위 컨트롤러를 테스트하기 위해 `StatisticsCalculator` 처리를 하는 방법

비관리 프로세스 외부 의존성을 참조하기 때문에 `StatisticsCalculator` 인스턴스를 넣을 수는 없음

해결 방법: `StatisticsCalculator` 을 목으로 처리하고 `GetDeliveries()` 메서드만 재정의

```csharp
[Fact]
public void Customer_with_no_deliveries() {
    // Arrange
    var stub = new Mock<StatisticsCalculator> { CallBase = true };  // 명시적으로 재정의 하지 않으면 기초 클래스의 동작을 유지하도록 함
    stub.Setup(x => x.GetDeliveries(1))                             // GetDeliveries()는 반드시 가상으로 돼 있어야 함
        .Returns(new List<DeliveryRecord>());
    var sut = new CustomerController(stub.Object);

    // Act
    string result = sut.GetStatistics(1);

    // Assert
    Assert.Equal("Total weight delivered: 0. Total cost: 0", result);
}
```

하지만, 일부 기능을 지키려고 구체 클래스를 목으로 처리해야 한면, 이는 단일 책임 원칙을 위반하는 결과

```csharp
public class DeliveryGateway : IDeliveryGateway {
    public List<DeliveryRecord> GetDeliveries(int customerId) {
        /* 프로세스 외부 의존성을 호출해 배달 목록 조회 */
    }
}

public class StatisticsCalculator {
    public (double totalWeight, double totalCost) Calculate(List<DeliveryRecord> records) {
        double totalWeight = records.Sum(x => x.Weight);
        double totalCost = records.Sum(x => x.Cost);

        return (totalWeight, totalCost);
    }
}
```

리팩터링 후의 컨트롤러

```csharp
public class CustomerController {
    private readonly StatisticsCalculator _calculator;
    private readonly IDeliveryGateway _gateway;

    public CustomerController(StatisticsCalculator calculator, IDeliveryGateway gateway) { // 두 개의 별도 의존성
        _calculator = calculator;
        _gateway = gateway;
    }

    public string GetStatistics(int customerId) {
        var records = _gateway.GetDeliveries(customerId);
        (double totalWeight, double totalCost) = _calculator.Calculate(records);

        return
            $"Total weight delivered: {totalWeight}. " +
            $"Total cost: {totalCost}";
    }
}
```

사실, 바로 위의 코드는 험블 객체 디자인 패턴의 실제 예시 (7장 참조)

<br/>

## 6. 시간 처리하기

- 많은 애플리케이션 기능에서 현재 날짜와 시간에 대한 접근이 필요함
- 시간에 따라 달라지는 기능을 테스트하면 거짓 양성이 발생할 수 있음

이를 안정확하는 하나의 안티 패턴과 두 개의 적절한 해결 방식이 있음

<br/>

### 6.1 앰비언트 컨택스트로서의 시간

앰비언트 컨택스트 ambient context 패턴 사용 (8장 참고)

```csharp
public static class DateTimeServer {
    private static Func<DateTime> _func;
    public static DateTime Now => _func();

    public static void Init(Func<DateTime> func) {
        _func = func;
    }
}

DateTimeServer.Init(() => DateTime.Now);                  // 운영 환경 초기화 코드
DateTimeServer.Init(() => new DateTime(2020, 1, 1));      // 단위 테스트 환경 초기화 코드
```

👉🏻 **Ambient context**: 아래와 같은 의존성 획득 방식, 정적 접근자를 통해 특정 타입의 의존성 하나만 참조하게 됨
 - `private static readonly ILogger _logger = LogManager.GetLogger(typeof(User));`


- 앰비언트 컨트스트는 제품 코드를 오염시키고 테스트를 더 어렵게 함

<br/>

### 6.2 명시적 의존성으로서의 시간

서비스 또는 일반 값으로 시간 의존성을 명시적으로 주입

```csharp
public interface IDateTimeServer {
    DateTime Now { get; }
}

public class DateTimeServer : IDateTimeServer {
    public DateTime Now => DateTime.Now;
}

public class InquiryController {
    private readonly IDateTimeServer _dateTimeServer;

    public InquiryController(IDateTimeServer dateTimeServer) {  // 시간을 서비스로 주입
        _dateTimeServer = dateTimeServer;
    }

    public void ApproveInquiry(int id) {
        Inquiry inquiry = GetById(id);
        inquiry.Approve(_dateTimeServer.Now);       // 시간을 일반 값으로 주입
        SaveInquiry(inquiry);
    }
}
```

- 시간을 서비스로 주입하는 것보다는 값으로 주입하는 것이 더 나음
- 제품 코드에서 일반 값으로 작업하는 것이 더 쉽고, 테스트에서 해당 값을 스텁으로 처리하기도 더 쉬움

- 비즈니스 연산을 시작할 때는 서비스로 시간을 주입한 다음, 나머지 연산에서 값으로 전달하는 것이 좋음
- 컨트롤러가 생성자에서 DateTimeServer를 받지만, 이후에는 Inquiry 도메인 클래스에 DateTime 값을 전달

<br/><br/>
