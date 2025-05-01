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
