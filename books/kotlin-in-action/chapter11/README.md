# CHAPTER 11. Generics

<small><i>제네릭스</i></small>

<br/>

- **실체화된 타입 파라미터<sup>Reified type parameters</sup>** 을 사용하면, 인라인 함수 호출에서 타입 인자로 쓰인 구체적인 타입을 실행 시점에 알 수 있음
  - 일반 클래스나 함수의 경우 타입 인자 정보가 실행 시점에 사라지기 때문에 이런 일이 불가능
- **선언 지점 변성<sup>Declaration-site variance</sup>** 을 사용하면, 베이스 타입은 같지만 타입 인자가 다른 두 제네릭 타입이 있을 때, 두 제네릭 타입의 상하위 타입 관계가 어떻게 되는지 지정할 수 있음
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

<br/>
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

`<T extends Number> T sum(List<T> list)`

</td></tr></table><br/>

Number 는 코틀린 표준 라이브러리에서 숫자 타입을 표현하는 모든 클래스의 상위 클래스

```kotlin
listOf(1, 2, 3).sum()    // 6
```

- 타입 파라미터 `T` 의 상계를 지정하면, 해당 상계 타입으로 취급
- 상계 타입의 정의 메서드 호출

```kotlin
fun <T : Number> oneHalf(value: T): Double {
  return value.toDouble() / 2.0   // NUmber 클래스의 정의 메서드 호출
}
```

<br/>

#### 타입 파라미터에 여러 제약을 적용하기

드물게 타입 파라미터에 대해 둘 이상의 제약을 가해야 하는 경우가 있음

Example. `CharSequence` 의 맨 끝에 마침표가 있는지 검사하는 제네릭 함수

→ **데이터 접근 연산**(`endsWith`) 과 **데이터 변경 연산**(`append`) 을 모두 사용

<br/>
<pre lang="kotlin"><code>fun <T> ensureTrailingPeriod(seq: T)
    <b>where T : CharSequence, T : Appendable</b> {   // 타입 파라미터 제약 목록
  if (!seq.endsWith('.')) {
    seq.append('.')
  }
}</code></pre>

<br/>

### 11.1.4 Excluding nullable type arguments by explicitly marking type parameters as non-null

<small><i>명시적으로 타입 파라미터를 널이 될 수 없는 타입으로 표시해서 널이 될 수 있는 타입 인자 제외시키기</i></small>

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

#### 자바와 상호운용할 때 제네릭 타입을 '확실히 널이 될 수 없음' 으로 표시하기

예를 들어 다음의 제네릭 `JBox` 인터페이스

- `put` 메서드의 타입 파라미터로 **널이 될 수 없는 `T`** 만 사용하도록 제약
- 동일하게 `T`를 사용하는 `putIfNotNull` 메서드는 **널이 될 수 있는 값**으로 입력받음

