# CHAPTER 5. Programming with lambdas

<small><i>람다를 사용한 프로그래밍</i></small>

<br/>

람다식 (lambda expression) 또는 람다: 다른 함수에 넘길 다른 함수에 넘길 수 있는 작은 코드 조각

## 5.1 Lambda expressions and member references

<small><i>람다식과 멤버 참조</i></small>

### 5.1.1 Introduction to lambdas: Blocks of code as values

<small><i>람다 소개: 코드 블록을 값으로 다루기</i></small>

익명 내부 클래스를 사용하면 코드를 함수에 넘기거나 변수에 저장할 수 있기는 하지만 상당히 번거로움

→ 함수를 값처럼 다루기
: 클래스를 선언하고 그 클래스의 인스턴스를 함수에 넘기는 대신, 함수를 직접 다른 함수에 전달할 수 있음

<br/>

> ### 함수형 프로그래밍
> - **First-class functions**: 함수를 값으로 다룰 수 있음. 
>   - 함수를 변수에 저장하거나 파라미터로 전달, 함수를 반환할 수 있음 
>   - 코틀린에서 람다는 함수를 일급 시민으로 다룸
> - **Immutability**: 불변성. 생성된 후에는 내부 상태가 변하지 않음을 보장하는 방법으로 설계 가능 
> - **No side effects**: 함수가 똑같은 입력에 대해 항상 같은 출력을 내놓고 다른 객체나 외부 세계의 상태를 변경하지 않게 구성.
>   - 파라미터 외에는 외부 세계로 부터 영향을 받지 않음
>   - 순수 함수

<br/>

<table>
<tr>
<th>Object 선언</th>
<th>Lambda</th>
</tr>
<tr>
<td>
<pre><code lang="kotlin">button.setOnClickListener(object: OnClickListener {
  override fun onClick(v: View) {
    println("I was clicked!")
  }
})
</code></pre>
</td>
<td>
<pre><code lang="kotlin">button.setOnClickListener {
  println("I was clicked!")
}
</code></pre>
</td>
</tr>
</table>

<br/>

### 5.1.2 Lambdas and collections

<small><i>람다와 컬렉션</i></small>

중복 제거는 중요한 코드 개선 중 하나 

<pre><code>data class Person(val name: String, val age: Int)
</code></pre>


<table>
<tr>
<th>Object 선언</th>
<th>Lambda</th>
</tr>
<tr>
<td>
<pre><code lang="kotlin">
fun findTheOldest(people: List<Person>) {
    var maxAge = 0                        ❶
    var theOldest: Person? = null         ❷
    for (person in people) {
        if (person.age > maxAge) {        ❸
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    findTheOldest(people)
    // Person(name=Bob, age=31)
}
</code></pre>
</td>
<td>
<pre><code lang="kotlin">
fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxByOrNull { it.age })                        ❶
    // Person(name=Bob, age=31)
}
</code></pre>
</td>
</tr>
<tr>
<td>
❶ 가장 큰 나이를 저장<br/>
❷ 가장 나이가 많은 사람을 저장<br/>
❸ 더 나이가 많으면 최댓값 변경
</td>
<td>
❶ age 비교로 가장 큰 원소 찾기
</td>
</tr>
</table>

람다가 단순히 함수나 프로퍼티에 위임할 경우에는 멤버 참조를 사용할 수 있음

<table>
<tr>
    <th>멤버 참조를 사용해 컬렉션 검색</th>
</tr>
<tr>
    <td><pre><code lang="kotlin">people.maxByOrNull(Person::age)</code></pre></td>
</tr>
</table>

<br/>

### 5.1.3 Syntax for lambda expressions

<small><i>람다식의 문법</i></small>

**람다식을 선언하기 위한 문법**

<br/><img src="./img/figure05-1.png" width="40%" /><br/>

코틀린 람다식은 중괄호 `{}` 로 둘러싸여 있고, 화살표 (`->`)로 파라미터와 본문을 구분


**람다식을 변수에 저장**

```kotlin
fun main() {
    val sum = { x: Int, y: Int -> x + y }
    println(sum(1, 2))
    // 3
}
```

**람다식을 직접 호출**

```kotlin
fun main() {
    { println(42) } // 42
}
```

위의 경우, 읽기 어렵고 그다지 쓸모 없음

→ `run` 사용

`run`은 인자로 받은 람다를 실행해 주는 라이브러리 함수


```kotlin
fun main() {
    run { println(42) } // 42
}
```

**식**이 필요한 부분에 **코드 블록**을 실행하고 싶을 때 아주 유용

```kotlin
val myFavoriteNumber = run {
    println("I'm thinking!")
    println("I'm doing some more work...")
    42
}
```

이 때, `run`은 호출에 부가 비용 없이 비슷한 성능을 냄 (10장 2절 참고)

<br/>

<table>
<tr>
<td>1</td>
<td>
정식 람다 표기법:

<pre><code lang="kotlin">people.maxByOrNull<b>({ p: Person -> p.age })</b></code></pre></td>
</tr>
<tr>
<td>2</td>
<td>
코틀린 문법: 함수 호출 시 맨 뒤에 있는 인자가 람다식이면, 그 람다를 괄호 밖으로 빼낼 수 있음

<pre><code lang="kotlin">people.maxByOrNull<b>()</b> { p: Person -> p.age }</code></pre>
</td>
</tr>
<tr>
<td>3</td>
<td>
코틀린 문법: 람다가 어떤 함수의 유일한 인자이고 괄호 밖에 람다를 썼다면, 호출 시 빈 괄호를 없애도 됨

