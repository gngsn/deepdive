# 16. Flow

<small><i>플로우</i></small>

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

**Example. 문자 플로우 수집**

수신한 값을 데이터베이스에 저장하거나 HTTP 요청 수행 가능

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

**Flow 수집자**

```kotlin
runBlocking {
    letters.collect {
        log("Collecting $it")
        delay(500.milliseconds)
    }
}
```

**Output:**

```
27 [main @coroutine#1] Emitting A!
38 [main @coroutine#1] Collecting A
757 [main @coroutine#1] Emitting B!
757 [main @coroutine#1] Collecting B
```

수집자가 `collect`를 호출할 때 플로우 시작

A와 B 사이의 지연은 다음 순서로 발생:
- 수집자가 플로우 실행을 시작하고 첫 값을 배출
- 수집자가 값을 받아 500ms 동안 처리
- 이후 플로우가 200ms 지연 후 다음 값을 배출하고, 수집자가 다시 처리


<br><br><img src="./img/figure16-1.png" alt="콜드 플로우의 수집자" width="50%"><br><br>


- 콜드 플로우에서 `collect`를 여러 번 호출하면 그 코드가 여러 번 실행됨
  - 네트워크 요청과 같은 부수 효과가 있을 때 주의
- `collect` 함수는 플로우의 모든 원소가 처리될 때까지 일시 중단됨
- 플로우에 무한한 원소가 있을 수 있으므로 `collect` 함수도 무기한 일시 중단될 수 있음
- 모든 원소가 처리되기 전에 플로우 수집을 중지하고 싶으면 플로우를 취소할 수 있음

<br>

### 16.2.3 Cancelling the collection of a flow

<small><i>플로우 수집 취소</i></small>

- 코루틴을 취소하는 메커니즘이 플로우 수집자에게도 적용됨
- 수집자의 코루틴을 취소하면 다음 취소 지점에서 플로우 수집이 중단됨

<br><br>

> [!NOTE] 
> 
> 다른 내장된 일시 중단 함수와 마찬가지로 `emit`도 코드에서 취소와 일시 중단 지점으로 작동

<br><br>

FYI. 플로우 실행을 취소할 수 있는 다른 방법: `take` 연산자 (17장 참고)

<br>

### 16.2.4 Cold flows under the hood

<small><i>콜드 플로우의 내부 구현</i></small>

코틀린의 콜드 플로우는 **일시 중단 함수**와 **수신 객체 지정 람다**를 **결합**

콜드 플로우의 정의는 `Flow`와 `FlowCollector`라는 2가지 인터페이스만 필요

- **Flow**: 플로우를 정의하는 인터페이스 - `collect` 단일 함수를 정의
- **FlowCollector**: 플로우의 원소를 수집하는 인터페이스 - `emit` 단일 함수 정의

<table><tr><td>

```kotlin
interface Flow<out T> {
    suspend fun collect(collector: FlowCollector<T>)
}
```

</td><td>

```kotlin
interface FlowCollector<in T> {
    suspend fun emit(value: T)
}
```

</tr></table>

1. `collect`를 호출하면 플로우 빌더 함수의 본문이 실행됨
2. 이 코드가 `emit`을 호출하면 `emit`에 전달된 파라미터로 `collect`에 전달된 람다가 호출됨
3. 람다 표현식이 실행을 완료하면 함수는 빌더 함수의 본문으로 돌아가 계속 실행됨

<br><br><img src="./img/figure16-2.png" alt="콜드 플로우의 내부 구현" width="40%"><br><br>

일반적인 콜드 플로우에는 움직이는 부분이 그리 많지 않지만 콜드 플로우는 값 스트림을 처리하는 가벼우면서 아주 쓸모있고 확장성 있는 추상화를 제공 

<br>

### 16.2.5 Concurrent flows with channel flows

<small><i>채널 플로우를 사용한 동시성 플로우</i></small>

- 지금까지 `flow` 빌더 함수를 사용해 만든 **콜드 플로우는 모두 순차적으로 실행됨**
- 코드 블록은 일시 중단 함수의 본문처럼 하나의 코루틴으로 실행됨
- 따라서 `emit` 호출도 순차적으로 실행됨
- 순차적 특성은 병목이 될 수 있음

<br>

**Example.**


<table>
<tr>
<th>Cold Flow</th>
<th>Output</th>
</tr>
<tr>
<td>

`randomNumbers` 플로우: 0.5초 딜레이를 준 `getRandomNumber` 함수 호출

