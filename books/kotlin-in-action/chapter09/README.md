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

## 9.3 Conventions used for collections and ranges

<small><i>컬렉션과 범위에 대해 쓸 수 있는 컨벤션</i></small>

<br/>

### 9.3.1 Accessing elements by index: The `get` and `set` conventions

<small><i>인덱스로 원소 접근 : `get`과 `set`</i></small>

- 맵에 접근할 때 각괄호 (`[]`) 를 사용
- **인덱스 접근 연산자**를 사용해 원소를 읽는 연산은 `get` 연산자 메서드로, 원소를 쓰는 연산은 `set` 연산자 메서드로 변환됨
- 코틀린에서는 이러한 **인덱스 연산**을 직접 구현할 수 있음: `operator fun get(...)`, `operator fun set(...)`

<br/>

#### `x[a, b]` → `x.get(a, b)`

**✅ `operator get` 구현 예시**

```kotlin
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```

**✅ 사용**

```kotlin
val p = Point(10, 20)
println(p[1])           // 20
```

<br/>

#### `x[a, b] = c` → `x.set(a, b, c)`

**Example. `operator set` 구현 예시**

```kotlin
data class MutablePoint(var x: Int, var y: Int)
 
operator fun MutablePoint.set(index: Int, value: Int) {
    when(index) {
        0 -> x = value
        1 -> y = value
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```

**✅ 사용**

```kotlin
val p = MutablePoint(10, 20)
p[1] = 42
println(p)                  // MutablePoint(x=10, y=42)
```

<br/>

### 9.3.2 Checking whether an object belongs to a collection: The `in` convention

<small><i>떤 객체가 컬렉션에 들어있는지 검사 : `in` 컨벤션</i></small>

- `in`은 객체가 컬렉션에 들어있는지 검사<sup>membership test</sup>: 대응 함수 `contains`

**Example. `operator set` 구현 예시**

```kotlin
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
  return p.x in upperLeft.x..<lowerRight.x &&           // 범위 생성 후 x좌표가 범위 내에 있는지 검사
  p.y in upperLeft.y..<lowerRight.y                     // ..< 연산자로 열린 범위 생성
}
```

**✅ 사용**

```kotlin
val rect = Rectangle(Point(10, 20), Point(50, 50))
println(Point(20, 30) in rect)          // true
println(Point(5, 5) in rect)            // false
```

- `a in c` → `c.contains(a)` 

<br/>

### 9.3.3 Creating ranges from objects: The `rangeTo` and `rangeUntil` conventions

<small><i>객체로부터 범위 만들기 : `rangeTo`와 `rangeUntil` 컨벤션</i></small>

<br/>

#### `rangeTo` 연산자 <sub>start ≤ x ≤ end</sub>

- `start..end` → `start.rangeTo(end)` (_start ≤ x ≤ end_)
- `rangeTo` 함수는 범위를 반환

`Comparable` 객체에 이미 `rangeTo` 함수가 있어서, `Comparable` 인터페이스 구현 시 따로 정의 할 필요가 없음

```kotlin
operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T>
```

<br/>

**Example. `LocalDate` 클래스로 날짜 범위 생성**

```kotlin
import java.time.LocalDate

fun main() {
  val now = LocalDate.now()
  val vacation = now..now.plusDays(10)    // now 부터 시작해 10 일짜리 범위 생성
  println(now.plusWeeks(1) in vacation)   // true
}
```

- `now..now.plusDays(10)` 식은 컴파일러에 의해 `now.rangeTo(now.plusDays(10))` 로 변환됨

<br/>

`rangeTo` 연산자는 다른 산술 연산자보다 우선순위가 낮음

- `0..(n + 1)` 는 `0..n + 1` 과 동일. (괄호로 더 뜻이 명확해짐)
- 우선순위가 낮은 이유로 메서드 호출은 괄호가 필요함
  -  ❌ `0..n.forEach {}` 컴파일 불가
  -  ✅ `(0..n).forEach {}`

<br/>

#### `rangeUntil` 연산자 <sub>start ≤ x < end</sub>

`rangeTo` 연산자와 비슷하게 `rangeUntil` 연산자(`..<`) 는 열린 범위를 만듦