<pre><code lang="kotlin">people.maxByOrNull { p<b>: Person</b> -> p.age }</code></pre>
</td>
</tr>
<tr>
<td>4</td>
<td>
파라미터 타입을 반드시 명시할 필요 없음: 컴파일러는 로컬 변수처럼 람다 파라미터의 타입도 추론할 수 있기 때문

<pre><code lang="kotlin">people.maxByOrNull { <b>p -> p</b>.age }</code></pre>

람다를 **변수에 저장할 때**는 파라미터의 타입을 추론할 문맥이 존재하지 않기 때문에 **파라미터 타입을 명시해야 함**.

<pre><code lang="kotlin">val getAge = { p: Person -> p.age }
people.maxByOrNull(getAge)</code></pre>
</td>
</tr>
<tr>
<td>5</td>
<td>
람다 파라미터 명을 디폴트 이름인 `it`으로 변경

<pre><code lang="kotlin">people.maxByOrNull <b>{ it.age }</b></code></pre>

람다 파라미터 이름을 따로 지정하지 않은 경우에만 it 이라는 이름이 자동으로 만들어짐
</td>
</tr>
<tr>
<td>6</td>
<td>
멤버 참조를 통해 더 짧게 쓸 수 있음

<pre><code lang="kotlin">people.maxByOrNull(Person::age)</code></pre>
</td>
</tr>
</table>


본문이 여러 줄로 이뤄진 경우 본문의 맨 마지막에 있는 식이 람다의 결과 값이 됨

```kotlin
fun main() {
    val sum = { x: Int, y: Int ->
       println("Computing the sum of $x and $y...")
       x + y
    }
    println(sum(1, 2))
    // Computing the sum of 1 and 2...
    // 3
}
```

<br/>

### 5.1.4 Accessing variables in scope

<small><i>현재 영역에 있는 변수 접근</i></small>

함수 내 람다 선언 시, **람다 본문에서 외부 영역인 함수의 파라미터와 로컬 변수를 참조할 수 있음**

<pre><code lang="kotlin">
fun printMessagesWithPrefix(messages: Collection&lt;String&gt;, <b>prefix: String</b>) {
    messages.forEach {
        println("<b>$prefix</b> $it")
    }
}
</code></pre>

`forEach` Lambda 함수 내에서 외부 함수 `printMessagesWithPrefix`의 파라미터인 `prefix` 참조

- 자바 람다는 final 변수만 참조 가능
- 코틀린 람다는 final 변수가 아니어도 참조 가능

<br/>

#### 코틀린에서 변경 가능한 변수를 참조할 수 있는 방법: Capturing a mutable variable

```kotlin
fun main() {
    val counter = 0
    val inc = { counter.value++ }    // 공식적으로는 변경 불가능한 변수를 캡처했지만 그 변수가 가리키는 객체의 필드 값을 바꿀 수 있음
}
```

위 코드는 실제로 아래 코드로 동작함 

```kotlin
class Ref<T>(var value: T)           // 변경 가능한 변수를 캡처하는 방법을 보여주기 위한 클래스
 
fun main() {
    val counter = Ref(0)
    val inc = { counter.value++ }    // 공식적으로는 변경 불가능한 변수를 캡처했지만 그 변수가 가리키는 객체의 필드 값을 바꿀 수 있음
}
```

#### 🚨주의할 함정

람다를 이벤트 핸들러나 비동기적으로 실행되는 코드로 활용하는 경우, **변수 변경은 람다 실행 내에서만 일어남**.

```Kotlin
fun tryToCountButtonClicks(button: Button): Int {
    var clicks = 0
    button.onClick { clicks++ }
    return clicks                   // ← 항상 0 반환
}
```

<br/>

### 5.1.5 Member references

<small><i>멤버 참조</i></small>

**멤버 참조**(member reference): 이미 선언된 함수의 경우, 이중 콜론(`::`)을 사용해 해당 함수를 값으로 바꿔 인자로 직접 넘길 수 있음.

`::` 은 클래스 이름과 참조하려는 멤버 (프로퍼티나 메서드) 이름 사이에 위치

```kotlin
people.maxByOrNull(Person::age)                        // 두 표현식은
people.maxByOrNull { person: Person -> person.age }    //         동일함
```

<br/>

#### 최상위 선언 함수 및 프로퍼티

최상위에 선언된 함수나 프로퍼티도 참조 가능

```Kotlin
fun salute() = println("Salute!")
 
fun main() {
    run(::salute)      // Salute!
}
```

<br/>

#### 인자가 여러 개인 함수의 작업 위임 시

```Kotlin
val action = { person: Person, message: String ->
    sendEmail(person, message)
}
val nextAction = ::sendEmail                    // 람다 대신 멤버 참조 사용 가능
```

<br/>

#### 생성자 참조 (constructor reference) 

클래스 생성 작업을 연기하거나 저장 가능

```kotlin
data class Person(val name: String, val age: Int)
 
fun main() {
    val createPerson = ::Person         // 인스턴스 생성 동작을 값으로 저장
    val p = createPerson("Alice", 29)
}
```

<br/>

#### 확장 함수

확장 함수도 동일한 방법으로 참조 가능

```kotlin
fun Person.isAdult() = age >= 21
val predicate = Person::isAdult
```

<br/>

### 5.1.6 Bound callable references

<small><i>값과 엮인 호출 가능 참조</i></small>
 
- 멤버 참조 구문과 형태 동일
- 특정 객체 인스턴스에 대한 메서드 호출에 대한 참조를 만들 수 있음

```Kotlin
fun main() {
    val seb = Person("Sebastian", 26)
    
    // 멤버 참조
    val personsAgeFunction = Person::age
    println(personsAgeFunction(seb))        // → 26
    
    // 바운딩된 멤버 참조
    val sebsAgeFunction = seb::age
    println(sebsAgeFunction())              // → 26
}
```


