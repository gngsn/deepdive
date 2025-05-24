# CHAPTER 10. Higher-order functions:Lambdas as parameters and return values

<small><i>고차 함수: 람다를 파라미터와 반환값으로 사용</i></small>

<br/>

## 10.1 Declaring functions that return or receive other functions: Higher-order functions

<small><i>다른 함수를 인자로 받거나 반환하는 함수 정의: 고차 함수</i></small>

- **고차 함수**: 다른 함수를 인자로 받거나 함수를 반환하는 함수
- 람다나 함수 참조를 사용해 함수를 값으로 표현할 수 있음
- Example: <code>list.filter <b>{ it > 0 }</b></code>

<br/>

### 10.1.1 Function types specify the parameter types and return values of a lambda

<small><i>함수 타입은 람다의 파라미터 타입과 반환 타입을 지정한다</i></small>

람다 파라미터의 선언 타입 방법

```
 << 코틀린 함수 타입 문법 >> 

(Int, string) -> Unit
—————————————    ————
 파라미터 타입     반환 타입
```

**Examples.**

```
val sum = { x, y -> x + y }      // (Int, Int) -> Int 
val action = { println(42) }     // () -> Unit 
```

<br/>

Null 이 될 수 있는 함수 타입 변수를 정의 가능

**Examples.**

```
var canReturnNull: (Int, Int) -> Int? = { x, y -> null }
```

<br/>

### 10.1.2 Calling functions passed as arguments

<small><i>인자로 전달 받은 함수 호출</i></small>

고차 함수 구현법

<pre lang="kotlin">
fun twoAndThree(<b>operation: (Int, Int) -> Int</b>) {
    val result = <b>operation(2, 3)</b>
    println(result)
}
</pre>

인자로 받은 함수를 호출하는 구문은 일반 함수를 호출하는 구문과 같음

```kotlin
twoAndThree { a, b -> a + b }   // 5
twoAndThree { a, b -> a * b }   // 6
```

<br/>

<table><tr><td>

#### 파라미터 이름과 함수 타입

함수 타입에서 파라미터 이름을 지정할 수도 있지만, 검사하지 않음

<br/>

**Example. 파라미터 명 `operandA`, `operandB` 지정**

```kotlin
fun twoAndThree(
    operation: (operandA: Int, operandB: Int) -> Int
) { ... }

twoAndThree { operandA, operandB -> operandA + operandB }
```

- 파라미터 이름은 타입 검사 시 무시됨
- 람다를 정의할 때, 파라미터 이름이 반드시 함수 타입 선언의 파라미터 이름과 일치하지 않아도 됨

```kotlin
twoAndThree { alpha, beta -> alpha + beta }
```

- 하지만 인자 이름을 추가 시, 코드 가독성이 좋아지고 IDE를 통한 자동 완성에 활용 가능

</td></tr></table>

<br/>

#### filter 함수 내부 구현

```
                  파라미터 이름 
                  —————————
fun String.filter(predicate: (Char) -> Boolean): String
    ——————                   ——————————————————
  수신 객체 타입                   파라미터 함수 타입
```

- String 타입의 filter 단순 버전 구현

```kotlin
fun String.filter(predicate: (Char) -> Boolean): String {
    return buildString {
        for (char in this@filter) {               // 입력 문자열을 한 문자씩 이터레이션
            if (predicate(char)) append(char)     // predicate 파라미터로 전달받은 함수를 호출
        }
    }
}

println("ab1c".filter { it in 'a'..'z' })     // abc
```

<br/>

### 10.1.3 Java lambdas are automatically converted to Kotlin function types

<small><i>자바에서 코틀린 함수 타입 사용</i></small>