- **Example**: `(0..n).forEach { print(it) }` ← `0123456789` 출력 

<br/>

### 9.3.4 Making it possible to loop over your types: The `iterator` convention

<small><i>자신의 타입에 대해 루프 수행 : `iterator` 컨벤션</i></small>

- `for (x in list) { ... }` → `list.iterator()`를 호출해서 이터레이터를 얻은 다음, 그 이터레이터에 대해 `hasNext` 와 `next` 호출을 반복하는 식으로 변환
- 코틀린 표준 라이브러리는 `String`의 상위 클래스인 `CharSequence`에 대한 `iterator` 확장 함수를 제공

```kotlin
operator fun CharSequence.iterator(): CharIterator
```

**직접 구현**

- `iterator` 함수가 `Iterator<LocalDate>` 인터페이스를 구현하는 객체를 반환해야만 함
- 즉, `hasNext` 와 `next` 함수 구현을 지정한 객체 선언을 사용


```kotlin
import java.time.LocalDate
 
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {                  // LocalDate 원소에 대한 Iterator 를 구현
        var current = start
 
        override fun hasNext() =
            current <= endInclusive                 // compareTo 관례를 사용해 날짜를 비교
 
        override fun next(): LocalDate {
            val thisDate = current
            current = current.plusDays(1)
            return thisDate
        }
    }
```

**✅ 사용**

```kotlin
val newYear = LocalDate.ofYearDay(2042, 1)
val daysOff = newYear.minusDays(1)..newYear
for (dayOff in daysOff) { println(dayOff) }
// 2041-12-31
// 2042-01-01
```

- `rangeTo` 라이브러리 함수는 `ClosedRange` 인스턴스 반환
- `ClosedRange<LocalDate>`에 대한 확장 함수 `iterator`를 정의했기 때문에 `LocalDate` 의 범위 객체를 for 루프에 사용할 수 있는 것

<br/>

### 9.4 Making destructuring declarations possible with component functions

<small><i>component 함수를 사용해 구조 분해 선언 제공</i></small>

- 구조 분해 선언의 각 변수를 초기화하고자 `componentN` 이라는 함수를 호출. 
  - `N` 은 구조 분해 선언에 있는 변수 위치에 따라 붙는 번호

- `val (a, b) = p` → `val a = p.component1()` and `val b = p.component2()`

**Example. `componentN` 정의 예시**

```
class Point(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}
```

<br/>

**Example. 데이터 클래스 구조 분해**

```kotlin
data class NameComponents(val name: String,
                          val extension: String)

fun splitFilename(fullName: String): NameComponents {
  val result = fullName.split('.', limit = 2)
  return NameComponents(result[0], result[1])
}

val (name, ext) = splitFilename("example.kt")     // name → "example", ext → "kt"
```

여러 값을 한꺼번에 반환해야 하는 함수가 있다면 반환해야 하는 모든 값이 들어갈 데이터 클래스를 정의하고 합수의 반환 타입을 그 데이터 클래스로 바꿈

<br/>

#### 구조 분해 + Collection 

크기가 정해진 컬렉션을 다루는 경우 구조 분해가 특히 더 유용함

```kotlin
data class NameComponents(
        val name: String,
        val extension: String)
 
fun splitFilename(fullName: String): NameComponents {
    val (name, extension) = fullName.split('.', limit = 2)  // 2개의 원소를 반환하는 split 함수 활용
    return NameComponents(name, extension)
}
```

- 함수 반환의 더 단순한 방법은 표준 라이브러리의 `Pair` 나 `Triple` 클래스를 사용
  - 클래스를 정의하지 않아도 되지만, 표현력을 잃게 됨
 
<br/>

### 9.4.1 Destructuring declarations and loops

<small><i>구조 분해 선언과 루프</i></small>

맵의 원소에 대해 이터레이션할 때 구조 분해 선언이 유용

**Example. Map 구조 분해**

```kotlin
for ((key, value) in map) {
    println("$key -> $value")
}
```

코틀린 표준 라이브러리에는 `Map` 확장 함수로 `iterator` 가 있고, 맵 항목에 대한 이터레이터를 반환함

