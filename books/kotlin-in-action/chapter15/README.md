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

## 15.1.2 Associating coroutine scopes with components: `CoroutineScope`

<small><i>구성요소와 코루틴 스코프 연결: `CoroutineScope`</i></small>

**`CoroutineScope` 생성자 함수**
-  사용해 새로운 독자적 인코루틴 스코프를 생성
- **구체적 생명주기를 정의**하거나 **동시처리나 코루틴의 시작과 종료를 관리**하는 클래스 필요 시 사용
- `coroutineScope`와는 달리 이 함수는 실행을 일시중단하지 않음
- 단순히 새로운 코루틴을 시작을 위한 새로운 코루틴 스코프를 생성

<br>

```kotlin
public interface CoroutineScope {
    /**
     * The context of this scope.
     * Context is encapsulated by the scope and used for implementation of coroutine builders that are extensions on the scope.
     * Accessing this property in general code is not recommended for any purposes except accessing the [Job] instance for advanced usages.
     *
     * By convention, should contain an instance of a [job][Job] to enforce structured concurrency.
     */
    public val coroutineContext: CoroutineContext
}
```

[🔗 CoroutineScope Interface](https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/common/src/CoroutineScope.kt#L76)


- `CoroutineScope`는 코루틴 콘텍스트 하나의 파라미터를 받음
- 해당 범위에서 시작된 코루틴이 사용할 디스패처를 지정할 수 있음

<br>

```kotlin
public fun CoroutineScope(context: CoroutineContext): CoroutineScope =
    ContextScope(if (context[Job] != null) context else context + Job())
```

[🔗 CoroutineScope Function](https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/common/src/CoroutineScope.kt#L298)


기본적으로 `CoroutineScope`를 디스패처만으로 호출하면 새로운 `Job`이 자동으로 생성됨

대부분의 실무에서는 `CoroutineScope`와 함께 `SupervisorJob`을 사용하는 것이 좋음

<br>

**✔️ `SupervisorJob`**
: 동일한 영역과 관련된 다른 코루틴을 취소하지 않고, 처리되지 않은 예외를 전파하지 않게 해주는 특수한 Job

<br>

```kotlin
class ComponentWithScope(dispatcher: CoroutineDispatcher = Dispatchers.Default) {

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
```

**Output:**

```bash
0 [main] Starting!
27 [DefaultDispatcher-worker-2] Doing a one-off task...
535 [DefaultDispatcher-worker-1] Task done!
535 [DefaultDispatcher-worker-2] Component working!
1038 [DefaultDispatcher-worker-1] Component working!
1544 [DefaultDispatcher-worker-1] Component working!
2031 [main] Stopping!
```

생명주기를 관리해야 하는 컴포넌트를 다루는 프레임워크에서는 내부적으로 `CoroutineScope` 함수를 많이 사용

<br>

#### `coroutineScope`와 `CoroutineScope` 생성자 함수

비슷해 보이지만 목적이 서로 다름

- **`coroutineScope`**: 작업을 동시성으로 실행하기 위해 분해할 때 사용
  - 여러 코루틴을 시작하고, 그들이 모두 완료될 때까지 기다리며, 결과를 계산할 수도 있음
  - `coroutineScope`는 자식들이 모두 완료될 때까지 기다리기 때문에 일시중단 함수임

- **`CoroutineScope`**: 코루틴을 클래스의 생명주기와 연관시키는 영역을 생성할 때 쓰임
  - 이 함수는 영역을 생성하지만 추가 작업을 기다리지 않고 즉시 반환됨
  - 반환된 코루틴 스코프를 나중에 취소<sup>Cancellation</sup> 할 수 있음


실무에서는 일시중단 함수인 `coroutineScope`가 더 많이 사용됨

- `coroutineScope` → 일시중단 함수의 본문에서 자주 호출되며, 
- `CoroutineScope` 생성자 → 클래스 프로퍼티로 코루틴 스코프를 저장할 때 주로 사용됨

<br>

## 15.1.3 The danger of `GlobalScope`

<small><i>`GlobalScope`의 위험성</i></small>

<br>

**`GlobalScope`**: 특수한 코루틴 스코프 인스턴스

- 전역 수준에 존재하는 코루틴 스코프
- 🚨 `GlobalScope`를 사용하면 구조화된 동시성이 제공하는 모든 이점을 포기해야함

**단점**
- 전역범위에서 시작된 코루틴은 자동으로 취소되지 않음
- 생명주기에 대한 개념 없음
- 리소스 누수가 발생하거나 불필요한 작업을 계속 수행하면서 계산 자원을 낭비하게 될 가능성이 큼

<br>

**Example.** `GlobalScope`는 구조화된 동시성 계층을 깨뜨림

```kotlin
fun main() {
    runBlocking {
        GlobalScope.launch {           // 일반 애플리케이션에서 사용하지 말 것
            delay(1000.milliseconds)
            launch {
                delay(250.milliseconds)
                log("Grandchild done")
            }
            log("Child 1 done!")
        }
        GlobalScope.launch {
            delay(500.milliseconds)
            log("Child 2 done!")
        }
        log("Parent done!")
    }
}
```

**Output:**

```bash
// 28 [main @coroutine#1] Parent done!
```

`GlobalScope`를 사용함으로써 구조화된 동시성에서 자동으로 설정되는 계층구조가 깨져서 즉시 종료

<br><img src="./img/figure15-03.png" alt="GlobalScope의 위험성" width="70%"><br>

- `coroutine#2` 부터 `coroutine#4`는 `runBlocking`과 연관된 `coroutine#1`과의 부모관계에서 벗어나있음
- 따라서 부모 코루틴이 없으므로 프로그램은 자식들이 완료되기 전에 종료
- 이 이유로, `GlobalScope`는 특수한 주석(`DelicateCoroutinesApi`)과 함께 선언됨

→ 대신 코루틴 빌더나 `coroutineScope` 함수를 사용 권장


<br>

## 15.1.4 Coroutine contexts and structured concurrency

<small><i>코루틴 콘텍스트와 구조화된 동시성</i></small>


코루틴 콘텍스트는 구조화된 동시성 개념과 밀접한 관련이 있으며, 이는 코루틴 간의 부모-자식 관계 계층을 따라 상속됨

<br>

**새로운 코루틴을 시작할 때 코루틴 콘텍스트**
1. 자식 코루틴은 부모의 콘텍스트를 상속받음
2. 이후, 새로운 코루틴은 부모-자식 관계를 설정하는 역할을 하는 새 `Job` 객체를 생성
  - 이 `Job` 객체는 부모 코루틴의 Job 객체의 자식이 됨
3. 마지막으로 코루틴 콘텍스트에 전달된 인자가 적용됨
  - 이 인자들은 상속받은 값을 덮어쓸 수 있음

<br>

```kotlin
fun main() {
    runBlocking(Dispatchers.Default) {
        log(coroutineContext)
        launch {
            log(coroutineContext)
            launch(Dispatchers.IO + CoroutineName("mine")) {
                log(coroutineContext)
            }
        }
    }
}
```

**Output:**

```bash
// 0 [DefaultDispatcher-worker-1 @coroutine#1] [CoroutineId(1),
    "coroutine#1":BlockingCoroutine{Active}@68308697, Dispatchers.Default]
// 1 [DefaultDispatcher-worker-2 @coroutine#2] [CoroutineId(2),
    "coroutine#2":StandaloneCoroutine{Active}@2b3ce773, Dispatchers.Default]
// 2 [DefaultDispatcher-worker-3 @mine#3] [CoroutineName(mine),
    CoroutineId(3), "mine#3":StandaloneCoroutine{Active}@7c42841a,
    Dispatchers.IO]
```

<br>

디스패처를 지정하지 않고 새로운 코루틴을 시작하면 어떤 디스패처에서 실행될까?

- ❌ `Dispatchers.Default`
- ✅ **부모 코루틴의 디스패처**

<br>
<br><img src="./img/figure15-04.png" alt="코루틴 콘텍스트와 구조화된 동시성" width="60%"><br>

- 1️⃣ `runBlocking`은 특수한 디스패처인 `BlockingEventLoop`로 시작되며, 
- 2️⃣ 인자로 받은 값에 의해 `Dispatchers.Default`로 덮어 씌워짐. 
- 3️⃣ 코루틴은 `BlockingCoroutine`이라는 `Job` 객체를 생성하고, 기본값인 "coroutine"으로 코루틴 이름을 초기화
- 4️⃣ `launch`는 기본 디스패처를 상속받고 자신의 Job 객체로 `StandaloneCoroutine`을 생성하며 
- 5️⃣ 부모 Job과의 관계를 설정 (코루틴 이름은 변경되지 않음). 
- 6️⃣ 두번째 `launch` 호출도 디스패처를 상속받고 새로운 자식 Job을 생성하며, 코루틴 이름인 `"coroutine"`이 함께 설정됨
- 7️⃣ `launch`에 전달된 파라미터는 디스패처를 `Dispatchers.IO`로 변경하고 코루틴 이름을 "mine"으로 지정함

<br><br>

**코루틴간의 부모-자식관계 확인** 

코루틴과 연관된 Job간의 관계를 코드 상에서 확인할 수 있음

각 코루틴의 코루틴 콘텍스트에서 `coroutineContext`, `coroutineContext.parent`, `job.children` 속성을 확인하면 이를 볼 수 있음

```kotlin
import kotlinx.coroutines.job

fun main() = runBlocking(CoroutineName("A")) {
    log("A's job: ${coroutineContext.job}")
    launch(CoroutineName("B")) {
       log("B's job: ${coroutineContext.job}")
       log("B's parent: ${coroutineContext.job.parent}")
    }
    log("A's children: ${coroutineContext.job.children.toList()}")
}
```

**Output:**

```bash
0 [main] A's job: BlockingCoroutine{Active}@65ae6ba4
22 [main] A's children: [StandaloneCoroutine{Active}@6842775d]
24 [main] B's job: StandaloneCoroutine{Active}@6842775d
24 [main] B's parent: BlockingCoroutine{Completing}@65ae6ba4
```

- `coroutineScope` 함수도 자체 Job 객체를 갖고 부모-자식 계층 구조
- `coroutineScope`의 `coroutineContext.job` 속성을 통해 확인 가능
 
<br>

```kotlin
fun main() = runBlocking<Unit> { // coroutine#1
    log("A's job: ${coroutineContext.job}")
    coroutineScope {
        log("B's parent: ${coroutineContext.job.parent}") // A
        log("B's job: ${coroutineContext.job}") // C
        launch { //coroutine#2
            log("C's parent: ${coroutineContext.job.parent}") // B
        }
    }
}
```

**Output:**

```bash
// 0 [main @coroutine#1] A's job: "coroutine#1":BlockingCoroutine{Active}@41
// 2 [main @coroutine#1] B's parent:
    "coroutine#1":BlockingCoroutine{Active}@41
// 2 [main @coroutine#1] B's job: "coroutine#1":ScopeCoroutine{Active}@56
// 4 [main @coroutine#2] C's parent:
    "coroutine#1":ScopeCoroutine{Completing}@56
```

<br>

## 15.2 Cancellation

<small><i>취소</i></small>

취소는 코드가 완료되기 전에 실행을 중단하는 것

- 불필요한 작업을 막아줌
- 메모리나 리소스 누수를 방지에 도움을 줌
- 불필요한 작업을 피하는 특별한 경우 등 오류 처리에서도 중요한 역할

<br>

### 15.2.1 Triggering cancellation

<small><i>취소 촉발</i></small>

- 여러 코루틴 빌더 함수의 반환값을 취소를 촉발하는 핸들로 사용할 수 있음
- `launch` 코루틴 빌더는 `Job`을 반환하고 `async` 코루틴 빌더는 `Deferred`을 반환함
- `Job.cancel`을 호출해 해당 코루틴의 취소를 촉발할 수 있음

```kotlin
fun main() {
    runBlocking {
        val launchedJob = launch {      ❶
            log("I'm launched!")
            delay(1000.milliseconds)
            log("I'm done!")
        }
        val asyncDeferred = async {     ❷
            log("I'm async")
            delay(1000.milliseconds)
            log("I'm done!")
        }
        delay(200.milliseconds)
        launchedJob.cancel()            ❸
        asyncDeferred.cancel()          ❸
    }
}
```

**Output:**

```bash
0 [main @coroutine#2] I'm launched!
7 [main @coroutine#3] I'm async
```

각 **코루틴 스코프의 코루틴 컨텍스트에도 `Job`이 포함**돼있으며, 이를 사용해 영역을 취소할 수 있음 

<br>

### 15.2.2 Invoking cancellation automatically after a time limit has been exceeded

<small><i>시간 제한이 초과된 후 자동으로 취소 호출</i></small>

라이브러리가 특정 조건에서 자동으로 코루틴을 취소하게 할 수 있음

**`withTimeout` 과 `withTimeoutOrNull` 함수**
: 코루틴의 취소를 자동 트리거하는 함수 (코루틴 라이브러리 제공)

- 특정 시간 제한이 초과된 후 자동으로 코루틴을 취소할 수 있음
- **예외 처리**
  - `withTimeout` 함수는 타임아웃이 되면 예외(`TimeoutCancellationException`)를 발생시킴
  - `withTimeoutOrNull` 함수는 타임아웃이 발생하면 `null`을 반환

<br><br>

> [!NOTE]
> `withTimeout`이 발생시키는 `TimeoutCancellationException`을 반드시 잡아야 함
> 
> 상위 예외 타입인 `CancellationException`은 코루틴을 취소하기 위한 특별한 표식으로 사용됨
> 
> 즉, `TimeoutCancellationException`을 잡지 않으면 호출한 코루틴이 의도와 다르게 취소될 수 있음
> 
> 이 문제를 완전히 피하려면 `withTimeoutOrNull` 함수를 사용하는 편이 좋음

<br>

**Example.**

500밀리초로 타임아웃을 짧게 설정해 호출하면 타임아웃이 발생

이후 `calculateSomething` 함수는 취소되고 `null`이 반환

두번째 호출에서는 함수가 완료되기에 충분한 시간을 제공해 실제로 계산된 값을 반환 받을 수 있다.

```kotlin
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds
 
suspend fun calculateSomething(): Int {
    delay(3.seconds)
    return 2 + 2
}
 
fun main() = runBlocking {
    val quickResult = withTimeoutOrNull(500.milliseconds) {
        calculateSomething()
    }
    println(quickResult)
    // null
    val slowResult = withTimeoutOrNull(5.seconds) {
        calculateSomething()
    }
    println(slowResult)
    // 4
}
```

- `withTimeoutOrNull`을 사용하면 일시중단 함수의 실행 시간을 제한할 수 있음
- 함수가 주어진 시간 내에 값을 반환하면 그 즉시 값을 반환하고, 시간이 초과되면 함수는 취소되고 `null`을 반환

<br><img src="./img/figure15-05.png" alt="시간 제한이 초과된 후 자동으로 취소 호출" width="60%"><br>

<br>

### 15.2.3 Cancellation cascades through all children

<small><i>취소는 모든 자식 코루틴에게 전파된다</i></small>

코루틴을 취소하면 해당 코루틴의 모든 자식 코 루틴도 자동으로 취소됨

- 여러 계층에 걸쳐 코루틴이 중첩돼 있는 경우에도 가장 바깥쪽 코루틴을 취소하면 증손자(손자의 손자)
- 코루틴까지도 모두 적절히 취소됨

```kotlin
fun main() = runBlocking {
    val job = launch {
        launch {
            launch {
                launch {
                    log("I'm started")
                    delay(500.milliseconds)
                    log("I'm done!")
                }
            }
        }
    }
    delay(200.milliseconds)
    job.cancel()
}
```

**Output:**

```bash
0 [main @coroutine#5] I'm started 
```

<br><br>

### 15.2.4 Cancelled coroutines throw `CancellationExceptions` in special places

<small><i>취소된 코루틴은 특별한 지점에서 `CancellationException`을 던진다</i></small>

- 취소 메커니즘은 `CancellationException`이라는 특수한 예외를 특별한 지점에서 던지는 방식으로 작동함
- 우선적으로는 일시중단 지점이 이런 지점
- 취소된 코루틴은 일시중단 지점에서 `CancellationException`을 던짐
- 일시중단 지점은 코루틴의 실행을 일시중단할 수 있는 지점
- 일반적으로 코루틴 라이브러리 안의 모든 일시중단 함수는 `CancellationException`이 던져질 수 있는 지점을 도입

```kotlin
coroutineScope {
    log("A")
    delay(500.milliseconds)   // 취소 가능 지점
    log("B")
    log("C")
}
```

- 영역이 취소 여부에 따라 `A` 나 `A B C` 가 출력
  - `A B`이 출력될 가능성은 없음. `B`와 `C` 사이에 취소 지점이 없기 때문

> [!WARNING]
> 
> ⚠️ 코루틴은 예외를 사용해 코루틴 계층에서 취소를 전파하기 때문에 이 예외를 삼켜버리거나 직접 처리하지 않도록 주의해야 함

<br>

`UnsupportedOperationException`을 던질 수 있는 코드를 반복해서 실행

```kotlin
suspend fun doWork() {
    delay(500.milliseconds)                             // CancellationException을 던지는데
    throw UnsupportedOperationException("Didn't work!")
}
 
fun main() {
    runBlocking {
        withTimeoutOrNull(2.seconds) {
            while (true) {
                try {
                    doWork()
                } catch (e: Exception) {                // 여기서 취소를 막아버림
                    println("Oops: ${e.message}")
                }
            }
        }
    }
}
```

`delay` 호출이 `CancellationException`을 던지는데, `catch` 구문에서 모든 종류의 예외를 잡기 때문에 코드 무한 반복

**수정**

- **방법1**: 예외를 다시 던짐: `if (e is CancellationException) throw e`
- **방법2**: `UnsupportedOperationException`만 잡아야 함

<br><br>

### 15.2.5 Cancellation is cooperative

<small><i>취소는 협력적이다</i></small>

일시 중단 함수는 스스로 취소 가능하게 로직을 제공해야 함

- 취소는 함수 안의 일시 중단 지점에서 `CancellationException`을 던짐
- `doCpuHeavyWork` 함수는 `suspend` 함수지만, 실제로는 일시 중단 지점을 포함하지 않음
- **일시 중단 지점을 추가해야 예상한 대로 Cancel 됨**
- 코드가 **취소 가능한 다른 함수를 호출**하면, 자동으로 취소 가능 지점이 도입
  - 예를 들어 `doCpuHeavyWork` 함수 본문에 `delay` 호출을 추가하면 해당 함수는 취소 가능한 지점을 갖게 됨
- 대신 코틀린 코루틴에는 코드를 취소 가능하게 만드는 유틸리티 함수들이 있음: `ensureActive` 와 `yield` 함수, `isActive` 속성 등

<br>

### 15.2.6 Checking whether a coroutine has been cancelled

<small><i>코루틴이 취소됐는지 확인</i></small>

코루틴이 취소됐는지 확인할 때는 `CoroutineScope` 의 `isActive` 속성 혹은 `ensureActive` 확인

**`isActive`**

```kotlin
import kotlinx.coroutines.isActive
...

val myJob = launch {
    repeat(5) {
        doCpuHeavyWork()
        if(!isActive) return@launch
    }
}
```

**`ensureActive`**

```kotlin
import kotlinx.coroutines.ensureActive
...

val myJob = launch {
    repeat(5) {
        doCpuHeavyWork()
        ensureActive()
    }
}
```

<br>

### 15.2.7 Letting other coroutines play: The `yield` function

<small><i>다른 코루틴에게 기회를 주기: `yield` 함수</i></small>

코드 안에서 취소 가능한 지점을 제공할 뿐만 아니라, 현재 점유된 디스패처에서 다른 코루틴이 작업할 수 있도록 해줌

```kotlin
import kotlinx.coroutines.*
 
suspend fun doCpuHeavyWork(): Int {
    var counter = 0
    val startTime = System.currentTimeMillis()
    while (System.currentTimeMillis() < startTime + 500) {
        counter++
        yield()             // 여러 코루틴이 번갈아가며 실행될 수 있음
    }
    return counter
}
 
fun main() = runBlocking {
    launch {
        repeat(3) {
            doCpuHeavyWork()
        }
    }
    launch {
        repeat(3) {
            doCpuHeavyWork()
        }
    }
}
```

`yield` 가 없을 때:

- `doCpuHeavyWork` 함수에 일시 중단 지점이 없음
  - → 코루틴 본문에 일시 중단 지점이 없음
  - → 첫 번째 코루틴의 실행이 일시 중단될 기회가 없어 두 번째 코루틴이 실행되지 못함
- `isActive` 를 확인하거나 `ensureActive` 를 호출해도 동일
  - 취소 여부만 확인. 실제로 코루틴을 일시 중단시키지는 않음

`yield` 가 있을 때:

- `yield` 함수는 취소 예외를 던질 수 있는 지점 제공 + 대기 중인 다른 코루틴이 있으면 디스패처가 제어를 다른 코루틴에게 넘길 수 있게 해줌
- 여러 코루틴이 번갈아가며 실행될 수 있음

일시 중단 지점과 취소 지점이 없는 경우 `isActive`, `ensureActive` 를
확인하는 것과 `yield` 를 호출하는 것의 차이

<br><img src="./img/figure15-06.png" alt="시간 제한이 초과된 후 자동으로 취소 호출" width="70%"><br>

<br>

**협력적 취소를 가능하게 하는 메커니즘**

| 함수 / 프로퍼티      | 사용 사례                                                         |
| -------------- | ---------------------------------------------------------- |
| `isActive`     | 취소가 요청됐는지 확인 (작업을 중단하기 전에 정리 작업을 수행하기 위함)                  |
| `ensureActive` | '취소 지점' 을 도입. 취소 시 `CancellationException` 을 던져 즉시 작업을 중단    |
| `yield()`      | CPU 집약적인 작업이 기저 스레드 (또는 스레드 풀) 를 소모하는 것을 방지하기 위해 계산 자원을 양도 |

<br>

### 15.2.8 Keep cancellation in mind when acquiring resources

<small><i>리소스를 얻을 때 취소를 염두에 두기</i></small>

데이터베이스 연결, IO 등과 같은 리소스를 사용 → 사용 후 이를 명시적으로 닫아야 적절하게 해제됨

- 취소는 다른 예외와 마찬가지로 코드의 조기 반환을 유발 가능
- 코루틴 기반 코드는 항상 취소 시에도 견고하게 작동하도록 설계돼야 함
- 취소가 발생하면 `CancellationException` 이 발생하기 때문에, `finally` 블록을 활용해서 해제

```kotlin 
val dbTask = launch {
    val db = DatabaseConnection()
    try {
        delay(500.milliseconds)
        db.write("I love coroutines!")
    } finally {
        db.close()
    }
}
```

- `AutoClosable` 인터페이스를 구현하는 경우, `.use` 함수를 사용해 간결하게 처리 가능

```kotlin
val dbTask = launch {
    DatabaseConnection().use {
        delay(500.milliseconds)
        it.write("I love coroutines!")
    }
}
```

<br>

### 15.2.8 Keep cancellation in mind when acquiring resources

<small><i>프레임워크가 여러 분 대신 취소를 할 수 있다</i></small>

- 실제 애플리케이션에서는 프레임워크 가 코루틴 스코프를 제공하고, 취소를 자동으로 처리
  - e.g. 안드로이드 플랫폼, 케이토 네트워크 프레임워크
- 개발자는 적절한 코루틴 스코프를 선택하고 코드가 실제로 취소될 수 있도록 설계해야 함

<br>

## Summary

- **구조화된 동시성**: 코루틴의 작업을 제어할 수 있게 해주며, 코루틴이 취소되지 않고 계속 실행되는 것을 방지
- **일시 중단 함수 `coroutineScope` vs. `CoroutineScope` 생성자 함수**
  - 둘 다 새로운 코루틴 스코프를 생성할 수 있음
  - `coroutineScope`: 작업을 병렬로 분해하기 위한 함수
    - 여러 코루틴을 시작하고 결과를 계산한 후 그 결과를 반환
  - `CoroutineScope`: 클래스의 생명 주기와 코루틴을 연관시키는 스코프를 생성
    - 일반적으로 `SupervisorJob` 과 함께 사용됨
- `GlobalScope`: 특별한 코루틴 스코프
  - 구조화된 동시성을 깨뜨리기 때문에 애플리케이션 코드에서는 사용하지 말아야 함
- **코루틴 컨텍스트**
  - 개별 코루틴이 어떻게 실행되는지 관리
  - 코루틴 계층을 따라 상속됨
- 코루틴과 코루틴 스코프 간의 부모-자식 계층 구조는 코루틴 컨텍스트에 있는 `Job` 객체를 통해 설정됨
- **일시 중단 지점**: 코루틴이 일시 중단될 수 있고, 다른 코루틴이 작업을 시작할 수 있는 지점
- **취소**
  - 일시 중단 지점에서 `CancellationException` 을 던지는 방식으로 구현됨
  - 취소는 정상적인 상황이므로 코드는 이를 처리할 수 있게 설계해야 함
  - **취소 예외**처리: 절대 무시(잡아내고 처리하지 않음)되면 안됨
    - 예외를 다시 던지든지 아니면 아예 잡아내지 않는 것이 좋음
- `cancel` 이나 `withTimeoutOrNull` 같은 함수를 사용해 직접 취소를 호출할 수 있음
  - 기존의 여러 프레임워크도 코루틴을 자동으로 취소할 수 있음
- 함수에 `suspend` 변경자를 추가하는 것만으로는 취소를 지원할 수 없음
- `ensureActive`, `yield` 함수, `isActive` 속성: 코틀린 코루틴은 취소 가능한 일시 중단 함수를 작성하는 데 필요한 유틸리티 제공
- 프레임워크는 `CoroutineScope` 를 사용해 코루틴을 애플리케이션의 생명 주기와 연결하는 데 도움을 줌
  - e.g. 화면에 `ViewModel` 이 표시되는 동안이나 요청 핸들러가 실행되는 동안
