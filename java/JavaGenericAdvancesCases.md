## Java Generic Advanced Cases

*본 글은 [java-generics-advanced-cases](https://levelup.gitconnected.com/java-generics-advanced-cases-d05db19b47d5?gi=e60afeece5d7)의 글을 번역한 내용입니다.*

<br/><br/>

우리가 작성하는 코드에서 제너릭을 가장 효율적이게 사용하는 방법은 무엇일까? 
지금부터 해당 주제를 다뤄보자.

<br/>

### Don’t Use Raw Types 
> Raw Type을 사용하지 마라.

이 내용은 당연하며, Raw 타입은 제너릭이라는 개념자체를 다룰 수 없다. 

Raw 타입을 사용하면 컴파일러가 타입 에러를 찾지 못한다.

<br/>

뿐만아니라 또 다른 문제가 발생한다.

아래와 같은 클래스가 정의 되어있다고 해보자.

<br/>

``` java
class GenericContainer<T> {
    private final T value;

    public List<Integer> getNumbers() {
        return numberList;
    }
}
```

<br/>

제너릭 타입 자체에 대한 문제를 고려하지 않다고 가정하고, 

목표는 `numberList` 를 순회하는 것이다.

<br/>

``` java
public void traverseNumbersList(Container container) {
    for (int num : container.getNumbers()) {
        System.out.println(num);
    }
}
```

<br/>

하지만 위의 코드는 컴파일되지 않는다.

<br/>

``` bash
error: incompatible types: Object cannot be converted to int
        for (int num : container.getNumbers()) {
                                           ^
```

<br/>

원시 타입은 제너릭 타입의 정보뿐만 아니라, 미리 정의된 내용들에 대해서도 지운다는 문제가 있다.

그렇게 되면 `List<Integer>`가 그냥 `List` 로 변경되는 것이다.

<br/>

그렇다면, 어떤 방법으로 이를 해결할 수 있을까?

간단하다. 바로 **wildcard operator**를 사용하는 것이다.

<br/>

``` java
public void traverseNumbersList(Container<?> container) {
    for (int num : container.getNumbers()) {
        System.out.println(num);
    }
}
```

<br/>

이제 위의 코드는 문제없이 돌아가게 될 것이다.
