# 함수형 인터페이스와 람다 표현식

<br/>

## Intro

### ✔️ 함수형 인터페이스 (Functional Interface)

<br/>

```java
@FunctionalInterface
public interface RunSomething {
    void doIt();
}
```

<br/>

- **추상 메소드를 딱 하나만 가지고 있는 인터페이스**
- SAM (Single Abstract Method) 인터페이스
- @FuncationInterface 애노테이션을 가지고 있는 인터페이스

<br/>

추상메서드가 몇 개 있냐만 따라서 **functional interface**를 정의함.

따라서 아래 둘 다 Functional Interface.

<br/>

<div style="display: flex;">
<div>

**ERROR**

```java
@FunctionalInterface
public interface Run {
    void doIt();
    void doIt2();
}
```

</div>
<div>
**PASS**

```java
@FunctionalInterface
public interface Run {
    void doIt();

    static void printName() {
        System.out.println("gngsn");
    }
}
```

</div>
</div>

<br/>

### ✔️ 람다 표현식 (Lambda Expressions)

<br/>

- 함수형 인터페이스의 인스턴스를 만드는 방법으로 쓰일 수 있다.
- 코드를 줄일 수 있다.
- 메소드 매개변수, 리턴 타입, 변수로 만들어 사용할 수도 있다.

<br/>

```java
public class Foo {
    public static void main(String[] args) {
        RunSomething runSomething = new RunSomething() {
            @Override
            public void doIt() {
                System.out.println("Hello");
            }
        };
				runSomething.doIt();
    }
}
```

<br/>

위의 코드를 아래와 같이 표현할 수 있다.

```java
public class Foo {
    public static void main(String[] args) {
        RunSomething runSomething = () -> System.out.println("Hello");
				runSomething.doIt();
    }
}
```

<br/>

실질적으로는 굉장히 **특수한 형태의 오브젝트**로 볼 수 있음

함수형 인터페이스를 인라인으로 구현한 오브젝트로 볼 수 있음

파라미터, 리턴 타입 등을 객체로 전달할 수 있음 → **고차 함수**

<br/>

### ✔️ 자바에서 함수형 프로그래밍

<br/>

- 함수를 First class object로 사용할 수 있다.

- 고차 함수 (Higher-Order Function) 

  - 함수가 함수를 매개변수로 받을 수 있고 함수를 리턴할 수도 있다.

- 불변성


<br/><br/>

#### 순수 함수 (Pure function)

같은 값을 넣었을 때 같은 값이 나와야 함

  - 사이드 이팩트가 없다. (함수 밖에 있는 값을 변경하지 않는다.)

  - 상태가 없다. (함수 밖에 있는 값을 사용하지 않는다.)


<br/>

  ```java
  public class Foo {
      public static void main(String[] args) {
          RunSomething runSomething = new RunSomething() {
              @Override
              public int doIt(int num) {
                  return num + 10;
              }
          };
  
  				System.out.println(runSomething.doIt(1));
          System.out.println(runSomething.doIt(1));
          System.out.println(runSomething.doIt(1));
      }
  }
  ```

<br/>

 위와 같이 몇 번을 호출해도 똑같은 값이 나와야함.

  그렇다면 다른 값이 나올 경우(순수함수가 아닌 경우)는 ?

<br/>

  ```java
  public class Foo {
      public static void main(String[] args) {
          RunSomething runSomething = new RunSomething() {
              int baseNumber = 10;
  
              @Override
              public int doIt(int num) {
  								// baseNumber++;
                  return num + baseNumber;
              }
          };
  
          System.out.println(runSomething.doIt(1));
      }
  }
  ```

<br/>

위와 같이 함수 외부 값을 참조 했을 경우 : 상태값을 가진다. 상태값에 의존한다고 말할 수 있다.

<br/>

  [ 참고 ]

  ```java
  public class Foo {
      public static void main(String[] args) {
          int baseNumber = 10;
          RunSomething runSomething = (num) -> num + baseNumber;
  
          System.out.println(runSomething.doIt(1));
      }
  }
  ```

<br/>

위와 같이 외부 변수를 참고하더라도 람다식으로 표현할 수 있다.

  단, baseNumber는 final이란 전제하에 가능하다. (`final int baseNumber = 10;`)

  baseNumber를 변경하게 되면 compile error가 발생한다.

<br/><br/>

*자바에서 미리 정의해둔 함수 인터페이스*

## Java가 제공하는 함수형 인터페이스

[java.util.function](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)

자바에서 미리 정의해둔 자주 사용할만한 함수 인터페이스

<br/>

### **Function<T, R>**

T 타입을 받아서 R 타입을 리턴하는 함수 인터페이스

- `R apply(T t)` 

<br/>

함수 조합용 메소드 : `andThen` ,  `compose`

<br/>

클래스에 implements 사용한 경우

```java
import java.util.function.Function;

public class Plus10 implements Function<Integer, Integer> {
    @Override
    public Integer apply(Integer integer) {
        return integer + 10;
    }
}
```

