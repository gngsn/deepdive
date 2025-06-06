# LLM에 실행 엔진을 탑재하다! Tool use와 Agents로 구현하는 엔터프라이즈 AI 솔루션


> LLM 100 + 100 과 같은 문제를 출 수 있나요?

2자리 수 계산 = 100%
3자리 수 계산 = 80%
4자리 수 계산 = 25%
5자리 수 계산 = 9%

수학 문제의 데이터의 대부분이 2자리 수 계산 식.
=> 자주 접하지 못한 데이터는 정확도가 낮아진다.


### Tool Use

- **Modular**: 서로 다른 기능을 가진 모듈을 조합
- **Reasoning**: 논리적 사고/추론 과정 포함
- **Knowledge**: 외부 지식 소스 활요
- **Language**: 자연어 입력을 이해하고 자연어로 응답

=> Tool Use의 시작

> 지금 서울의 날씨?

실시간 답변 불가


날씨 데이터가 필요한 건 알지만, 실시간 데이터에 액세스 할 수 없음
-> 질문에 정확하게 답할 수 없음
오랜 기간 LLM의 제한을 가져옴

**모델이 외부 세계와 상호작용하는 방법이 필요함**  
= **Tool Use ≈ Function Calling**


Tool Use ≈ Function Calli

모델이 호출 가능한 함수 목록에서 적절한 함수를 선택해 호출 외부 Tool을 활용해 답변을 생성하거나 작업을 수행

### 동작과정

1. 미리 Tool 정의
2. LLM은 Tool을 선택
3. LLM에게 결과를 다시 전달
4. LLM은 결과를 사용해 답을 생성


HallucinationL 모델이 존재하지 않는 Tool을 생성해서 호출
보안 취약점: 프롬프트 Injection 공격으로 내부 툴 정보 노출
응답 속ㅈ도 저하
불분명한 응답 형식


Tool Use에 최적화된 구조

- 툴 전용 특수 구문: 툴 정의, 사용자 질문, 시스템 프롬프트를 명확히 구분
- 툴 사용이 가능한 모델
- 일관된 중단 규칙 (stop_sequence): 예측 가능한 토큰 생성 중지 시점


### 툴 유즈가 어떻게 동작하나요?

##### 1. 시스템 메시지를 넣음
```kotlin
당신은 <function> 태그에 정의된 함수 집합에 접근    <-- 정보를 미리 줌

<function_calls>
    <function>
        {
            "name": "get_weather",
            ...
        }
    </function>
</function_calls>
```

##### 2. 사용자 메시지 입력 받음

> 저는 오스틴에 있는데, 오늘 코트가 필요할까요?


##### 3. LLM에게 전달. 어시스텐트 메시지 (모델의 응답 메시지)

```
<function_calls>
    <function>
        get_weather(city="Austin), state_code="TX)
    </function>
</function_calls>
```
중간 규칙(stop_sequence)으로 `</function_calls>` 추가
부정확한 응답값 생성 방지


##### 4. 함수 호출 구문 포함 확인

if (코드 include `stop_sequence` and `</function_calls>`)

##### 5-1. No, 최종 결과


##### 5-2. Yes, 구문 분석 및 함수 실행


##### 6. 함수 실행 결과

이후, 프롬프트 결과를 가지고 대화를 이어나감

**사용자 메세지**

```
<function_calls>
    <function>
        "현재 오스틴의 온도는 14도 이며, 맑은 날씨입니다."
    </function>
</function_calls>
```


==> AWS BedRock의 Converse API 는 **Tool Use** 를 표준화함

이 떄, 모델의 도메인 지식은 적절한 Tool을 활용하는데에 중요

#### 문제점
- 툴 선택 단계에서부터 문제가 발생할 것.
- 툴을 선택해도 파라미터 값을 제대로 입력하지 않을 가능성도 있음


**Example**. 시가/고가/저가/종가가 있는 가격 목록에서 일일 수익률을 어떻게 계산할까?