`Map.Entry` 에 대한 확장 함수로 `component1` 과 `component2` 제공

위 `Map 구조 분해` 예시는 실제로 아래와 동등한 코드로 컴파일 됨

```kotlin
for (entry in map.entries) {
    val key = entry.component1()
    val value = entry.component2()
    // ...
}
```

<br/>

### 9.4.2 Ignoring destructured values using the `_` character

<small><i>`_` 문자를 사용해 구조 분해 값 무시</i></small>

**필요 없는 구조 분해한 필드**는 쓰이지 않는데 코드를 지저분하게 함

```kotlin
data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
)

fun introducePerson(p: Person) {
    val (firstName, lastName, age, city) = p
    println("This is $firstName, aged $age.")
}
```

- `lastName`, `city` 는 사용되지 않음

- 구조 분해 선언에서 뒤쪽의 구조 분해 선언을 제거할 수 있음
- 대신, **순서에 따라** 앞에 있는 요소만 가져오고 뒤만 버림

`val (firstName, lastName, age, city) = p` → `val (firstName, lastName, age) = p`

<br/>

**`_` 문자를 사용해 사용하지 않는 구조 분해 선언을 무시할 수 있음**

```kotlin
val (firstName, _, age) = p      // lastName → _ 으로 치환
```

<br/>

#### 코틀린 구조 분해의 한계와 단점

- 코틀린의 구조 분해 선언의 **한계**
  - : 구조 분해 연산의 결과가 오직 인자의 위치에 따라 결정된 필드 순서대로 대입된다는 점
- 코틀린의 구조 분해 선언은 '위치'에 의해 지정됨
  - 가령, `firstName`과 `lastName`이 서로 위치를 바꾸면 구조 분해문에서도 변경해야 함
- 따라서, 복잡한 엔티티에 대해 구조 분해 사용을 가능한 한 피해야 함
- 잠재적 해법은 이름 기반 구조 분해를 도입하는 것
  - 코틀린 값 클래스에 대한 이름 기반 구조 분해가 논의되고 있음
  - https://github.com/Kotlin/KEEP/blob/master/notes/value-classes.md#name-based-construction-of-classes 
  - 한편 2 개 이상의 필드가 있는 Value 클래스는 향후 코틀린 버전에 추가될 계획

<br/>

## 9.5 Reusing property accessor logic: Delegated properties

<small><i>프로퍼티 접근자 로직 재활용 : 위임 프로퍼티</i></small>

- 위임은 객체가 직접 작업을 수행하지 않고 다른 헬퍼 객체가 그 작업을 처리하도록 맡기는 디자인 패턴
- **위임 프로퍼티**는 더 복잡한 방식으로 작동하는 프로퍼티를 접근자 로직을 매번 재구현할 필요 없이 쉽게 구현할 수 있음

<br/>

### 9.5.1 Basic syntax and inner workings of delegated properties

<small><i>위임 프로퍼티의 기본 문법과 내부 동작</i></small>

**일반적인 위임 프로퍼티 문법**:

`var p: Type by Delegate()`

- `p` 프로퍼티는 접근자 로직을 `Delegate` 인스턴스에게 위임

아래 예시로 내부 동작 살펴보기

```kotlin
class Foo {
    var p: Type by Delegate()
}
```

- 컴파일러는 숨겨진 헬퍼 프로퍼티를 만들고 그 프로퍼티를 위임 개체의 인스턴 초기화
- `p` 프로퍼티는 바로 그 위임 재체에게 자신의 작업을 위임

```kotlin
class Foo {
    private val delegate = Delegate()     // 컴파일러가 생성한 도우미 프로퍼티
 
    var p: Type                           // p 프로퍼티를 위해 컴파일러가 생성한 접근자는 delegate 의 getValue 와 setValue 호출
       set(value: Type) = delegate.setValue(/* ... */, value)
       get() = delegate.getValue(/* ... */)
}
```

`Delegate` 클래스는 `getValue` 와 `setValue` 메서드를 제공해야 하며, 
변경 가능한 프로퍼티(`var` 로 선언한 경우)만 `setValue` 를 사용

