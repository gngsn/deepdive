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

