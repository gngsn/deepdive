## Record Command

컨트롤 플레인 구성 요소의 상태 확인
API 서버는 각 컨트롤 플렝니 구성 요소의 상태를 표시하는 ComponentStatus라는 API 리소스를 제공.

```
❯ kubectl get componentstatuses                                                            
Warning: v1 ComponentStatus is deprecated in v1.19+
NAME                 STATUS    MESSAGE   ERROR
controller-manager   Healthy   ok
scheduler            Healthy   ok
etcd-0               Healthy   ok
```

경고에서 알 수 있다시피, ComponentStatus는 불안정한 이유로 Deprecated.

대신 아래와 같이 Component들의 상태를 확인할 수 있다.

[참고 링크](https://kubernetes.io/docs/reference/using-api/health-checks/)


1. Method 1
```
❯ curl -k https://localhost:6443/livez\?verbose
[+]ping ok
[+]log ok
[+]etcd ok
[+]poststarthook/start-kube-apiserver-admission-initializer ok
[+]poststarthook/generic-apiserver-start-informers ok
[+]poststarthook/priority-and-fairness-config-consumer ok
...
```

2. Method 2
```
❯ kubectl get --raw='/readyz?verbose'
[+]ping ok
[+]log ok
[+]etcd ok
[+]etcd-readiness ok
[+]informer-sync ok
[+]poststarthook/start-kube-apiserver-admission-initializer ok
[+]poststarthook/generic-apiserver-start-informers ok
```

### 구성 요소 실행 방법

kube-proxy와 같은 컨트롤 플레인 구성 요소는 시스템에 직접 배포하거나 파드로 실행할 수 있음

(단, kubelet 는 반드시 시스템 구성 요소(데몬)으로 실행되어야 함)

이때, 파드로 실행되는 구성 요소를 아래와 같이 조회할 수 있음

```
❯ kubectl get po -o custom-columns=POD:metadata.name,NODE:spec.nodeName --sort-by spec.nodeName -n kube-system
POD                                          NODE
coredns-76f75df574-nj9tm                     kind-control-plane
coredns-76f75df574-wdkq4                     kind-control-plane
etcd-kind-control-plane                      kind-control-plane
kindnet-jbqkp                                kind-control-plane
kube-apiserver-kind-control-plane            kind-control-plane
kube-controller-manager-kind-control-plane   kind-control-plane
kube-proxy-lzr78                             kind-control-plane
kube-scheduler-kind-control-plane            kind-control-plane
```

FYI. 위에 사용한 옵션 정리
- `-o custom-columns`: 사용자 지정한 컬럼 조회
- `--sort-by`: 정렬


---


## ETCD

Etcd 를 위한 CLI Client 가 있는데, 따로 설치 과정이 필요

### `etcdctl` 설치

[Github link](https://github.com/etcd-io/etcd/releases/) 를 참고해서 shell script 파일 생성

```
# install_etcdctl.sh
ETCD_VER=v3.5.12

# choose either URL
GOOGLE_URL=https://storage.googleapis.com/etcd
GITHUB_URL=https://github.com/etcd-io/etcd/releases/download
DOWNLOAD_URL=${GOOGLE_URL}

rm -f /tmp/etcd-${ETCD_VER}-darwin-amd64.zip
rm -rf /tmp/etcd-download-test && mkdir -p /tmp/etcd-download-test

curl -L ${DOWNLOAD_URL}/${ETCD_VER}/etcd-${ETCD_VER}-darwin-amd64.zip -o /tmp/etcd-${ETCD_VER}-darwin-amd64.zip
unzip /tmp/etcd-${ETCD_VER}-darwin-amd64.zip -d /tmp && rm -f /tmp/etcd-${ETCD_VER}-darwin-amd64.zip
mv /tmp/etcd-${ETCD_VER}-darwin-amd64/* /tmp/etcd-download-test && rm -rf mv /tmp/etcd-${ETCD_VER}-darwin-amd64

/tmp/etcd-download-test/etcd --version
/tmp/etcd-download-test/etcdctl version
/tmp/etcd-download-test/etcdutl version
```

위 파일 저장후, 쉘 파일 실행 (`sh install_etcdctl.sh`).

하지만, `/tmp/etcd-download-test/etcdutl` 으로 명령하기 보다는 `etcdctl` 로 명령을 하면 편할테니, bashrc 혹은 zshrc 파일 수정

```
❯ vi ~/.zshrc
# 가장 하단에 추가 
# alias etcdctl="/tmp/etcd-download-test/etcdctl"
❯ source ~/.zshrc
❯ etcdctl version
etcdctl version: 3.5.12
API version: 3.5
```

여기까지 `etcdctl` 설치 완료

---


