# Basics of Network performance - Bandwidth, Latency, Jitter, Throughput, PPS, MTU

## MTU - Maximum Transmission Unit

네트워크를 통해 전송할 수 있는 가장 큰 단위의 패킷

```
Host A — [ 1 Packet: 1,500bytes ] —> Host B
```

HostA가 HostB 한테 패킷을 전달하고 싶고 패킷 하나를 전달하고자 함
패킷은 인터넷 표준인 1500 bytes.

```
Host A — [ 1 Packet: Jumbo Frame > 1,500bytes] —> Host B
```

특정 경우에는 Jumbo Frame 형식의 패킷을 전송할 수 있음
Jumbo Frame은 1500 바이트 이상을 의미
이후, 네트워크 퍼포먼스 향상을 위해 AWS에서 Jumbo Frame을 사용하는 방법을 알아볼것임

```
Host A — [ 1 Packet: 9,000 bytes ] —> Host B
```

가령, Jumbo Frame의 사이즈가 9,000 bytes 이라고 가정하면,
해당 패킷이 전부 전송되기 위해서는 6차례 (1,500 bytes * 6)에 걸쳐 진행되어야 함

- 대부분의 네트워크는 1,500bytes MTU 지원
- Jumbo Frame 패킷: 1,500bytes < MTU < 9,000bytes (9,001bytes in AWS)
- Jumbo Frame 장점
    - Less Packets
    - More throughput
    - PPS 를 증가하지 못하는 경우 MTU를 증가시켜 throughput 을 증가시킬 수 있음

—

_How the Jumbo Frame works?_
## Path MTU Discovery example

HostA에서 HostB 까지 Packet이 전달될 때 2개의 홉이 있다고 해보자.

```
🔀: Router

Host A —— 🔀 —— 🔀 ——> Host B
```

이 때, HostA에서 첫 번째 홉으로 전달될 때 1,500MTU가 소모됨


```
                       ✅
Host A ——— 🔀 —— 🔀 ——> Host. B
               ➡️ 
       MTU 1500 (DF=1)
```

`DF=1`:  Don’t Fragment. 해당 패킷을 작은 패킷으로 분리하지 말라는 의미

Router 1이 1,500MTU를 지원하면 다음 과정을 처리할 수 있음
(Can I process the MTU of 1,500?)

만약 Router 2 가 1,000 MTU 까지만 지원한다면?

```
                       ✅           ❌
Host A ——— 🔀 ——— 🔀 ——> Host B
               ➡️           ➡️ 
       MTU 1500 (DF=1)
```

이 때, DF=1 을 설정되어 있기 때문에 Router 2는 처리하지 못한다는 응답을 전달

```
                       ✅           ❌
Host A ——— 🔀 ——— 🔀 ——> Host B
                               ⬅️ 
      ❌ “ICMP - Change MTU 1000”
```

호스트는 네트워크에서 오직 1000까지만 지원한다는 것을 알기 때문에 1000 단위로 전송함


```
                       ✅           ✅
Host A ——— 🔀 ——— 🔀 ———> Host B
               ➡️           ➡️            ➡️
                   MTU 1000 (DF=1)
```

\* FYI. MTU Path Discovery 를 위해 ICMP 허용 정책 필요

—

## Jumbo Frames in AWS

- 9001 MTU
    - AWS는 9001 MTU를 지원하는데, 상당히 큰 수치
- VPC 내에서 기본값으로 enabled 설정됨
- “VPC 내에서” 허용
    - ⭐️ IGW 로 인터넷이 연결되어 있거나 혹은 Peering VPC으로 트래픽이 나가는 경우엔 지원되지 않음 (1,500 bytes 제한)
- AWS Direct Connect 를 사용한 VPC와 on-premises 네트워크 사이에서 지원됨
- EC2 Cluster placement groups 내의 Jumbo Frame을 사용하는 것은 최대 네트워크 throughput 을 제공
    - EC2 Cluster placement groups: 특정 EC2들을 최대한 가까이 배치하겠다. 사실상 하나의 AZ에 물리적으로 하나의 랙에 배치하겠다.why 네트워크 최적화와 EC2 HPC 워크로드 (가장 높은 bandwidth를 위해)
