# CHAPTER 8. Basic types, collections, and arrays

<small><i>기본 타입, 컬렉션, 배열</i></small>

## 8.1 Primitive and other basic types

<small><i>원시 타입과 기본 타입</i></small>

자바와 달리 코틀린은 원시 타입과 래퍼 타입을 구분하지 않음

<br/>

## 8.1.1 Representing integers, floating-point numbers, characters, and Booleans with primitive types

<small><i>정수, 부동소수점 수, 문자, 불리언 값을 원시 타입으로 표현</i></small>

**Java**

- 자바는 원시 타입과 참조 타입을 구분
  - **원시 <sup>primitive</sup> 타입** (`int` 등) 의 변수에는 그 값이 직접 들어감
    - 더 효율적으로 저장하고 여기저기 전달
    - 하지만 메서드를 호출하거나 컬렉션에 원시 타입 값을 담을 수는 없음
  - **참조 타입** (`String` 등) 의 변수에는 메모리상의 객체 위치가 들어감
- 참조 타입이 필요한 경우 특별한 래퍼 타입으로 원시 타입 값을 감싸서 사용
  - e.g. 정수의 컬렉션을 정의하려면 `Collection<int>`가 아니라 `Collection<Integer>`를 사용해야 함

**Kotlin**

- 코틀린은 **원시 타입**과 **래퍼 타입**을 **구분하지 않음**

```kotlin
val i: Int = 1
val list: List<Int> = listOf(1, 2, 3)
```

- 래퍼 타임을 따로 구분하지 않아 편리
- 원시 타입의 값에 대해 메서드를 호출할 수 있음

<pre><code lang="kotlin">
fun showProgress(progress: Int) {
    val percent = <b>progress.coerceIn(0, 100)</b>
    println("We're $percent % done!")
}
 
fun main() {
    showProgress(146)   // We're 100 % done!
}
</code></pre>

<br/>

#### 코틀린 컴파일러가 객체를 다루는 방법

- 실행 시점에 숫자 타입은 가능한 한 **가장 효율적인 방식으로 표현**됨
  - 대부분의 경우, 코틀린의 `Int` 타입은 자바 `int` 타입으로 컴파일 됨
  - e.g. 변수, 프로퍼티, 파라미터, 반환 타입 등
- **컴파일이 불가능한 경우, 래퍼 객체로 컴파일 됨** 
  - 컬렉션과 같은 제네릭 클래스를 사용하는 경우 뿐
  - e.g. `Collection<Int>`의 경우, `Int`의 래퍼 타입에 해당하는 `Java.lang.Integer` 객체가 들어감
- **자바 원시 타입에 해당하는 코틀린 타입**:
  - 정수 타입 : `Byte`, `Short`, `Int`, `Long`
  - 부동소수점 숫자 타입: `Float`, `Double`
  - 문자 타입: `Char`
  - 불리언 타입: `Boolean`


## 8.1.2 Using the full bit range to represent positive numbers: Unsigned number types

<small><i>양수를 표현하기 위해 모든 비트 범위 사용: 부호 없는 숫자 타입</i></small>


- 양수를 표현하기 위해 모든 비트 범위를 사용하고 싶은 때, 코틀린은 JVM의 **일반적인 원시 타입을 확장해 부호 없는 타입을 제공**
- 다른 원시 타입과 마찬가지로 코틀린의 부호 없는 수도 필요할 때만 래핑됨

**가령,** 
- 비트와 바이트 수준에서 작업을 수행할 때 
- 비트맵 픽셀을 조작할 때 
- 파일에 담긴 바이트들을 다룰 때
- 다른 2진 데이터를 다를 때

<br/>

#### 부호 없는 타입

| Type     | Size   | Value range      |
|----------|--------|------------------|
| `UByte`  | 8 bit  | `0` - `255`      |
| `UShort` | 16 bit | `0` - `65535`    |
| `UInt`   | 32 bit | `0` - `2^32 - 1` |
| `ULong`  | 64 bit | `0` - `2^64 - 1` |


- 부호 없는 숫자 타입들은 상응하는 부호 있는 타입의 범위를 '시프트' 해서, **같은 크기의 메모리를 사용해 더 큰 양수 범위를 표현할 수 있게 해줌**
  - e.g. **일반 `Int`** → 대략 `-20억`~ `+20억` 값 표현 vs. **`UInt`** → 대략  `0` ~ `+40억` 값 표현

<br/>

> 음수가 아닌 정수를 사용하기 위해 **부호 없는 정수**를 사용? 
> → 코틀린의 부호 없는 정수의 목적이 아님
> 
> **명시적으로 전체 비트 범위가 필요한 경우가 아니라면**,
> 보통의 정수를 사용하고 음수 범위가 함수에 전달됐는지 검사하는 편이 더 나음

<br/>

<pre><b>부호 없는 숫자 타입: 구현 세부 사항</b>
ref. https://docs.oracle.com/javase/specs/jvms/se18/html/jvms-2.html#jvms-2.3.1
ref. https://kotlinlang.org/docs/unsigned-integer-types.html#unsigned-arrays-and-ranges

- JVM 공식 문서에는부호가 없는 수에 대한 원시 타입을 명시·제공하지 않음
- 코틀린은 이를 따라 기존의 부호가 있는 원시 타입 위에 자체적인 추상화를 제공
- 코틀린에서 부호 없는 값을 표현하는 각 클래스는 실제로는 인라인 클래스
- 각 인라인 클래스는 기억 장소로 자신에 상응하는 부호있는 타입을 사용
- 내부에서 <code>UInt</code> 값은 실제로 <code>Int</code> 코틀린 컴파일러가 가능할 때마다 인라인 클래스를 기저의 프로퍼티로 대신하거나 래핑하는 등의 처리를 해주기 때문에,
  <b>부호 없는 숫자 타입이 부호 있는 숫자 타입과 같은 성능으로 작동하다고 예상할 수 있음</b>
</pre>

- 코틀린 타입은 컴파일러가 쉽게 상응하는 JVM 원시 타입으로 변환 가능
  - 둘 다 `null` 이 될 수 없음
- 자바 원시 타입을 코틀린에서 사용할 때(플랫폼 타입 X)도 널이 될 수 없는 타입으로 취급할 수 있음
  - 자바 원시 타입의 값은 결코 `null` 이 될 수 없기 때문 

<br/>