- 자동 SAM 변환을 통해 코틀린 람다를 함수형 인터페이스를 요구하는 자바 메서드에게 넘길 수 있음
  - [🔗 5.2 Using Java functional](https://github.com/gngsn/deepdive/tree/main/books/kotlin-in-action/chapter05#52-using-java-functional)
- 코틀린 코드가 자바 라이브러리에 의존할 수 있고, 자바에서 정의된 고차 함수를 문제없이 사용 가능

리스트처럼 자바 람다는 자동으로 코틀린 합수 타입으로 변환됨


<table>
<tr>
    <th>Kotlin declaration</th>
    <th>Java call</th>
</tr>
<tr>
<td>

```kotlin
fun processTheAnswer(f: (Int) -> Int) {
  println(f(42))
}
```

</td>
<td>

```java
processTheAnswer(number -> number + 1); 
// 43
```

</td>
</tr>
</table>


- 자바 코드가 깔끔하진 않음
  - 수신 객체를 명시적으로 전달해야 함 
  - 코틀린 Unit 타입에는 값이 존재하기 때문에, 자바에서 Unit을 명시적으로 반환해줘야 함

```java
/* Java */
import kotlin.collections.CollectionsKt;
 
CollectionsKt.forEach(strings, s -> {    // 코틀린 표준 라이브러리 함수 호출
   return Unit.INSTANCE;                 // Unit 명시적 반환 필요: void 불가
});
```

<table><tr><td>

#### 함수 타입 : 자세한 구현

[FunctionN: Function0 ~ Function22](https://github.com/JetBrains/kotlin/blob/master/libraries/stdlib/jvm/runtime/kotlin/jvm/functions/Functions.kt)

함수 타입의 변수는 함수에 대응하는 FunctionN 인터페이스를 구현하는 클래스의 인스턴스

`invoke` 메서드에는 람다가 들어감
내부적으로 대략 다음처럼 생겼다는 뜻

```kotlin
// kotlin code
fun processTheAnswer(f: (Int) -> Int) {
  println(f(42))
}

// actual behavior
fun processTheAnswer(f: (Int) -> Int) {
  println(f.invoke(42))
}
```

<code>FunctionN</code> 인터페이스는 컴파일러가 생성한 합성 타입 (코틀린 표준 라이브러리에서 이들의 정의를 찾을 수 없음)
대신 컴파일러는 필요할 때 이런 인터페이스들을 생성해줌
→ 개수 제한 없이 파라미터를 정의하여 사용할 수 있음

<pre><b>Function22 이후부터는 어떻게 될까 ❓</b>
<a href="./demo/functionN_Args.kt">functionN_Args.kt</a> 파일에서 테스트
- 인자 22개일 때: <code>kotlin.jvm.functions.Function22<? super java.lang.Integer,...,></code>
- 인자 23개 부터: <code>kotlin.jvm.functions.FunctionN<java.lang.Integer></code>
- 가변 인자일 때: <code>kotlin.jvm.functions.Function1<? super java.lang.Integer[], java.lang.Integer>)</code>
</pre>
</td></tr></table>

<br/>

### 10.1.4 Parameters with function types can provide defaults or be nullable

<small><i>함수 타입의 파라미터에 대해 기본값을 지정할 수 있고, 널이 될 수도 있다</i></small>

**'함수 파라미터'에 대한 기본값 지정 가능**

```kotlin
fun funA(
  param: () -> String = { it.toString() }
) { ... }
```

<br/>

**Example. `joinToString`**

```kotlin
fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = ""
): String {
    val result = StringBuilder(prefix)
 
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
 
    result.append(postfix)
    return result.toString()
}
```

- **동작**: 항상 객체를 toString 메서드를 통해 문자열로 바꿈
- **한계**: 컬렉션의 각 원소를 문자열로 변환하는 방법 제어 불가
- **해결**: 함수 타입의 파라미터에 대한 기본값으로 람다식을 넣음


```kotlin
fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }         // 함수 타입 파라미터를 선언 시, 디폴트 람다 지정
): String {
    val result = StringBuilder(prefix)
 
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))                    // tranform 파라미터 함수 호출
    }
 
    result.append(postfix)
    return result.toString()
}
```
```kotlin
letters.joinToString()                                   // 디폴트 값 사용: Alpha, Beta 
println(letters.joinToString { it.lowercase() })         // 람다 넘김: alpha, beta
println(letters.joinToString(
  separator = "! ",
  postfix = "! ",
  transform = { it.uppercase() }))                       // 람다 넘김: ALPHA! BETA!
```

<br/>


🚨 **널이 될 수 있는 합수 타입으로 합수를 받으면 그 함수를 직접 호출할 수 없음**

- NPE 발생 가능성 때문에 컴파일 불가

- **해결 1**: null 여부를 명시적으로 검사해야 함
    - ```kotlin
      fun foo(callback: (() -> Unit)?) {
        // ...
        if (callback != null) {
          callback()
        }
      }
      ```
- **해결 2**: 안전한 호출을 통해 `invoke` 함수 사용
  - ```kotlin
    fun <T> Collection<T>.joinToString(
        ...
        transform: ((T) -> String)? = null
    ): String {
      ...
      val str = transform?.invoke(element) ?: element.toString()
      result.append(str)
      ...
    }
    ```


### 10.1.5 Returning functions from functions

<small><i>함수를 함수에서 반환</i></small>

- 함수를 반환하는 함수를 정의해 사용할 수 있음
  - 프로그램의 상태나 다른 조건에 따라 달라질 수 있는 로직

<br/>

**Example. 사용자가 선택한 배송 수단에 따라 배송비를 계산하는 로직**

```kotlin
enum class Delivery { STANDARD, EXPEDITED }
 
class Order(val itemCount: Int)
 
fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {  // 반환 타입: 반환할 함수 타입
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }                   // 람다 반환
    }
 
    return { order -> 1.2 * order.itemCount }                           // 람다 반환
}
```

```kotlin
val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
println("Shipping costs ${calculator(Order(3))}")                   // Shipping costs 12.3
```

<br/>

### 10.1.6 Making code more reusable by reducing duplication with lambdas

<small><i>람다를 활용해 중복을 줄여 코드 재사용성 높이기</i></small>

- `Sitevisit`: 방문한 사이트의 경로 , 사이트에서 머문 시간 , 사용자의 운영체제가

```kotlin
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)
```
 
- OS: 운영체제 이넘<sup>enum</sup>

```kotlin
enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }
```

- `averageWindowsDuration`: 윈도우 사용자의 평균 방문 시간 출력

```kotlin
val log = listOf(
  SiteVisit("/", 34.0, OS.WINDOWS),
  SiteVisit("/", 22.0, OS.MAC),
  SiteVisit("/login", 12.0, OS.WINDOWS),
  SiteVisit("/signup", 8.0, OS.IOS),
  SiteVisit("/", 16.3, OS.ANDROID)
)

val averageWindowsDuration = log
    .filter { it.os == OS.WINDOWS }
    .map(SiteVisit::duration)
    .average()
```

- **맥 사용자**에 대해 통계 구하고 싶어서, OS를 파라미터로 입력받도록 수정

```kotlin
fun List<SiteVisit>.averageDurationFor(os: OS) =
        filter { it.os == os }.map(SiteVisit::duration).average()
```

**결과:**
 
```kotlin
println(log.averageDurationFor(OS.WINDOWS))     // 23.0
println(log.averageDurationFor(OS.MAC))         // 22.0
```

더 복잡한 요구사항은, 간단한 파라미터로 처리할 수 없음

**가령,** 
- 모바일 디바이스 사용자의 평균 방문 시간?
- iOS 사용자의 `/signup` 페이지 평균 방문 시간?

→ 함수 타입으로 필요 조건을 파라미터 뽑기

<br/>

```kotlin
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
        filter(predicate).map(SiteVisit::duration).average()
```

**결과:**

```kotlin
log.averageDurationFor { it.os in setOf(OS.ANDROID, OS.IOS)}         // 12.15
log.averageDurationFor { it.os == OS.IOS && it.path == "/signup" }   // 8.0
```

<br/>

## 10.2 Removing the overhead of lambdas with inline functions

<small><i>인라인 함수를 사용해 람다의 부가 비용 없애기</i></small>

- 코틀린은 보통, **람다를 익명 클래스로 컴파일** (5장 참고)
- 람다식마다 새로운 클래스가 생기고 람다가 변수를 캡처한 경우 람다 정의가 포함된 코드를 호출하는 시점마다 새로운 객체가 생김
- 부가 비용 발생 
- 반복되는 코드를 별도 함수로 빼면서 효율적으로 코드를 실행하는 방법: **`inline` 변경자**
  - 컴파일러는 **`inline` 변경자**가 붙은 함수가 쓰이는 위치에, 함수 호출을 생성하는 대신, 함수를 구현하는 코드로 바꿔치기 해줌

<br/>

### 10.2.1 Inlining means substituting a function body to each call site

<small><i>인라이닝이 작동하는 방식</i></small>

- 함수의 inline 선언: **함수의 본문이 인라인**이 됨
  - 다른 말로, 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트코드로 컴파일한다는 뜻

다중 스레드 환경에서 어떤 공유 자원에 대한 동시 접근을 막기 위한 것
이 함수는 Lock 객체를 잠그고 주어진 코드 블록을 실행한 다음에 Lock 객체에 대한 잠금 해제

```kotlin
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
 
inline fun <T> synchronized(lock: Lock, action: () -> T): T {   // inline 함수를 호출하는 부분은
    lock.lock()                                                 // 해당 함수의 본문으로 치환
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

fun main() {
    val l = ReentrantLock()
    synchronized(l) {
        // ...
    }
}
```

> ✔️ 락을 건 상태에서 코드를 실행해야 한다면 `withLock` 을 우선 고려해야 함

<table>
<tr>
<th>`Synchronized()` 사용 예제</th>
<th>컴파일 버전</th>
</tr>
<tr>
<td>

```kotlin
fun foo(l: Lock) {
    println("Before sync")
    synchronized(l) {
        println("Action")
    }
    println("After sync")
}
```

</td>
<td>

<pre lang="kotlin">fun __foo__(l: Lock) {
    println("Before sync")
    <b>l.lock()                 // 해당 함수의 본문으로 치환
    try {
      println("Action")
    } finally {
      l.unlock()
    }</b>
    println("After sync")
}
</pre>

</td>
</tr>
</table>

<br/>

람다가 아닌, 함수 타입의 변수를 넘기면?

<pre lang="kotlin">class LockOwner(val lock: Lock) {
    fun runUnderLock(<b>body: () -> Unit</b>) {
        synchronized(lock, <b>body</b>)        // 람다가 아닌 파라미터로 받은 함수 넘김
    }
}
</pre>

- 위 코드는 `synchronized` 함수의 본문만 인라이닝됨
- **람다 본문은 인라이닝되지 않음**
  - 인라인 함수를 호출하는 코드 위치에서는 **변수에 저장된 람다의 코드를 알 수 없기 때문**

`runUnderLock` 은 아래와 비슷하게 컴파일 됨

```kotlin
class LockOwner(val lock: Lock) {
    fun __runUnderLock__(body: () -> Unit) {
        lock.lock()
        try {
            body()           // synchronized 호출부에서의 람다를 알 수 없으므로
        }                    // 인라이닝되지 않음
        finally {
            lock.unlock()
        }
    }
}
```

- 하나의 인라인 함수를 **두 개의 다른 람다에서 호출**하면, **두 호출은 각각 따로 인라이닝**됨
  1. **인라인 함수의 본문 코드가 호출 지점에 복사**되고,
  2. **각 람다의 본문**이 **인라인 함수의 본문 코드에서 람다를 사용하는 위치에 복사**됨


> 함수에 더해 프로퍼티 접근자 (get, set) 에도 inline을 붙일 수 있음
> 코틀린을 실체화한 제네릭<sup>reified generic</sup> 에서 유용      ← 11장 참고

<br/>

### 10.2.2 Restrictions on inline functions

<small><i>인라인 함수의 제약</i></small>

- 파라미터로 받은 람다를 호출 시, 쉽게 람다 본문으로 바꿀 수 있음
- 하지만 파라미터로 받은 람다를 변수에 저장 후, 나중에 그 변수를 사용하면, 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없음

<br/>

**일반적으로 인라인 함수를 사용할 수 있는 경우**

- 인라인 함수의 본문에서 람다식을 바로 호출
- 다른 인라인 함수의 인자로 전달하는 경우

**하지만 이외의 경우, 컴파일러는 인라이닝을 금지: `"legal usage of inline-parameter"`**

```kotlin
class FunctionStorage {
    var myStoredFunction: ((Int) -> Unit)? = null
    inline fun storeFunction(f: (Int) -> Unit) {
        myStoredFunction = f                       // 전달된 파라미터 저장
    }
}
// 🚨 Illegal usage of inline-parameter
// 'f' in 'public final inline fun storeFunction(f: (Int) -> Unit): Unit defined in README. FunctionStorage'.
// Add 'noinline' modifier to the parameter declaration
```

예를 들어, 시퀀스에 대해 동작하는 메서드 중에는 람다를 받아 모든 시퀀스 원소에 그 람다를 적용한 새 시퀀스를 반환하는 함수가 많음

<br/>

**Example. `Sequence.map` 정의를 보여줌**

**`map` 함수는** 
- ❌ `transform` 파라미터로 전달받은 함수 값을 호출하지 않는 대신, 
- ✅ `TransformingSequence` 라는 클래스의 생성자에게 그 함수 값을 넘김

둘 이상의 람다를 인자로 받는 함수에서 일부 다만 인라이닝하고 싶을 때도 있음

예를 들어 어떤 람다에 너무 많은 코드가 들어가거나 어떤 람다에 인라이닝을 하면 안 되는 코드가 들어갈 가능성이 있는 경우가 그렇다. 
이런 식으로 인라이닝하면 안 되는 람다를 파라미터로 받는다면 noinl1ne 변경자를 파라미터 이름 앞에 붙여 인라이닝을 금지할 수 있음

<br/>

### 10.2.3 Inlining collection operations

<small><i></i></small>

<br/>

### 10.2.4 Deciding when to declare functions as inline

<small><i></i></small>

<br/>

### 10.2.5 Using inlined lambdas for resource management with withLock, use, and useLines

<small><i></i></small>

<br/>

## 10.3 Returning from lambdas: Control flow in higher-order functions

<small><i></i></small>

<br/>

### 10.3.1 Return statements in lambdas: returning from an enclosing function

<small><i></i></small>

<br/>

### 10.3.2 Returning from lambdas: Return with a label

<small><i></i></small>

<br/>

### 10.3.3 Anonymous functions: Local returns by default

<small><i></i></small>

<br/>

## Summary

<small><i></i></small>




