```kotlin
suspend fun getRandomNumber(): Int {
    delay(500.milliseconds)
    return Random.nextInt()
}
 
val randomNumbers = flow {
    // 총 5초 (500ms * 10) 소요
    repeat(10) {
        emit(getRandomNumber())
    }
}
```

</td>
<td>

```
fun main() = runBlocking {
    randomNumbers.collect {
        log(it)
    }
}

// 583 [main @coroutine#1] 1514439879
// 1120 [main @coroutine#1] 1785211458
// 1693 [main @coroutine#1] -996479986
// ...
// 5463 [main @coroutine#1] -2047597449
```

</td>
</tr>
</table>

<br><img src="./img/figure16-3.png" alt="순차적으로 실행되는 플로우" width="70%">

- 플로우는 순차적으로 실행
- 모든 계산은 동일한 코루틴에서 실행

수행 작업이 동시성으로 수행하기 적합하고 병렬로 수행 가능하다면 

→ `async` 같이 동시성 적용해서 실행을 빨라지게 할 수 있음

<br>

**❓ 플로우 빌더에서 백그라운드 코루틴을 실행하고 그 코루틴에서 직접 값을 배출하면?**

`Flow invariant is violated: Emission from another coroutine is detected. FlowCollector is not thread-safe and concurrent emissions are prohibited.` 오류 발생

→ 플로우 수집자가 스레드 안전하지 않다는 것을 나타내는 코드를 사용하면 안된다는 뜻

```kotlin
val randomNumbers = flow {
    coroutineScope {
        repeat(10) {
            launch { emit(getRandomNumber()) }   // Error: emit can’t be called from a different coroutine
        }
    }
}
```

→ 기본적인 콜드플로우 추상화가 같은 코루틴 안에서만 `emit` 함수를 호출할 수 있게 허용하기 때문


#### Channel Flow

- **`ChannelFlow` 빌더 함수**: **여러 코루틴에서 배출을 허용하는 동시성 플로우**를 생성
- 콜드 플로우의 특별한 유형
  - **일반 콜드 플로우**: `emit` 함수로 값 제공
  - **채널 플로우**: `send` 함수로 값 제공

수집자는 동일하게 `collect` 람다로 값을 순차적으로 수신

```kotlin
val randomNumbers = channelFlow {     // 새 채널 플로우 생성
    repeat(10) {
        launch {
            send(getRandomNumber())   // 여러 코루틴에서 send 호출 가능
        }
    }
}
```

`coroutineScope` 함수처럼 `ChannelFlow`의 람다는 새로운 백그라운드 코루틴을 시작할 수 있는 코루틴 스코프를 제공

<br><br><img src="./img/figure16-4.png" alt="동시적으로 실행되는 플로우" width="40%"><br><br>

#### 채널 플로우 vs. 일반 콜드 플로우

결론: **플로우 안에서 새로운 코루틴을 시작해야 하는 경우에만 채널 플로우를 선택**

<br>

**✔️ 채널 플로우:**

- 동시 작업이라는 구체적 용례를 위해 설계됨
- 일반 플로우가 할 수 있는 모든 일을 할 수 있으면서 더 많은 기능을 제공
- 코루틴 간 통신을 위한 비교적 저수준의 추상화이며, `send` 함수는 채널이 노출하는 복잡한 인터페이스의 일부
- 내부적으로 또 다른 동시성 기본 요소인 채널을 관리해야 하기 때문에 생성하는 데 약간 비용이 듦

<br>

**✔️ 일반 플로우:**
- 가장 간단하고 성능이 좋은 추상화
- 콜드 플로우는 엄격하게 순차적으로 실행되며 새로운 코루틴을 시작할 수 없지만 아주 쉽게 생성할 수 있고, 인터페이스가 한 가지 함수(emit)로 구성되며, 관리해야 할 추가적인 원소나 오버헤드가 없음

<br><br>

**정리하자면,**
콜드 플로우는 시간이 지남에 따라 계산된 값을 처리하는 데 유용한 추상화
콜드 플로우는 항상 수집자와 직접적으로 연관돼 있고,각 수집자는 플로우에 지정된 코드를 독립적으로 실행

<br>

## 16.3 Hot flows

<small><i>핫 플로우</i></small>

핫 플로우는 콜드 플로우와 배출<sup>Emission</sup>과 수집<sup>Collection</sup> 구조는 동일하지만 다른 특성을 가짐

**핫 플로우**

