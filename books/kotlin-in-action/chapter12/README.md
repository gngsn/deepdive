# CHAPTER 12. Annotations and reflection

<small><i>어노테이션과 리플렉션</i></small>

코틀린의 리플렉션 API 의 일반 구조는 자바와 같지만 세부 사항에서 약간 차이가 있음

<br/>

## 12.1 Declaring and applying annotations

<small><i>어노테이션 선언과 적용</i></small>

- 어노테이션을 사용하면 선언에 추가적인 메타데이터를 연관시킬 수 있음
- 소스코드, 컴파일된 클래스 파일, 런타임에서 해당 메타데이터에 접근할 수 있음

<br/>

### 12.1.1 Applying annotations to mark declarations

<small><i>어노테이션을 적용해 선언에 표지 남기기</i></small>

#### 어노테이션의 인자 지정 방법

- 사용 금지를 설명하는 메시지와 대체할 패턴 지정
- 일반 함수와 마찬가지로 인자를 괄호 안에 전달

```kotlin
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) { /* ... */ }
```

→ `@Deprecated`을 통해 `remove` 함수 사용을 지양하고, `removeAt`를 대체하라는 지시를 표현

<br/>

#### 코틀린의 어노테이션 인자 지정: Kotlin vs. Java

코틀린의 어노테이션 인자 지정 문법은 자바와 약간 다름

<br/>

<b>✔️ 클래스를 어노테이션 인자로 지정 <sup>Specifying a class as an annotation argument</sup></b>

\: `@MyAnnotation(MyClass::class)`처럼 `::class` 를 클래스 이름 뒤에 넣어야 함

<br/>

<b>✔️ 다른 어노테이션을 인자로 지정 <sup>Specifying another annotation as an argument</sup></b>

\: 인자로 들어가는 어노테이션의 이름 앞에 `@` 를 붙이지 않음
- e.g. <code>@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))</code> → `ReplaceWith`는 어노테이션이지만, `Deprecated` 어노테이션의 인자로 들어가므로 Replacewith 앞에 `@` 를 사용하지 않음 

<br/>

<b>✔️ 배열을 인자로 지정 <sup>Specifying an array as an argument</sup></b>

\: `@RequestMapping(path = ["/f00", "/bar"])`처럼 각괄호, 혹은 `arrayof` 함수를 사용할 수도 있음

자바에서 선언한 어노테이션 클래스를 사용하면, `value` 파라미터가 필요에 따라 자동으로 가변 길이 인자로 변환됨

<br/>

- 프로퍼티를 어노테이션 인자로 사용하려면 그 앞에 `const` 변경자를 붙여야 함
  - 어노테이션 인자를 컴파일 시점에 알 수 있어야 하기 때문
  - 컴파일러는 `const` 가 붙은 프로퍼티를 컴파일 시점 상수로 취급
  - 임의의 프로퍼티를 인자로 지정할 수는 없음

```kotlin
const val TEST_TIMEOUT = 10L
 
class MyTest {
    @Test
    @Timeout(TEST_TIMEOUT)
    fun testMethod() {
        // ...
    }
}
```

- 일반 프로퍼티를 어노테이션 인자로 사용하려 시도하면 오류 발생
  - e.g. `TEST_TIMEOUT` 상수에 `const` 를 빼면 "`Only const val can be used in constant expressions`" 컴파일 오류 발생
- `const`가 붙은 프로퍼티를 파일의 최상위나 object 안에 선언해야 하며 기본 타입이나 `String`으로 초기화해야만 함

<br/>

### 12.1.2 Specifying the exact declaration an annotation refers to: Annotation targets

<small><i>어노테이션이 참조할 수 있는 정확한 선언 지정: 어노테이션 타깃</i></small>

코틀린 소스코드에서 한 선언을 컴파일한 결과가 여러 자바 선언과 대응하는 경우가 자주 있음

**사용 지점 타깃<sup>use-site target</sup> 선언**을 통해 어노테이션을 붙일 요소를 정할 수 있음

<br/>

**Example. 사용 지점 타깃**

```
사용 지점 타깃
   ———
  @get:JvmName("obtainCertificate")
       ———————
     어노테이션 이름
```

- 사용 지점 타깃은 `@` 기호와 어노테이션 이름 사이에 위치하며 어노테이션 이름과는 콜론 (`:`)으로 어노테이션 이름과 구분됨


<table>
<tr>
<th>Method</th>
<th>Property</th>
</tr>
<tr>
<td>

```kotlin
@JvmName("performCalculation")
fun calculate(): Int {
    return (2 + 2) - 1
}
```

