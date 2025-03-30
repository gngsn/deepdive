# CHAPTER 7. Working with nullable values

<small><i>널이 될 수 있는 값</i></small>

**TL;DR**


<br/><br/>

---

<br/>

## 7.1 Avoiding `NullPointerException`s and handling the absence of values: **Nullability**

<small><i>`NullPointerException`을 피하고 값이 없는 경우 처리: 널 가능성</i></small>

- **Nullability**: 널-가능성은 `NullPointerException`(NPE) 오류를 피할 수 있게 돕는 코틀린 타입 시스템의 특성
- 최신 언어의 **`null` 접근 방법**: 가능한 이 문제를 실행 시점에서 컴파일 시점으로 옮기는 것

<br/>

## 7.2 Making possibly `null` variables explicit with nullable types

<small><i>널이 될 수 있는 타입으로 널이 될 수 있는 변수 명시</i></small>

- 코틀린 타입 시스템: 널이 될 수 있는 타입을 명시적으로 지원
- 널이 될 수 있는 타입은 **프로퍼티나 변수에 `null`이 가능함을 명시**해서 허용하게 만드는 방법
- `null` 을 포함하는 인자의 함수 정의는 타입 이름 뒤에 물음표 (`?`) 명시

타입 이름 뒤에 물음표를 붙이면 그 타입의 변수나 프로퍼티에 null 참조를 저장

> `Type?` = `Type` or `null`

<br/>

<pre><b>Example.</b>

- 코틀린에서 <code>null</code>이 인자로 들어올 수 없을 때의 함수 정의:

<code lang="kotlin">fun strLen(s: String) = s.length</code>

- strlen 에 <code>null</code> 이거나 <code>null</code> 이 될 수 있는 인자를 넘기는 것은 금지됨 - 컴파일 시 오류가 발생.

<code lang="kotlin">strLen(null)
// ERROR: Null can not be a value of a non-null type String</code>

- <code>null</code>을 포함하는 인자의 함수 정의:

<code lang="kotlin">fun strLenSafe(s: String?) = ...</code>
</pre>

- Nullable 타입의 값은 수행할 수 있는 연산 종류가 제한됨

```kotlin
fun strLenSafe(s: String?) = s.length()
// ERROR: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type kotlin.String?
```

- Nullable 값을 Non-Nullable 타입의 변수에 대입할 수 없음

```kotlin
fun main() {
val x: String? = null
var y: String = x
// ERROR: Type mismatch: inferred type is String? but String was expected
}
```

<br/>

- **Nullable 타입 처리**: 중요한 일 바로 `null` 과 비교
- **스마트 캐스트 적용**: 코틀린 컴파일러는 `null` 이 아닐이 확실한 영역에서는 해당 값을 Non-Nullable 타입의 값처럼 사용할 수 있도록 함

```kotlin
fun strLenSafe(s: String?): Int = if (s != null) s.length else 0
```

<br/>

## 7.3 Taking a closer look at the meaning of types

<small><i>타입의 의미 자세히 살펴보기</i></small>

<pre><b>NullPointerException 오류를 다루는 다른 방법</b>
자바에서 NullPointerException 해결에 도움을 주는 도구
- <code>@Nullable</code>, <code>@NotNull</code>
  - 단점: 오류를 피하기 위해 모든 위치에 어노테이션을 추가해야함
- 인텔리제이 IDEA 코드 검사기
  - 단점: 자바 컴파일 절차의 일부가 아니기 때문에 일관성 보장을 할 수 없음
- null 값을 코드에서 쓰지 않는 것
  - 자바 8 에 새로 도입된 optional 타입 등 null 을 감싸는 특별한 래퍼 타입을 활용할 수 있음.
    - optional은 어떤 값이 정의되거나 정의되지 않을 수 있음을 표현하는 타입
  - 단점 1: 코드가 지저분해짐
  - 단점 2: Wrapper 가 추가됨에 따라 <b>실행 시점에 성능이 저하</b>
  - 단점 3: <b>전체 생태계에서 일관성 있게 활용하기 어려움</b> - JDK 메서드나 다른 서드파티 라이브러리 등에서 반환되는 null 을 처리해야함
</pre>

#### Support for nullable types

