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



