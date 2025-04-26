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





10:30 -> 11:10 -> ... 2hours ... 13:30시 출발 -> 15:00시~17:00시 -> 19:30시 



<br/>

### 10.1.4 Parameters with function types can provide defaults or be nullable

<small><i></i></small>


<br/>

### 10.1.5 Returning functions from functions

<small><i></i></small>


<br/>

### 10.1.6 Making code more reusable by reducing duplication with lambdas

<small><i></i></small>