[🔗 Kotlinlang - Java to Kotlin Nullability Guide](https://kotlinlang.org/docs/java-to-kotlin-nullability-guide.html#support-for-nullable-types)

- Kotlin은 NullPointerException 발생 가능성이 있는 호출을 **컴파일 타임에 방지**하여, 많은 예외 상황을 사전에 방지. 
- 실행 시점에서는 Nullable 타입과 Non-Nullable 타입의 객체가 동일하게 취급됨.
- 즉, **Nullable 타입이 Non-Nullable 타입의 래퍼(wrapper)가 아님**.
- 모든 검사는 **컴파일 타임에 수행되므로**, Kotlin에서 Nullable 타입을 다룰 때 **실행 시간 오버헤드는 '거의' 없음**.
  - *"거의"* 라는 표현을 쓰는 이유는, Kotlin이 내부적으로 널 체크를 수행하긴 하지만, 그 오버헤드는 **최소한**이기 때문. 

<br/>

### 널 가능 타입 지원

Kotlin과 Java의 타입 시스템에서 가장 중요한 차이점은 **Kotlin이 널 가능 타입을 명시적으로 지원한다는 것**입니다. 널 가능 타입은 특정 변수가 `null` 값을 가질 수 있음을 나타내는 방법입니다. 만약 변수가 `null`일 가능성이 있다면, 해당 변수에서 메서드를 호출하는 것은 안전하지 않습니다. 이는 **NullPointerException(NPE)**이 발생할 수 있기 때문입니다.

Kotlin은 이러한 호출을 **컴파일 타임에 금지**하여, 많은 예외 상황을 사전에 방지합니다. 실행 시점에서는 널 가능 타입과 널 불가능 타입의 객체가 동일하게 취급됩니다. 즉, **널 가능 타입이 널 불가능 타입의 래퍼(wrapper)가 아닙니다**. 모든 검사는 **컴파일 타임에 수행되므로**, Kotlin에서 널 가능 타입을 다룰 때 **실행 시간 오버헤드는 거의 없습니다**.

여기서 *"거의"* 라는 표현을 쓰는 이유는, Kotlin이 내부적으로 널 체크를 수행하긴 하지만, 그 오버헤드는 **최소한**이기 때문입니다.

반면, Java에서는 직접 `null` 체크를 하지 않으면 **NullPointerException(NPE)**이 발생할 수 있습니다.

<br/>

## 7.4 Combining null checks and method calls with the safe call operator: `?.`

<small><i>안전한 호출 연산자로 nll 검사와 메서드 호출 합치기: `?.`</i></small>

- `?.`: null 검사와 메서드 호출을 한 연산으로 수행하는 안전한 호출 연산자
  - e.g. `str?.uppercase()` = `if (str != null) str.uppercase() else null`
    - `str?.uppercase()` 의 결과 타입: `String?`
- 객체 그래프에서 널이 될 수 있는 중간 객체가 여럿 있을 때, 한 식 안에서 **안전한 호출을 연쇄**해서 함께 사용하면 편리
  - e.g. `val country = this.company?.address?.country`

<br/>

## 7.5 Providing default values in null cases with the Elvis operator: `?:`

<small><i>엘비스 연산자로 null 에 대한 기본값 제공: `?:`</i></small>

- `?:`: **엘비스(Elvis) 연산자**. 코틀린은 null 대신 사용할 기본값을 지정할 때 편리하게 사용할 수 있는 연산자
  - e.g. `val recipient: String = name ?: "unnamed"` ← `name` 이 `null`이면, `recipient`에 `unnamed` 값이 할당 됨 
- 함수의 전제조건 <sup>precondition</sup> 을 검사하는 경우 특히 유용
  - e.g. `val address = person.company?.address ?: throw IllegalArgumentException("No address")`
    - `NullPointerException`이 아닌 의미 있는 오류로 변경

<br/>

## 7.6 Safely casting values without throwing exceptions: `as?`

<small><i>예외를 발생시키지 않고 안전하게 타입을 캐스트하기: `as?`</i></small>

- `as?`: 값을 지정한 타입으로 변환. 대상 타입으로 변환할 수 없으면 `null` 반환
- 스마트 캐스트 적용
- 자바 `instanceof` 검사 대신 코틀린이 제공하는 **안전한 캐스트 연산자** <sup>safe-cast operator</sup>
- 엘비스 연산자(`?:`)나 안전한 호출 연산자(`?.`)와 함께 자주 사용됨
  - e.g. <code>val otherPerson = o <b>as?</b> Person <b>?:</b> return false</code>
- 자바 타입 캐스트와 마찬가지로 대상 값을 `as` 로 지정한 타입으로 바꿀 수 없으면 ClassCastException 발생

<br/>

## 7.7 Making promises to the compiler with the non-null assertion operator: `!!`

<small><i>널 아님 단언: `!!`</i></small>

- `!!`: 직접 컴파일러에게 어떤 값이 `null` 이 아니라고 직접 알려주는 연산자
  - "이 값이 `null` 이 아님을 잘 알고 있다. 내가 잘못 생각했다면 예외가 발생해도 감수하겠다"
- 실제 `null` 에 대해 `!!` 적용 시 `NPE` 발생

<br/>

#### `!!` 체인

- `!!` 를 `null` 에 대해 사용해서 발생하는 예외 Stack trace 에는 어떤 파일의 몇 번째 줄인지에 대한 정보는 들어있지만 어떤 식에서 예외가 발생했는지에 대한 정보는 들어있지 않음
- 어떤 값이 null 이었는지 확실히 하기 위해 여러 `!!` 단언문을 한 줄에 함께 쓰는 일을 피하면 가장 좋음

```kotlin
person.company!!.address!!.country  // ❌
```

<br/>

## 7.8 Dealing with nullable expressions: The `let` function

<small><i>`let` 함수</i></small>

- `let`: 원하는 식의 결과가 `null`인지 검사한 후, 결과 값을 처리하는 작업을 한번에 처리
- `?.let {}`의 대상이 `null` 이 아니면 let 블록 실행, `null` 이면 let 블록 실행 안함
  - e.g. `"Null이 아닐 때"?.let { /* 블록 실행 */ }`
  - e.g. `null?.let { /* 블록 무시 */ }`
- 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는 경우

**예시:**

```kotlin
fun sendEmailTo(email: String) { /* ... */ }

val email: String? = "foo@bar.com"
email?.let { sendEmailTo(it) }
```

<br/>

#### 코틀린의 영역 함수 비교 : `with`, `apply`, `let`, `run`, `also` 를 언제 사용할까?

- 모든 영역 함수는 **대상 객체를 어떻게 접근하는지**와 **반환값**에 따라 활용이 달라짐

| 함수                | `x` 참조 방식   | 반환값    |
|-------------------|-------------|--------|
| `x.let { ... }`   | `it`        | 람다의 결과 |
| `x.also { ... }`  | `it`        | x      |
| `x.apply { ... }` | `this`      | x      |
| `x.run { ... }`   | `this`      | 람다의 결과 |
| `with(x) { ... }` | `this`      | 람다의 결과 |

- `?.let`: 객체가 `null` 아닌 경우, 코드 블록을 실행하고 싶을 때 
  - `let`: 어떤 식의 결과를 변수에 담되, 그 영역을 한정시키고 싶을 때
    - e.g. `anyFunction().let { /* anyfunction 결과 사용 한정 */ }`
- `apply`: 빌더 스타일의 API(e.g. 인스턴스 생성)를 사용해 객체 프로퍼티를 설정할 때
- `also`: 객체에 어떤 동작을 실행한 후, 원래의 객체를 다른 연산에 사용하고 싶을 때
- `with`: 하나의 객체에 대해 이름을 반복하지 않으면서 여러 함수 호출을 그룹으로 묶고 싶을 때 
- `run`: 객체를 설정한 다음에 별도의 결과를 돌려주고 싶을 때 

✅ 여러 영역 함수의 용법은 세부 사항에 따라 달라짐. 팀이나 프로젝트에서 관례를 정해두면 좋음

<br/>

## 7.9 Non-null types without immediate initialization: Late-initialized properties

<small><i>직접 초기화하지 않는 널이 아닌 타입: 지연 초기화 프로퍼티</i></small>

- `lateinit`: 지연 초기화<sup>late-initialize</sup> 지원. 프로퍼티를 나중에 초기화할 수 있음.
- 객체 생성 후, 나중에 특정 메서드로 초기화해야할 경우
- 코틀린에서는 클래스 안의 Non-Nullable 프로퍼티를 생성자 안에서 초기화하지 않고 다른 메서드로 초기화할 수 없음
  - 일반적으로 생성자에서 모든 프로퍼티를 초기화해야 함
  - Nullable 타입 사용 시, 프로퍼티 접근 시 `null` 검사 필요
- ⚠️ **지연 초기화 프로퍼티는 항상 `var` 이어야 함**
  - 초기화 전, 프로퍼티에 접근하면 오류 발생: 
  - `kotlin.UninitializedPropertyAccessException: lateinit property myService has not been initialized`

```kotlin
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyTest {
    private lateinit var myService: MyService
 
    @BeforeAll fun setUp() {
        myService = MyService()
    }
 
    @Test fun testAction() {
        assertEquals("Action Done!", myService.performAction())
    }
}
```

- `lateinit` 프로퍼티는 자바 의존관계 주입<sup>DI, Dependency Injection</sup> 프레임워크와 함께 자주 사용
  - `lateinit` 프로퍼티의 값을 DI 프레임워크가 외부에서 설정
- `lateinit` 프로퍼티가 반드시 클래스의 멤버일 필요는 없음: 함수 본문 안의 지역 변수나 최상위 프로퍼티도 지연 초기화할 수 있음

<br/>