- 여러 구독자<sup>Subscriber</sup>라고 불리는 수집자들이 배출된 항목을 공유
- 시스템에서 이벤트나 상태 변경이 발생해서 수집자가 존재하는지 여부에 상관없이 값을 배출해야 하는 경우에 적합

핫 플로우는 항상 활성 상태이기 때문에 구독자의 유무에 관계없이 배출이 발생할 수 있음

<br>

**코틀린 코루틴에는 기본적으로 2가지 핫 플로우 구현 제공**

- **Shared Flow**: 공유 플로우. 값을 브로드캐스트하기 위해 사용
- **State Flow**: 상태 플로우. 상태를 전달하는 특별한 경우에 사용

<br>

상태 플로우를 공유 플로우보다 더 자주 사용하게 될 것

공유 플로우를 사용하는 코드를 상태플로우로 변환할 수 있음

<br>

### 16.3.1 Shared flows broadcast values to subscribers

<small><i>공유 플로우는 값을 구독자에게 브로드캐스트한다</i></small>

공유 플로우는 구독자<sub>공유 플로우의 수집자</sub>가 존재하는지 여부에 상관없이 배출이 발생하는 브로드캐스트 방식으로 동작

<br><br><img src="./img/figure16-5.png" alt="핫 플로우의 수집자" width="70%"><br><br>

공유 플로우는 보통 컨테이너 클래스 안에 선언됨

<br><br>

**Example. `RadioStation` 클래스**

