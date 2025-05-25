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

<br/>

## 12.2 Reflection: Introspecting Kotlin objects at run time

<small><i>리플렉션: 실행 시점에 코틀린 객체 내부 관찰</i></small>

- **리플렉션**: 실행 시점에<sub>동적으로</sub> 객체의 프로퍼티와 메서드에 접근
- 컴파일러는 지정된 이름을 참조해 코드 내에 선언된 속성을 찾음
- 컴파일러는 정적으로 (컴파일 시점에) 해당하는 선언을 찾고, 동시에 실제 존재함을 보장할 수 있음
  - 하지만, 실행 시점에만 알 수 있는 경우도 있음

**대표 코틀린 리플렉션 API**
- `kotlin.reflect`, `kotlin.reflect.full` 패키지
- 코틀린은 **자바 리플렉션 API** `java.lang.reflect` 지원
  - 리플렉션을 사용하는 자바 라이브러리와 코틀린 코드가 완전히 호환

<br/>

### 12.2.1 The Kotlin reflection API: `KClass`, `KCallable`, `KFunction`, and `KProperty`

<small><i>코틀린 리플렉션 API: `KClass`, `KCallable`, `KFunction`, `KProperty`</i></small>

- `::class` 식을 통해 KClass 인스턴스를 얻을 수 있음
- 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업이 가능 

<br/>

**Example**. 클래스에 포함된 모든 프로퍼티 명 확인

```kotlin
import kotlin.reflect.full.*
 
class Person(val name: String, val age: Int)
 
fun main() {
    val person = Person("Alice", 29)
    val kClass = person::class
    println(kClass.simpleName)
    // Person
    kClass.memberProperties.forEach { println(it.name) }
    // age
    // name
}
```

<br/>

KClass 인터페이스에 다양한 메서드 정의되어 있음

```
interface KClass<T : Any> {
    val simpleName: String?
    val qualifiedName: String?
    val members: Collection<KCallable<*>>
    val constructors: Collection<KFunction<T>>
    val nestedClasses: Collection<KClass<*>>
    // ...
}
```
자세한 정보는 Kotlin KClass 공식 안내문서 [kotlinlang: kotlin-stdlib/kotlin.reflect/KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/) 참조

<pre>
<code>simpleName</code> 과 <code>qualifiedName</code> 프로퍼티는 익명 객체에 대해 널 값을 반환함
- 익명 객체 생성 시에는 여전히 클래스의 인스턴스지만 익명 클래스임
- 이런 경우 <code>simpleName</code> 과 <code>qualifiedName</code> 모두 존재하지 않아, 필드 접근 시 <code>null</code> 반환

Example. 

<code>val anonymousObject = object {
    val x = 1
}

println(anonymousObject::class.simpleName) // null
println(anonymousObject::class.qualifiedName) // null
</code></pre>

<br/>

#### `KCallable`의 `call()` 메소드

- `KClass`에 정의된 `members` 필드는 `Collection<KCallable<*>>` 타입
- `KCallable` 은 `call` 메소드를 가짐

```kotlin
interface KCallable<out R> {
    fun call(vararg args: Any?): R
    // ...
}
```

- `call`을 사용하면 함수나 프로퍼티의 `Getter`나 메소드를 호출할 수 있음

<br/>

