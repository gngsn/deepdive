# CHAPTER 12. Annotations and reflection

<small><i>어노테이션과 리플렉션</i></small>

코틀린의 리플렉션 API 의 일반 구조는 자바와 같지만 세부 사항에서 약간 차이가 있음

<br/>

## 12.1 Declaring and applying annotations

<small><i>어노테이션 선언과 적용</i></small>

- 어노테이션을 사용하면 선언에 추가적인 메타데이터를 연관시킬 수 있음
- 소스코드, 컴파일된 클래스 파일, 런타임에서 해당 메타데이터에 접근할 수 있음

<br/>

### 12.1.1. Applying annotations to mark declarations

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

### 12.1.2. Specifying the exact declaration an annotation refers to: Annotation targets

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


