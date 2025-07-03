# 15. Structured concurrency

<small><i>구조화된 동시성</i></small>

여러 동시 작업을 처리할때 어려운 점:
- 실행중인 개별 작업을 추적
- 더이상 필요하지 않을때 이를 취소
- 오류를 제대로 처리

<br>

애플리케이션에서는 보통 많은 코루틴을 처리하는데,
이 때 코루틴을 추적하지 않으면, 리소스 누수 혹은 불필요한 작업을 하게 될 수 있음

#### ➡️ **구조화된 동시성(Structured Concurrency)**

: 애플리케이션 안에서 코루틴과 그 생애 주기의 계층을 관리하고 추적할 수 있는 기능이
- 코틀린 코루틴의 핵심에 내장돼 있음
- 수동으로 시작된 각 코루틴을 일일이 추적하지 않아도 기본적으로 작동

<br>

## 15.1 Coroutine scopes establish structure between coroutines With

<small><i>15.1 코루틴 스코프가 코루틴 간의 구조를 확립한다</i></small>

구조화된 동시성을 통해 각 코루틴은 <b>코루틴 스코프<sup>coroutine scope</sup></b>에 속함

코루틴 스코프는 코루틴 간의 부모-자식 관계를 확립하는데 도움

- `launch`와 `async` 코루틴 빌더 함수들은 사실 `CoroutineScope` 인터페이스의 확장 함수
  - 즉, 다른 코루틴 빌더의 본문에서 `launch`나 `async`를 사용해 새로운 코루틴을 만들면 이 새로운 코루틴은 자동으로 해당 코루틴의 자식이 됨

<br>

**Example. 완료 시간이 다른 여러 코루틴**

출력을 보면 `runBlocking` 함수 본문이 즉시 실행을 마쳤음에도 (Parent done으로 알 수 있음) 모든 자식 코루틴이 완료될 때까지 프로그램이 종료되지 않는 것을 알 수 있음

```kotlin
fun main() {
    runBlocking { // this: CoroutineScope     // 암시적 수신 객체
        launch { // this: CoroutineScope      // launch가 시작한 코루틴은 runBlocking의 자식
            delay(1.seconds)
            launch {
                delay(250.milliseconds)
                log("Grandchild done")
            }
            log("Child 1 done!")
        }
        launch {
            delay(500.milliseconds)
            log("Child 2 done!")
        }
        log("Parent done!")
    }
}
```

- 코루틴 간에는 부모-자식관계가 존재 (정확히 말하면 Job 객체들 간의 관계)
- 따라서, `runBlocking`은 자식 코루틴의 작업 상태를 알고, 모든 작업이 완료될 때까지 기다림
- 부모 코루틴이 취소되면 자식 코루틴도 자동으로 취소되는 기능이 있음
  - 실행한 코루틴이나 자손을 수동 추적할 필요가 없음
  - 수동으로 `await`를 호출할 필요도 없음


<br><br><img src="./img/figure15-01.png" width="60%">

구조화된 동시성 덕분에 코루틴은 계층 구조 안에 존재. 

명시적으로 지정하지 않았음에도 각 코루틴은 자식이나 부모를 알고 있음.

가령, `runBlocking`은 종료되기 전에 모든 자식이 완료되기를 기다릴 수 있음

<br><br>

## 15.1.2 Associating coroutine scopes with components: CoroutineScope

<small><i>구성요소와 코루틴 스코프 연결: CoroutineScope</i></small>

`coroutineScope` 함수가 작업을 분해하는데 사용되는 반면 
구체적 생명주기를 정의하고, 동시처리나 코루틴의 시작과 종료를 관리하는 클래스를 만들고 싶을 때도 있음. 

→ `CoroutineScope` 생성자 함수를 사용해 새로운 독자적 인코루틴 스코프를 생성

- `coroutineScope`와는 달리 이 함수는 실행을 일시중단하지 않음
- 단순히 새로운 코루틴을 시작을 위한 새로운 코루틴 스코프를 생성