```java
import org.jetbrains.annotations.NotNull;
public interface JBox<T> {
    /**
     *  널이 될 수 없는 값을 박스에 넣음
     */
    void put(@NotNull T t);

    /**
     *  널 값이 아닌 경우 값을 박스에 넣고
     *  널 값인 경우 아무것도 하지 않음
     */
    void putIfNotNull(T t);
}
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

### 11.2.1 Limitations to finding type information of a generic class at run time: Type checks and casts

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

- `as?` 캐스팅은 성공하지만 실행 시점에 `ClassCastException` 발생

```kotlin
printSum(listOf("a", "b", "с"))
// ClassCastException: String cannot be cast to Number
```

<br/>

- 컴파일 시점에 타입 정보를 전달 후, `is` 검사 수행 가능

```kotlin
fun printSum(c: Collection<Int>) {
  when (c) {
    is List<Int> -> println("List sum: ${c.sum()}") //  올바른 타입 검사
    is Set<Int> -> println("Set sum: ${c.sum()}")
  }
}
```

<br/>

**결과:**

- `as?` 캐스트가 성공하고 문자열 리스트에 대해 Sum 함수가 됨

```kotlin
printSum(listOf(1,2,3)) // List sum: 6
printSum(setOf(3,4,5)) // Set sum: 12
```

- 컵파일 시점에 원소의 타입에 대한 정보가 있기 때문에, 어떤 컬렉션인지 검사 가능
  - 원소가 `Int` 타입임을 알기 때문에 리스트나 결합 등 컬렉션 종류는 문제 안됨
- 코틀린 컴파일러는 가능한 모든 검사의 위험성과 가능성을 알려줌
- 인전하지 못한 `is` 검사는 **금지**, 위험한 `as` 캐스팅은 **경고**

<br/>

## 11.2.2 Functions with reified type parameters can refer to actual type arguments at run time

<small><i>실체화된 타입 파라미터를 사용하는 함수는 타입 인자를 실행 시점에 언급할 수 있다</i></small>

- 코틀린 제네릭 타입의 타입 인자 정보는 실행 시점에 지워짐
- **제네릭 클래스**의 **인스턴스**를 만들면 **사용한 타입 인자를 알아낼 수 없음**
- **제네릭 함수**의 **타입 인자**도 마찬가지로, **함수 본문에 쓰인 타입 인자를 알 수 없음**

```kotlin
fun <T> isA(value: Any) = value is T
// Error: Cannot check for instance of erased type: T
```

- 위 제약의 예외: **인라인 함수의 타입 파라미터는 실체화**
- 실행 시점에 인라인 함수의 실제 타입 인자를 알 수 있음

<br/>
<pre><b><code>inline</code> 함수</b> 
함수 앞에 <code>inline</code> 키워드를 붙이면, 컴파일러는 그 함수를 호출한 식을 모두 함수를 구현하는 코드로 바꿈</pre>
<br/><br/>

#### 실체화된 타입 파라미터를 사용하는 함수 정의

- 함수가 람다를 인자로 사용하는 경우, 그 함수를 인라인 함수로 만들면 **람다 코드도 함께 인라이닝**되고 익명 클래스와 객체가 생성되지 않아서 성능이 더 좋아질 수 있음
- 인라인 함수가 유용한 다른 이유인 **타입 인자 실체화**

- `isA` 함수를 인라인 함수로 만들고 타입 파라미터를 `reified`로 지정하면 `Value` 의 타입이 `T` 의 인스턴스인지를 실행 시점에 검사할 수 있음

```kotlin
inline fun <reified T> isA(value: Any) = value is T
```

**결과:**

```kotlin
isA<String>("abc")   // true
isA<String>(123)     // false
```

<br/>

- **실체화된 타입 파라미터를 활용 예제**: 표준 라이브러리 함수 - `filterIsInstance`
- 인자로 받은 컬렉션에서 지정 클래스 인스턴스만을 모은 리스트 반환

```kotlin
// 간단하게 정리한 버전
inline fun <reified T>  // reified 키워드: 해당 타입 파라미터가 실행 시점에 지워지지 않음을 표시
      Iterable<*>.filterIsInstance(): List<T> {
  val destination = mutableListOf<T>()
  for (element in this) {
    if (element is T) {
      destination.add(element) // ❶
    }
  }
  return destination
}
```

**사용 예시:**

```kotlin
listOf("one", 2, "three").filterIsInstance<String>()  // [one, three]
```

<br/>
<table><tr><td>

#### 인라인 함수에서만 실체화된 타입 인자를 쓸 수 있는 이유

- 컴파일러는 인라인 함수의 본문을 구현한 바이트코드를 모든 호출 지점에 대치
- 컴파일러는 타입 인자로 쓰인 구체적인 클래스를 참조하는 바이트코드를 생성해 삽입할 수 있음

따라서, 컴파일러가 실체화된 타입 인자를 사용해 인라인 함수를 호출하는 각 부분의 정확한 타입 인자를 알 수 있게됨

- 만들어진 바이트코드는 타입 파라미터가 아니라 구체적인 타입을 사용하므로 실행 시점에 벌어지는 타입 소거의 영향을 받지 않음

- ⚠️ 자바 코드에서는 `reified` 타입 파라미터를 사용하는 `inline` 함수를 호출할 수 없음
- 자바에서는 코틀린 인라인 함수를 다른 보통 함수처럼 호출
  - 실체화된 타입 파라미터가 있는 함수의 경우 타입 인자 값을 바이트코드에 넣기 위해 더 많은 작업이 필요
  - 인라인 함수를 호출해도 실제로 인라이닝이 되지 않음

<br/>
</td></tr></table>

<br/>

### 11.2.3 Avoiding `java.lang.Class` parameters by replacing class references with reified type parameters

<small><i>클래스 참조를 실체화된 타입 파라미터로 대신함으로써 `java.lang.Class` 파라미터 피하기</i></small>

- `java.lang.Class` 타입 인자를 파라미터로 받는 API 에 대한 코틀린 어댑터를 구축하는 경우 실체화된 타입 파라미터를 자주 사용

<br/>

**Example. Serviceloader**

```kotlin
val serviceImpl = ServiceLoader.load(Service::class.java)
```

<br/>

`loadService` 의 파라미터를 실체화된 타입으로 지정한 형식:

```kotlin
inline fun <reified T> loadService() {
  return ServiceLoader.load(T::class.java)
}

