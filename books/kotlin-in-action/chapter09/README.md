# CHAPTER 9. Operator overloading and other conventions

<small><i>연산자 오버로딩과 다른 컨벤션</i></small>

어떤 언어 기능과 미리 정해진 이름의 함수를 연결하는 기법을 코린에서는 컨벤션 (Conventions) 라고 함

확장 함수 메커니즘을 사용하면 기존 클래스에 새로운 메서드를 추가할 수 있음


```kotlin
data class Point(val x: Int, val y: Int)
```

<br/>

## 9.1 Overloading arithmetic operators makes operations for arbitrary classes more convenient

<small><i>산술 연산자를 오버로드해서 임의의 클래스에 대한 연산을 더 편리하게 만들기</i></small>

- 자바에서는 오직 기본 타입에 대해서만 산술 연산자를 사용할 수 있음
  - 추가로 `String` 값에 대해 `+` 연산자를 사용할 수 있음
- 그러나 다른 클래스에서도 산술 연산자가 유용한 경우가 있음
  - 가령, `BigInteger` 클래스를 다룬다면 add 메서드를 명시적으로 효출하기보다는 `+` 연산을 사용하는 편이 더 나음

<br/>

### 9.1.1 Plus, times, divide, and more: Overloading binary arithmetic operations

<small><i>plus, times, divide 등: 이항 산술 연산 오버로딩</i></small>

**Example.** 두 점의 x 좌표와 y 좌표를 각각 더함

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {     // plus 라는 연산자 함수 정의
        return Point(x + other.x, y + other.y)   // 좌표를 성분별로 더한 새로운 점을 반환
    }
}
 
fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)                             // + 기호를 쓰면 plus 함수가 호출됨
    // Point(x=40, y=60)
}
```

연산자를 오버로당하는 함수 앞에는 반드시 `operator`가 있어야 함

- `operator` 변경자를 추가해 `plus` 함수를 선언하고 나면, `+` 기호로 두 `Point` 객체를 더할 때 `plus` 함수가 호출됨
  - `a + b` → `a.plus(b)`

확장 함수로 정의할 수도 있음

```kotlin
operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```

- 외부 함수의 클래스에 대한 연산자를 정의할 때는, 컨벤션을 따르는 이름의 확장 함수로 구현하는 것이 일반적인 패턴
- 코틀린에서는 직접 연산자를 만들어 사용할 수 없고, 미리 정해둔 컨벤션에 따른 연산자만 오버로딩할 수 있음

<br/>

#### 오버로딩 가능한 이항 산술 연산자

| Expression | Function name |
|------------|---------------|
| `a * b`    | times         |
| `a / b`    | div           |
| `a % b`    | mod           |
| `a + b`    | plus          |
| `a - b`    | minus         |


- 연산자 우선순위는 언제나 표준 숫자 타입에 대한 연산자 우선순위와 같음
- `*`, `/`, `%` 는 모두 우선순위가 같고, 이 세 연산자의 우선순위는 `+` 와 `-` 연산자보다 높음

<br/>

#### 📌 Operator functions and Java

<small><i>연산자 함수와 자바</i></small>

<table>
<tr><th>1. Kotlin의 연산자 오버로딩은 함수로 정의됨</th></tr>
<tr>
<td>

Kotlin에서 `operator` 키워드를 사용해 연산자를 오버로딩할 수 있고, 이 함수는 일반 함수처럼 Java에서 호출 가능.

✅ **예시** (Kotlin):

```kotlin
data class Point(val x: Int, val y: Int) {
   operator fun plus(other: Point): Point {
       return Point(x + other.x, y + other.y)
   }
}
```

✅ **Java에서 사용**:

```java
Point p1 = new Point(1, 2);
Point p2 = new Point(3, 4);
Point result = p1.plus(p2);  // 연산자 대신 함수로 호출
```

</td>
<tr><th>2. Kotlin에서 Java 메서드는 연산자처럼 사용 가능</th></tr>
<tr>
<td>

Java 메서드가 Kotlin의 연산자 이름 규칙을 따르기만 하면, Kotlin에서는 연산자 문법으로 호출 가능함.

✅ **Java 클래스**:

```java
public class Counter {
   private int value;

   public Counter(int value) {
       this.value = value;
   }

