# Chapter 20. Load Balancing in the Datacenter

<i><small>데이터센터의 로드밸런싱</small></i>

## The Ideal Case

**이상적인 경우?**

서비스의 부하가 백엔드에 완벽하게 퍼져서, 특정 한 시점에 '가장 적은 부하를 처리할 때'와 '가장 많은 부하를 처리할 때'의 백엔드 태스크의 CPU 사용률이 완전히 일치

<br><img src="./img/figure20-1.png" width="70%" /><br>

위 그래프는 어느 특정 시간 범위 내에서 실행 중이던 두 태스크의 상태 

이 시간 동안 데이터센터 로드밸런싱 알고리즘은 이 데이터센터에 추가 트래픽을 전달하면 안됨

어느 한 태스크의 부하가 한계를 넘어서기 때문

<br><img src="./img/figure20-2.png" width="70%" /><br>

위 그래프의 왼쪽 그래프를 보면 상당한 양의 수용량이 낭비되고 있음

가장 부하가 높은 태스크를 제외하면 나머지 태스크들에서는 수용량이 낭비되고 있음

#### Example.

`CPU[i]`는 어느 특정 시점에 태스크 `i` 의 CPU 사용률이며, 태스크 `0` 가 가장 많은 부하를 처리 중인 태스크라고 가정해보자.

그래프 전체를 보면 다른 태스크들이 사용 중인 CPU 와 CP 이와의 차이를 모두 합한 것만큼의 CPU 를 낭비하고 있는 셈

테스크 0 를 제외한 태스크 1 로부터 (CPU 이 - CPU) 를 계산한 값의 합이 낭비되고 있는 것

여기서 '낭비'란, CPU 사용이 예약되어 있지만 전혀 사용되고 있지 않음을 의미

<br>

## Identifying Bad Tasks: Flow Control and Lame Ducks

<br>

## Limiting the Connections Pool with Subsetting

<br>

## Load Balancing Policies