// Usage
val serviceImpl = loadService<Service>()
```

<br/>

### 11.2.4 Declaring accessors with reified type parameters

<small><i>실체화된 타입 파라미터가 있는 접근자 정의</i></small>

- 게터와 세터의 커스텀 구현 가능
- 제네릭 타입에 대해 프로퍼티 접근자를 정의하는 경우 프로퍼티를 `inline`으로 표시하고 타입 파라미터를 `reified`로 하면 타입 인자에 쓰인 구체적인 클래스를 참조 가능

**Example. 확장 프로퍼티: `canonical`**

```kotlin
inline val <reified T> T.canonical: String
    get() = T::class.java.canonicalName
```
 
**사용:**

```kotlin
listOf(1, 2, 3).canonical   // java.util.List
1.canonical                 // java.lang.Integer
```

<br/>

### 11.2.5 Reified type parameters come with restrictions

<small><i>실체화된 타입 파라미터의 제약</i></small>

**실체화된 타입 파라미터 사용 케이스 ✅**

- 타입 검사와 캐스팅 (`is`, `!is`, `as`, `as?`)
- **코틀린 리플렉션 API** (`::class`) - 12장 참고
- 코틀린 타입에 대응하는 **`Java.lang.class` 얻기** (`::class.java`)
- 다른 함수를 호출하기 위한 **타입 인자**로 사용

<br/>

**실체화된 타입 파라미터 사용 불가 케이스 ❌**

- 타입 파라미터 클래스의 인스턴스 생성
- 타입 파라미터 클래스의 컴패니언 객체 (Companion Object) 메서드 호출
- 실체화된 타입 파라미터를 받는 함수 호출 시, 실체화되지 않은 타입 파라미터를 넘기기
- 클래스나 논-인라인 함수의 타입 파라미터를 `reified`로 지정하기
  - 인라인 함수에만 사용 → 즉, 실체화된 타입 파라미터를 사용하는 함수는 자신에게 전달되는 모든 람다를 인라이닝 
  - `noinline` 변경자를 함수 타입 파라미터에 붙여 인라이닝을 금지할 수 있음

<br/>

## 11.3 Variance describes the subtyping relationship between generic arguments

<small><i>변성은 제네릭과 타입 인자 사이의 하위 타입 관계를 기술</i></small>

- **변성**<sup>variance</sup>은 베이스 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명
  - Example. `List<String>`과 `List<Any>`

<br/>

### 11.3.1 Variance determines whether it is safe to pass an argument to a function

<small><i>변성은 인자를 함수에 넘겨도 안전한지 판단하게 해준다</i></small>

- `Any`가 `List` 인터페이스의 타입 인자로 들어가는 경우 안전성을 장담할 수 없음
- `List` 와 `MutableList` 의 타입 인자의 변성이 다름

```kotlin
fun addAnswer(list: MutableList<Any>) {
  list.add(42)
}
```

**결과:**

```kotlin
val strings = mutableListOf("abc", "bac")
addAnswer(strings)
println(strings.maxBy { it.length })  // ClassCastException: Integer cannot be cast to String
```

<br/>

### 11.3.2 Understanding the differences between classes, types, and subtypes

<small><i>클래스, 타입, 하위 타입</i></small>

- `타입 B`가 `타입 A`의 하위 타입이라면, `타입 A`의 값 대신 `타입 B` 값을 넣어도 문제가 없음

**Example. `i`가 `Int` 타입일 때,** 

- `val n: Number = i` 은 컴파일 ✅
- `val s: String = i` 은 컴파일 ❌

<br/>

- **널이 될 수 없는 타입은 널이 될 수 있는 타입의 하위 타입**
  - Example. `val s = "abc"`일 때, `val t: String = s` 은 컴파일 ✅

```
  A?   Int?   Int
  ↑     ↑      ↑
  ✅    ✅    ❌
  ⏐     ⏐      ⏐
  A    Int    Int?
