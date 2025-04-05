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

## 8.1.3 Nullable primitive types: `Int?`, `Boolean?`, and more

<small><i>널이 될 수 있는 기본 타입 : `Int?`, `Boolean?` 등</i></small>


- 코틀린의 Nullable 타입은 자바의 래퍼 타입으로 컴파일 됨
  - 자바의 참조 타입의 변수에만 대입할 수 있기 때문 (원시 타입은 `null` 대입 불가)

<br/>

```kotlin
data class Person(val name: String,
                  val age: Int? = null) {

    fun isOlderThan(other: Person): Boolean? {
        if (age == null || other.age == null)
            return null
        return age > other.age
    }
}

fun main() {
    println(Person("Sam", 35).isOlderThan(Person("Amy", 42)))   // false
    println(Person("Sam", 35).isOlderThan(Person("Jane")))      // null
}
```

- 컴파일러는 `null` 검사 후 두 값을 일반 값으로 다룸
  - `java.lang.Integer`로 저장


### 제네릭 클래스 
- 제네릭 클래스의 경우 래퍼 타입을 사용
- 어떤 클래스의 타입 인자로 원시 타입을 넘기면 코틀린은 그 타입에 대한 박스 타입을 사용
- `val listOfInts = listOf(1, 2, 3)` → `java.util.List<java.lang.Integer> getListOfInts();`

<br/>

## 8.1.4 Kotlin makes number conversions explicit

<small><i>수 변환</i></small>

- 코틀린과 자바의 가장 큰 차이점 중 하나 → 수를 변환하는 방식
- 코틀린은 한 타입의 수를 다른 타입의 수로 자동 변환하지 않음
  - 결과 타입이 허용하는 수의 범위가 원래 타입의 범위보다 넓은 경우도 자동 변환 불가능

- 코틀린은 모든 원시 타입 (`Boolean` 제외) 에 대해 변환 함수를 제공
  - `toByte()`, `toShort()`, `toChar()` 등
 
- 양방향 변환 함수가 모두 제공
  - 더 넓은 범위의 타입으로 변환하는 함수 지원 (e.g.`Int.toLong()`) 
  - 더 좁은 범위의 타입으로 변환하면서 값을 벗어나는 경우에는 일부를 잘라내는 함수 지원 (e.g. `Long.toInt()`)
- 박스 타입 간의 `equals` 메서드는 값이 아니라 박스 타입 객체를 비교
- 따라서 자바에서 `new Integer(42).equals(new Long(42))` → `false` 
- 코틀린에서 암시적 변환을 허용한다면 다음과 같이 쓸 수 있을 것

```kotlin
val x = 1                       // int 타입
val list = listOf(1L, 2L, 3L)   // Long 타입 리스트
x in list                       // ❌ 암시적 타입 변환으로 인해 false
```

위 코드는 컴파일 오류를 발생 시킴

```bash
figure08_03.kt:10:7: error: type inference failed. The value of the type parameter 'T' should be mentioned in input types (argument types, receiver type, or expected type). Try to specify it explicitly.
    x in list
      ^^
```

코틀린에서는 타입을 명시적으로 변환해서 같은 타입의 값으로 만든 후 비교해야 함

```kotlin
println(x.toLong() in listOf(1L, 2L, 3L))
```

예상치 못한 동작을 피하기 위해 각 변수를 명시적으로 변환해야 함

#### 원시 타입 리터럴

코틀린은 수 리터럴을 허용

- `L` 접미사가 붙은 `Long` 타입 리터럴: `123L`
- 표준 부동소수점 표기법을 사용한 `Double` 타입 리터럴 : `0.12`, `2.0`, `1.2e10`, `1.2e-10`
- `f`/`F` 접미사가 붙은 `Float` 타입 리터럴 : `123.4f`, `.456F`, `1e3f`
- `0x` 나 `0x` 접두사가 붙은 16 진 리터럴 : `0XCAFEBABE`, `0xbcdL`
- `0b` 나 `0B` 접두사가 붙은 2 진 리터럴 : `0b000000101`
- `U` 접미사가 붙은 부호 없는 정수 리터럴 : `123U`, `123UL`, `0x10cU` 
- **코틀린 문자 리터럴**: 이스케이스 시퀀스를 사용 가능
  - `'1'`
  - `'\t'`(이스케이프 시퀀스로 정의한 탭 문자)
  - `\u009` (유니코드 이스케이프 시퀀스로 정의한 탭 문자)


숫자 리터럴을 타입이 알려진 변수에 대입하거나 함수에 인자로 넘기면 컴파일러가 필요한 변환을 자동으로 넣어줌

추가로 산술 연산자는 적당한 타입의 값을 받아들일 수 있도록 이미 오버로드돼 있음

```kotlin
fun printALong(l: Long) = println(l)
 
fun main() {
    val b: Byte = 1        // 상수 값은 적절한 타입으로 해석됨
    val l = b + 1L         // + 는 Byte 와 Long 을 인자로 받을 수 있음
    printALong(42)         // 컴파일러는 42를 Long 값으로 해석
}
```

- 숫자 연산 시 **오버플로** <sup>overflow</sup> 나 **언더플로** <sup>underflow</sup> 가 발생할 수 있음
- 코틀린은 검사에 추가 비용을 들이지 않음

```kotlin
fun main() {
    println(Int.MAX_VALUE + 1)
    // -2147483648                 // 오버플로로 인해 값이 양수 최댓값에서 음수 최솟값으로 넘어감
    println(Int.MIN_VALUE - 1)
    // 2147483647                  // 언더플로로 인해 값이 음수 최솟값에서 양수 최댓값으로 넘어감
}
```

<br/>

### 문자열을 수로 변환하기

- 코틀린 표준 라이브러리는 문자열을 수로 변환하는 함수 제공: `toInt`, `toByte`, `toBoolean`, ...
  - `"42".toInt()` ← `42`
- 파싱에 실패하면 `NumberFormatException` 발생
- 각 확장 함수로 변환 실패 시 `null` 을 돌려주는 `toIntOrNull`, `toByteOrNull` 등의 함수가 존재
  - `"seven".toIntOrNull()` ← `null`
- 문자열을 불리언 값으로 변환할 때, 문자열이 `null`이 아니고 단어 `"true"`와 같으면 `true` 반환 (대소 문자 구분 X)
  - `"trUE".toBoolean()` ← `true`
  - `"7".toBoolean()`    ← `false`
  - `null.toBoolean()`   ← `false`

- `toBooleanStrict`: 정확히 `true` `false` 와 일치할 때 변환. 일치하지 않은 경우에는 예외 발생
  - `"true".toBooleanStrict()` ← `true`
  - `"trUE".toBooleanStrict()` ← `Exception in thread "main" java.lang.IllegalArgumentException: The string doesn't represent a boolean value: trUE`