### Tool Use-LLM 의 도메인 지식

- **시스템 메시지**: "..."
- **Tools**: Calculator(operation, operand1, operand2) 라고 할 때,

지난 5일 동안 SPY의 일일 수익률을 계산하려면 종가를 사용해야 함

지난 5일 동안의 데이터를 입력하면,
모델이 제공된 툴을 사용해서 `operation`, `operand1`, `operand2` 에 맞는 적절한 파라미터를 넣어줌


---
---

### 에이전트란?

- 자율성 (Agency): 에이전트가 무엇을 할지, 언제 시작하고 멈출지, 어떤 결과를 보여줄지 스스로 판단하는 능력
- 외부 영향력 (Influence):


**발전**: 단일 응답 모델 > 업무 수행 챗봇 > 가상 직원

##### 단일 응답 모델

사용자 메시지 -> 어시스턴트 메시지.
어떤 일을 해야하는지 스스로 판단 불가. 답변하면 끝

##### 업무 수행 챗봇

별도의 분류기를 통해 의도에 맞는 프롬프트와 툴을 선택
그렇게 사용자 메시지 분류 후, 어시스턴트 메시지가 생성되면
관리자의 검토 끝에 규칙에 따라 응답


##### 가상 지원

- Tool Use 구조로 돌아감
- Tool 호출 포함 여부 => yes, Tool 실행 => Tool 실행 결과
    -                =>  No, 최종 결과


#### 툴 유즈의 범위
- 현재는 **많은 수의 툴 사용, 복잡한 작업, 여러 단계를 진행** 할 수 있음
- 다음 주? **제한 없는 툴 사용, 고도로 복잡한 작업, 연계된 단계**를 할 수 있게 될 것임


#### Bedrock 모델의 3가지 핵심 요소

- 에이전트: 오케스트레이션. 에이전트에게 함수 정보 제공해야함.
- Bedrock 모델: 다양한 파운데이션 모델
- Tools
    - 액션 그룹: 다양한 함수의 모음
    - 지식 기반: 자연어로 지식을 제공

**실행**

1. (Tools -> Agent) 지식 기반에 대한 정보 제공
2. (Agent -> Bedrock 모델) Bedrock 모델에 요청 메시지와 사용 가능한 툴 정보 (Converse API 사용)
3. (Bedrock 모델 -> Agent) **다음 계획**과 **툴 호출 지시**
    - (Bedrock 모델 -> Agent) 사용자에게 답변을 줄 수 있다고 생각하면 최종 답변 전달


### Summary

#### Tools Use
- 필요한 툴을 사용
- 작업을 정확하게 처리

#### 에이전트
- 적합한 계획 수립 앤 툴 유즈
- 복잡한 작업을 자동화

#### 베드락
- **Converse API**: 표준화된 구문을 통해 툴을 활용
- 아마존 베드락 에이전트:


---------------------------------------------------------------------------


AWS Summit 강연 노트를 다시 정리한 버전

---

# LLM에 실행 엔진을 탑재하다!

## Tool Use와 Agents로 구현하는 엔터프라이즈 AI 솔루션

---

## 📌 LLM의 한계와 필요성

* **LLM의 연산 능력 한계**

    * 2자리 수 계산 정확도: **100%**
    * 5자리 수 계산 정확도: **9%**
    * 📉 자주 접하지 않은 복잡한 연산일수록 성능이 급격히 저하됨

* **기본 한계**

    * 예: "지금 서울의 날씨?" → 실시간 정보에 접근 불가
    * LLM은 외부 세계와 **직접 상호작용하지 못함**
    * 해결책: **Tool Use (= Function Calling)** 필요

---

## 🧰 Tool Use란?

### 개념

* LLM이 외부의 기능(Function)을 **직접 호출하여 결과를 받아오는 구조**
* 목적: 모델이 **실시간 데이터**나 **외부 시스템**을 활용할 수 있게 함

### 구성 요소

