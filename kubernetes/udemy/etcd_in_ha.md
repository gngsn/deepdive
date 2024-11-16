# ETCD in HA

## ETCD

> ETCD is a distributed reliable key-value store that is Simple, Secure & Fast

ETCD는 분산되고 신뢰할 수 있는 key-value 저장소어로 간단하고 안전하며 빠름

### Key-Value Store

Key-Value 저장소는 Document 나 Page 형태로 정보를 저장

하나의 Document에 관한 모든 정보가 해당 파일에 저장됨

이 파일들은 어떤 형식이나 구조로든 존재할 수 있으며, 한 파일을 수정해도 다른 파일에 영향을 주지 않음

데이터가 복잡해지면 일반적으로 JSON이나 YAML 같은 데이터 포맷을 사용하게 됨

### Distributed

Etcd 단일 서버를 갖을 때, 이는 데이터베이스이기 때문에 중요한 데이터를 저장할 수 있음

그래서 여러 분산 서버로 저장할 가능성이 있음

만약 3개의 서버를 실행하면, 데이터베이스의 동일한 복사본을 유지하고 있기 때문에 하나를 잃어도 데이터는 두 개가 남죠

### Consistent

Etcd 클러스터는 모든 인스턴스에서 동시에 접근할 때 동일한 데이터를 사용할 수 있도록 보장함

**Read**

: 모든 노드에서 동일한 데이터가 사용 가능하기 때문에 읽는 건 쉬움

**Write**

: 쓰기는 까다로움

동시에 데이터를 서로 다른 이름으로 수정하는 요청이 온다면?

Etcd는 노드마다 쓰기를 처리하지 않는데,
내부적으로 하나의 Leader 가 있고, 여러 개의 Follower 들이 있음

쓰기 요청이 리더 노드를 통해 들어오면 리더가 쓰기를 실행함 

이후 리더가 다른 노드들에게 데이터 복사본을 보냄 

팔로워 노드를 통해 쓰기 요청이 들어오면 팔로워 노드가 리더에게 해당 쓰기 요청을 전달하고 리더는 쓰기를 실행함

쓰기가 처리되면 리더는 해당 복사본이 다른 인스턴스로 분산되도록 보장함

즉, 클러스터의 다른 멤버들은 리더가 동의를 얻어야만 완성된 것으로 여겨짐


### Leader Election - RAFT

지도자 선출 방식?

RAFT Protocol 을 이용함 


가령, 클러스터에 세 개의 노드가 있다고 가정

클러스터가 설정될 때 3개의 노드는 지도자가 없음

RAFT 알고리즘은 무작위로 타이머를 맞춰 신호를 보내는데,
세 매니저에게 무작위 타이머가 작동하고 타이머가 먼저 끝나는 사람(B)이 다른 노드에 리더 권한을 요청함

<br/><img src="./img/etcd_in_ha_img1.png" width="60%" /><br/>
_위 A, 왼 B, 오 C_

다른 관리자들(A, C)이 투표로 요청에 대한 답변을 받으면 노드(B)가 리더 역할을 맡음

리더로 선출됐으니 다른 마스터들에게 주기적으로 정기적으로 알림를 보내 자신이 계속 리더 역할을 하고 있다고 알림

언젠가 리더가 다운되거나 네트워크 문제로 인해 연결이 끊기면 다른 노드들이 리더로부터 알림을 받지 못할 수 있음

그럼 노드들 사이에서 재선 프로세스를 시작함

<br/><img src="./img/etcd_in_ha_img2.png" width="60%" /><br/>

새 리더를 뽑음

### Write 

쓰기는 클러스터의 다른 인스턴스로 한 번 복제됐을 때만 완료된 것으로 간주됨

Etcd 클러스터는 고가용성이기 때문에 노드를 잃어도 기능을 함

가령, 새 쓰기 요청이 들어왔는데 노드 하나가 응답하지 않는다고 가정해보자

리더는 클러스터에서 두 노드에만 쓸 수 있음

클러스터 내 '대부분'의 노드에 작성될 수 있다면 쓰기도 완료로 간주됨