```

<br/>

### 11.3.3 Covariance preserves the subtyping relation

<small><i>공변성은 하위 타입 관계를 유지한다</i></small>

- `A`가 `B`의 하위 타입이면 `List<A>` 는 `List<B>` 의 하위 타입
- 이런 클래스나 인터페이스를 공변적<sup>covariant</sup> 이라 부름

- **공변적인 클래스**: 제네릭 클래스 `Producer<A>` 에 대해 `A` 가 `B` 의 하위 타입일 때, `Producer<A>` 가 `Producer<B>` 의 하위 타입인 경우
  - > "하위 타입 관계를 유지한다"
- 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 `out` 표시

<table>
<tr>
<td colspan="2">

```kotlin
open class Animal {
  fun feed() { /* ... */ }
}
```

</td>
</tr>
<tr>
<th>공변적 ❌</th>
<th>공변적 ✅</th>
</tr>
<tr>
<td>

```kotlin
class Herd<T : Animal> {             // T는 공변적이지 않음
  val size: Int get() = /* ... */
    operator fun get(i: Int): T { /* ... */ }
}
```

</td>
<td>

```kotlin
class Herd<out T : Animal> {    // T는 공변적임
  /* ... */
}
```

</td>
</tr>
<tr>
<td>

```kotlin       
fun feedAll(animals: Herd<Animal>) {
  for (i in 0..<animals.size) {
    animals[i].feed()
  }
}
```

**무공변 클래스에 하위 타입 대입하기** 

```kotlin
class Cat : Animal() {               // Cat 은 Animal
    fun cleanLitter() { /* ... */ }
}
 
fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0..<cats.size) {
        cats[i].cleanLitter()
    }
    // feedAll(cats) ← Error: inferred type is Herd<Cat>, but Herd<Animal> was expected          
}
```

</td>
<td>

```kotlin 
fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0..<cats.size) {
        cats[i].cleanLitter()
    }
    feedAll(cats)               // 캐스팅할 필요 X
}
```

</td>
</tr>
</table>

<br/>

- **타입 파라미터 T 에 붙은 out 키워드는 다음 2 가지를 의미**
  - 하위 타입 관계가 유지됨 (`Producer<Cat>`은 `Producer<Animal>` 의 하위 타입).
  - T 를 아웃 위치에서만 사용할 수 있음
- 어떤 위치가 아웃인지 인인지 판정하는 정확한 알고리듬이 궁금한 독자는 코틀린 언어 문서 참고
- `MutableList<T>` 를 타입 파라미터 `T` 에 대해 공변적인 클래스로 선언할 수는 없음
  - `MutableList<T>` 에는 `T` 를 인자로 받아, 그 타입의 값을 반환하는 메서드가 있음 (`T`가 인과 아웃 위치에 동시에 쓰임)

```kotlin
interface MutableList<out T>               //  T에 대해 공변적일 수 없음
       : List<T>, MutableCollection<T> {
   override fun add(element: T): Boolean  // T가 In 위치(함수 파라미터의 타입)에 쓰이기 때문
}
```

<br/>

#### 📌 생성자 파라미터

⚠️ 생성자 파라미터는 인이나 아웃 위치 어느 쪽도 아님

```kotlin
class Herd<out T: Animal>(vararg animals: T) { /* ... */ }
```

- `val` 이나 `var` 키워드를 생성자 파라미터에 적는다면 게터나 (변경 가능한 프로퍼티의 경우) 세터를 정의하는 것과 같음
- **읽기 전용 프로퍼티**는 **아웃 위치**, **변경 가능 프로퍼티**는 **아웃과 인 위치 모두**에 해당

```kotlin
// leadAnimal 프로퍼티가 인 위치에 있기 때문에 T 를 out으로 표시할 수 없음
class Herd<T: Animal>(var leadAnimal: T, vararg animals: T) { /* ... */ }
```

- 비공개<sup>private</sup> 메서드의 파라미터는 인도 아니고 아웃도 아님

```kotlin
// Herd 를 T에 대해 공변적으로 선언해도 안전
class Herd<out T: Animal>(private var leadAnimal: T,
                          vararg animals: T) { /* ... */ }
```

<br/>

### 11.3.4 Contravariance reverses the subtyping relation

<small><i>반공성은 하위 타입 관계를 뒤집는다</i></small>

- 반공변<sup>contravariance</sup> 클래스의 하위 타입 관계는 해당 클래스의 타입 파라미터의 상하위 타입 관계와 반대

<table>
<tr>
<td colspan="2">

```kotlin
open class Animal {
  fun feed() { /* ... */ }
}
```

</td>
</tr>
<tr>
<th>계층 구조의 과일 클래스: 공통 프로퍼티 `weight`를 가짐</th>
<th>사용</th>
</tr>
<tr>
<td>

```kotlin
sealed class Fruit {
    abstract val weight: Int
}
 
data class Apple(
    override val weight: Int,
    val color: String,
): Fruit()
 
data class Orange(
    override val weight: Int,
    val juicy: Boolean,
): Fruit()
```

</td>
<td>

```kotlin
val weightComparator = Comparator<Fruit> { a, b ->
    a.weight - b.weight
}
val fruits: List<Fruit> = listOf(
    Orange(180, true),
    Apple(100, "green")
)
val apples: List<Apple> = listOf(
    Apple(50, "red"),
    Apple(120, "green"),
    Apple(155, "yellow")
)
```

</td></tr>
<tr><th colspan="2">결과</th></tr>
<tr><td colspan="2">

```kotlin
fruits.sortedWith(weightComparator)  
// [Apple(weight=100, color=green), Orange(weight=180, juicy=true)]
apples.sortedWith(weightComparator)
// [Apple(weight=50, color=red), Apple(weight=120, color=green), Apple(weight=155, color=yellow)]
```

</td></tr></table>
<br/>

- 어떤 클래스에 대해 (`Consumer<T>` 를 예로 들면) `타입 B` 가 `타입 A` 의 하위 타입일 때 `Consumer<A>` 가 `Consumer<B>` 의 하위 타입인 관계가 성립하면, **제네릭 클래스는 타입 인자 `T` 에 대해 반공변임**

```
  Animal     Producer<Cat>     Consumer<Animal>
    ↑             ↑                  ⏐
    ⏐           공변적              반공변적
    ⏐             ⏐                  ↓
   Cat       Producer<Cat>     Consumer<Cat>
