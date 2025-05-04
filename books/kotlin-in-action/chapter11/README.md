# CHAPTER 11. Generics

<small><i>제네릭스</i></small>

<br/>

- **실체화된 타입 파라미터<sup>Reified type parameters</sup>**을 사용하면, 인라인 함수 호출에서 타입 인자로 쓰인 구체적인 타입을 실행 시점에 알 수 있음
  - 일반 클래스나 함수의 경우 타입 인자 정보가 실행 시점에 사라지기 때문에 이런 일이 불가능
- **선언 지점 변성<sup>Declaration-site variance</sup>**을 사용하면, 베이스 타입은 같지만 타입 인자가 다른 두 제네릭 타입이 있을 때, 두 제네릭 타입의 상하위 타입 관계가 어떻게 되는지 지정할 수 있음
  - 가령, `List<Any>` 를 인자로 받는 함수에 `List<Int>` 타입의 값을 전달 가능 여부를 지정

<br/>

## 11.1 Creating types with type arguments: Generic type parameters

<small><i>타입 인자를 받는 타입 만들기: 제네릭 타입 파라미터</i></small>

- 제네릭스는 타입 파라미터로 받는 타입 정의
- 제네릭 타입의 인스턴스 생성 시, 타입 파라미터를 구체적인 타입 인자로 치환
- 코틀린 컴파일러는 보통 타입과 마찬가지로 타입 인자도 추론
- 빈 리스트의 경우, 타입 추론 근거가 없기 때문에 타입 인자를 명시해야 함
  - `val readers: MutableList<String> = mutableListOf()`
  - = `val readers = mutableListOf<String>`

<br/>

<table><tr><td>

#### 코틀린에는 로(raw) 타입이 없음

- 자바는 1.5 에 제네릭을 늦게 도입했기 때문에, 이전 버전 호환성을 위해 타입 인자가 없는 제네릭 타입(raw type) 허용
- 즉, 자바에서는 리스트 원소 타입을 지정하지 않아도 선언 가능

```java
// Java
ArrayList aList = new ArrayList();
```

반면 코틀린에서는, 반드시 제네릭 타입의 인자 타입를 명시하거나 컴파일러가 추론 가능하도록 해야 함

</td></tr></table>

<br/>

### 11.1.1 Functions and properties that work with generic types

<small><i>제네릭 타입과 함께 동작하는 함수와 프로퍼티</i></small>

- 컬렉션을 다루는 라이브러리 함수는 대부분 제네릭 함수

```kotlin
fun <T> List<T>.slice(indices: IntRange): List<T>
```

- 타입 파리미터가 수신 객체와 반환 타입에 쓰이는데, 대부분 컴파일러가 타입 인자를 추론해서 둘 다 명시하지 않음

```kotlin
// 사용
val letters = ('a'..'z').toList()
letters.slice<Char>(0..2)   // 명시적 타입 인자 지정
letters.slice(10..13)       // 타입 인자 생략
```

<br/>

- 클래스나 인터페이스 안에 정의된 **메서드**, **최상위 함수**, **확장 함수**에서 타입 파라미터 선언 가능
- 제네릭 함수를 정의할 때와 마찬가지 구문으로 제네릭 **확장 프로퍼티**를 선언할 수 있음
  - ```kotlin
      val <T> List<T>.penultimate: T  // 모든 리스트 타입에서 사용 가능
        get() = this[size - 2]
    ```
- **확장 프로퍼티**가 아닌 일반 프로퍼티는 제네릭 불가
  - `val <T> x: T = TODO()` ← `ERROR: type parameter of a property must be used in its receiver type`

<br/>

### 11.1.2 Generic classes are declared with the angle bracket syntax

<small><i>제네릭 클래스를 홀화살괄호 구문을 사용해 선언한다</i></small>

코틀린도 자바와 동일하게, 클래스나 인터페이스에 타입 파라미터를 넣은 **홑화살괄호(`<>`)**를 붙여 제네릭 표현

**자바 제네릭스와 비슷한 점**

- 클래스 본문 안에서 타입 파라미터를 다른 일반 타입처럼 사용
- 클래스가 자신을 타입 인자로 참조할 수도 있음

<br/>
<pre lang="kotlin">
interface Comparable<T> {
  fun compareTo(other: T): Int
}