- `CoroutineScope`는 코루틴 콘텍스트 하나의 파라미터를 받음
  - e.g. 해당 범위에서 시작된 코루틴이 사용할 디스패처를 지정할 수 있음.
- 기본적으로 `CoroutineScope`를 디스패처만으로 호출하면 새로운 Job이 자동으로 생성됨
하지만 대부분의 실무에서는 `CoroutineScope`와 함께 `SupervisorJob`을 사용하는 것이 좋음


**✔️ `SupervisorJob`**
: 동일한 영역과 관련된 다른 코루틴을 취소하지 않고, 처리되지 않은 예외를 전파하지 않게 해주는 특수한 Job


```kotlin
class ComponentWithScope(dispatcher: CoroutineDispatcher = 
➥ Dispatchers.Default) {
 
    private val scope = CoroutineScope(dispatcher + SupervisorJob())
 
    fun start() {
        log("Starting!")
        scope.launch {
            while(true) {
                delay(500.milliseconds)
                log("Component working!")
            }
        }
        scope.launch {
            log("Doing a one-off task...")
            delay(500.milliseconds)
            log("Task done!")
        }
    }
 
    fun stop() {
        log("Stopping!")
        scope.cancel()
    }
}
```

자체 생명주기를 따르며 코루틴을 시작하고 관리할 수 있는 클래스를 만듦 
이 클래스는 생성자인자로 코루틴 디스패처를 받고, `CoroutineScope` 함수를 사용해 클래스와 연관된 새로운 코루틴 스코프를 생성

- `start` 함수: 계속 실행되는 코루틴 하나와 작업을 수행하는 코루틴 하나를 시작
- `stop` 함수: 클래스와 연관된 범위를 취소하며, 이로 인해 이전에 시작된 코루틴들도 함께 취소됨

이 `Component` 클래스의 인스턴스를 생성하고 `start`를 호출하면 컴포넌트 내부에서 코루틴이 시작
그 후 `stop`을 호출하면 컴포넌트의 생명주기가 종료됨

```kotlin
fun main() {
    val c = ComponentWithScope()
    c.start()
    Thread.sleep(2000)
    c.stop()
}
// 22 [main] Starting!
// 37 [DefaultDispatcher-worker-2 @coroutine#2] Doing a one-off task...
// 544 [DefaultDispatcher-worker-1 @coroutine#2] Task done!
// 544 [DefaultDispatcher-worker-2 @coroutine#1] Component working!
// 1050 [DefaultDispatcher-worker-1 @coroutine#1] Component working!
// 1555 [DefaultDispatcher-worker-1 @coroutine#1] Component working!
// 2039 [main] Stopping!
```

생명주기를 관리해야 하는 컴포넌트를 다루는 프레임워크에서는 내부적으로 CoroutineScope 함수를 많이 사용한다. 

<br>

#### CoroutineScope와 CoroutineScope 

비슷한 이름이지만 `coroutineScope` 함수와 `CoroutineScope` 함수의 목적은 서로 다름

- `coroutineScope`는 작업을 동시성으로 실행하기 위해 분해할 때 사용. 여러 코루틴을 시작하고, 그들이 모두 완료될 때까지 기다리며, 결과를 계산할 수도 있음. `coroutineScope`는 자식들이 모두 완료될 때까지 기다리기 때문에 일시중단 함수임.


- `CoroutineScope`는 코루틴을 클래스의 생명주기와 연관시키는 영역을 생성할 때 쓰임. 이 함수는 영역을 생성하지만 추가 작업을 기다리지 않고 즉시 반환됨. 반환된 코루틴 스코프를 나중에 취소<sup>Cancellation</sup> 할 수 있음


실무에서는 일시중단 함수인 `coroutineScope`가 `CoroutineScope` 생성자 함수보다 더 많이 사용됨. `coroutineScope`는 일시중단 함수의 본문에서 자주 호출되며, `CoroutineScope` 생성자는 클래스 프로퍼티로 코루틴 스코프를 저장할 때 주로 사용됨


