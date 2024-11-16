

## 인터페이스 기본 메소드와 스태틱 메소드

<br/><br/>

### 기본 메소드 (Default Methods)

<br/>

- 인터페이스에 메소드 선언이 아니라 **구현체를 제공**하는 방법
- 해당 인터페이스를 **구현한 클래스를 깨트리지 않고 새 기능을 추가**할 수 있다.

<br/>

```java
public interface Foo {

    void printName();

    /*
    * @ImplSpec 이 구현체는 getName() 으로 가져온 문자열을
    * 대문자로 바꿔 출력한다.
    * */
    default void printNameUpperCase() {
       System.out.println(getName().toUpperCase());
    }

    String getName();

}
public class DefaultFoo implements Foo {
    String name;

    public DefaultFoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(getName());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
```

<br/>

기본 메소드는 구현체가 모르게 추가된 기능으로 그만큼 **리스크**가 있다.

(메소드를 사용할 때 어떤 값이 오는 지 모두 파악할 수 없음.)

<br/>

✔️ 컴파일 에러는 아니지만 구현체에 따라 런타임 에러가 발생할 수 있다.

✔️ 항상 제대로 동작할 것이라는 보장이 없기 때문에 반드시 문서화 할 것. (`@implSpec` 자바독 태그 사용)

<br/><br/>

**Object가 제공하는 기능 (equals, hasCode)는 기본 메소드로 제공할 수 없음**

<br/>

```java
public interface Foo {

		// ...
    // ERROR: Default method 'toString' overrides a member of "java.lang.Object"
    default String toString() {}
}
```

<br/>

- 구현체가 재정의해야 한다.
- 본인이 수정할 수 있는 인터페이스에만 기본 메소드를 제공할 수 있다. (e.g. toString() X)

<br/>

**인터페이스를 상속받는 인터페이스에서 다시 추상 메소드로 변경할 수 있음**

```java
public interface Bar extends Foo {
    // 다시 추상화
    void printNameUpperCase();
}
```

<br/>

근데 Bar를 구현하는 하위 클래스는 모두 다시 구현해야한다.

- 인터페이스 구현체가 재정의 할 수도 있다. (default method를 default method로 override)
- 구현하는 하위 클래스에서 재정의 할 수도 있음.

<br/>
<br/>


📌 **다이아몬드 문제 Solution**

1 ) 클래스가 항상 이긴다.

2 ) 1번 규칙 이외의 상황에서는 Sub Interface가 이긴다.

3 ) 그밖에는 명시적 호출을 한다.

<br/>

### 스태틱 메소드 (Static Method)

해당 타입 관련 헬퍼 또는 유틸리티 메소드를 제공할 때 인터페이스에 static method를 제공할 수 있음

```java
public interface Bar extends Foo {
    // ...
    static void printBar() {
        System.out.println("Bar");
    }
}
public class Main {
    public static void main(String[] args) {
				// ...
        Bar.printBar();   // Bar
    }
}
```

<br/><br/>

**참고**

[Evolving Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/nogrow.html), [Default Methods](https://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html)



<br/><br/>

## 기본 메소드로 인한 API 변화

<br/>

### Iterable의 기본 메소드

<br/>

### forEach()

```java
public class ChangeMethod {
    public static void main(String[] args) {
        List<String> name = new ArrayList<>();
        name.add("gngsn");
        name.add("whiteship");
        name.add("toby");
        name.add("foo");

//        name.forEach(s -> System.out.println(s));
        name.forEach(System.out::println);
    }
}
```



### spliterator()

쪼갤 수 있는 Iterator

병렬 작업에 특화된 인터페이스

<br/><br/>

### Collection의 기본 메소드

<br/>

### stream() / parallelStream()

spliterator를 사용하고 있음.

element들을 스트림으로 만들어서 처리

```java
Stream stream = name.stream().map(String::toUpperCase)
                .filter(s -> s.startsWith("G"));

stream.forEach(System.out::println);
```

<br/>

### removeIf(Predicate)

  ```java
  name.removeIf(s -> s.startsWith("g"));
  System.out.printf(name.toString());
  ```

- spliterator()

<br/>

### Comparator의 기본 메소드 및 스태틱 메소드

- reversed()

  ```java
  Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
  name.sort(compareToIgnoreCase.reversed());
  ```

- thenComparing()

- static reverseOrder() / naturalOrder()

- static nullsFirst() / nullsLast()

- static comparing()

위와 같은 기능으로 비침투적인 기술을 구현할 수 있게 되었다.

<br/>

<br/>

### Spring은 비침투적인 기술 (non-invasive)

✔️ 비침투적기술 : 기술에 적용 사실이 코드에 직접 반영되지 않음

✔️ 침투적 : 기술과 관련된 코드나 규약 등이 코드에 저장

<br/>

무슨 말이냐면,,, 침투적이라는 것은 특정 기술을 적용시키기 위해, 기술이 요구하는 무언가를 해주어야만 하는 것.

예를 들어, 이 클래스를 사용하려면 어떤 메서드를 override 해줘야한다.

실제로 아래 `WebMvcConfigurerAdapter` 가 `Deprecated` 됨

<br/>

```java
public abstract class WebMvcConfigurerAdapter
extends Object
implements WebMvcConfigurer
```

<br/><br/>

**참고**

[Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html), [Iterable](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html), [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html), [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)