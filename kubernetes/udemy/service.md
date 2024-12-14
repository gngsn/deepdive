## Kubernetes Service

서비스는 앱 안팎의 구성 요소 간의 통신을 가능하게 함

애플리케이션을 다른 애플리케이션 또는 사용자와 연결하는 걸 도와줌

<br/><img width="60%" src="./img/service_img1.png" /><br/> 

가령, 위와 같이 설정되어 있다고 가정.

외부 사용자(`192.168.1.10`)는 Pod(`10.244.0.2`)에 직접 접근할 수 없음

접근하는 방법 1. 컨테이너 명령을 붙어서 접근하는 방법

해당 노드에 ssh로 접근한 후, 노드에서 curl을 통해 해당 pod에 접근할 수 있음

접근하는 방법 2. 노드가 GUI를 가지고 있다면 노드 브라우저로 `http://10.244.0.2` 입력 후 접근

하지만, 둘 다 노드 내부에서 접근한 것이기 때문에 목적에 맞지 않음

그래서 외부 사용자와 노드 내부의 POD에 접근할 수 있는 네트워크 매핑이 필요

 => Service 


## Service Types

<br/><img width="60%" src="./img/service_img2.png" /><br/> 


- **NodePort**: 노드에서 포트를 listen 하고 있다가, 요청이 들어오면 해당 포트를 웹 어플리케이션이 실행하고 있는 POD의 포트로 forwarding
- **CluserIP**: 서비스는 클러스터 내부에 가상 IP를 생성하여 서로 다른 Service 간에 통신을 가능하게 합니다. (가령, 프론트엔드 셋과 백엔드 셋)
- **LoadBalancer**: Cloud Provider 존재 하에, 앱을 위한 프로비저닝된 로드 밸런서

<br/>

### NodePort
노드에서 포트를 listen 하고 있다가, 요청이 들어오면 해당 포트를 웹 어플리케이션이 실행하고 있는 POD의 포트로 forwarding

<br/><img width="60%" src="./img/service_img3.png" /><br/> 

모든 용어는 Service의 관점
<br/>

**① TargetPort**

실제 웹 서버가 실행되는 Pod 포트 (`80`).
서비스가 인입되는 요청을 포워딩 시켜줘야 하는 Target이기 때문에 TargetPort 이라고 함

<br/>

**② Port**

서비스의 Port (`80`). 사실 서비스는 노드 위에서 실행되는 Virtual Server 같은 존재.
클러스터 내부에는 그 만의 IP 주소를 가지며 (위 그림의 `10.106.1.12`), 해당 IP 주소는 서비스의 ClusterIP 라고 불려짐.

<br/>

**③ NodePort**

노드 자체 Port (`30008`).  
노드 포트는 `30000 ~ 32767` 가용 범위를 가짐

---

**STEP1. Service Spec 정의** 

```yaml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
  labels:
    app: myapp
    #
spec:
  type: NodePort
  ports:
    targetPort: 80
    port: 80
    nodePort: 30008
    #
```


**STEP2. Pods Selector**

STEP1 까지만 확인하면, Service와 Pod와의 연결점이 없는데, 수많은 Pod 중에서 Service와 연결점을 갖게 하는 건 쿠버네티스에서 자주 보이는 패턴인 `label - selector` 방식

```yaml
# pod-definition
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app: myapp
    type: front-end
spec:
  containers:
    - name: nginx-container
      image: nginx
```

pod 정의 중, `.metadata.labels` 하위를 참고해서 selector 입력

**STEP1. Service Spec 정의**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
  labels:
    app: myapp
    #
spec:
  type: NodePort
  ports:
    targetPort: 80
    port: 80
    nodePort: 30008
  selector:
    app: myapp
    type: front-end
```

<br/><br/>

#### ✔️ NodePort - 한 노드에 여러 Pod가 있을 때

<br/><img width="60%" src="./img/service_img4.png" /><br/>


서비스는 자동으로 세 개의 Endpoint pod를 선택해, 사용자가 보내는 요청을 전달

추가적인 구성 필요 없음

세 포드의 LoadBalancing 전략으로는 무작위 알고리즘 사용

서비스는 내장된 LoadBalancer 처럼 작용해 다양한 포드로 부하를 분산함


####  ✔️ NodePort - 여러 노드에 Pod가 분산되어 있을 때

<br/><img width="60%" src="./img/service_img5.png" /><br/>

자동으로 클러스터 내 모든 노드에 걸친 서비스를 생성하고, 클러스터 내 모든 노드 포트를 동일하게 매핑 (모든 Node Port `30080` 사용)

추가적인 구성 필요 없음


<br/><br/>

### ClusterIP

풀 스택 웹 응용 프로그램은 응용 프로그램의 여러 부분을 호스팅하는 다른 종류의 포드가 존재

<br/><img width="60%" src="./img/service_img6.png" /><br/>

- 프론트엔드 웹 서버를 실행하는 Port
- 백엔드 서버를 실행하는 Port
- Redis 같은 키 값 저장소를 실행하는 Port
- MySQL 같은 Persistent 데이터베이스 Port

서비스나 계층 간의 연결을 확립하는 방법이 필요

- 조건1. 각 앱마다 원하는 통신 대상이 있음
  - 가령, 프론트엔드는 백엔드 서버와, 백엔드 서버는 데이터베이스 혹은 Redis 서비스 등과 통신해야함
- 조건2. 고정 IP
  - 하지만, Pod는 언제든 제거되고 새로 계속 만들어지니 Port는 정적이지 않음

**서비스의 해결책** 

<br/><img width="60%" src="./img/service_img7.png" /><br/>

각 그룹 별 Pod를 하나로 묶고 하나의 인터페이스를 통해 단일 Port 접근

- 그 하위에서 부터 요청은 무작위로 Port로 전달
- 각 그룹 내에서 확장이 용이해짐

각각의 서비스는 클러스터 내부에서 IP와 그에 할당된 이름을 갖으며, 다른 Pod가 접근할 때 해당 이름을 사용

```yaml
apiVersion: v1
kind: Service
metadata:
  name: back-end
spec:
  type: ClusterIP
  ports:
    targetPort: 80
    port: 80
  selector:
    app: myapp
    type: front-end
```

<br/><br/>

### LoadBalancer

네 개의 노드 클러스터가 있을 때, 외부 사용자가 응용 프로그램에 액세스할 수 있도록 하는 방식을 생각해보자.

<br/><img width="60%" src="./img/service_img8.png" /><br/>

NodePort 타입에서는 Node의 Port에서 트래픽을 수신해 트래픽을 각각의 포드로 라우팅하는 걸 도왔음.

그럼, 여러 노드일 때는 어떤 URL로 전송해야 할까

<br/><img width="60%" src="./img/service_img9.png" /><br/>

그림과 같이 IP:Port로 접속할 순 있지만, 사용자는 단일 도메인을 원함

#### **Service의 해결법**

<br/><img width="60%" src="./img/service_img10.png" /><br/>

VM과 Cloud 서비스와 같이 Cluster를 모두 포괄하는 공간에 LoadBalancer를 구비

구글 클라우드, AWS, Azure 등 클라우드 플랫폼과의 통합을 지원하기 때문에, 쉽게 연결해서 Natvie Load Balancer를 활용 가능

단, 지원되는 클라우드 플랫폼에 한해서만 작동