이 때 '대부분'이라는 기준은 Majority 혹은 더 정확히는 **Quorum** 라고 하는데, 
가령, 노드가 3개면 대다수는 2개이며 2개에 쓰기가 끝나면 완료된 것으로 간주

> **Quorum**: 노드의 최소 값
> = **N/2 + 1**

쿼럼은 최소의 노드로 클러스터가 제대로 기능하고 성공적으로 쓰려면 반드시 필요함

가령 노드가 3개일 때, `3/2 + 1 = 2.5 ~= 2` 임

| Instances  | Quorum |
|------------|--------|
| 1          | 1      |
| 2          | 2      |
| 3          | 2      |
| 4          | 3      |
| 5          | 3      |
| 6          | 4      |
| 7          | 4      |

쿼럼이 없으면 쓰기도 할 수 없음

즉, 단일 노드 클러스터라면 이런 건 적용 안됨

인스턴스가 2개인 건 1개인 것과 같음

쿼럼이 충족될 수 없으면 실질적 가치가 없음

그래서 추가적인 클러스터에 최소 3개의 인스턴스를 갖도록 권장함

위 표에서 첫 번째 Colume에 두 번째 Colume을 빼면 Fault Tolerance가 됨

| Instances | Quorum | Fault Tolerance |
|-----------|--------|-----------------|
| 1         | 1      | 0               |
| 2         | 2      | 0               |
| **3**     | **2**  | **1**           |
| 4         | 3      | 1               |
| **5**     | **3**  | **2**           |
| 6         | 4      | 2               |
| **7**     | **4**  | **3**           |

Fault Tolerance 를 보면 연속된 홀수와 짝수의 개수가 동일하기 때문에,
마스터 노드의 수를 정할 때는 홀수를 선택해야 하는 걸 추천함

예시를 들어 보면, 6개의 노드가 있다고 가정해보자

```
+--------- Cluster ----------+
|                            |    
|   +------ Network ------+  |    
|   |   ⬛️ ⬜️ ⬜️ ⬜️ ⬜️ ⬜️   |  |    
|   +---------------------+  |    
|                            |    
+----------------------------+
Nodes: 6
Quorum: 6/2 + 1 = 4
```

이 때, Quorum 은 `4` 임

네트워크의 교란으로 인해 망이 실패해 네트워크가 분할되었다고 가정해보자 

만약 4/2 로 분리되었다고 하면, 

```
+------------------ Cluster ------------------+
|  +---- Network ----+  +----- Network ----+  | 
|  |                 |  |                  |  |    
|  |   ⬛️ ⬜️ ⬜️ ⬜️    |  |      ⬜️ ⬜️        |  |    
|  |                 |  |                  |  |    
|  |  ✅ Nodes: 4    |  | ❌ Nodes: 2       |  |    
|  +-----------------+  +------------------+  |
+---------------------------------------------+
Quorum: 6/2 + 1 = 4
```

하지만, 3/3으로 분리되었다고 하면,

```
+------------------ Cluster ------------------+
|  +---- Network ----+  +----- Network ----+  | 
|  |                 |  |                  |  |    
|  |    ⬛️ ⬜️ ⬜️      |  |    ⬜️ ⬜️ ⬜️       |  |    
|  |                 |  |                  |  |    
|  |  ❌ Nodes: 3    |  | ❌ Nodes: 3       |  |    
|  +-----------------+  +------------------+  |
+---------------------------------------------+
Quorum: 6/2 + 1 = 4
```

4인 Quorum 보다 작게 되어 쓰기를 수행하지 못함

하지만 7개인 경우는 항상 동작할 수 있게 됨

```
+--------- Cluster ---------+
|                           |    
|   ⬛️ ⬜️ ⬜️ ⬜️ ⬜️ ⬜️ ⬜️      |    
|                           |    
+---------------------------+
Nodes: 7
Quorum: 7/2 + 1 = 4
```

이번에도 분리가 된다고 하면,

```
+------------------ Cluster ------------------+
|  +---- Network ----+  +----- Network ----+  | 
|  |                 |  |                  |  |    
|  |   ⬛️ ⬜️ ⬜️ ⬜️    |  |     ⬜️ ⬜️ ⬜️      |  |    
|  |                 |  |                  |  |    
|  |  ✅ Nodes: 4    |  | ❌ Nodes: 3       |  |    
|  +-----------------+  +------------------+  |
+---------------------------------------------+
Quorum: 7/2 + 1 = 4
```

