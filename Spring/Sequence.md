Sequence
: 한 번에 하나씩 열거될 수 있는 원소의 시퀀스를 표현



코틀린 지연 계산 시퀀스는 Sequence 인터페이스에서 시작한다.



public interface Sequence<out T> {
    public operator fun iterator(): Iterator<T>
}

Sequence 안에는 iterator라는 단 하나의 메소드가 있는데, 이를 통해 원소 값을 얻을 수 있다.





Collection API VS Sequence
이전 포스팅에서 map, filter 등 주요 컬렉션 함수를 살펴보았는데,

이런 함수는 각각의 함수 연산마다 새로운 컬렉션을 생성 후 다음 연산을 이어 결과 컬렉션을 생성한다.

시퀀스sequence를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.



위의 말이 직관적으로 이해하기 어려울 수 있는데, 아래 예시를 확인해보자.



/* Basic Collection API */
listOf(1, 2, 3, 4)
	.map { it * it }
	.filter { it % 2 == 0 }
// map(1) map(2) map(3) map(4) filter(1) filter(4) filter(9) filter(16)


/* Sequnce */
listOf(1, 2, 3, 4)
	.map { it * it }
	.filter { it % 2 == 0 }
    .toList()
// map(1) filter(1) map(2) filter(4) map(3) filter(9) map(4) filter(16)


출력된 순서를 확인해보면 금방 이해할 수 있을 것이다.

기본 Collection API는 map에 대한 연산을 모두 수행한 리스트를 생성해서 filter로 넘기고,

Sequence는 하나의 원소를 map 연산 후 filter 연산을 거쳐 리스트에 담는다.



시퀀스를 사용한 예제는 중간 결과를 저장하는 컬렉션이 생기지 않고, 

특히 filter를 사용해 필요없는 연산을 미리 없앨 수 있기 때문에 원소가 많은 경우 성능이 눈에 띄게 좋아진다.

이 내용은 아래에서 더 살펴보도록 하자.



이처럼 코틀린 시퀀스는 Sequence 인터페이스는 지연 계산 실행하는데, 그림으로 보면 아래와 같다.




Intermediate and Terminal Operations
시퀀스에 대한 연산은 중간Intermediate 연산과 최종terminal 연산으로 나뉜다.



sequence.map { ... }.filter { ... }	// 중간 연산
    	.toList()			// 최종 연산


✔️ 중간 연산

: 원본 시퀀스 요소를 변경한 다른 시퀀스를 반환한다. 항상 지연 계산된다. 

(직역: 원본 시퀀스 요소를 변환하는 방법을 아는 다른 시퀀스를 반환한다.)


✔️ 최종 연산

: 초기 컬렉션의 변환 시퀀스로 얻은 컬렉션, 요소, 숫자 또는 기타 개체일 수 있는 결과를 반환한다.








중간 연산은 항상 지연 연산된다고 했는데,

그 의미를 알아보기 위해 최종 연산이 없는 예제를 살펴보자.



listOf(1, 2, 3, 4).asSequence()
	.map { it * it }
	.filter { it % 2 == 0 }  // 출력 값 없음


위 코드를 실행하면 아무 내용도 출력되지 않는다.

이는 map과 filter 변환이 지연되는데

이는 결과를 얻을 필요가 있을 때, 즉 최종 연산이 호출될 때 실행된다.



listOf(1, 2, 3, 4).asSequence()
	.map { it * it }
	.filter { it % 2 == 0 }
	.toList()
// map(1) filter(1) map(2) filter(4) map(3) filter(9) map(4) filter(16)


최종 연산을 호출하면 지연됐던 모든 계산이 수행된다.





toList()
위의 예시에서 시퀀스를 다시 리스트로 변경 시켜주었다.

시퀀스의 원소를 순서에 따라 차례대로 순회iteration 한다면 시퀀스를 사용해도 된다.

하지만 인덱스 접근이나 다른 API 메소드가 필요하다면 시퀀스를 리스트로 변환해야 한다.







Order of operations execution
컬렉션 연산에서는 수행 순서를 잘 따져봐야 한다.








예시를 map과 find 연산으로 살펴보자.
map으로 리스트의 각 숫자를 제곱하고 제곱한 숫자중에서 find로 3보다 큰 첫번째 원소를 찾아보자.



listOf(1, 2, 3, 4).asSequence ()
    .map { it * it }
    .find { it > 3 }
// 4

시퀀스 연산에서는 연산을 차례대로 적용하다가

첫 원소가 map과 filter에 대한 모든 연산을 수행한 후, 두 번째 원소가 처리되는 식으로 모든 원소가 처리된다.

결과가 얻어지면 그 이후의 원소에 대해서는 변환이 이뤄지지 않을 수도 있다.