```java
import java.util.function.Function;

public class Foo {
    public static void main(String[] args) {
        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));
		}
}
```

<br/>

**람다 표현식으로 구현한 경우**

```java
import java.util.function.Function;

public class Foo 
{
    public static void main(String[] args) 
		{
        Function<Integer, Integer> plus11 = (i) -> i + 11;
        System.out.println(plus11.apply(1));
		}
}
```

<br/>

두 개의 Function<T, R> 구현체를 조합하는 방법

```java
import java.util.function.Function;

public class Foo 
{
    public static void main(String[] args) 
		{
				Function<Integer, Integer> plus11 = (i) -> i + 11;
        System.out.println(plus11.apply(1));  // 12

        Function<Integer, Integer> multiply2 = (i) -> i * 2;
        System.out.println(multiply2.apply(1));  // 2

        System.out.println(plus11.compose(multiply2).apply(1));  // 13
        System.out.println(plus11.andThen(multiply2).apply(1));  // 24
		}
}
```

<br/>

#### 함수의 조합

입력값을 가지고 뒤에 오는 함수에 적용함. 그 결과값을 다시 앞의 함수의 입력값으로 사용함.

##### `compose()` 메서드

`Second.compose(First)`

multiply2 -> plus11 로 인자가 전해져서 결과값이 나옴

<br/>

##### `andThen()` 메서드

`First.compose(Second)`

plus11 -> multiply2  로 인자가 전해져서 결과값이 나옴

<br/>

### BiFunction\<T, U, R\>

두 개의 값(T, U)를 받아서 R 타입을 리턴하는 함수 인터페이스

<br/>

- `R apply(T t, U u)`

<br/>

함수 조합용 메소드 : `andThen`

```java
BiFunction<Integer, Integer, Integer> multiply = (i, j) -> i * j;
System.out.println(multiply.andThen(multiply2).apply(2, 3));  // 12
```

<br/>

### Consumer\<T\>

T 타입을 받아서 아무값도 리턴하지 않는 함수 인터페이스

- `void accept(T t)`

<br/>

함수 조합용 메소드 : `andThen`

```java
Consumer<Integer> printT = (i) -> System.out.println(i);
printT.accept(2);
```

<br/>

### Supplier<T>

T 타입의 값을 제공하는 함수 인터페이스

- `T get()`

<br/>

함수 조합용 메소드 : `andThen`

```java
Supplier<Integer> get10 = () -> 10;
System.out.println(get10.get());  // 10
```

<br/>

### Predicate\<T\>

T 타입을 받아서 boolean을 리턴하는 함수 인터페이스

- `boolean test(T t)`

<br/>

*Definition*

  ```java
  public interface Predicate<T> {
  
      /**
       * Evaluates this predicate on the given argument.
       *
       * @param t the input argument
       * @return {@code true} if the input argument matches the predicate,
       * otherwise {@code false}
       */
      boolean test(T t);
  
      ....
  }
  ```

<br/>

함수 조합용 메소드 :  `and`, `or`, `negate`

```java
Predicate<String> startsWithGngsn = (s) -> s.startsWith("gngsn");
Predicate<Integer> isEven = (i) -> i % 2 == 0;

System.out.println(startsWithGngsn.test("gngsntrue"));  // true
System.out.println(startsWithGngsn.test("falsegngsn"));  // false

System.out.println(isEven.test(10));  // true
System.out.println(isEven.test(11));  // false
```

<br/>

**조합 예제**

```java
Predicate<Integer> isEven = (i) -> i % 2 == 0;
Predicate<Integer> le10 = (i) -> i < 10;

System.out.println(isEven.and(le10).test(12));  // false
System.out.println(isEven.or(le10).test(12));  // true
```

<br/>

### UnaryOperator\<T\>

Function<T, R>의 특수한 형태로, 입력값 하나를 받아서 동일한 타입을 리턴하는 함수 인터페이스

즉, 입력값과 출력값의 타입이 같을 경우(입력값 한개)

<br/>

- `T apply(T t)`

<br/>

함수 조합용 메소드 : `andThen` ,  `compose`

```java
// Function<Integer, Integer> plus11 = (i) -> i + 11;
// Function<Integer, Integer> multiply2 = (i) -> i * 2;
//
// System.out.println(plus11.compose(multiply2).apply(1));  // 13

UnaryOperator<Integer> plus11U = (i) -> i + 11;
UnaryOperator<Integer> multiply2U = (i) -> i * 2;

System.out.println(plus11U.compose(multiply2U).apply(1));  // 13
```

<br/>

### BinaryOperator\<T\>

BiFunction<T, U, R>의 특수한 형태로, 동일한 타입의 입력값 두개를 받아 리턴하는 함수 인터페이스

- `R apply(T t, U u)`

<br/>

함수 조합용 메소드 : `andThen`

```java
BinaryOperator<Integer> multiplyB = (i, j) -> i * j;
System.out.println(multiply.andThen(multiply2).apply(2, 3));  // 12
```