```

<br/>

- `in`: 해당 클래스의 메서드 안으로 전달돼 메서드에 의해 소비된다는 뜻

<br/>

| 공변성                                          | 반공변성                                         | 무공변성                        |
|----------------------------------------------|----------------------------------------------|-----------------------------|
| `Producer<out T>`                            | `Consumer<in T>`                             | `MutableList<T>`            | 
| 타입 인자의 하위 타입 관계가 제네릭 타입에서도 유지                | 타입 인자의 하위 타입 관계가 제네릭 타입에서 뒤집힘                | 하위 타입 관계가 성립하지 않음           |
| `Producer<Cat>`은 `Producer<Animal>` 의 하위 타입  | `Producer<Animal>`은 `Producer<Cat>` 의 하위 타입  |                             |
| `T` 를 **아웃 위치**에서만 사용할 수 있음                  | `T` 를 **인 위치**에서만 사용할 수 있음                   | `T` 를 **아무 위치**에서나 사용할 수 있음 | 

<br/>

- 어떤 타일 파라미터에 대해서는 공변적이면서 다른 타입 파라미터에 대해서는 반공변적일 수도 있음

```kotlin
interface Function1<in P, out R> {
    operator fun invoke(p: P): R
}
```

<br/>

### 11.3.5 Specifying variance for type occurrences via use-site variance

<small><i>사용 지점 변성을 사용해 타입이 언급되는 지점에서 변성 지정</i></small>

- **선언 지점 변성**<sup>declaration site variance</sup>: 
  - 클래스를 선언하면서 변성을 지정하면 그 클래스를 사용하는 모든 장소에 변성 지정자가 영향을 끼치므로 편리

- **사용 지점 변성** <sup>user-site variance</sup> 
  - 자바에서는 타입 파라미터가 있는 타입을 사용할 때마다, 그 타입 파라미터를 하위 타입이나 상위 타입 중 어떤 타입으로 대치할 수 있는지 명시
  - **Example**. `Functions<? super T, ? extends R>`
  - 코틀린도 사용 지점 변성을 지원

<br/>

<pre>코틀린의 <b>사용 지점 변성 선언</b>은 자바의 <b>한정 와일드카드 (bounded wildcard)</b> 와 동일
코틀린 <code>MutableList<out T></code> 는 자바 <code>MutableList<? extends T></code> 와 동일
코틀린 <code>MutableList<in T></code> 는 자바 <code>MutableList<? super T></code> 와 동일</pre>

선언 지점 변성을 사용하면 변성 변경자를 단 한 번만 표시하고 클래스를 쓰는 쪽에서는 변성에 대해 신경을 쓸 필요가 없어서 코드가 더 간결

<br/>

**Example. 타입 파라미터가 둘인 데이터 복사 함수**

원본 리스트 원소 타입은 대상 리스트 원소 타입의 하위 타입이어야 함

<table>
<tr>
<th>두 번째 제네릭 타입 정의</th>
<th>더 우아하게 표현한 방식</th>
</tr>
<tr>
<td>

```kotlin
fun <T: R, R> copyData(source: MutableList<T>,
                       destination: MutableList<R>) {
    for (item in source) {
        destination.add(item)
    }
}
```

</td>
<td>

```kotlin
fun <T> copyData(source: MutableList<out T>,
                 destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}