같은 연산을 시퀀스가 아니라 컬렉션에 수행하면 map의 결과가 먼저 평가되어 최초 컬렉션의 모든 원소가 변환된다.

두 번째 단계에서는 map을 적용해서 얻은 중간 컬렉션으로 부터 술어를 만족하는 원소를 찾는다.



시퀀스를 사용하면 지연 계산으로 인해 원소 중 일부의 계산은 이뤄지지 않는다.

이 코드를 즉시 계산(Eager operation, 컬렉션 사용)과 지연 계산(Lazy operation, 시퀀스 사용)으로 평가하는 경우의 차이를 보여준다.







Eager vs Lazy
: 즉시 계산 지연 계산



즉시 계산은 전체 컬렉션에 연산을 적용하지만 지연 계산은 원소를 한번에 하나씩 처리한다.







Eager Operation

: 컬렉션을 사용한 연산으로 리스트가 다른 리스트로 변환된다. 



위의 그림의 map 연산은 모든 원소를 변환한다. 

그 후 find가 조건을 만족하는 첫 번째 원소인 4($2^2$)를 찾는다.



Lazy Operation

: 시퀀스를 사용하면 find 호출이 원소를 하나씩 처리하기 시작한다. 



최초 시퀀스로 부터 원소를 하나 가져와서 map에 지정된 변환을 수행한다.

그 후 find에 지정된 조건을 만족하는지 검사한다. 



최초 시퀀스에서 2를 가져오면 제곱 값(4)이 3보다 커지기 때문에 그 제곱 값을 결과로 반환한다. 

이때 이미 답을 찾았으므로 3과 4를 처리할 필요가 없다.







위와 같은 연산 방식으로, 컬렉션 연산에서는 연산 순서가 성능에 큰 역할을 할 수 있다.



사람의 컬렉션에서 이름이 특정 길이보다 짧은 사람의 명단을 얻고 싶다고 하자.



 이 경우 map과 filter를 어떤 순서로 수행해도 된다.

그러나 map → filter의 경우와 filter → map의 경우, 결과는 같아도 수행해야 하는 변환의 전체 횟수는 다르다.



val people = listOf(Person("Alice", 29), Person ("Bob", 31), Person("Charles", 31), Person("Dan", 21))

// map → filter
people.asSequence()
    .map(Person::name)		// [Alice, Charles, Bob, Dan]
    .filter { it.length < 4 }	// [Bob, Dan]
    .toList() 

// filter → map
people.asSequence()
    .filter { it.name.length < 4 }	// [Person ("Bob", 31), Person("Dan", 21)]
    .map(Person::name)			// [Bob, Dan]
    .toList()


위 처럼 filter를 먼저 적용하면 전체 변환 횟수가 줄어든다.





시퀀스라는 개념은 자바 8 스트림과 비슷하다.

자바 스트림과 코틀린 시퀀스 비교하는 글은 이 곳을 참고할 수 있다.






시퀀스 만들기
지금까지 살펴본 시퀀스 예제는 모두 컬렉션에 대해 asSequence()를 호출해 시퀀스를 만들었다. 

시퀀스를 만드는 다른 방법으로 generateSequence 함수를 사용할 수 있다. 

이 함수는 이전의 원소를 인자로 받아 다음 원소를 계산한다. 

다음은 generateSequence 로 0부터 100까지 자연수의 합을 구하는 프로그램이다.



val naturalNumbers = generateSequence(0) { it + 1 }
val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
println(numbersTo100.sum()) // 모드 지연 연산은 "sum"의 결과를 계산할 때 수행된다.
// 5050


이 예제에서 naturalNumbers와 numbersTo100은 모두 시퀀스며, 연산을 지연 계산한다. 

최종 연산인 sum() 메소드를 수행하기 전까지는 시퀀스의 각 숫자는 계산되지 않는다.





Characteristic
✔️ 연산 수행
: 시퀀스의 원소는 정의를 할 때 실행되는게 아니라 필요한 때에 계산
하나의 규칙으로써, 연쇄적인 큰 컬렉션 연산이 필요할 때는 시퀀스를 사용하라.

중간 컬렉션을 생성함에도 불구하고 코틀린에서 즉시 계산 컬렉션에 대한 연산 이 더 효율적인 이유를 설명한다.

하지만 컬렉션에 들어있는 원소가 많으면 중간 원소를 재배열하는 비용이 커지기 때문에 지연 계산이 더 낫다.

시퀀스에 대한 연산을 지연 계산하기 때문에 정말 계산을 실행하게 만들려면 최종 시퀀스의 원소를 하나씩 이터레이션하거나 최종 시퀀스를 리스트로 변환해야 한다. 다음 절에서 이에 대해 설명한다.



✔️ 중간 처리 결과를 따로 저장하지 않고 연산을 연쇄적으로 적용해서 효율적으로 계산