</td>
<td>

```kotlin
class CertificateManager {
  @get:JvmName("obtainCertificate")                        // Getter JVM 이름 지정
  @set:JvmName("putCertificate")                           // Setter JVM 이름 지정
  var certificate: String = "-----BEGIN PRIVATE KEY-----"
}
```

→ **자바**에서 `certificate` 프로퍼티를 `obtainCertificate`와 `putCertificate` 으로 호출


```java
class Foo {
  public static void main(String[] args) {
    var certManager = new CertificateManager();
    var cert = certManager.obtainCertificate();
    certManager.putCertificate("-----BEGIN CERTIFICATE-----");
  }
}
```

</td>
</tr>
</table>

<br/>

#### '사용 지점 타깃' 지원 목록

- `property`: 프로퍼티 전체
  - ⚠️ 자바에서 선언된 어노테이션에는 이 사용 지점 타깃을 지정할 수 없음
- `field`: 프로퍼티에 의해 생성되는 필드
- `get`: 프로퍼티 게터
- `set`: 프로퍼티 세터
- `receiver`: 확장 함수나 프로퍼티의 수신 객체 파라미터
- `param`: 생성자 파라미터
- `setparam`: setter 파라미터
- `delegate`: 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
- `file`: 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스
  - file 대상을 사용하는 어노테이션은 파일에서 `package` 선언보다 더 앞에만 넣을 수 있음
  - e.g. 최상위 선언을 담는 클래스의 이름을 바꿔주는 `@JvmName`: `@file:JvmName("StringFunctions")`

<br/>

#### 자바 API를 어노테이션으로 제어하기

- 코틀린은 **코틀린으로 선언한 내용을 자바 바이트코드로 컴파일하는 방법**과 **코틀린 선언을 자바에 노출 방법 제어**를 위한 많은 어노테이션을 제공
- 즉, 어노테이션을 사용하면 코틀린 선언을 자바에 노출시키는 방법을 변경할 수 있음
- 이런 어노테이션 중 일부는 자바 언어의 일부 키워드를 대신하기도 함
  - 예를 들어 `@volatile` 어노테이션은 자바의 `volatile` 키워드를 그대로 대신함

**대표 코틀린 어노테이션**

- `@JvmName`: 코틀린 선언이 만들어내는 자바 필드나 메서드 이름을 변경
- `@JvmStatic`: 객체 선언이나 동반 객체의 메서드에 적용하면 메서드가 자바 정적 메서드로 노출됨
- `@JvmOverloads` 를 사용하면 디폴트 파라미터 값이 있는 함수에 대해 컴파일러가 자동으로 오버로드 당한 함수를 생성해줌
- `@JvmField` 를 프로퍼티에 사용하면 대상 프로퍼티를 게터나 세터가 없는 공개된 (Public) 자바 필드로 노출시킴
- `@JvmRecord`: `data class` 에 사용하면 자바 레코드 클래스를 선언할 수 있음

<br/>

### 12.1.3 Using annotations to customize JSON serialization

<small><i>어노테이션을 활용해 JSON 직렬화 제어</i></small>

어노테이션을 사용하는 고전적인 예제: JSON 직렬화

코틀린 객체를 JSON 으로 변환하는 코틀린 라이브러리로 젯브레인즈의 `kotlinx.serialization`