```

</td></tr>
</table>

<br/>

#### 타입 프로젝션 _type projection_

\: 파라미터를 프로젝션(제약을 가한) 타입으로 만듦

- 타입 선언에서 타입 파라미터를 사용하는 위치에 변성 변경자를 붙일 수 있음
  - 위 `copyData` 함수는 `MutableList` 의 메서드 중에서 타입 파라미터 `T` 를 아웃 위치(반환)에만 사용할 수 있음 
  - 즉, 컴파일러는 타입 파라미터 `T` 를 인위치(함수 인자 타입)로 사용하지 못하게 막음

```kotlin
val list: MutableList<out Number> = mutableListOf()
list.add(42)
// Error: Out-projected type 'MutableList<out Number>' prohibits
// the use of 'fun add(element: E): Boolean'
```

<br/>

### 11.3.6 Star projection: Using the * character to indicate a lack of information about a generic argument

<small><i>스타 프로젝션 : 제네릭 타입 인자에 대한 정보가 없음을 표현하고자 `*` 사용</i></small>

- **스타 프로젝션** <sup>star projection</sup>: 제네릭 타입 인자 정보가 없음 표현
- 타입 인자 정보가 중요하지 않을 때 사용

- `MutableList<*>` ≠ `MutableList<Any?>`
  - `MutableList<*>`: 정확히 모르는 특정 구체적인 타입의 원소만을 담는 리스트 
    - 아무 원소나 다 담아도 된다는 뜻이 아님
    - `MutableList<*>` 타입의 리스트를 생성할 수 없음
  - `MutableList<Any?>`: 모든 타입의 원소를 담을 수 있음을 알 수 있는 리스트
  - `MutableList<T>`는 `T`에 대해 무공변성

 ```kotlin
val list: MutableList<Any?> = mutableListOf('a', 1, "qwe")
val chars = mutableListOf('a', 'b', 'c')
val unknownElements: MutableList<*> =
        if (Random.nextBoolean()) list else chars

println(unknownElements.first())                 // ✅ Any? 타입 원소 반환: "a"
unknownElements.add(42)
// 🚨 Error: Out-projected type 'MutableList<*>' prohibits
// the use of 'fun add(element: E): Boolean'
```

- 리스트의 원소 타입을 몰라도 `Any?` 타입의 원소를 꺼내올 수 있지만, 타입을 모르는 리스트에 원소를 마음대로 넣을 수는 없음
- `Any?` 는 모든 코틀린 타입의 상위 타입이기 때문
- Kotlin `MyType<*>` = Java `MyType<?>`

<br/>

#### 스타 프로젝션 사용 시 빠지기 쉬운 함정

- `FieldValidator` 에는 인 위치에만 쓰이는 타입 파라미터가 있음 (`FieldValidator`는 반공변성)

```kotlin
interface FieldValidator<in T> {
    fun validate(input: T): Boolean
}
```

<br/>

`String`과 `Int` 타입의 `FieldValidator` 구현

```kotlin
object DefaultStringValidator : FieldValidator<String> {
    override fun validate(input: String) = input.isNotEmpty()
}
 
object DefaultIntValidator : FieldValidator<Int> {
    override fun validate(input: Int) = input >= 0
}
```

<br/>

모든 타입의 검증기를 맵에 넣을 수 있어야 하므로 `KClass`를 키로 하고, `FieldValidator<*>`를 값으로 하는 맵을 선언 (`KClass`: 코틀린 클래스)

```kotlin
val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()
validators[String::class] = DefaultStringValidator
validators[Int::class] = DefaultIntValidator
```

<br/>

🚨 `String` 타입의 필드를 `FieldValidator<*>` 타입의 검증기로 검증할 수 없음

```kotlin
validators[String::class]!!.validate("")                     
// Error: Out-projected type 'FieldValidator<*>' prohibits
// the use of 'fun validate(input: T): Boolean'
```

<br/>

**방법 1:** 안전하진 않지만 타입 캐스팅을 하면 사용 가능

```kotlin
val stringValidator = validators[String::class] as FieldValidator<String>  // Warning: unchecked cast 경고 발생
println(stringValidator.validate(""))   // false
```

<br/>

**방법 2:** 검증기를 등록하거나 가져오는 작업을 수행할 때 타입을 제대로 검사하도록 캡슐화

- `Warning: unchecked cast 경고 발생` 여전히 발생하지만, `Validators` 객체가 맵 접근을 통제하기 때문에 맵에 잘못된 값이 들어가지 못하게 막음

```kotlin
object Validators {
    private val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()   // 외부 접근 불가
 
    fun <T: Any> registerValidator(
            kClass: KClass<T>, fieldValidator: FieldValidator<T>) {
        validators[kClass] = fieldValidator                 // 타입이 맞을 때만 키/값 쌍으로 입력
    }
 
