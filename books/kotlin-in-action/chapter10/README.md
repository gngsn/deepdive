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

내부에서 코틀린 함수 타입은 일반 인터

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