>[!NOTE]
> 아래 두 패키지의 차이점이 뭘까?
> 
> 주요 차이점은 **플랫폼 독립적인 코드**와 **JVM에 특화된 코드** 사이의 분리
>
> - [kotlin/libraries/stdlib/src/kotlin/reflect/KCallable.kt](https://github.com/JetBrains/kotlin/blob/whyoleg/dokka2-sync-stdlib/libraries/stdlib/src/kotlin/reflect/KCallable.kt#L13)
>   - 플랫폼에 독립적인 공통 코드를 포함
>   - 모든 Kotlin 지원 플랫폼에서 사용 (e.g. JVM, JS, Native)
>
> 
> - [kotlin/libraries/stdlib/jvm/src/kotlin/reflect/KCallable.kt](https://github.com/JetBrains/kotlin/blob/whyoleg/dokka2-sync-stdlib/libraries/stdlib/jvm/src/kotlin/reflect/KCallable.kt)
>   - JVM에 특화된 구현을 포함
>   - JVM 특정 기능 혹은 JVM 버전의 특정 API를 사용해야 하는 경우, 이 파일에서 관련 코드를 찾을 수 있음

<br/><br/>

⚠️ `call` 인자 개수와 원래 함수에 정의된 **파라미터 개수가 반드시 일치**해야 함

- 불일치 시 **런타임 오류** 발생
  - `IllegalArgumentException: Callable expects 1 argument, but 0 were provided` 
- 실수 방지를 위해 함수 호출 시, 구체적인 메서드 타입을 지정할 수 있음

**Example.**

```kotlin
fun sum(x: Int, y: Int) = x + y
```

아래와 같이 `KFunction2<Int, Int, Int>` 타입 명시 

```kotlin
val kFunction: KFunction2<Int, Int, Int> = ::sum
println(kFunction.invoke(1, 2) + kFunction(3, 4))   // 10
// kFunction(1)    // ← Compile Error: No value passed for parameter 'p2'
```

- ✅ 인자 타입과 반환 타입을 모두 다 안다면 invoke 메서드를 호출하는 것이 나음
  - `KFunction`의 `invoke` 메서드 호출 시에는 **컴파일이 안 되기 때문에** 입력 인자에 대해 실수할 일이 없음

<br/>

>[!NOTE]
> **`KFunctionN` 인터페이스 정의되는 시점**
> 
> - `KFunction1` 과 같은 타입은 파라미터 개수가 다른 여러 함수를 표현
> - 원하는 수만큼 많은 파라미터를 갖는 함수에 대한 인터페이스를 사용할 수 있음 
> - 각 `KFunctionN` 타입은 `KFunction` 을 확장하며 `N` 과 파라미터 개수가 같은 `invoke` 를 추가로 포함
>   - e.g. `KFunction2<P1,P2,R>` 에는 `operator fun invoke(p1: P1, p2: P2): R` 선언이 들어있음
> - `KFunctionN` 함수 타입은 컴파일러가 생성한 합성 타입
>   - `kotlin.reflect` 패키지에서 이런 타입의 정의를 찾을 수는 없음

<br/>

#### `KProperty` 의 `call` 메서드 호출

`KProperty` 의 `call` 메서드를 호출할 수도 있음 → 프로퍼티의 Getter 호출

- 최상위 읽기 전용 프로퍼티: `KProperty0` 인터페이스로 표현됨
- 최상위 가변 프로퍼티:`KMutableProperty0` 인터페이스로 표현됨
- `KProperty0` & `KMutableProperty0` 인터페이스 둘 다 `get()` 메서드를 제공

```kotlin
var counter = 0
 
fun main() {
    val kProperty = ::counter    // KMutableProperty0<Int>
    kProperty.setter.call(21)
    println(kProperty.get())     // 21
}
```

- 멤버 프로퍼티는 `KProperty1` 이나 `KMutableProperty1` 인스턴스로 표현
- `KProperty1` & `KMutableProperty1` 인터페이스 **둘 다 인자 1개**를 가진 `get` 메서드를 제공

<br/>

```kotlin
val person = Person("Alice", 29)
val memberProperty = Person::age    // KProperty1<Person, Int>

/*
  Property 의 타입 파라미터와 일치하는 타입의 객체만을 넘길 수 있음
  e.g. memberProperty.get("Alice") 은 컴파일되지 않음
  */
memberProperty.get(person)
```

- 함수의 로컬 변수에는 리플렉션으로 접근 불가
- 함수 내 로컬 변수 `x` 정의 후, 변수 참조를 얻으려 시도하면(`::x`) 오류
  - **오류**: 변수에 대한 참조는 아직 지원하지 않음<sup>"References to variables arent supported yet"</sup>

<br/>

#### ✔️ 프로퍼티 접근 인터페이스 계층 구조

실행 시점에 소스코드 요소에 접근하기 위해 사용할 수 있는 인터페이스의 계층 구조

<br/><img src="./img/figure12-01.png" width="60%" /><br/>

<br/>

- `KAnnotatedElement`: 구현한 클래스들에 적용된 어노테이션 정보를 가져올 수 있음<br/>
  ```kotlin
  package kotlin.reflect

  public interface KAnnotatedElement {
      public val annotations: List<Annotation>
  }
  ```
- `KClass`: 클래스와 객체를 표현할 때 쓰임
- `KProperty`: 모든 프로퍼티를 표현할 수 있음
  - 내부에 선언된 `Getter` 로 프로퍼티 접근자를 함수처럼 다룰 수 있음
- `KMutableproperty`: `var` 로 정의한 변경 가능한 프로퍼티 표현
  - 선언된 `Setter` 인터페이스로 프로퍼티 접근자를 함수처럼 다룰 수 있음

<br/>

> [!TIP]
> **`KProperty` 인터페이스**는 **`Getter` 타입의 필드**를 가지며,
> **`KMutableproperty` 인터페이스**는 **`Setter` 타입의 필드**를 가짐
> 
> → 프로퍼티 접근자를 함수처럼 다룰 수 있음
> e.g. 메서드에 붙어있는 어노테이션을 알아내기
> 
> `Getter` 와 `Setter`는 모두 `KFunction` 을 확장

<br/>

### 12.2.2 Implementing object serialization using reflection

<small><i>리플렉션을 사용해 객체 직렬화 구현</i></small>

```kotlin
private fun StringBuilder.serializeObject(obj: Any) {
    val kClass = obj::class as KClass<Any>
    val properties = kClass.memberProperties
 
    properties.joinToStringBuilder(
        this, prefix = "{", postfix = "}") { prop -> 
        serializeString(prop.name)
        append(": ")
        serializePropertyValue(prop.get(obj))
    }
}
```

- prop 변수 타입: `KProperty1<Any, *>`
- prop.get(obj) 호출 시: `Any?`

<br/>

### 12.2.3 Customizing serialization with annotations

<small><i>어노테이션을 활용해 직렬화 제어</i></small>

- `KAnnotatedElement` 인터페이스에는 `annotations`라는 프로퍼티가 있는데, 소스코드상에서 타겟 요소에 적용된 모든 어노테이션 인스턴스의 컬렉션이 있음
  - = `@Retention` 을 RUNTIME 으로 지정한 경우
- `KProperty`는 `KAnnotatedElement`를 확장하므로 `property.annotations`를 통해 프로퍼티의 모든 어노테이션을 얻을 수 있음
- **특정 어노테이션을 찾으려면?** `KAnnotatedElement` 에 대해 호출할 수 있는 `findAnnotation` 함수 사용 가능
  - 타입 인자로 지정한 타입과 일치하는 어노테이션들을 돌려줌

<br/>

**Example.**

#### ✔️ Example 1. 어노테이션으로 프로퍼티 제외하기

`findAnnotation` 과 `filter` 표준 라이브러리 함수를 조합하면 `@JsonExclude` 어노테
이션이 붙지 않은 프로퍼티만 남길 수 있음

```kotlin
val properties = KClass.memberProperties
    .filter { it.findAnnotation<JsonExclude>() == null }
```

<br/>

#### ✔️ Example 2. 인자를 포함한 어노테이션 찾기

`@JsonName` 의 인자는 프로퍼티를 직렬화해서 JSON 에 넣을 때 사용할 이름

```kotlin
annotation class JsonName(val name: String)
data class Person {
  @JsonName("alias") val firstName: String,
  val age: Int
}
```

- 어노테이션를 찾은 후, 어노테이션에 전달한 인자도 알아야함
- `findAnnotation`이 이 경우에도 도움이 됨

```kotlin
// 프로퍼티에 `@JsonName` 어노테이션이 없다면 `null` 값
val jsonNameAnn = prop.findAnnotation<JsonName>()  
val propName = jsonNameAnn?.name ?: prop.name
```

<br/>

<table><tr><td>

##### 🧐 Data Class 필드에 어노테이션이 적용되지 않는다 ❓

```kotlin
annotation class JsonName(val name: String)

fun findNames(obj: Any) {
    val kClass = obj::class as KClass<*>
    val properties = kClass.memberProperties

    properties.forEach { prop ->
        val jsonName = prop.findAnnotation<JsonName>()
        val propName = jsonName?.name ?: prop.name
        println(propName)
    }
}
```

위와 같은 코드 작성 후,
JsonName을 data class 필드에 아래와 같이 붙이면,
`propName` 으로 `alias` 가 출력될 줄 알았던 예상과는 달리 `jsonName` 자체가 `null` 이 출력


<table>
<tr>
<th>Kotlin Code</th>
<th>Compiled Code</th>
</tr>
<tr>
<td>

```kotlin
data class Coffee(
    @JsonName("alias") val name: String,
    val price: Int
)
```

</td>
<td>

```kotlin
public final data class Coffee public constructor(@com.gngsn.JsonName name: kotlin.String, price: kotlin.Int) {
    public final val name: kotlin.String /* compiled code */
    public final val price: kotlin.Int /* compiled code */
    ...
}
```
</td></tr></table>

<br/>

**실행 결과:**

```kotlin
findNames(Coffee("Americano", 6000)) // 출력: name
```

→ 예상된 결과인 `alias` 가 아닌 `name` 이 출력


#### 시도 1

```kotlin
data class Coffee(
    @field:JsonName("alias") val name: String,
    val price: Int
)
```

→ ❌ 동작 안함

왤까 ....

#### 시도 2

`AnnotationTarget` 를 `PROPERTY` 로 지정

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonName(val name: String)
```

**→ ✅ 실행 결과: `alias` 출력**

</td></tr></table>

<br/>

아래는 주어진 내용을 정리하고 각 개념에 맞는 예시 문장을 추가한 설명입니다. 마지막에는 전체 내용을 요약합니다.

<br/>

#### 📌 `@CustomSerializer`

- 속성에 커스텀 직렬화기를 지정하기 위해 `@CustomSerializer` 사용
- 어떤 `ValueSerializer` 클래스를 사용할지 명시

```kotlin
annotation class CustomSerializer(
    val serializerClass: KClass<out ValueSerializer<*>>
)
```

**사용 예시:**

```kotlin
data class Person(
    val name: String,
    @CustomSerializer(DateSerializer::class) val birthDate: Date
)
```

<br/>

#### 📌 2. `getSerializer()` 함수

- `getSerializer()`는 Kotlin 리플렉션 API의 `KProperty`에 확장된 함수로, 해당 속성에 지정된 커스텀 직렬화기 인스턴스를 반환
- 클래스인지 객체(`object`)인지에 따라 인스턴스를 반환하는 방식이 다름
  - `Person::birthDate.getSerializer()`를 호출하면 `DateSerializer`의 인스턴스를 반환

```kotlin
fun KProperty<*>.getSerializer(): ValueSerializer<Any?>? {
    val customSerializerAnn = findAnnotation<CustomSerializer>() ?: return null
    val serializerClass = customSerializerAnn.serializerClass

    val valueSerializer = serializerClass.objectInstance ?: serializerClass.createInstance()
    @Suppress("UNCHECKED_CAST")
    return valueSerializer as ValueSerializer<Any?>
}
```

<br/>

#### 📌 3. 객체(`object`)와 클래스의 구분

- Kotlin에서 `object`는 싱글턴으로 정의되며, `objectInstance` 프로퍼티를 통해 인스턴스에 접근할 수 있음
  - 일반 클래스는 `createInstance()`를 호출하여 새 인스턴스를 생성해야 함
- `DateSerializer`가 `object` 타입 → `getSerializer()`가 `DateSerializer.objectInstance` 반환

<br/>

### 12.2.4 JSON parsing and object deserialization

<small><i>JSON 파싱과 객체 역직렬화</i></small>

- JSON 문자열을 Kotlin 객체로 역직렬화

```kotlin
inline fun <reified T: Any> deserialize(json: String): T
```

- `T`는 런타임에도 타입 정보를 유지할 수 있도록 `reified`로 선언됨.
  - `inline` 키워드 필수

**사용 예시:**

```kotlin
val json = """{"title": "Catch-22", "author": {"name": "J. Heller"}}"""
val book = deserialize<Book>(json)
```

<br/>

#### 전체 파이프라인

역직렬화는 총 **3단계**로 구성됨:

1. **Lexer (렉서, 어휘 분석기)**: JSON을 토큰으로 나눔

   - 입력 문자열을 **토큰(token)** 리스트로 변환
   - 두 가지 종류:
     - **문자 토큰**: JSON 문법 기호 (`{`, `:`, `,` 등)
     - **값 토큰**: 문자열, 숫자, Boolean, null 등 실제 데이터

2. **Parser (파서, 구문 분석기)**: 여러 다른 의미 단위를 처리

   - 토큰을 JSON 구조 (객체, 배열, 키/값 쌍 등)로 변환
   - 구조에 따라 아래의 `JsonObject` 인터페이스의 메서드를 호출:

     ```kotlin
     interface JsonObject {
         fun setSimpleProperty(propertyName: String, value: Any?)
         fun createObject(propertyName: String): JsonObject
         fun createArray(propertyName: String): JsonObject
     }
     ```

3. **Deserializer (역직렬화기)**: 필요한 클래스의 인스턴스를 생성하여 반환

   - `JsonObject`의 구현체를 통해 객체를 구성

<br/>

#### ✔️ Seed: 객체 생성을 위한 빌더 유사 구조

- 역직렬화의 경우 해법이 완전히 제네릭해야 함
- 객체를 구성하기 전에 데이터를 임시로 저장할 필요가 있음 → **Seed**라는 추상화 사용
- **주요 구현체**
  - **일반 객체**: `ObjectSeed`. [🔗 Code](https://github.com/gngsn/deepdive/blob/main/books/kotlin-in-action/chapter12/demo/src/main/kotlin/com/gngsn/jkid/deserialization/Deserializer.kt#L67-L94)
  - **컬렉션**:
    - `ObjectListSeed`. [🔗 Code](https://github.com/gngsn/deepdive/blob/main/books/kotlin-in-action/chapter12/demo/src/main/kotlin/com/gngsn/jkid/deserialization/Deserializer.kt#L96-L110)
    - `ValueListSeed`. [🔗 Code](https://github.com/gngsn/deepdive/blob/main/books/kotlin-in-action/chapter12/demo/src/main/kotlin/com/gngsn/jkid/deserialization/Deserializer.kt#L112-L128)

- `ObjectSeed`를 만들고, 파서를 실행한 후, `spawn()` 호출로 객체 생성
- `ObjectSeed` 동작 방식
  - `valueArguments`: 단순 값 보관
  - `seedArguments`: 복합 객체 값 보관 (중첩 구조)
  - `arguments`: 위 둘을 합쳐서 생성자 호출 시 사용
- 재귀적으로 `spawn()`을 호출하여 중첩 객체도 완성

<br/>

### 12.2.5 The final step of deserialization: `callBy()` and creating objects using reflection

<small><i>최종 역직렬화 단계 : `callBy()`와 리플렉션을 사용해 객체 만들기</i></small>

- `KCallable.call(args: List<Any?>)` 는 기본값을 지원하지 않음
- 때문에, 디폴트 파라미터 값을 지원하는 다른 메서드인 `Kcallable.callBy` 사용해야 함

```kotlin
interface KCallable<out R> {
    fun callBy(args: Map<KParameter, Any?>): R
}
```

- 파라미터 이름 기반의 `Map` 사용
- 기본값 있는 파라미터는 생략 가능
- 파라미터 순서도 상관 없음

<br/>

**⚠️ 타입 처리 시, 타입이 생성자의 파라미터 타입과 일치해야 함**

- JSON의 값 타입이 정확히 매칭되어야 함
- 특히 숫자 타입(Int, Long, Double 등) 은 정확히 변환 필요
- `KParameter.type` 을 사용해 타입 확인


```kotlin
fun serializerForType(type: KType): ValueSerializer<out Any?>? =
        when (type) {
            typeOf<Byte>() -> ByteSerializer
            typeOf<Int>() -> IntSerializer
            typeOf<Boolean>() -> BooleanSerializer
            // ...
            else -> null
        }
```

- `typeOf<T>()` 를 통해 런타임 타입(`KType`)을 얻고 매핑

<br/>

#### ClassInfoCache

- JSON 에서 모든 키/값 쌍을 읽을 때마다 매번 프로퍼티 검색을 수행하면 아주 느려질 수 있음
- 때문에, 리플렉션 비용 감소를 위해 **캐싱 수행**
- 생성자 파라미터와 속성 간 매핑에는 리플렉션 필요 → 클래스 단위로 캐싱

```kotlin
class ClassInfoCache {
    private val cacheData = mutableMapOf<KClass<*>, ClassInfo<*>>()
 
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(cls: KClass<T>): ClassInfo<T> =
            cacheData.getOrPut(cls) { ClassInfo(cls) } as ClassInfo<T>
}
```

- ClassInfo 클래스 구현 코드 참고: [🔗 Code](https://github.com/gngsn/deepdive/blob/main/books/kotlin-in-action/chapter12/demo/src/main/kotlin/com/gngsn/jkid/deserialization/ClassInfoCache.kt#L11C7-L11C21)
  1. JSON을 파싱하여 `Map<String, Any?>` 형태의 값 추출
  2. 각 키를 `KParameter`와 매핑
  3. `ValueSerializer` 로 타입 변환
  4. `ClassInfo.createInstance()` 에서 `callBy()`로 객체 생성
  5. `ClassInfoCache` 를 통해 반복되는 리플렉션 정보 캐싱

<br/>

## Summary

- 코틀린에서는 넓은 범위(파일, 식 등)의 타깃에 대해 어노테이션을 붙일 수 있음
- 어노테이션 인자로 기본 타임 값, 문자열, 이넘, 클래스 참조, 다른 어노테이션 클래스의 인스턴스, 배열을 사용할 수 있음
- 어노테이션의 사용 지점 타깃을 명시 가능 (e.g. `@get:JvmName`)
  - 여러 가지 바이트코드 요소를 만들어내는 경우, 정확히 어떤 부분에 어노테이션을 적용할지 지정할 수 있음
- 어노테이션 클래스 정의: `annotation class` 
  - 모든 파라미터를 `val` 프로퍼티로 표시한 주 생성자가 있어야 하고, 본문은 없어야 함
- 메타어노테이션을 사용해 타깃, 어노테이션 유지 모드 등 여러 어노테이션 특성을 지정할 수 있음
- **리플렉션 API**: 실행 시점에 객체의 메서드와 프로퍼티를 동적으로 열거하고 접근할 수 있음. 
  - 리플렉션 API에는 클래스(`KClass`), 함수(`KFunction`) 등 여러 종류의 선언을 표현하는 인터페이스가 있음
- `::class`로 `KClass` 인스턴스 가져오기
  - 클래스는 `ClassName::class`를 사용
  - 객체는 `objName::class`를 사용
- `Function`과 `KProperty` 인터페이스는 모두 `Kcallable` 을 확장
  - `KCallable`은 제네릭 `call` 메서드 제공
  - `KCallable.callBy` 메서드: 메서드 호출 시, 디폴트 파라미터 값을 사용할 수 있음
- `KFunction0`, `KFunction1` 등의 인터페이스는 모두 파라미터 개수가 다른 함수를 표현하며 `invoke` 메서드를 사용해 함수를 호출할 수 있음
- `KProperty`, `KProperty1` 은 수신 객체의 개수가 다른 프로퍼티들을 표현하며 값을 얻기 위한 `get` 메서드를 지원
- `KMutableProperty0` 과 `KMutableProperty1`은 각각 `KProperty0` 과 `KProperty1` 을 확장하며 `set` 메서드를 통해 프로퍼티 값을 변경할 수 있음
- `KType` 의 실행 시점 표현을 얻기 위해 `typeOf<T>()` 함수 사용