[Backing Property](https://kotlinlang.org/docs/properties.html#backing-properties)를 활용한 공유 플로우 정의

```kotlin
class RadioStation {
    private val _messageFlow = MutableSharedFlow<Int>()  // 새 가변 공유 플로우
    val messageFlow = _messageFlow.asSharedFlow()        // 전역 읽기 전용 공유 플로우
 
    fun beginBroadcasting(scope: CoroutineScope) {
        scope.launch {
            while(true) {
                delay(500.milliseconds)
                val number = Random.nextInt(0..10)
                log("Emitting $number!")
                _messageFlow.emit(number)                // 가변 공유 플로우에 값 배출
            }
        }
    }
}
```

<br>

- `SharedFlow` 같은 핫 플로우를 만드는 방식이 콜드 플로우와 다름
- 플로우 빌더를 사용하는 대신 가변적인 플로우에 대한 참조를 얻음

→ 배출이 구독자 유무와 관계없이 발생하므로, 개발자가 실제 배출을 수행하는 코루틴을 시작할 책임이 있음

다르게 해석하면, 여러 코루틴에서 가변 공유 플로우에 값을 배출할 수 있음

<br>

#### 핫 플로우 이름을 붙일 때 밑줄 쓰기

공유 플로우(와 상태 플로우)에서 비공개 변수 이름에는 밑줄을 사용하고, 공개 변수에는 밑줄을 사용하지 않는 패턴을 따르는 이유?

- 현재(*2025-07-13*) 코틀린에서는 `private`과 `public` 프로퍼티에 대해 서로 다른 타입을 지정하는 기능을 지원하지 않음
- 캡슐화와 정보 은닉이라는 관심사에 따른 것
- 클래스의 소비자는 보통 플로우를 구독하기만 할 뿐, 원소를 배출하지 않아야 함

[📄 KEEP-0430-explicit-backing-fields](https://github.com/Kotlin/KEEP/blob/main/proposals/KEEP-0430-explicit-backing-fields.md)

<br>

`Radiostation` 클래스의 인스턴스를 생성하고 `beginBroadcasting` 함수를 호출하면, 구독자가 없어도 브로드캐스트가 즉시 시작됨

```kotlin
fun main() = runBlocking {
    RadioStation().beginBroadcasting(this)    // runBlocking의 코루틴 스코프에서 코루틴 시작
}
```

**Output:**
 
```kotlin
0 [main] Emitting 6!
513 [main] Emitting 3!
1017 [main] Emitting 6!
1519 [main] Emitting 10!
...
```

구독자를 추가하는 방법은 `collect`를 호출 (콜드 플로우와 동일)

배출이 발생할 때마다 제공한 람다가 실행됨

단, 구독자는 구독을 시작한 이후에 배출된 값만 수신함

```kotlin
fun main(): Unit = runBlocking {
    val radioStation = RadioStation()
    radioStation.beginBroadcasting(this)
    delay(600.milliseconds)
    radioStation.messageFlow.collect {
        log("A collecting $it!")
    }
}
```

**Output:**

```kotlin
0 [main] Emitting 9!            // 🚨 첫 번째 값이 구독자에 의해 수집되지 않음 (약 500밀리초 후에 배출된 값)
516 [main] Emitting 2!
519 [main] A collecting 2!
1023 [main] Emitting 5!
...
```

공유 플로우는 브로드캐스트 방식으로 작동하기 때문에, 구독자를 추가해서 이미 존재하는 `messageFlow`의 배출을 수신할 수 있음

**Example.** `runBlocking` 블록 안에 `launch`로 같은 플로우를 구독하는 두 번째 코루틴을 추가

```kotlin
launch {
    radioStation.messageFlow.collect {
        log("B collecting $it!")
    }
}
```

**같은 공유 플로우를 구독하는 모든 구독자와 똑같은 값을 수신**

<br>

#### Replaying values for subscribers

<small><i>`replay` 파라미터 (구독자를 위한 값 재생)</i></small>

**`replay` 파라미터**: 구독자가 처음 구독 시 항상 최신 값 N 개를 사용할 수 있게 함

공유 플로우 구독자는 구독을 시작한 이후에 배출된 값만 수신

구독자가 구독 이전에 배출된 원소도 수신을 원한다면?

→ `MutableSharedFlow`를 생성할 때 `replay` 파라미터를 사용해 새 구독자를 위해 제공할 값의 캐시를 설정할 수 있음

```kotlin
// 마지막 5개의 값을 재생하도록 `messageFlow`를 설정
private val _messageFlow = MutableSharedFlow<Int>(replay = 5)
```

600밀리초가 지난 다음에 수집자를 시작해도, 구독 직전 발생한 최대 5개의 값을 수신할 수 있음

(예제에서는 구독자는 구독 시작 전인 560밀리초에 배출된 값을 수신)

<br>

#### From cold flow to shared flow with `shareIn`

<small><i>`SharedIn` 으로 콜드 플로우를 공유 플로우로 전환</i></small>

온도 센서에서 50밀리초 간격으로 수집되는 값의 스트림을 제공하는 함수

```kotlin
fun querySensor(): Int = Random.nextInt(-10..30)
 
fun getTemperatures(): Flow<Int> {
    return flow {
        while(true) {
            emit(querySensor())
            delay(500.milliseconds)
        }
    }
}
```

이 함수를 여러 번 호출할 때 각 수집자는 센서에 독립적으로 질의

```kotlin
fun celsiusToFahrenheit(celsius: Int) =
    celsius * 9.0 / 5.0 + 32.0
 
fun main() {
    val temps = getTemperatures()
    runBlocking {
        launch {
            temps.collect {           // 플로우 수집 1 - 섭씨 출력
                log("$it Celsius")
            }
        }
        launch {
            temps.collect {           // 플로우 수집 2 - 화씨로 변환 후 출력
                log("${celsiusToFahrenheit(it)} Fahrenheit")
            }
        }
    }
}
```

- 불필요한 외부 시스템 연산을 피하고 싶을 때가 자주 있음
  - (e.g. 센서와 상호작용하거나 네트워크 요청을 보내거나 데이터베이스 쿼리를 실행할 때)
- 이 때, 반환된 플로우를 두 수집자가 **모두 같은 원소를 받아야 함**

`shareIn` 함수를 사용하면 주어진 콜드 플로우를 한 플로우인 공유 플로우로 변환할 수 있음

⚠️ 플로우 코드가 실행되게 하므로 `shareIn`을 코루틴 안에서 호출해야 함

→ 이를 위해 `ShareIn`은 `Coroutinescope` 타입의 `scope` 파라미터를 받아서 코루틴을 실행


```kotlin
fun main() {
    val temps = getTemperatures()
    runBlocking {
        val sharedTemps = temps.shareIn(this, SharingStarted.Lazily)
        launch {
            sharedTemps.collect {
                log("$it Celsius")
            }
        }
        launch {
            sharedTemps.collect {
                log("${celsiusToFahrenheit(it)} Fahrenheit")
            }
        }
    }
}
```

**Output:**

```kotlin
...
497 [main] 29 Celsius
497 [main] 84.2 Fahrenheit
1003 [main] 6 Celsius
1003 [main] 42.8 Fahrenheit
...
```

<br>

#### `shareIn` 정의

```kotlin
public fun <T> Flow<T>.shareIn(
    scope: CoroutineScope,
    started: SharingStarted,
    replay: Int = 0
): SharedFlow<T> {
    val config = configureSharing(replay)
    val shared = MutableSharedFlow<T>(
        replay = replay,
        extraBufferCapacity = config.extraBufferCapacity,
        onBufferOverflow = config.onBufferOverflow
    )
    @Suppress("UNCHECKED_CAST")
    val job = scope.launchSharing(config.context, config.upstream, shared, started, NO_VALUE as T)
    return ReadonlySharedFlow(shared, job)
}
```

두 번째 파라미터 `started`는 플로우가 실제로 언제 시작돼야 하는지를 정의

- `Eagerly`: 플로우 수집을 즉시 시작
- `Lazily`: 첫 번째 구독자가 나타나야만 수집 시작
- `WhileSubscribed`: 첫 번째 구독자가 나타나야 수집을 시작하고, 마지막 구독자가 사라지면 플로우 수집을 취소

<br>

**`shareIn`는 코루틴 스코프를 통해 구조적 동시성에 참여**

즉, 애플리케이션이 더 이상 공유 플로우에서 정보를 필요로 하지 않을 때, 공유 플로우를 둘러싼 코루틴 스코프가 취소될 때 공유 플로우 내부 로직도 자동으로 취소됨

<br>

시간이 지남에 따라 여러 값을 계산하는 작업을 단순한 콜드 플로우로 노출하고, 
필요할 때 이 콜드 플로우를 핫 플로우로 변환하는 패턴이 자주 사용됨

<br>

### 16.3.2 Keeping track of state in your system: State flow

<small><i>시스템 상태 추적: 상태 플로우</i></small>

- **상태 플로우**: 변수의 상태 변화를 쉽게 추적할 수 있는 공유 플로우의 특별한 버전
- 동시 시스템에서 '상태 추적'은 자주 요구
  - 즉, 시간이 지남에 따라 변할 수 있는 값을 다룸

**특징**
- 생성자에게 초기값을 제공 필수
  - 시간이 지남에 따라 변경될 수 있는 값을 나타내므로 
- 값을 배출하는 `emit`을 사용하는 대신, 값을 갱신하는 `update` 함수 사용
- 현재 상태를 **`value` 속성**으로 접근
  - 일시 중단 없이 값을 안전하게 읽을 수 있게 해줌  

<br>

**상태 플로우 관련 주제들**:
- 상태 플로우를 생성하고 구독자에게 노출시키는 방법  
- 상태 플로우의 값을 (병렬로 접근해도) 안전하게 갱신하는 방법  
- 값이 실제로 변경될 때만 상태 플로우가 값을 배출하게 하는 **동등성 기반 통합**<sup>quality-based conflation</sup> 개념  
- 콜드 플로우를 상태 플로우로 변환하는 방법

<br>

#### 상태 플로우 생성 방법

클래스의 `private` 속성으로 `MutableStateFlow`를 생성하고, 같은 변수의 읽기 전용 `StateFlow` 버전을 노출 (공유 플로우 생성 방식과 비슷)

```kotlin
class ViewCounter {
    private val _counter = MutableStateFlow(0)
    val counter = _counter.asStateFlow()
 
    fun increment() {
        _counter.update { it + 1 }
    }
}
```

**호출**

```kotlin
fun main() {
    val vc = ViewCounter()
    vc.increment()
    println(vc.counter.value)       // 1
}
```

<br>

#### 원자적 계산 지원: `update` 함수로 안전하게 상태 플로우에 쓰기

`value` 속성이 실제로는 가변 속성이긴 하지만,
코루틴들이 여러 스레드에서 실행되기 때문에,
`++` 연산자를 사용하면 원자적이지 않아 동시성 문제 발생

```kotlin
// 카운터의 최종 값은 10,000보다 훨씬 낮음
fun increment() {
    _counter.value++
}
```

상태 플로우는 **원자적으로 값을 갱신할 수 있는 `update` 함수 제공**

이 함수는 이전 값을 기반으로 새 값을 어떻게 계산해야 하는지 정하는 람다 표현식을 인자로 받음

#### 상태 플로우는 값이 실제로 달라졌을 때만 값을 배출한다: 동등성 기반 통합

공유 플로우처럼 상태 플로우도 `collect` 함수를 호출해 시간에 따라 값을 구독할 수 있음

스위치를 왼쪽이나 오른쪽으로 돌리는 함수를 노출하는 방향 선택자를 정의

<br><br><img src="./img/figure16-6.png" width="60%" /><br><br>

LEFT라는 인자를 2번 연속으로 전달했음에도 구독자가 한 번만 호출됨

→ 실제로 달라졌을 때만 구독자에게 값을 배출함

<br>

#### `stateIn`을 사용한 콜드 플로우 → 상태 플로우 변환

- 최신 값을 항상 유지하고 여러 수집자와 공유 가능
- `stateIn` 함수에게 시작 전략을 전달하지 않음
  - 시작 전략 없이 항상 주어진 스코프 내에서 시작되고, 코루틴 스코프가 취소될 때까지 `value`로 최신 값 제공

<br>

### 16.3.3 Comparing state flows and shared flows

<small><i>상태 플로우와 공유 플로우의 비교</i></small>

**공유 플로우**
- 구독 중에만 배출
- 구독자가 들어오고 나갈 수 있음
- 배출 시점에 구독자 존재 여부의 보장 책임이 개발자에게 있기 때문에 복잡할 수 있음

**상태 플로우**
- 값이 변경될 때만 배출
- 상태 플로우가 나타내는 값은 실제로 변경될 때만 배출이 발생
  - 특정 상태를 나타내며, 동등성 기반 통합을 사용
- API가 더 단순
- 재생 가능한 메시지 관리에 유리

<br>

공유 플로우 대신 상태 플로우로 재구성하면 더 간단한 API를 사용할 수 있으며,  
나중에 참여하는 수집자가 모든 갱신 기록을 확인할 수 있음

**Example. 브로드캐스트 시스템 - 공유 플로우 vs. 상태 플로우**

공유 플로우를 사용하면, 여러 구독자가 한 번에 메세지 수신

하지만, 메시지가 브로드캐스트된 다음에 구독자가 나타났을 때, 구독자는 이미 브로드캐스트된 메시지를 받을 수 없음

공유 플로우의 재생 캐시를 조정할 수 있지만, 더 간단한 추상화인 상태 플로우를 선택할 수도 있음

개별 메시지를 배출하는 대신, 상태 플로우는 전체 메시지 기록을 리스트로 저장하면서  

구독자가 모든 이전 메시지에 쉽게 접근할 수 있게 할 수 있음


<br>

### 16.3.4 Hot, cold, shared, state: When to use which flow

<small><i>핫 플로우, 콜드 플로우, 공유 플로우, 상태 플로우: 언제 어떤 플로우를 사용할까</i></small>

| 콜드 플로우 | 핫 플로우 |
|----|----|
| 기본적으로 비활성 (수집자에 의해 활성화됨) | 기본적으로 활성화됨 |
| 수집자가 하나 있음 | 여러 구독자가 있음 |
| 수집자는 모든 배출을 받음 | 구독자는 구독 시작 시점부터 배출을 받음 |
| 보통은 완료됨 | 완료되지 않음 |
| 하나의 코루틴에서 배출 발생 (channelFlow 사용 시 예외) | 여러 코루틴에서 배출할 수 있음 |

<br>

## Summary


- **코틀린 플로우**: **시간이 지남에 따라 발생하는 값을 처리**할 수 있는 코루틴 기반의 추상화
- 플로우는 두 가지 유형 존재:
1. **핫 플로우**
  - 항상 활성 상태
  - 여러 구독자와 연결됨
  - 핫 플로우의 두 종류: 공유 플로우, 상태 플로우
    - **공유 플로우**: 코루틴 간에 값을 브로드캐스트 방식으로 전달하는 데 활용 가능
      - 공유 플로우의 구독자는 **구독을 시작한 시점부터 배출된 값을 받으며**, 재생된 값도 수신할 수 있음
    - **상태 플로우**: 동시성 시스템에서 상태를 관리할 때 활용 가능
      - 상태 플로우는 동등성 기반 통합을 수행. 
        - 값이 실제로 변경된 경우에만 배출이 발생하고, 같은 값이 여러 번 대입되면 배출이 발생하지 않는다는 뜻
2. **콜드 플로우**
  - 기본적으로 비활성 상태
  - 하나의 수집자와 연결됨
  - `flow` 빌더 함수로 콜드 플로우를 생성, `emit` 함수로 비동기적으로 값을 제공
  - **채널 플로우**는 콜드 플로우의 특수 유형
    - 여러 코루틴에서 `send` 함수를 통해 값 배출

- `shareIn`이나 `stateIn` 함수를 통해 콜드 플로우를 핫 플로우로 전환할 수 있음

