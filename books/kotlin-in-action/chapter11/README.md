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

문자열 리스트를 printsum 함수에 전달하면 발생하는 예외를 좀 더 설명
어떤 값이 List<Int>인지 검사할 수는 없으므로 I11egalArgumentException이 발생하지
는 않음. 따라서 as? 캐스트가 성공하고 문자열 리스트에 대해 Sum 함수가 호출
된다. sum 이 실행되는 도중에 예외가 발생한다. sum 은 Number 타입의 값을 리스트
에서 가져와 서로 더하려고 시도한다. 하지만 String을 Number 로 사용하려고 하면
실행 시점에 ClassCastException이 발생한다.
코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 is 검사를 수행하
게 허용할 수 있을 정도로 똑똑하다.
리스트 11.6 알려진 타입 인자를 사용해 타입 검사하기
fun printSum(c: Collection<Int>) { when ( c ) {
i s List‹Int> -> println("List sum: ${c.sum()}")
i s Set<Int> → > println("Set sum: #{c.sum()}") 컴파일 시점에 원소 타입이 알려져 있으므로...
| .•. 이 타입 검사는 올바르다.
fun main() {
printSum listof (1,2,3))
/ / L i s t sum: 6
printSum(setof (3,4,5))
/ / Set sum: 12
486
컵파일 시점에 원소의 타입에 대한 정보가 없었던 리스트 11. 5 와 달리 리스트 11.6
에서는 컴파일 시점에 C 컬렉션 ( 리스트나 결합 등 컬렉션 종류는 문제가 안 된다이 ITE 값을 저장
한다는 사실이 알려져 있으므로 C Listcmts 인지 검사할 수 있다.
일반적으로 코틀린 컴파일러는 여러분에게 어떤 검사가 위험하고 어떤 검사가 가
능한지 알려주고자 최대한 노력한다 ( 인전하지 못한 1. 검사는 금지하고 위험한 5 개스팅은 경고를
출력한다 , 따라서 컴파일러 경고의 의미와 어떤 연산이 안전한지 알아야 한다.
이미 언급한 것처럼 코틀린은 제네릭 합수의 본문에서 그 함수의 타입 인자를 가리
킬 수 있는 특별한 기능을 제공하지 않는다. 하지만 inline 함수 안에서만 타입
인자를 사용할 수 있다. 이제 그 기능을 살펴보

11.2. 2 실체화된 타입 파라미터를 사용하는 함수는 타입 인자를 실행 시점에 언급할
수 있다
앞에서 설명했지만 코틀린 제네릭 타입의 타입 인자 정보는 실행 시점에 지워진다.
따라서 제네릭 클래스의 인스턴스가 있어도 그 인스턴스를 만들 때 사용한 타입
인자를 알아낼 수 없다. 제네릭 함수의 타입 인자도 마찬가지다. 제네릭 함수가
호출돼도 그 함수의 본문에서는 호출 시 쓰인 타입 인자를 알 수 없다.
fun <T> isA(value: Any) = value i s T
/ / Error: Cannot check f o r instance o f erased type: T
이는 일반적으로는 사실이다. 하지만 이런 제약을 피할 수 있는 경우가 하나 있다.
인라인 함수의 타입 파라미터는 실체화된다. 이는 실행 시점에 인라인 함수의 실제
타입 인자를 알 수 있다는 뜻이다.
10.2 절에서 1nline 함수를 자세히 설명했다. 기억을 되살리기 위해 간단하게 다시
살펴보자. 어떤 함수에 inline 키워드를 붙이면 컴파일러는 그 함수를 호출한 식을
모두 함수를 구현하는 코드로 바꾼다. 합수가 람다를 인자로 사용하는 경우 그 함
11장 제네릭스 | 4 8 7
수를 인라인 함수로 만들면 람다 코드도 함께 인라이닝되고 그에 따라 익명 클래스
와 객체가 생성되지 않아서 성능이 더 좋아질 수 있다. 이번 절에서는 인라인 함수
가 유용한 다른 이유인 타입 인자 실체화를 설명한다.
방금 살펴본 i5A 함수를 인라인 함수로 만들고 타입 파라미터를 reified로 지정하
면 Value 의 타입이 T 의 인스턴스인지를 실행 시점에 검사할 수 있다.
리스트 11.7 실체화된 타입 파라미터를 사용하는 함수 정의하기
inline fun < reified T› isA(value: Any) = value i s T 이제는 이 코드가 컴파일된다.
fun main() {
println(isA<String>("abc"))
/ / true
printin(isA<String> (123))
/ / f a l s e
실체화된 타입 파라미터를 사용하는 데 있어 조금은 뻔하지 않은 예를 살펴보자.
실체화된 타입 파라미터를 활용하는 가장 간단한 예제 중 하나는 표준 라이브러리
함수인 filterisInstance다. 이 함수는 인자로 받은 컬렉션에서 지정한 클래스의
인스턴스만을 모아 만든 리스트를 반환한다. 다음 예제는 filterIsInstance 사용
법을 보여준다.
리스트 11.8 filterisInstance 표준 라이브러리 함수 사용하기
fun main() f
val items = listof("one", 2, "three")
printin(items.filterIsInstance<String>())
/ / [one, three]
문자열에만 관심이 있다면 이 함수의 타입 인자로 String 을 지정한다. 그 경우 이
4 8 8
함수의 반환 타입은 List string> 이 될 것이다. 여기서는 타입 인자를 실행 시점에
알수 있고 filterIsInstance 는 그 타입 인자를 사용해 리스트의 원소 중에 타입이
일치하는 원소만 추려낼 수 있다.
다음은 코틀린 표준 라이브러리에 있는 filterIsinstance 선언을 간단히 정리했다.
리스트 11.9 filterisInstance를 간단하게 정리한 버전
inline fun ‹ reified T›
Iterable‹\*>.filterIsInstance(): List<T> {
val destination = mutableListOf<T>()
for (element i n this) {
i f (element i s T) {
destination. add (element)
reified 키워드는 이 타입 파라미터가
실행 시점에 지워지지 않음을 표시한다.
각 원소가 타입 인자로 지정한 클래스의
인스턴스인지 검사할 수 있다.
return destination
인라인 함수에서만 실체화된 타입 인자를 쓸 수 있는 이유
그렇다면 실체화된 타입 인자는 어떻게 작동하는 걸까? 왜 일반 함수에서는 element is T 를 쓸 수
없고 인라인 함수에서만 쓸 수 있는 걸까?
10.2 절에서 설명했지만 컴파일러는 인라인 함수의 본문을 구현한 바이트코드를 그 함수가 호출되는
모든 지점에 삽입한다. 컴파일러는 실체화된 타입 인자를 사용해 인라인 함수를 호출하는 각 부분의
정확한 타입 인자를 알 수 있다. 따라서 컴파일러는 타입 인자로 쓰인 구체적인 클래스를 참조하는
바이트코드를 생성해 삽입할 수 있다. 결과적으로 리스트 11.8 의 filterIsInstance string> 호출
은 다음과 동등한 코드를 만들어낸다.
for (element i n t h i s ) {
i f (element i s String) 1
destination. add (element)
구체적인 클래스를 참조한다.
11장 제네릭스 | 4 8 9
만들어진 바이트코드는 타입 파라미터가 아니라 구체적인 타입을 사용하므로 실행 시점에 벌어지는
타입 소거의 영향을 받지 않는다.
자바 코드에서는 reified 타입 파라미터를 사용하는 1nline 함수를 호출할 수 없다는 점을 기억하
자. 자바에서는 코틀린 인라인 함수를 다른 보통 함수처럼 호출한다. 그런 경우 인라인 함수를 호출해
도 실제로 인라이닝이 되지는 않는다. 실체화된 타입 파라미터가 있는 함수의 경우 타입 인자 값을
바이트코드에 넣기 위해 더 많은 작업이 필요하다. 따라서 실체화된 타입 파라미터가 있는 인라이닝
함수를 일반 수처럼 자바에서 호출할 수는 없다.
인라인 함수에는 실체화된 타입 파라미터가 여럿 있거나 실체화된 타입 파라미터
와 실체화하지 않은 타입 파라미터가 함께 있을 수도 있다. 람다를 파라미터로 받
지 않지만 filterisInstance를 인라인 함수로 정의했다는 점에 유의하자. 10.2.4 절
에서 함수의 파라미터 중에 함수 타입인 파라미터가 있고 그 파라미터에 해당하는
인자( 대) 를 함께 인라이닝함으로써 얻는 이익이 더 큰 경우에만 함수를 인라인 함
수로 만들라고 했다. 하지만 이 경우에는 함수를 Inl1ne으로 만드는 이유가 성능
향상이 아니라 실체화된 타입 파라미터를 사용하기 위함이다.
성능을 좋게 하려면 인라인 함수의 크기를 계속 관찰해야 한다. 함수가 커지면 실
체화된 타입에 의존하지 않는 부분을 별도의 일반 함수로 뽑아내는 편이 낫다.
11.2.3 클래스 참조를 실체화된 타입 파라미터로 대신함으로써 java.lang.Class 파라
미터 피하기
java.lang.Class 타입 인자를 파라미터로 받는 API 에 대한 코틀린 어댑터를 구축
하는 경우 실체화된 타입 파라미터를 자주 사용한다. 그런 API 의 예로는 JDK 의
ServiceLoader 가 있다. Serviceloader는 어떤 추상 클래스나 인터페이스를 표현하
는 Java.lang•Class를 받아 그 클래스나 인스턴스를 구현한 인스턴스를 반환한다.
실체화된 타입 파라미터를 활용해 이런 API 를 쉽게 호출할 수 있도록 만드는 방법
을 살펴보자.
4 9 0
표준 자바 AP 인 ServiceLoader 를 사용해 서비스를 읽어 들이려면 다음 코드처럼
호출해야 한다.
val serviceImp1 = ServiceLoader. load(Service:: class. java)
: cass. Java 구문은 코틀린 클래스에 대응하는 Java, 1ang. chass 참조를 얻는 방
법을 보여준다. Service: class.Java라는 코드는 Service.class 라는 자바 코드와
완전히 같다. 이는 12.2 절에서 리플렉션을 설명할 때 더 자세히 다룬다.
이 예제를 실체화된 타입 파라미터를 사용해 서비스가 읽을 클래스를 1oadService
의 타입 파라미터로 지정해 다시 작성하자.
val serviceImpl = loadService<Service>()
훨씬 짧다. 이제는 읽어 들일 서비스 클래스를 1oadservice 함수의 타입 인자로
지정한다. 클래스를 타입 인자로 지정하면 ::class.java라고 쓰는 경우보다 훨씬
더 읽고 이해하기 쉽다.
이제 loadService 함수를 어떻게 정의할 수 있는지 살펴보자.
inline fun ‹ reified T> l o a d S e r v i c e 1
return ServiceLoader. load (T:: class. java)
• 타입 파라미터를 reiied로 표시한다.
T:class로 타입 파라미터의 클래스를 가져온다.
}
일반 클래스에 사용할 수 있는 ::class.java 구문을 이 경우에도 사용할 수 있다.
이를 통해 타입 파라미터로 지정된 클래스에 따른 java.lang.Class를 얻을 수 있고
그렇게 얻은 클래스 참조를 보통 때와 마찬가지로 사용할 수 있다.
안드로이드의 startActivity 함수 간단하게 만들기
안드로이드 개발자라면 익숙한 다른 예제를 찾을 수 있다. 액티비티를 표시하는 과정이 그런 예다.
액티비티의 클래스를 java.1ang.class로 전달하는 대신 실체화된 타입 파라미터를 사용할 수 있다.
11장 제네릭스 | 491
i n l i n e f u n ‹ r e i f i e d T : A c t i v i t y >

- 타입 파라미터를 reiled로 표시한다.
  Context. startActivity) {
  val intent = Intent (this, T::class.java) <
  startActivity(intent)
- T:cass로 타입 파라미터의 클래스를 가져온다.
  startActivity‹DetailActivity> <
- 액티비티를 표시하는 메서드를 호출한다.
  11.2.4 실체화된 타입 파라미터가 있는 접근자 정의
  인라인과 실체화된 타입 파라미터를 사용할 수 있는 코틀린 구성 요소가 함수만
  있는 것은 아니다. 2.2.2 절에서 살펴본 것처럼 프로퍼티 접근자에 대해 게터와 세
  터의 커스텀 구현을 만들 수 있다. 제네릭 타입에 대해 프로퍼티 접근자를 정의하
  는 경우 프로퍼티를 inline으로 표시하고 타입 파라미터를 reified로 하면 타입
  인자에 쓰인 구체적인 클래스를 참조할 수 있다.
  다음 예제는 canonical 이라는 확장 프로퍼티를 만든다. 이 프로퍼티는 제네릭 클래
  스의 표준적인 이름을 반환한다. 11.2.3절과 마찬가지로 이 예제도 T::class.java
  호출을 감싸서 CanonicalName 프로퍼티를 더 쉽게 사용할 수 있는 방법을 제공
  한다.
  inline val ‹ reified T› T.canonical: String
  g e t ) = T: :class.java.canonicalName
  fun main () {
  printin(listof(1, 2, 3). canonical)
  / / java.util.List
  printin (1. canonical)
  / / java.lang. Integer