    @Suppress("UNCHECKED_CAST")                            // FieldValidator<T> 캐스팅이 안전하지 않다는 경고를 무시
    operator fun <T: Any> get(kClass: KClass<T>): FieldValidator<T> =
        validators[kClass] as? FieldValidator<T>
                ?: throw IllegalArgumentException("No validator for ${kClass.simpleName}")
}
```

- `Validators` 의 제네릭 메서드가 항상 올바른 검증기를 돌려주기 때문에, 컴파일러가 잘못된 검증기를 쓰지 못하게 막음
- **안전하지 못한 코드의 위치를 한곳으로 한정** → 오사용 방지 + 안전하게 사용하도록 만들 수 있음

<br/>

### 11.3.7 Type aliases

<small><i>타입 별명</i></small>

- **타입 별명**<sup>type aliases</sup>: 기존 타입에 대해 다른 이름을 부여

```kotlin
typealias NameCombiner = (String, String, String, String) -> String    // typealias로 타입 별명 정의

fun combineAuthors(combiner: NameCombiner) {
    println(combiner("Sveta", "Seb", "Dima", "Roman"))
}
```

**사용:**

```kotlin
val bandCombiner: NameCombiner = { a, b, c, d -> "$a, $b & The Gang" }
combineAuthors(bandCombiner)                        // Sveta, Seb & The Gang
combineAuthors { a, b, c, d -> "$d, $c & Co."}      // Roman, Dima & Co.
```

<br/>

## 11.4 Summary

<small><i>요약</i></small>

- 코틀린 제네릭스는 자바와 아주 비슷해서, 제네릭 함수와 클래스를 자바와 비슷하게 선언할 수 있음
- **타입 소거** <sup>Type Erasure</sup>: 타입 인자가 실행 시점에 지워짐
  - 제네릭 타입의 타입 인자는 컴파일 시점에만 존재 (자바와 동일)
  - 제네릭 타입을 `is` 연산자로 검사할 수 없음
- 인라인 함수의 타입 파라미터를 `reified`로 표시해서 실체화
  - 실행 시점에 그 타입을 `is`로 검사하거나 `java.lang.Class` 인스턴스를 얻을 수 있음
- 변성은 **베이스 클래스가 같고 타입 파라미터가 다른 두 제네릭 타입 사이**의 상하위 타입 관계를 명시하는 방법
- 제네릭 클래스의 타입 파라미터가 **아웃 위치**에서만 사용되는 경우: 타입 파라미터를 `out` 으로 표시해서 공변성 명시 - 생성자
- 제네릭 클래스의 타입 파라미터가 **인 위치**에서만 사용되는 경우: 타입 파라미터를 `in` 으로 표시해서 반공변성 명시 - 소비자
- 공변성의 반대는 반공변성. 
  - 코틀린의 읽기 전용 `List` 인터페이스: **공변적** ← `List<String>`은 `List<Any>`의 하위 타입
  - `Function1<in P, out R>` 함수 인터페이스: **첫 번째 타입 파라미터**에 대해서는 **반공변적**, **두 번째 타입 파라미터**에 대해서는 **공변적**
    - `(Animal) -> Int` 는 `(Cat) -> Number` 의 하위 타입
    - 즉, 함수 타입은 함수 파라미터 타입에 대해서는 반공변적이며 함수 반환 타입에 대해서는 공변적
- 코틀린에서의 제네릭 클래스의 **공변성 정의 지점**:
  - **선언 지점 변성**: 전체적으로 지정
  - **사용 지점 변성**: 구체적인 사용 위치에서 지정
- **스타 프로젝션**: 제네릭 클래스의 타입 인자가 어떤 타입인지 정확히 모르거나 혹은 중요하지 않을 때 사용
- **타입 별명**: 타입에 대해 더 짧은 이름이나 다른 이름을 부여
  - 타입 별명은 컴파일 시점에 원래의 타입으로 치환.