- 선택적으로 provideDelegate 함수 구현 가능
  - `provideDelegate`: 최초 생성 시 검증 로직을 수행하거나 위임이 인스턴스 화되는 방식을 변경할 수 있음

```kotlin
class Delegate {
    operator fun getValue(/* ... */) { /* ... */ }
 
    operator fun setValue(/* ... */, value: Type) { /* ... */ }
 
    operator fun provideDelegate(/* ... */): Delegate { /* ... */ }   // 위임 객체를 생성하거나 제공하는 로직을 담음
}
 
 
class Foo {
    var p: Type by Delegate()                                         // by 키워드로 프로퍼티와 위임 객체 연결
}
 
fun main() {
    val foo = Foo()                                                   // 위임 객체에 provideDelegate 가 있으면 해당 함수를 호출해 위임 객체를 생성
    val oldValue = foo.p                                              // delegate.getValue(...) 호출
    foo.p = newValue                                                  // delegate.setValue(...) 호출
}
```


<br/>

### 9.5.2 Using delegated properties: Lazy initialization and `by lazy()`

<small><i>위임 프로퍼티 사용 : `by lazy()`를 사용한 지연 초기화</i></small>

**지연 초기화** <sup>Lazy initialization</sup> 는 객체 일부를 처음에 초기화하지 않고, 그 부분의 값이 필요할 경우 초기화할 때 쓰이는 패턴

```kotlin
class Person(val name: String) {
    private var _emails: List<Email>? = null
 
    val emails: List<Email>
       get() {
           if (_emails == null) {
               _emails = loadEmails(this)      // 최초 접근 시 이메일 로딩
           }
           return _emails!!                    // 로딩 이후 가져온 이메일 반환
       }
}
```

**백킹 프로퍼팅**: `_emails` 라는 프로퍼티는 값을 저장하고 다른 프로퍼티인 `emails` 는 `_emails` 라는 프로퍼티에 대한 읽기 연산을 제공

→ 코틀린의 **위임 프로퍼티**를 사용하면 이 코드가 훨씬 더 간단해짐

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```

- `lazy` 함수의 인자는 값을 초기화할 때 **호출할 람다**임
- `lazy` 함수는 기본적으로 스레드 세이프

<br/>

### 9.5.3 Implementing your own delegated properties

<small><i>위임 프로퍼티 구현</i></small>

어떤 객체의 프로퍼티가 바필 때마다 리스너에게 변경 통지를 보내고 싶을 떄 → **옵저버블** <sup>Observable</sup>

- `Oservable` 클래스는 `Observer` 들의 리스트를 관리. 
- `notifyObservers` 호출 시, 옵저버는 등록된 모든 `Observer` 의 `onChange` 함수를 통해 프로퍼티의 이전 값과 새 값을 전달

```kotlin
fun interface Observer {
    fun onChange(name: String, oldValue: Any?, newValue: Any?)
}
 
open class Observable {
    val observers = mutableListOf<Observer>()
    fun notifyObservers(propName: String, oldValue: Any?, newValue: Any?) {
        for (obs in observers) {
            obs.onChange(propName, oldValue, newValue)
        }
    }
}
```

<br/>

**Example. 1️⃣ `Observer` 확장 → `Person`**

나이나 급여가 바뀌면 그 사실을 리스너에게 통지

<small><i>Figure09_03.kt 참고</i></small>

```kotlin
class Person(val name: String, age: Int, salary: Int): Observable() {
    var age: Int = age
        set(newValue) {
            val oldValue = field                                  // 뒷받침하는 필드에 접근할 때 field 식별자 사용
            field = newValue
            notifyObservers(                                      // 옵저버들에게 프로퍼티 변경 통지
                "age", oldValue, newValue
            )
        }
 
    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            notifyObservers(
                "salary", oldValue, newValue
            )
        }
}
```

하지만, `setter` 에 중복이 많이 보임

**Example. 2️⃣ `ObservableProperty` 구현**

<small><i>Figure09_04.kt 참고</i></small>

프로퍼티의 값을 저장하고 필요에 따라 통지를 보내주는 클래스를 추출: `ObservableProperty`

```kotlin
class ObservableProperty(
  val propName: String,
  var propValue:
  Int,
  val observable: Observable
) {
  fun getValue(): Int = propValue
  fun setValue(newValue: Int) {
    val oldValue = propValue
    propValue = newValue
    observable.notifyObservers(propName, oldValue, newValue)
  }
}
```

`ObservableProperty` 구현

```kotlin
class Person(val name: String, age: Int, salary: Int): Observable() {
  val _age = ObservableProperty("age", age, this)
  var age: Int
    get() = _age.getValue()
    set(newValue) {
      _age.setValue(newValue)
    }