* **Modular**: 다양한 기능을 가진 모듈을 조합
* **Reasoning**: 논리적 사고 기반 선택
* **Knowledge**: 외부 지식 활용
* **Language**: 자연어 이해 및 출력

---

## ⚙️ Tool Use의 동작 방식

1. **툴 정의**: 시스템 메시지로 사용 가능한 함수(툴)를 사전 정의
2. **질문 입력**: 사용자로부터 자연어 질의 수신
3. **툴 선택**: LLM이 적절한 툴을 선택하여 호출
4. **실행 결과 활용**: 응답 생성 시 툴 결과 반영

### 예시 구조

```kotlin
<function_calls>
  <function>
    get_weather(city="Austin", state_code="TX")
  </function>
</function_calls>
```

* `</function_calls>`로 명확한 **중단 시점 (stop\_sequence)** 설정
* 응답 정확성 및 보안성 향상

### 문제점 및 주의사항

* 존재하지 않는 툴 호출 (Hallucination)
* Prompt Injection 공격 가능성
* 파라미터 오입력
* 응답 지연 가능성

---

## 📊 예시: 주가 데이터 활용

> "지난 5일간 SPY의 일일 수익률을 계산해줘"

* Tool: `Calculator(operation, operand1, operand2)`
* 필요한 정보: 종가(close price)
* 모델은 툴을 사용해 파라미터를 채워 계산 수행

---

## 🤖 Agent란?

### 정의

> 에이전트는 LLM이 외부 세계와 **자율적으로 상호작용**하며 작업을 수행하는 실행 주체

### 특징

* **자율성(Agency)**: 작업 방식과 흐름을 스스로 결정
* **외부 영향력(Influence)**: 환경과 상호작용 가능

---

## 👥 Agent의 발전 단계

| 단계                 | 설명                                   |
| ------------------ | ------------------------------------ |
| **단일 응답 모델**       | 단순 Q\&A. 한 번 응답하면 끝                  |
| **업무 수행 챗봇**       | 질문 분류 후 프롬프트 + 툴 선택. 결과는 관리자 검토 후 응답 |
| **가상 지원자 (Agent)** | 툴 호출, 결과 처리, 다단계 실행까지 자율 수행          |

---

## 🏗️ Bedrock 기반 실행 구조

### 핵심 요소

1. **Tools**: 다양한 기능을 갖춘 함수 모음 (Action Group), 지식 기반 포함
2. **Agent**: 도메인에 맞는 계획 수립 + 툴 호출 조율
3. **Bedrock 모델**: 다양한 Foundation Model을 사용하여 응답 생성

### 실행 흐름

1. **Tools → Agent**: 툴 및 지식 정보 제공
2. **Agent → Bedrock 모델**: 사용자 요청 + 사용 가능한 툴 전달 (Converse API 활용)
3. **Bedrock → Agent**: 툴 호출 or 최종 응답 지시

---

## 🧩 Tool Use vs Agent

| 항목     | Tool Use            | Agent              |
| ------ | ------------------- | ------------------ |
| 핵심 기능  | 외부 기능 호출            | 계획 수립 및 다단계 실행     |
| 자동화 수준 | 낮음 (단일 작업)          | 높음 (복잡한 다단계 처리 가능) |
| 활용 모델  | Function Calling 중심 | Orchestration 포함   |

---

## 🧵 Summary

### ✅ Tool Use

* 외부 데이터를 사용해 정확한 작업 수행
* Function 호출 구조
* 예: 날씨 확인, 수식 계산 등

### ✅ Agent

* 복잡한 흐름과 의사결정을 자동으로 처리
* 툴 활용을 넘어서 ‘업무 수행’의 개념으로 확장

### ✅ AWS Bedrock

* **Converse API**로 툴 호출 표준화
* 다양한 Foundation Model과의 연결
* Tool + Agent 조합으로 **가상 직원 수준의 AI 구현 가능**

---

필요하시면 위 내용을 발표 자료, 블로그 포스트 또는 회의용 요약본으로도 변환해드릴 수 있습니다.