class String : <b>Comparable\<String\></b> {
override fun compareTo(other: String): Int = TODO() // Comparable 인터페이스의 타입 파라미터 사용
}

</pre>

<br/>

### 11.1.3 Restricting the type a generic class or function can use: Type parameter constraints

<small><i>제네릭 클래스나 함수가 사용할 수 있는 타입 제한: 타입 파라미터 제약</i></small>

- 타입 파라미터 제약 <sup>type Parameter constraint</sup>: 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능
  - e.g. 제네릭 타입의 타입 파라미터를 특정 상계 타입<sup>upper bound</sup> 으로 지정
  - → 상계 타입 혹은 상계 타입의 하위 타입(하위 클래스)이어야 함
- **형식**: 타입 파라미터 이름 뒤에 클론(`:`) 와 상계 타입 작성

```kotlin
 타입 파라미터
    ——
fun <T : Number> List<t>.sum(): T
       ————————
          상계
```

<br/>
<table><tr><td>
⚠️ 자바에서는 `extends` 를 사용해서 동일하게 표현

`<T extends Number> T sum(List<T> 1ist)`

</td></tr></table><br/>

Number 는코틀린 표준 라이브러리에서 숫자 타입을 표현하는 모든 클래스의 상위 클래스

```kotlin
listof (1, 2, 3) sum()    // 6
```

- 타입 파라미터 `T` 의 상계를 지정하면, 해당 상계 타입으로 취급
- 상계 타입의 정의 메서드 호출

```kotlin
fun <T : Number> oneHalf(value: T): Double {
  return value.toDouble() / 2.0   // NUmber 클래스의 정의 메서드 호출
}
```

<br/>

#### 타입 파라미터에 여러 제약을 가하기

드물게 타입 파라미터에 대해 둘 이상의 제약을 가해야 하는 경우가 있음

Example. `CharSequence` 의 맨 끝에 마침표가 있는지 검사하는 제네릭 함수

→ **데이터 접근 연산**(`endsWith`) 과 **데이터 변경 연산**(`append`) 을 모두 사용

<br/>
<pre lang="kotlin"><code>
fun \<T\> ensureTrailingPeriod(seq: T)
    <b>where T : CharSequence, T : Appendable {   // 타입 파라미터 제약 목록
  if (!seq.endsWith('.')) {
    seq.append('.')
  }
}
</code></pre>

<br/>

### 11.1.4 Excluding nullable type arguments by explicitly marking type parameters as non-null

<small><i>명시적으로 타입 파라미터를 널이 될 수 없는 타입으로 표시해서 널이 될 수
있는 타입 인자 제외시키기</i></small>

- 타입 파라미터를 널이 될 수 있는 값으로 지정할 수 있음
  - 제네릭 클래스나 함수를 정의 후 그 타입을 인스턴스화할 때는, 널이 될 수 있는
    타입을 포함하여 치환할 수 있음
- 기본적으로, 타입 파라미터는 `Any?` 를 상계로 하는 파라미터와 동일

```kotlin
class Processor<T> {
  fun process(value: T)
    value?.hashCode() // value 는 nullable: 안전한 호출을 사용해야만 함
  }
}
```

정상 실행한 형식은 다음과 같음

```kotlin
val nullableStringProcessor = Processor<String?>()
nullableStringProcessor.process(null)
```

- 널이 불가능한 타입 지정은 `Any?` 대신 `Any` 를 상계로 사용
  - `<T : Any>` 라는 제약은 `T` 타입이 항상 널이 될 수 없는 타입이 되도록 보장

```kotlin
class Processor<T : Any> {
  fun process(value: T) {
    value.hashCode()    // null 불가
  }
}
```

- Nullable 사용 시 컴파일 오류

```kotlin
Processor<String?>()
// Error: Type argument is not within its bounds: should be subtype of 'Any'
```

<br/>

<table>

#### 자바와 상호운용할 때 제네릭 타입을 '확실히 널이 될 수 없음' 으로 표시하기

예를 들어 다음의 제네릭 `JBox` 인터페이스

- `put` 메서드의 타입 파라미터로 **널이 될 수 없는 `T`** 만 사용하도록 제약
- 동일하게 `T`를 사용하는 `putIfNotNull` 메서드는 **널이 될 수 있는 값**으로 입력받음