   public Counter plus(Counter other) {
       return new Counter(this.value + other.value);
   }
}
```

✅ **Kotlin에서 연산자처럼 사용**:

```kotlin
val a = Counter(5)
val b = Counter(3)
val result = a + b  // plus 메서드를 연산자처럼 사용
```

</td>
</tr>
<tr><th>3. Java에서는 연산자 키워드 없음 — 이름과 시그니처만 맞추면 됨</th></tr>
<tr>
<td>

Java는 `operator` 키워드가 없기 때문에, Kotlin에서 연산자 문법을 사용하려면 **함수 이름과 매개변수 수**만 맞추면 됨.

✅ Kotlin은 `operator fun plus(...)` 식으로 정의해야 연산자 문법이 가능하지만, Java는 이름만 `plus`면 됨.

</td>
</tr>
<tr><th>4. 이름이 다르면 Kotlin 확장 함수로 감쌀 수 있음</th></tr>
<tr>
<td>

Java 메서드 이름이 Kotlin 연산자 규칙과 다르다면, Kotlin에서 **확장 함수**를 만들어 연산자 문법으로 사용 가능.

✅ **Java 클래스**:
```java
public class Vector {
   public Vector add(Vector other) {
       return new Vector(); // 간단한 예시
   }
}
```

✅ **Kotlin 확장 함수 추가**:
```kotlin
operator fun Vector.plus(other: Vector): Vector {
   return this.add(other)
}

val v1 = Vector()
val v2 = Vector()
val result = v1 + v2  // add 대신 + 사용 가능
```

</td>
</tr>
</table>

<br/>

#### 서로 다른 타입의 두 피연산자

연산자를 정의할 때 두 피연산자가 같은 타입일 필요는 없음

**Example. Point * Double**

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
```

- `Point(10, 20) * 1.5` → `Point(x=15, y=30)`

<br/>

**✔️ 교환 법칙 성립 ❌**

코틀린 연산자가 자동으로 **교환법칙** <sup>commutativity</sup> (`a op b e` == `b op a` 인 성질) 을 지원하지는 않음

즉, 위 예시는 `Point * Double` 를 정의했지 `Double * Point`를 정의한 것은 아님

- `1.5 * Point(10, 20)` ❌

`operator tun Double.times(p: Point): Point` 를 정의해야 함

<br/>

**✔️ 반환 타입 제한 ❌**

연산자 함수의 반환 타입이 꼭 두 피연산자 중 하나와 일치해야 하는 것은 아님

```kotlin
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}
```

- `'a' * 3` → `"aaa"`

<br/>

#### 비트 연산자에 대해 특별한 연산자 함수를 사용하지 않는다.

코틀린은 표준 숫자 타입에 대해 비트 연산자를 정의하지 않음
따라서 커스텀 타입에서 비트 연산자를 정의할 수도 없음
대신에 중위 연산자 표기법을 지원하는 일반 함수를 사용해 비트 연산을 수행

커스텀 타입에서도 그와 비슷한 함수를 정의해 사용할 수 있다.
다음은 코틀린에서 비트 연산을 수행하는 함수의 목록이다.

- `shl`: Signed shift left. 왼쪽 시프트 (`<<`)
- `shr`: Signed shift right. 부호 유지 오른쪽 시프트 (`>>`)
- `ushr`: Unsigned shift right. 오른쪽 시프트 (0으로 부호 비트 설정, `>>>`)
- `and`: Bitwise and. 비트 곱 (`&`)
- `or`: Bitwise or. 비트 합 (`|`)
- `xor`: Bitwise xor. 비트 배타 합 (`^`)
- `inv`: Bitwise inversion. 비트 반전 (`~`)

가령, 아래와 같이 사용할 수 있음

- `0x0F and 0xF0` → `0`
- `0x0F or 0xF0` → `255`
- `0x1 shl 4` → `16`

<br/>

### 9.1.2 Applying an operation and immediately assigning its value: Overloading compound assignment operators

<small><i>연산을 적용한 다음에 그 결과를 바로 대입: 복합 대입 연산자 오버로딩</i></small>

- **복합 대입** <sup>compound assignment</sup> **연산자**: 연산과 할당을 합쳐놓은 식의 표현법

- 코틀린은 연산자 오버로딩 시, 관련 복합 대입 연산자도 자동으로 함께 지원
  - e.g. `plus` 오버로딩 시, `+` 연산자 뿐만 아니라, `+=` 등도 함께 지원  

```kotlin
var point = Point(1, 2)
point += Point(3, 4)
println(point)          // Point(x=4, y=6)
```

<br/>

#### 복합 대입 연산자 직접 구현

코틀린에서 `+=` 연산자는 `operator fun plusAssign(...): Unit`로 정의

다른 복합 대입 연산자 함수도 비슷하게 `minusAssign`, `timesAssign` 등의 이름을 사용

**Example. 변경 가능한 컬렉션에 원소를 추가하는 경우**

```kotlin
val numbers = mutableListOf<Int>()
numbers += 42
println(numbers[0])          // 42
```

실제 구현 함수를 보면 다음과 같음 

```kotlin
operator fun <T> MutableCollection<T>.plusAssign(element: T) {
    this.add(element)
}
```


이론적으로는 코드에 있는 `+-` 를 `plus` 와 `plusAssign` 양쪽으로 컴파일할 수 있음

