# CHAPTER 13. DSL construction

<small><i>DSL 만들기</i></small>

**DSL**
\: Domain-specific language. 특정 테스크, 도메인, 그리고 그 도메인에 관련된 기능만 사용하고 그 외 기능은 제외하는 언어.

<br>

## 13.1 From APIs to DSLs: Creating expressive custom code structures

<small><i>API 에서 DSL 로: 표현력이 좋은 커스텀 코드 구조 만들기</i></small>

#### 깔끔한 API
- 코드를 읽을 때 코드가 하는 일을 명확하게 이해할 수 있어야 함
  - 이름과 개념을 적절히 잘 선택하는 것이 중요
- 불필요한 구문이나 번잡한 준비 코드를 최소화
  - 깔끔한 API는 언어에 내장된 기능과 거의 구분할 수 없을 정도

<br>
<table>
  <tr>
    <th>Regular syntax</th>
    <th>Clean syntax</th>
    <th>Feature in use</th>
  </tr>
  <tr>
    <td><code>StringUtil.capitalize(s)</code></td>
    <td><code>s.capitalize()</code></td>
    <td>Extension function</td>
  </tr>
  <tr>
    <td><code>1.to("one")</code></td>
    <td><code>1 to "one"</code></td>
    <td>Infix call</td>
  </tr>
  <tr>
    <td><code>set.add(2)</code></td>
    <td><code>set += 2</code></td>
    <td>Operator overloading</td>
  </tr>
  <tr>
    <td><code>map.get("key")</code></td>
    <td><code>map["key"]</code></td>
    <td>Convention for the get method</td>
  </tr>
  <tr>
    <td><code>file.use({ f -> f.read() } )</code></td>
    <td><code>file.use { it.read() }</code></td>
    <td>Lambda outside of parentheses</td>
  </tr>
  <tr>
    <td><pre><code>with (sb) {<br>  append("yes")<br>  append("no")<br>}</code></pre></td>
    <td><pre><code>with (sb) {<br>  append("yes")<br>  append("no")<br>}</code></pre></td>
    <td>Lambda with a receiver</td>
  </tr>
  <tr>
    <td><pre><code>val m = mutableListOf<Int>()<br>m.add(1)<br>m.add(2)<br>return m.toList()</code></pre></td>
    <td><pre><code>val m = mutableListOf<Int>()<br>m.add(1)<br>m.add(2)</code></pre></td>
    <td>Builder functions with lambdas</td>
  </tr>
</table>

<br>

## 13.1.1 Domain-specific languages

<small><i>도메인 특화 언어</i></small>

#### 명령적 언어 vs 선언적 언어

- **명령적<sup>imperative</sup> 언어**: 어떤 연산을 완수하기 위해 필요한 각 단계를 순서대로 정확히 기술
  - e.g. 대부분의 프로그래밍 언어
- **선언적<sup>declarative</sup> 언어**: 원하는 결과를 기술하기만 하고 그 결과를 달성하기 위해 필요한 세부 실행은 언어를 해석하는 엔진에 맡김
  - e.g. DSL 언어 (SQL, 정규식)

<br>

> [!NOTE]
> 실행 엔진이 결과를 얻는 과정을 한번에 최적화하기 때문에 선언적 언어가 더 효율적인 경우가 자주 있음
> 
> 반면 명령형 접근법에서는 각 연산에 대한 구현을 독립적으로 최적화해야 함
> 
> <br>
> 
> **Example. SQL Query**
> 
> `DELETE` 쿼리 시, '실행 엔진'이 인덱스와 조인 등을 감안해 최적의 방법을 만들어냄.
>
> 테이블의 '각 레코드를 순회하면서 개별 필드를 추출해서 어떤 동작을 수행할지 결정하는 코드'를 작성하지는 않음

<br>

#### DSL의 단점
- DSL과 호스트 애플리케이션의 통합이 어려움
  - DSL 자체 문법 차이로 직접 포함 불가
  - 별도 파일/문자열로 저장해서 포함 시킬 수 있겠지만, IDE 지원, 디버깅, 컴파일 시점 검증 등이 제한됨
- DSL 문법 자체를 학습해야 하는 부담

→ 코틀린은 이러한 문제를 해결하기 위해 내부 DSL을 지원

<br>

## 13.1.2 Internal DSLs are seamlessly integrated into the rest of your program

<small><i>내부 DSL은 프로그램의 나머지 부분과 완벽하게 통합됨</i></small>

- **External DSL**: 독립적인 문법 구조를 갖음
- **Internal DSL**: 범용 언어로 작성된 프로그램의 일부로, 동일한 문법을 사용
- 따라서 내부 DSL은 완전히 다른 언어가 아니라 DSL 의 핵심 장점을 유지하면서 주 언어를 별도의 문법으로 사용하는 것

<br>

#### SQL<sub>외부 DSL</sub> vs. Exposed 프레임워크<sub>내부 DSL</sub>

[🔗 Exposed 프레임워크](https://github.com/JetBrains/Exposed): SQL library on top of a JDBC driver for the Kotlin

#### Example. 가장 많은 고객이 사는 나라를 찾는 예제

- Customer와 Country 테이블
- Customer가 Country를 외래키로 참조하는 구조

<table>
<tr>
  <th>SQL</th>
  <th>Exposed</th>
</tr>
<tr>
  <td>
  
  ```sql
SELECT Country.name, COUNT(Customer.id)
      FROM Country
INNER JOIN Customer
        ON Country.id = Customer.country_id
  GROUP BY Country.name
  ORDER BY COUNT(Customer.id) DESC
   LIMIT 1
```

  </td>
  <td>
  
```kotlin
(Country innerJoin Customer)
    .slice(Country.name, Count(Customer.id))
    .selectAll()
    .groupBy(Country.name)
    .orderBy(Count(Customer.id), order = SortOrder.DESC)
    .limit(1)
```

- `selectAll`, `groupBy`, `orderBy` 등 → 일반 코틀린 메서드

  </td>
</tr>
