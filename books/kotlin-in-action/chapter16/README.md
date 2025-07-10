# 16. Flow

<small><i>플로우는 연속적인 값의 스트림을 모델링한다</i></small>

<br>

## 16.1 Flows model sequential streams of values

<small><i>플로우는 연속적인 값의 스트림을 모델링한다</i></small>

- 일시 중단 함수는 한 번 또는 여러 번 실행을 중단할 수 있음
- 단, 일시 중단 함수는 원시 타입, 객체, 객체의 컬렉션과 같은 단일 값만 반환할 수 있음


**플로우**
- 시간이 지남에 따라 나타나는 값과 작업할 수 있게 해주는 코루틴 기반의 추상화

<br><br>

> [!NOTE] 
> 
> 반응형 스트림<sup>reactive stream</sup>에서 영감을 받음
> - **반응형 스트림 구현체**: [RxJava](https://reactivex.io), [Project Reactor](https://projectreactor.io)
> - **범용적인 추상화 설계**: 점진적인 로딩, 이벤트 스트림 작업, 구독 스타일 API 모델링

<br><br>

### 16.1.1 플로우를 사용하면 배출되자마자 원소를 처리할 수있다

<small><i>플로우를 사용하면 배출되자마자 원소를 처리할 수있다</i></small>

<br>

- **플로우 X**: 여러 값을 계산하는 로직 → 모든 로직 실행 후 결과 값 모두 반환
- **플로우 O**: 여러 값을 계산하는 로직 → 비동기적으로 값을 반환하고 싶을 때 사용

<br>

**플로우 사용법**
- `flow` 빌더 함수를 사용
- 플로우에 원소를 추가하려면 `emit`을 호출
- 빌더 함수 호출 후에는 `collect` 함수를 사용해 플로우의 원소를 순회

**Example.**

<table>
<tr>
<th>플로우 X</th>
<th>플로우 O</th>
</tr>
<tr>
<td>

```kotlin
buildList {      // 모든 데이터 출력 시간: 약 3초
    add(1)              // 3099 [main @coroutine#1] 1
    delay(1.seconds)
    add(2)              // 3107 [main @coroutine#1] 2
    delay(1.seconds)
    add(3)              // 3107 [main @coroutine#1] 3
    delay(1.seconds)
}.forEach { log(it) }
```

</td>
<td>

```kotlin
flow {          // 각 데이터는 생성된 즉시 출력됨
    emit(1)            // 29 [main @coroutine#1] 1
    delay(1000.milliseconds)
    emit(2)            // 1100 [main @coroutine#1] 2
    delay(1000.milliseconds)
    emit(3)            // 2156 [main @coroutine#1] 3
    delay(1000.milliseconds)
}.collect { log(it) }
...

```

**`emit()`**에서 배출된 원소는 즉시 `collect` (수집자)에 의해 처리됨

</td>
</tr>
</table>


<br>

### 16.1.2 코틀린 플로우의 여러 유형

<small><i>플로우를 사용하면 배출되자마자 원소를 처리할 수있다</i></small>

- **콜드 플로우**: 비동기 데이터 스트림으로, 값이 실제로 소비되기 시작할 때만 값을 배출
- **핫 플로우**: 값이 실제로 소비되고 있는지와 상관없이 값을 독립적으로 배출하며, 브로드캐스트 방식으로 동작

<br>

## 16.2 cold flow

<small><i>콜드 플로우</i></small>

<br>

### 16.2.1 Creating a cold flow with the flow builder function

<small><i>flow 빌더 함수를 사용해 콜드 플로우 생성</i></small>

새로운 콜드 플로우를 생성하는 것은 간단
- `flow`: 컬렉션과 마찬가지로 새로운 플로우를 생성할 수 있는 빌더 함수
- **`emit` 함수**: 플로우의 수집자에게 값을 제공 ← 빌더 함수의 블록 내부
  - 비동기 return과 비슷하게 생각할 수 있음
- **수집자**: 해당 값을 처리할 때까지 빌더 함수의 실행을 중단

<br>

**Flow 생성**

```kotlin
val letters = flow {
    log("Emitting A!")
    emit("A")
    delay(200.milliseconds)
    log("Emitting B!")
    emit("B")
}
```

- 이 코드를 실행하면 실제로 아무런 출력도 나타나지 않음
- 빌더 함수가 `Flow` 타입 객체를 반환하기 때문


플로우는 처음에 비활성 상태 → **최종 연산자**<sup>terminal operator</sup>가 호출돼야만 빌더에서 정의된 계산이 시작

→ **플로우가 콜드라고 불리는 이유**: 기본적으로 수집되기 시작할 때까지 비활성 상태이기 때문

**`flow` 빌더 함수**

- `flow` 빌더 함수는 호출만으로는 작업이 시작되지 않음
- 일시 중단 함수가 아닌 일반 함수에서도 플로우를 반환할 수 있음
- `flow` 빌더 내부에서는 일시 중단 함수 호출이 가능함
- 실무에서는 여러 값을 비동기적으로 반환하는 콜드 플로우를 자주 사용함

- `flow` 빌더 블록은 `suspend`로 정의되어 있어, 내부에서 `delay` 등 일시 중단 함수를 자유롭게 호출할 수 있음


```kotlin
fun getElementsFromNetwork(): Flow<String> {
    return flow {
        // suspending network call here
    }
}
```

빌더 함수 안의 코드는 플로우가 수집될 때만 실행됨

때문에 시퀀스와 마찬가지로 무한 플로우를 정의하고 반환해도 됨

**Example.** 실제로 플로우가 수집될 때만 루프 실행

```kotlin
val counterFlow = flow {
    var x = 0
    while (true) {
        emit(x++)
        delay(200.milliseconds)
    }
}
```

<br>

### 16.2.2 Cold flows don’t do any work until collected

<small><i>flow 빌더 함수를 사용해 콜드 플로우 생성</i></small>

- `Flow`에 `collect` 함수를 호출하면 플로우의 로직이 실행됨
- 플로우를 수집하는 코드를 **수집자**<sup>collector</sup>라고 부름
- `collect` 호출 시, 플로우에서 배출된 각 원소에 대해 실행할 람다를 전달할 수 있음

<br>

#### `collect`는 일시 중단 함수

- `collect`는 일시 중단 함수이기 때문에, 플로우가 끝날 때까지 일시 중단됨
- 수집자 람다도 일시 중단 함수이므로, 내부에서 다른 일시 중단 함수 호출 가능