  val _salary = ObservableProperty("salary", salary, this)
  var salary: Int
    get() = _salary.getValue()
    set(newValue) {
      _salary.setValue(newValue)
    }
}
```

하지만 여전히 위임하는 코드가 상당히 필요

코틀린의 위임 프로퍼티 기능을 활용하면 이런 준비 코드를 없앨 수 있음

- 위임 프로퍼티를 사용하기 전, 코틀린의 컨벤션에 맞게 정의해야 함

```kotlin
import kotlin.reflect.KProperty
 
class ObservableProperty(var propValue: Int, val observable: Observable) {
  operator fun getValue(thisRef: Any?, prop: KProperty<*>): Int = propValue
 
  operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: Int) {
      val oldValue = propValue
      propValue = newValue
      observable.notifyObservers(prop.name, oldValue, newValue)
    }
}
```

**Example. 3️⃣ 위임 프로퍼티**

```kotlin
class Person(val name: String, age: Int, salary: Int) : Observable() {
    var age by ObservableProperty(age, this)
    var salary by ObservableProperty(salary, this)
}
```

- `by` 오른쪽에 오는 객체를 **위임 객체** 라고 함 
- 위임 객체를 감춰진 프로퍼티에 저장하고, 주 객체의 프로퍼티를 읽거나 쓸 때마다 위임 객체의 `getValue`와 `setValue` 호출

<br/>

#### `kotlin.properties.Delegates`

- 코틀린 표준 라이브러리에는 `ObservableProperty`와 비슷하게 제공해주는 옵저버블 로직을 사용할 수 있음
- 다만 이 표준 라이브러리의 클래스는 앞에서 정의한 `Observable` 와는 연결돼 있지 않음
- 따라서 프로퍼티 값의 변경을 통지받을 때 쓰일 다를 그 표준 라이브러리 클래스에게 넘겨야 함


```kotlin
import kotlin.properties.Delegates
 
class Person(val name: String, age: Int, salary: Int) : Observable() {
    private val onChange = { property: KProperty<*>, oldValue: Any?,
      newValue: Any? ->
        notifyObservers(property.name, oldValue, newValue)
    }
 
    var age by Delegates.observable(age, onChange)
    var salary by Delegates.observable(salary, onChange)
}
```

<br/>

### 9.5.4 Delegated properties are translated to hidden properties with custom accessors

<small><i>위임 프로퍼티는 커스텀 접근자가 있는 감춰진 프로퍼티로 변환된다</i></small>

**코틀린 컴파일러가 `by Delegate`를 처리하는 방식** 

```kotlin
class C {
    var prop: Type by MyDelegate()
}
 
val c = C()
```

컴파일러는 위의 코드를 아래 코드로 생성

```kotlin
class C {
    private val <delegate> = MyDelegate()
 
    var prop: Type
       get() = <delegate>.getValue(this, <property>)
       set(value: Type) = <delegate>.setValue(this, <property>, value)
}
```

- `<delegate>` : `MyDelegate` 클래스의 인스턴스를 저장하는 감춰진 프로퍼티
- `<property>`: 컴파일러는 프로퍼티를 표현하기 위해 `KProperty` 타입의 객체

컴파일러는 모든 프로퍼티 접근자 안에 `getValue`, `setValue` 호출 코드를 생성해줌

- `val x = c.prop` → `val x = <delegate>.getValue(c, <property>)`
- `c.prop = x` → `<delegate>.setValue(c, <property>, x)`


<br/>

### 9.5.5 Accessing dynamic attributes by delegating to maps

<small><i>맵에 위임해서 동적으로 애트리뷰트 접근</i></small>

자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용 하는 경우

→ `C#` 에서의 확장 가능한 객체 <sup>expando object</sup>