- [**kotlinx.serialization**](http://github.com/kotlin/kotlinx.serialization)
- 자바 객체를 JSON 으로 변환하기 위해 설계된 대표 라이브러리와도 완전히 호환
  - [**Jackson**](https://github.com/FasterXML/jackson) 
  - [**Gson**](https://github.com/google/gson) 

[Kotlin in Action 2e: JKid Implementation 데모 코드 참고](https://github.com/Kotlin/kotlin-in-action-2e-jkid/tree/main/src/test/kotlin/examples)

##### Examples.

```kotlin
data class Person(val name: String, val age: Int)
```

<table>
<tr>
  <td>✔️ <b><code>serialize()</code></b>: 코틀린 객체 → JSON</td>
  <td></td>
  <td>✔️ <b><code>deserialize()</code></b>: JSON → 코틀린 객체</td>
</tr>
<tr>
<td>

```kotlin
import kia.jkid.serialization.serialize
// ...
val person = Person("Alice", 29)
println(serialize(person))          // {"age": 29, "name": "Alice"}
```

</td>
<td>

 직렬화
⎯⎯⎯⎯→
←⎯⎯⎯⎯
 역직렬화

</td>
<td>

JSON에 객체의 타입 정보가 들어있지 않기 때문에 타입 인자로 지정해야 함

```kotlin
import kia.jkid.deserialization.deserialize
// ...
val json = """{"name": "Alice", "age": 29}"""
println(deserialize<Person>(json))      // Person(name=Alice, age=29)
```

</td>
</tr>
</table>

[_1PersonExample.kt](https://github.com/Kotlin/kotlin-in-action-2e-jkid/blob/main/src/test/kotlin/examples/_1PersonExample.kt)

<br/>

✔️ **`@JsonName`**

- 어노테이션을 사용하면 프로퍼티를 표현하는 키/값 쌍의 이름 대신, **어노테이션이 지정한 문자열을 쓰게 할 수 있음**

✔️ **`@JsonExclude`**

- 어노테이션을 사용하면 직렬화나 **역직렬화할 때 무시해야 하는 프로퍼티를 표시할 수 있음**

```kotlin
data class Person(
    @JsonName("alias") val firstName: String,
    @JsonExclude val age: Int? = null
)
```

<br/>

### 12.1.4 Creating your own annotation declarations

<small><i>어노테이션 선언</i></small>

<br/>

#### 어노테이션을 선언하는 방법

- 일반 클래스 선언과 비슷하지만, `class` 키워드 앞에 `annotation` 이라는 변경자가 붙어있음

```kotlin
annotation class JsonName
```

- 어노테이션 클래스는 내부에 아무 코드도 들어있을 수 없음
  - 선언이나 식과 관련 있는 **메타데이터의 구조만 정의**하기 때문
  - 컴파일러가 어노테이션 클래스에서 본문을 정의하지 못하게 막음
- 파라미터가 있는 어노테이션을 정의하려면 어노테이션 클래스의 주 생성자에 파라미터를 선언해야 함
- 일반적인 주 생성자 구문을 사용하면서 모든 파라미터를 `val`로 선언

```kotlin
annotation class JsonName(val name: String)
```

#### vs. Java

자바 어노테이션 선언과의 비교
비교를 위해 같은 어노테이션을 자바로 선언한 경우 어떤 모습일지 다음에 적었다

```Java
/* Java */
public @interface JsonName {
    String value();
}
```

- 자바 어노테이션에는 `value` 라는 메서드가 있음
- 코틀린 어노테이션에는 `name` 이라는 프로퍼티가 있음
자바에서 value 메서드는 특별
어떤 어노테이션을 적용할 때 `value` 를 제외한 모든 애트리뷰트에는 이름을 명시해야 함


<br/>

### 12.1.5 Meta-annotations: Controlling how an annotation is processed

<small><i>메타어노테이션: 어노테이션을 처리하는 방법 제어</i></small>

표준 라이브러리에는 여러 메타어노테이션이 있으며 그런 메타어노테이션들
은 컴파일러가 어노테이션을 처리하는 방법을 제어

표준 라이브러리에서 가장 많이 사용하는 메타어노테이션: `@Target`

제이키드의 `JsonExclude` 와 `JsonName` 어노테이션도 적용 가능한 타깃을 지정하기 위해 `@Target` 을 사용

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude
```

- 어노테이션이 붙을 수 있는 타깃이 정의된 이넘은 `AnnotationTarget`
- 클래스, 파일, 프로퍼티, 프로퍼티 접근자, 타입, 식 등에 대한 이넘 정의가 되어 있음
  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.annotation/-annotation-target/#
- 둘 이상의 타깃을 한꺼번에 선언할 수도 있음
  - `@Target(AnnotationTarget.CLASS, AnnotationTarget.METHOD)`

<br/>

- **메타어노테이션** 생성을 위해 `ANNOTATION_CLASS` 를 타깃으로 지정

```kotlin
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class BindingAnnotation
 
@BindingAnnotation
annotation class MyBinding
```

<br/>

**⚠️ 대상을 `PROPERTY` 로 지정한 어노테이션을 자바 코드에서 사용할 수는 없음**

- 자바에서 타겟을 `PROPERTY` 로 지정한 코틀린 어노테이션을 사용하려면 `AnnotationTarget.FIELD`를 두 번째 타깃으로 추가해야 함
- 어노테이션을 코틀린 프로퍼티와 자바 필드에 적용할 수 있음

<br/>

#### `@Retention` 어노테이션

`@Retention` 은 정의 중인 어노테이션 클래스 접근 범위를 지정하는 메타어노테이션

- **소스 수준에서만 유지**할지,
- **`.class` 파일에 저장**할지,
- **실행 시점에 리플렉션을 사용해 접근할 수 있게 할지** 

<br/>

- **자바 컴파일러**는 기본적으로 어노테이션을 `.class` 파일에는 저장하지만 런타임에는 사용할 수 없게 함
- **코틀린**에서는 자바와 달리 `@Retention` 을 디폴트로 `RUNTIME` 으로 지정
  - 대부분의 어노테이션은 런타임에도 사용할 수 있어야 하기 때문 

지금까지 살펴본 예제에서 제이키드 어노테이션에 별도로 `@Retention` 메타어노테이션을 붙이지 않았지만, 
여전히 리플렉션을 통해 제이키드 어노테이션에 접근할 수 있음

<br/>

### 12.1.6 Passing classes as annotation parameters to further control behavior

<small><i>어노테이션 파라미터로 클래스 사용</i></small>

- 어떤 클래스를 선언 메타데이터로 참조할 수 있는 기능이 필요할 때 사용

**Example. `@DeserializeInterface`: 인터페이스를 구현하는 클래스를 지정하는 예제**

- 제이키드 라이브러리에 있는 `@DeserializeInterface`는 인터페이스 타입인 프로퍼티에 대한 역직렬화를 제어할 때 쓰는 어노테이션 
- 인터페이스의 인스턴스를 직접 만들 수는 없기 때문에
- 따라서 역직렬화 시 어떤 클래스를 사용해 인터페이스를 구현할지를 지정할 수 있어야 함

```kotlin
interface Company {
    val name: String
}
 
data class CompanyImpl(override val name: String) : Company
 
data class Person(
    val name: String,
    @DeserializeInterface(CompanyImpl::class) val company: Company
)
```

- `@DeserializeInterface` 어노테이션의 인자로 `CompanyImpl::class` 를 넘김
  - `CompanyImpl`의 인스턴스를 만들어 Person 인스턴스의 `company` 프로퍼티에 설정

<br/>

```kotlin
annotation class DeserializeInterface(val targetClass: KClass<out Any>)
```

- `KClass` 타입은 **코틀린 클래스에 대한 참조**를 저장
  - `KClass`의 인스턴스가 가리키는 코틀린 타입을 지정
  - e.g. 위 예시에서, `CompanyImpl::class`의 타입은 `KClass<CompanyImpl>`

```
    KClass<out Any>
          ↑      
          ⏐       
  KClass<CompanyImpl>
```

<br/>

### 12.1.7 Generic classes as annotation parameters

<small><i>어노테이션 파라미터로 제네릭 클래스 받기</i></small>

`@CustomSerializer` 어노테이션은 커스텀 직렬화 클래스에 대한 참조를 인자로 받음

이 직렬화 클래스는 ValueSerializer 인터페이스를 구현해야 함

```kotlin
interface ValueSerializer<T> {
    fun toJsonValue(value: T): Any?
    fun fromJsonValue(jsonValue: Any?): T
}
```

직렬화 로직을 Person 클래스에 적용하는 방법을

```kotlin
data class Person(
    val name: String,
    @CustomSerializer(DateSerializer::class) val birthDate: Date
)
```

`@CustomSerializer` 어노테이션을 구현

어노테이션이 어떤 타입에 대해 쓰일지 알 수 없으므로, 스타 프로젝션을 인자로 사용할 수 있음

```kotlin
annotation class CustomSerializer(
    val serializerClass: KClass<out ValueSerializer<*>>
)
```


- `ValueSerializer` 인터페이스를 구현하는 클래스만 인자로 받아야 함을 명시할 필요가 있음
- 예를 들어 Date 가 ValueSerializer 를 구현하지 않으므로 `@CustomSerializer(Date::class)` 라는 어노테이션을 금지시켜야 함 

약간 어려워 보이지만 다행히 
- 클래스를 어노테이션 인자로 받아야 할 때마다 같은 패턴을 사용할 수 있음
- `KClasss<out 자신의 클래스 이름<*>>` 을 쓰면 되고, 
- 자신의 클래스 이름 자체가 타입 인자를 받아야 한다면 `KClasss<out 자신의 클래스 이름<*>>` 처럼 타입 인자를 `*` 로 바꿈

<pre>
            <code>DateSerializer::class</code>는 OK ✅ 
                <code>Date::class</code>는 거부 ❌
               —————————————————————
       KClasss&lt;out ValueSerializer&lt;*&gt;&gt;
               ———                   ———
      모든 ValueSerializer    어떤 타입의 값이든 직렬화 가능
        구현 클래스를 받음
</pre>





