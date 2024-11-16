# Persistent Volumes

Kubernetes 의 PV 설정 방법

Pod 정의 파일에 Volume을 지정하면 넓은 환경에서 많은 포드를 배포할 경우, 
각각 Pod에 매번 저장소를 구성해야 함 

변경 사항이 있을 때마다 사용자는 클러스터 내 모든 Pod에 수정 사항을 작성함

이렇게 매번 작성하고 수정해야하기 보단, 중앙에서 관리하는 걸 선호

관리자가 거대한 저장소 풀을 생성할 수 있고, 요구에 따라 일부를 나눌 수 있도록 구성되어야 함

<br>

### Persistence Volume

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv-vol1
spec:
  accessModes:
    - ReadWriteOnce
  capacity:
    storage: 1Gi
  hostPath:
    path: /tmp/data
```

<br>

**`accessModes`**

- ReadWriteOnce (RWO) : 볼륨은 읽기/쓰기 모드에서 단일 작업자 노드로 탑재 가능, 하나의 워커 노드에 마운트되면, 다른 노드는 마운트할 수 없음
- ReadOnlyMany (ROX) : 볼륨은 읽기 전용 모드로 여러 작업자 노드에 동시에 탑재 가능
- ReadWriteMany (RWX) : 볼륨은 동시에 여러 작업자 노드에 읽기/쓰기 모드로 탑재 가능
- ReadWriteOncePod (RWOP): 하나의 pod만 읽기/쓰기 모드로 탑재 가능, 1.22+ 쿠버네티스 버전부터 지원


Volume을 생성하기 위해서, `kubectl create` 명령어를 사용할 수 있음

```Bash
❯ kubectl create -f pv-definition.yaml
persistentvolume/pv-vol1 created
❯ kubectl get persistentvolume
NAME      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM   STORAGECLASS   VOLUMEATTRIBUTESCLASS   REASON   AGE
pv-vol1   1Gi        RWO            Retain           Available                          <unset>
```

클라우드 같은 외부 저장소를 사용하고 싶다면, 아래와 같이 지정할 수 있음

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv-vol1
spec:
  accessModes:
    - ReadWriteOnce
  capacity:
    storage: 1Gi
  awsElasticBlockStore:
    volumeID: <volume-id>
    fsType: ext4
```