<br/>

**Example. 값을 맵에 저장하는 프로퍼티 정의하기**

```kotlin
class Person {
    private val _attributes = mutableMapOf<String, String>()
 
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
 
    var name: String
        get() = _attributes["name"]!!     // 맵 속성 꺼냄
        set(value) {
            _attributes["name"] = value   // 맵에 속성 입력
        }
}
```

**위임 프로퍼티를 활용하도록 변경**

```kotlin
class Person {
    private val _attributes = mutableMapOf<String, String>()
 
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
 
    var name: String by _attributes     // 위임 프로퍼티로 맵을 사용
}
```

표준 라이브러리가 `Map` 과 `MutableMap` 인터페이스에 대해 `getValue` 와 `setValue` 확장 함수를 제공하기 때문에 가능

<br/>

### 9.5.6 How a real-life framework might use delegated properties

<small><i>실전 프레임워크가 위임 프로퍼티를 활용하는 방법</i></small>

객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있으면 프레임워크를 개발할 때 유용

```kotlin
object Users : IdTable() {                           // 데이터베이스 테이블에 해당
    val name = varchar("name", length = 50).index()  // 프로퍼티는 테이블 칼럼에 해당
    val age = integer("age")
}
 
class User(id: EntityID) : Entity(id) {              // 각 User 인스턴스는 테이블에 들어있는 구체적인 엔티티에 해당
    var name: String by Users.name                   // 데이터베이스에 저장된 사용자의 이름 값
    var age: Int by Users.age
}
```

- User 의 프로퍼티에 접근할 때 자동으로 Entry 클래스에 정의된 데이터베이스 매핑으로부터 필요한 값을 가져오고 
- 객체를 변경하면 그 객체가 변경<sup>dirty</sup> 상태로 변하고
- 프레임워크가 나중에 적절히 데이터베이스에 변경 내용을 반영해줌

`name` 과 `age`는 Column 타입인데, 아래 처럼 `getValue`, `setValue` 정의가 되어있음

```kotlin
operator fun <T> Column<T>.getValue(o: Entity, desc: KProperty<*>): T  { 
    /* retrieve the value from the database  */ }
operator fun <T> Column<T>.setValue(o: Entity, desc: KProperty<*>, value: T)  { 
    /* update the value in the database  */ }
```

완전한 구현을 EXposed 프레임워크 소스코드에서 볼 수 있음
- https://github.com/JetBrains/Exposed

<br/>

## Summary

- 코틀린은 정해진 컨벤션에 따라 함수를 정의해서 수학 연산을 오버로드할 수 있음 
- 비교 연산자 (`=`, `!=`, `>`, `<` 등 ) 를 모든 객체에 사용할 수 있음
  - 비교 연산자는 `equals`와 `compareTo` 메서드 호출로 변환됨
- 자신이 정의한 클래스의 인스턴스에 대해 `[]` 와 `in` 연산을 사용할 수 있음
  - 단, 해당 클래스에 `get`, `set`, `contains` 함수 정의 필수
- 미리 정해진 컨벤션을 따라 범위를 만들거나 컬렉션과 배열의 원소를 이터레이션할 수 있음
  - `rangeTo`, `rangeUntil`
- 구조 분해 선언을 통해 한 객체의 상태를 분해해서 여러 변수에 대입할 수 있음 
  - 데이터 클래스에 대해 구조 분해를 사용할 수 있음
  - 혹은, 클래스에 `componentN` 함수를 정의하면 구조 분해를 지원할 수 있음
- **위임 프로퍼티**를 통해 프로퍼티 값을 저장하거나 초기화하거나 읽거나 변경할 때 사용하는 로직을 재활용할 수 있음
- 표준 라이브러리 함수인 `lazy` 를 통해 지연 초기화 프로퍼티를 쉽게 구현할 수 있음
- `Delegates.observable` 함수를 사용하면 프로퍼티 변경을 관찰할 수 있는 옵저버를 쉽게 추가할 수 있음
- 맵을 위임 객체로 사용하는 위임 프로퍼티를 통해 다양한 속성을 제공하는 객체를 유연하게 다룰 수 있음

