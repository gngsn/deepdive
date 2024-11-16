# Kubernetes Software Version

[🔗 Kubernetes Releases](https://kubernetes.io/releases/)

<br>

`kubectl get nodes` 명령을 실행하면 설치한 쿠버네티스 클러스터를 특정 버전을 확인할 수 있음

```Shell
❯ kubectl get nodes
NAME                 STATUS   ROLES           AGE   VERSION
kind-control-plane   Ready    control-plane   55d   v1.29.2
```

<br>

위 경우엔 버전 `v1.11.3`

> FYI. Semantic Versioning: `MAJOR.MINOR.PATCH`
> - MAJOR version 
>   - when you make incompatible API changes
> - MINOR version: features / functionalities
>   - when you add functionality in a backward compatible manner (이후 버전에 호환)
> - PATCH version 
>   - when you make backward compatible bug fixes

`MINOR` 버전은 몇 달마다 새로운 feature 와 functionality 출시, `PATCH` 버전은 치명적인 버그를 수정하며 더 자주 업데이트

쿠버네티스는 표준 소프트웨어 릴리스 버전 관리 절차를 따름

몇 달에 한 번씩 소규모 릴리스를 통해 새로운 피처와 기능을 발표

첫 번째 Major 버전인 `1.0` 은 2015년에 발표되었고, 최근 버전은 `1.30` (2024-04-17)

---

<br>

위 버전 말고도, Alpha와 Beta가 릴리즈 되는데,

```Shell
|
* v1.10.0 (March 2018)
|\
| * v1.10.0-beta
| |
| |
| * v1.10.0-alpha
|/
|
```

모든 버그를 고치고 개선하면 먼저 `alpha` 태그를 부착한 Alpha를 릴리즈

기본적으로 Alpha는 기능이 비활성화되어 있으며 버그가 존재할 수 있음

그 다음 코드를 잘 테스트하면 Beta 릴리스로 넘어가고, 새 기능이 디폴트로 활성화됨 

그리고 마지막으로, 안정적인 상태로 릴리즈함

---

이는 모두 쿠버네티스 [GitHub 레포지토리의 Release 페이지](https://github.com/kubernetes/kubernetes/tree/master/CHANGELOG)에서 모든 릴리스를 찾을 수 있음

쿠버네츠를 tar 파일로 다운로드할 수도 있으며, 이를 추출하면 해당 쿠버네티스 버전에 해당하는 컴포넌트가 존재

다운로드된 패키지는 추출되면 컨트롤 플레인의 모든 컴포넌트가 모두 같은 버전으로 포함되어 있음

<br/><img src="./img/kubernetes_software_version_img1.png" width="60%" /><br/>

**Components**: `kube-apiserver`, `controller-manager`, `kube-scheduler`, `kubelet`, `kube-proxy`, `kubectl`, 

하지만, Etcd cluster 과 CoreDNS 는 다른 프로젝트로, 즉 고유 버전이 있다는 것을 알 필요가 있음

---

### Reference

- https://kubernetes.io/docs/concepts/overview/kubernetes-api/

Here is a link to kubernetes documentation if you want to learn more about this topic (You don't need it for the exam though):

- https://github.com/kubernetes/community/blob/master/contributors/devel/sig-architecture/api-conventions.md

- https://github.com/kubernetes/community/blob/master/contributors/devel/sig-architecture/api_changes.md