- `a += b`
  1. `a = a.plus(b)`
  2. `a.plusAssign(b)`

- 어떤 클래스가 이 두 함수를 모두 정의하고 둘 다 `+=` 에 사용 가능한 경우 컴파일러는 오류를 던짐
- `plus` 와 `plusAssign` 연산을 동시에 정의하지 마라
- 어떤 클래스가 이 두 함수를 모두 정의하고 둘 다 `+=` 에 사용 가능한 경우 컴파일러는 오류를 던짐 (`demo/Figure09_02.kt` 참고)


#### 코틀린의 컬렉션에 대한 접근법

- `+`, `-`: 항상 새로운 컬렉션을 반환
- `+=`, `-=`: 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킴
- **읽기 전용 컬렉션**에서 `+=` 와 `=`: 변경을 적용한 복사본을 반환

<br/>

### 9.1.3 Operators with only one operand: Overloading unary operators

<small><i>피연산자가 1 개뿐인 연산자: 단항 연산자 오버로딩</i></small>

단항 연산자도 미리 정해진 함수 이름으로 operator 선언하면 됨

```kotlin
operator fun Point.unaryMinus(): Point {     // 파라미터 X
    return Point(-x, -y)
}
 
fun main() {
    val p = Point(10, 20)
    println(-p)                              // Point(x=-10, y=-20)
}
```

단항 연산자 오버로딩 함수는 인자를 취하지 않음

- `+a` → `a.unaryPlus()`

| Expression   | Function name     |
|--------------|-------------------|
| `+a`         | `unaryPlus`       |
| `-a`         | `unaryMinus`      |
| `!a`         | `not`             |
| `++a`, `a++` | `inc`             |
| `—a`, `a—`   | `dec`             |

<br/>

**Example. BigDecimal**

```
operator fun BigDecimal.inc() = this + BigDecimal.ONE

fun main() {
  var bd = BigDecimal.ZERO
  println(bd++)               // 0
  println(bd)                 // 1
  println(++bd)               // 2
}
```

<br/>

## 9.2 Overloading comparison operators makes it easy to check relationships between objects

<small><i>비교 연산자를 오버로딩해서 객체들 사이의 관계를 쉽게 검사</i></small>

<br/>

### 9.2.1 Equality operators: equals (`==`)

<small><i>동등성 연산자: equals</i></small>

- 코틀린은 `==`, `!=` 연산자 호출을 `equals` 메서드 호출로 컴파일
- `a == b` → `a?.equals(b) ?: (b == null)`


#### 동등성 identity equals 연산자 (`===`)

- `===`는 두 피연산자가 서로 같은 객체를 가리키는지 (원시 타입인 경우 두 값이 같은지) 비교
- 자바의 `==` 연산자와 동일

- `equals` 를 구현할 때는 `===` 를 사용해 자신과의 비교를 최적화하는 경우가 많음
  - `equals` 오버로딩 시에는 `override` 선언 필요 (Any 정의 메서드이기 때문)
- 하지만, `===` 를 오버로딩할 수는 없음

<br/>

### 9.2.2 Ordering operators: compareTo (`<`, `>`, `<=`, and `>=`)

<small><i>순서 연산자: compareTo (`<`, `>`, `<=`, and `>=`)</i></small>


자바에서 정렬이나 최댓값, 최솟값 등 값을 비교해야 하는 알고리듬에 사용할 클래스는 `Comparable` 인터페이스를 구현해야 함

- `Comparable`의 `compareTo` 메서드는 한 객체와 다른 개체의 크기를 비교해 정수로 반환
  - `element.compareTo(element2)` 를 명시적으로 사용
- 코틀린도 동일한 `Comparable` 인터페이스를 지원
  - 비교 연산자 (`<`, `>`, `<=`, `>=`)를 사용하는 코드를 `compareTo` 호출로 컴파일
  - 즉, `p1 < p2` 는 `p1.compareTo(p2) < 0` 와 동일


**Example.**

```kotlin
class Person(
        val firstName: String, val lastName: String
) : Comparable<Person> {
 
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other,
            Person::lastName, Person::firstName)
    }
}
 
fun main() {
    val p1 = Person("Alice", "Smith")
    val p2 = Person("Bob", "Johnson")
    println(p1 < p2)                    // false
}
```

`Comparable` 의 `compareTo` 에 `operator` 변경자가 붙어있기 때문에,
하위 클래스에서 오버라이드할 때 함수 앞에 `operator` 를 붙일 필요가 없음

> 처음에는 성능에 신경 쓰지 말고 이해하기 쉽고 간결하게 코드를 작성하고, 
> 나중에 그 코드가 자주 호출됨에 따라 성능이 문제가 되면 성능을 개선하라.

<br/>