홀수 노드의 네트워크 세그먼트화 경우 클러스터가 생존할 확률이 더 높음

따라서 짝수보다 홀수 노드를 더 추천함

5개가 6개보다 낫고 노드가 5개 이상이면 불필요함

---

### Installation

ETCD를 서버에 설치하려면, 최신 지원되는 바이너리를 다운로드하고 
압축해 필수 디렉터리 구조를 생성하고 추가로 생성된 인증서 파일 위로 복사해야함

```Bash
$ wget -q --https-only \
       "https://github.com/coreos/etcd/releases/download/v3.3.9/etcd-v3.3.9-linux-amd64.tar.gz"
$ tar -xvf etcd-v3.3.9-linux-amd64.tar.gz
$ mv etcd-v3.3.9-linux-amd64/etcd* /usr/local/bin/
$ mkdir -p /etc/etcd /var/lib/etcd
$ cp ca.pem kubernetes-key.pem kubernetes.pem /etc/etcd/
```

<pre><code lang="bash"># etcd.service
ExecStart=/usr/local/bin/etcd \\
    --name ${ETCD_NAME} \\
    --cert-file=/etc/etcd/kubernetes.pem \\
    --key-file=/etc/etcd/kubernetes-key.pem \\
    --peer-cert-file=/etc/etcd/kubernetes.pem \\
    --peer-key-file=/etc/etcd/kubernetes-key.pem \\
    --trusted-ca-file=/etc/etcd/ca.pem \\
    --peer-trusted-ca-file=/etc/etcd/ca.pem \\
    --peer-client-cert-auth \\
    --client-cert-auth \\
    --initial-advertise-peer-urls https://${INTERNAL_IP}:2380 \\
    --listen-peer-urls https://${INTERNAL_IP}:2380 \\
    --listen-client-urls https://${INTERNAL_IP}:2379,https://127.0.0.1:2379 \\
    --advertise-client-urls https://${INTERNAL_IP}:2379 \\
    --initial-cluster-token etcd-cluster-0 \\
    <b>--initial-cluster peer-1=https://${PEER1_IP}:2380,peer-2=https://${PEER2_IP}:2380 \\</b>
    --initial-cluster-state new \\
    --data-dir=/var/lib/etcd
</code></pre>

이 때, 피어 정보 옵션을 통해, 각 별도의 서비스가 클러스터의 피어가 어디 있는지 알게 함

<br>

### ETCDCTL

Etcd 유틸리티는 V2와 V3 API 버전이 있으며, 버전마다 명령이 다름

Version 3으로 알아볼 예정 (Version 2가 기본값)

1. 환경 변수 ETCDCTL_API 버전 설정

```Bash
export ETCDCTL_API=3 
```

2. 데이터 추가

```Bash
etcdctl put name john 
```

3. 데이터 검색

```Bash
etcdctl get name
name
john 
```

4. 모든 Key를 출력

```Bash
etcdctl get / --prefix --keys-only
name
```

### Number of Nodes

HA에서 필요한 최소 노드는 3개이며,
HA을 위한 클러스터에 노드는 홀수로 구성해야 함

| Instances | Quorum | Fault Tolerance  |
|-----------|--------|------------------|
| **3**     | **2**  | **1**            |
| **5**     | **3**  | **2**            |
| **7**     | **4**  | **3**            |

마스터 노드는 3개 혹은 5개면 충분 (그 이상은 과함)

<br/><img src="./img/etcd_in_ha_img3.png" width="60%" /><br/>

---

### Kubernetes the Hard Way

Kubernetes 를 직접 설치해보면 각각의 서로 다른 컴포넌트들이 어떻게 구성되어 있는지 이해하기 좋음 

**| 참고 |**

- Youtube: https://www.youtube.com/watch?v=uUupRagM7m0&list=PL2We04F3Y_41jYdadX55fdJplDvgNGENo
- Git 튜토리얼: https://github.com/mmumshad/kubernetes-the-hard-way