<br/><br/>

## 람다 표현식

<br/>

### Lambda

**(params) -> { body }**

<br/>

✔️ **params**

- 인자가 없을 때: ()
- 인자가 한개일 때: (one) 또는 one
- 인자가 여러개 일 때: (one, two)
- 인자의 타입은 생략 가능, 컴파일러가 추론(infer)하지만 명시할 수도 있다. (Integer one, Integer two)

<br/>

✔️ **body**

- 화살표 오른쪽에 함수 본문을 정의한다.
- 여러 줄인 경우에 { }를 사용해서 묶는다.
- 한 줄인 경우에 생략 가능, return도 생략 가능.

<br/><br/>

### 변수 캡처 (Variable Capture)

**로컬 변수 캡처**

- final이거나 effective final 인 경우에만 참조할 수 있다.
- 그렇지 않을 경우 concurrency 문제가 생길 수 있어서 컴파일가 방지한다.

<br/>

**내부 클래스와 익명 클래스 vs 람다**

공통점 : 외부 변수 참조 가능. 로컬 변수 캡처 (effective final)

차이점 : 쉐도잉

<br/>

### effective final

- 이것도 역시 자바 8부터 지원하는 기능으로 “사실상" final인 변수.
- final 키워드 사용하지 않은 변수를 익명 클래스 구현체 또는 람다에서 참조할 수 있다.
- 람다에서 final을 생략할 수 있는 경우가 있음. 변수가 사실상 `final` 인 경우 (어디서도 변수를 변경하지 않는 경우) → effective final

<br/>

📌 **익명 클래스 구현체와 달리 ‘쉐도윙’하지 않는다.**

- 내부 클래스나 익명 클래스는 새로 스콥을 만들지만, 람다는 람다를 감싸고 있는 스콥과 같다.

<br/>

```java
private void run() {
		int baseNumber = 10;

		// 로컬 클래스
		class LocalClass {
				void printBaseNumber() {
						int baseNumber = 11;
						System.out.println(baseNumber);
	    }
		}

        // 익명 클래스
    Consumer<Integer> integerConsumer = new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) {
            int baseNumber = 11;
            System.out.println(integer + baseNumber);
        }
    };

		new LocalClass().printBaseNumber();
		integerConsumer.accept(10);

		// 람다
		IntConsumer printInt = (i) -> System.out.println(i + baseNumber);
		printInt.accept(10);

		// ERROR - baseNumber
		// IntConsumer printInt = (baseNumber) -> System.out.println(baseNumber);
		
}
```

<br/>

[Nested Classes - Shadowing](https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html#shadowing)

[Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)

<br/>

## 메소드 레퍼런스

람다가 하는 일이 기존 메소드 또는 생성자를 호출하는 거라면, 메소드 레퍼런스를 사용해서 매우 간결하게 표현할 수 있다.

<br/>

**메소드 참조하는 방법**

| 스태틱 메소드 참조               | 타입::스태틱 메소드            |
| -------------------------------- | ------------------------------ |
| 특정 객체의 인스턴스 메소드 참조 | 객체 레퍼런스::인스턴스 메소드 |
| 임의 객체의 인스턴스 메소드 참조 | 타입::인스턴스 메소드          |
| 생성자 참조                      | 타입::new                      |

- 메소드 또는 생성자의 매개변수로 람다의 입력값을 받는다.
- 리턴값 또는 생성한 객체는 람다의 리턴값이다.

<br/>

```java
public class MethodReferenceTest {
    public static void main(String[] args) {

				// 스태틱 메소드 참조
        UnaryOperator<String> hi = Greeting::hi;
        hi.apply("gngsn");                          // hi gngsn

				// 특정 객체의 인스턴스 메소드 참조
        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        hello.apply("gngsn");                       // hello gngsn

				// 생성자 참조 - 1
        Supplier<Greeting> greetingSupplier = Greeting::new;
        greetingSupplier.get();

			  // 생성자 참조 - 2 
        Function<String, Greeting> greetingFunction = Greeting::new;
        Greeting greeting1 = greetingFunction.apply("gngsn");
        System.out.println(greeting1.getName());    // gngsn
    }
}
```

<br/>

**임의 객체의 인스턴스 메소드 참조**

```java
public class MethodReferenceTest {
    public static void main(String[] args) {
				// 임의 객체의 인스턴스 메소드 참조
				String[] names = {"gngsn", "whiteship", "toby"};

//      Java 8 이전
//        Arrays.sort(names, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return 0;
//            }
//        });

        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
    }
}
```

<br/>
참고 **Comparator**

  ```java
  @FunctionalInterface
  public interface Comparator<T> {
  ...
  		int compare(T o1, T o2);
  		boolean equals(Object obj);  // 이건 추상메서드 아님 (Object에 있음)
  }
  
  public class Object {
  ...
  		public boolean equals(Object obj) {
          return (this == obj);
      }
  }
  ```

<br/>

[Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)