```kotlin
import org.jetbrains.annotations.NotNull;
public interface JBox<T> {
  /**
   *  널이 될 수 없는 값을 박스에 넣음
   */
  void put (@NotNull T t);
  /**
   *  널 값이 아닌 경우 값을 박스에 넣고
   *  널 값인 경우 아무것도 하지 않음
   */
  void putIfNotNull(T t);
```

- `T : Any` 로 제한하면 `putIfNotNull`에서 Nullable하게 사용할 수 없음

```kotlin
class KBox<T : Any>: JBox<T> {
  override fun put(t: T) { /* ... */ }
  override fun putIfNotNull(t: T) { /* ERROR */ }
}
```

- 코틀린은 **타입 사용 지점**에서 절대로 널이 될 수 없다고 표시하는 방법을 제공
  - → **`T & Any`**

```kotlin
class KBox<T : Any>: JBox<T> {
  override fun put(t: T & Any) { /* ... */ }
  override fun putIfNotNull(t: T) { /* ... */ }
}
```

<br/>

## 11.2 Generics at run time: Erased and reified type parameters

<small><i>실행 시점 제네릭스 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터</i></small>

- JVM의 제네릭스는 보통 **타입 소거**<sup>type erasure</sup>로 구현
- 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다는 의미
- 코틀린에서는 함수를 `inline`으로 만들면 타입 인자가 지워지지 않게 할 수 있음
  - 이를 '실체화됐다'<sup>reified</sup>고 표현

<br/>

## 11.2.1 Limitations to finding type information of a generic class at run time: Type checks and casts

<small><i>실행 시점에 제네릭 클래스의 타입 정보를 찾을 때 한계: 타입 검사와 캐스팅</i></small>

- 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워짐
- 제네릭 클래스 인스턴스가 그 인스턴스를 생성할 때 쓰인 타입 인자에 대한 정보를 유지하지 않는다는 의미

```kotlin
val list1: List<String> = listof("a", "b")
val list2: List<Int> = listof(1, 2, 3)
```

- 컴파일러는 두 리스트를 서로 다른 타입으로 인식하지만 실행 시점에 그 둘은 완전히 같음

  - 컴파일러가 올바른 타입의 값만 각 리스트에 넣도록 보장

- 타입 인자를 따로 저장하지않기 때문에 실행 시점에 타입 인자를 검사 불가
- `is` 검사를 통해 타입 인자로 지정한 타입을 검사 불가

```kotlin
fun printList(l: List<Any>) {
  when (l) {
    is List<String> -> println("Strings: $1")
    is List<Int> -> println("Integers: $1")
  } // Error: Cannot check for an instance of erased type
}
```

- 실행 시점의 List 인자 타입 정보는 지워짐
- **장점**: 저장할 타입 정보 크기가 줄어듦
- **단점**: 타입 정보를 알 수 없음

<br/>

> 베이스 타입 (가령, 어떤 값이 집합이나 맵이 아니라 리스트인지)의 구분은 가능
>
> → 스타 프로젝션<sup>star projection</sup>

<br/>

- `as` / `as?` 캐스팅에도 제네릭 타입 가능
- 🚨 기저 클래스(base class)는 일치하지만, 타입 인자가 불일치하는 타입으로 캐스팅해도 여전히 캐스팅 성공
  - 실행 시점에는 제네릭 타입의 타입 인자를 알 수 없기 때문
  - 컴파일러가 `unchecked cast`(검사할 수 없는 캐스팅) 경고

```kotlin
fun printSum(c: Collection<*>) {
  // [warn] Unchecked cast: List<*> to List
  val intList = c as? List<Int>
        ?: throw IllegalArgumentException("List is expected")
}
```

**결과:**

```kotlin
printSum(listOf(1, 2, 3)) // 6 (정상 작동)
printSum(setOf(1, 2, 3)) // IllegalArgumentException: List is expected
```

- 하지만, 잘못된 타입 원소 리스트를 전달하면 실행 시점에 `ClassCastException` 발생
- `as?` 캐스팅은 성공하지만 문자열을 합할 수는 없으므로 나중에 다른 예외가 발생

```kotlin
printSum(listOf("a", "b", "с"))
// ClassCastException: String cannot be cast to Number
```