✔️ asSequence 확장 함수를 호출하면 어떤 컬렉션이든 시퀀스로 바꿀 수 있다.

✔️ 시퀀스를 리스트로 만들 때는 toList를 사용한다.









자바 함수형 인터페이스 활용
코틀린 라이브러리와 람다를 사용하는 것은 멋지지만, 여러분이 다뤄야 하는 API 중 상당수는 코틀린이 아니라 자바로 작성된 API일 가능성이 높다.

다행인 점은 코틀린 람다를 자바 API에 사용해도 아무 문제가 없다는 사실이다.

이번 절에서는 어떻게 코틀린 람다를 자바 API에 활용할 수 있는지 살펴본다.



5장을 시작하면서 자바 메소드에 람다를 넘기는 예제를 보여줬다. 



button.setOnClickListener { /* 클릭 시 수행할 동작 */ } // 람다를 인자로 넘김

Button 클래스는 setOnClickListener 메소드를 사용해 버튼의 리스너를 설정한다.

이때 인자의 타입은 OnClickListener다.



/* 자바 */
public class Button {
	public void setOnClickListener(OnClickListener l) { ... }
}


OnClickListener 인터페이스는 onClick이라는 메소드만 선언된 인터페이스다.


/* 자바 */
public interface OnClickListener {
	void onClick(View v);
}


자바 8 이전의 자바에서는 setOnClickListener 메소드에게 인자로 넘기기 위해 무명 클래스의 인스턴스를 만들어야만 했다.



button.setOnClickListener(new OnClickListener() { 
	@Override
	public void onClick (View v) {
    	...
	}
}


코틀린에서는 무명 클래스 인스턴스 대신 람다를 넘길 수 있다.



button.setOnClickListener { view -> ... }

OnClickListener를 구현하기 위해 사용한 람다에는 view라는 파라미터가 있다.

view 의 타입은 View다.

이는 onClick 메소드의 인자 타입과 같다. 이런 관계를 그림 5.10에 서 볼 수 있다.



public interface OnClickListener {
	void onClick(View v);  // -> { view - > ... }
}


그림 5.10 람다의 파라미터는 메소드의 파라미터와 대응한다.
이런 코드가 작동하는 이유는 OnClickListener에 추상 메소드가 단 하나만 있기 때문이다.

그런 인터페이스를 함수형 인터페이스functional interface 또는 SAM 인터페이스라고 한다.

SAM은 단일 추상 메소드single abstract method라는 뜻이다.



자바 API에는 Runnable이나 Callable과 같은 함수형 인터페이스와 그런 함수형 인터페이스를 활용하는 메소드가 많다.

코틀린은 함수형 인터페이스를 인자로 취하는 자바 메소드를 호출할 때 람다를 넘길 수 있게 해준다.

따라서 코틀린 코드는 무명 클래스 인스턴스를 정의하고 활용할 필요가 없어서 여전히 깔끔하며 코틀린다운 코드로 남아있을 수 있다.



노트
자바와 달리 코틀린에는 제대로 된 함수 타입이 존재한다. 따라서 코틀린에서 함수를 인 자로 받을 필요가 있는 함수는 함수형 인터페이스가 아니라 함수 타입을 인자 타입으로 사용해야 한다. 코틀린 함수를 사용할 때는 코틀린 컴파일러가 코틀린 람다를 함수형 인 터페이스로 변환해주지 않는다. 함수 선언에서 함수 타입을 사용하는 방법은 8.1절에서 설명한다.

이제 함수형 인터페이스 타입을 인자로 요구하는 자바 메소드에 람다를 전달하는 경우 어떤 일이 벌어지는지 자세히 살펴보자.





자바 메소드에 람다를 인자로 전달
함수형 인터페이스를 인자로 원하는 자바 메소드에 코틀린 람다를 전달할 수 있다.

예를 들어 다음 메소드는 Runnable 타입의 파라미터를 받는다. 



/* 자바 */
void postponeComputation(int delay, Runnable computation);


코틀린에서 람다를 이 함수에 넘길 수 있다.

컴파일러는 자동으로 람다를 Runnable 인스턴스로 변환해준다.



postponeComputation(1000) { println(42) }

여기서 'Runnable 인스턴스'라는 말은 실제로는 'Runnable을 구현한 무명 클래스의 인스턴스'라는 뜻이다.

컴파일러는 자동으로 그런 무명 클래스와 인스턴스를 만들어준다. 이때 그 무명 클래스에 있는 유일한 추상 메소드를 구현할 때 람다 본문을 메소드 본문으 로 사용한다. 여기서는 Runnable의 run이 그런 추상 메소드다.
Runnable을 구현하는 무명 객체를 명시적으로 만들어서 사용할 수도 있다.



