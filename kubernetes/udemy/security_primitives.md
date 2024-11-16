# Security Primitives

쿠버네티스는 분산된 리소스와 애플리케이션을 결합을 지원하기 위한 플랫폼이 되어 감에 따라 보안 또한 굉장히 중요함

클러스터를 형성하는 호스트의 모든 액세스에 보안을 보장해야 하고, 
root 권한 접근은 막아야 하며,
암호 기반 인증을 비활성화 하고,
SSH 키 기반 인증만 사용 가능하게 해야함

물론 쿠버네티스를 호스팅하는 물리적 또는 가상 인프라를 보호하기 위해 다른 조치를 취할 수도 있고,
물론 그 수단이 손상되면 모든 것이 손상됨

그럼, 클러스터를 확보하기 위해 어떤 위험이 있으며 어떤 조치를 취해야 할지 알아볼 필요가 있음

<br/>

### Secure Kubernetes

`kube-apiserver` 는 쿠베네티스 내의 모든 작업의 중심에 있음

API 서버와 직접 통신하거나 Kube 제어 유틸리티를 사용해서 통신할 수 있는데,
거의 모든 클러스터 내의 작업을 처리할 수 있음

따라서, API 서버 접근을 컨트롤 하는 것이 방어의 최전선이라고 생각할 수 있음

<br/>

#### Authentication - 접근 가능 대상

다양한 방법의 Authentication 방법이 존재

- Files: Username / Password
- Files: Username / Tokens
- Certifications
- External Authentication Provider - LDAP
- Service Accounts (for machines)

<br/>

#### Authorization - 해당 유저의 접근 가능 액션

대상에 대한 인증을 마친 후, 접근 가능한 액션을 확인해야 함 

Role-based access control 은 특정 그룹이 가진 역할의 권한을 인증과 함께 구현하는 것  

- RBAC Authorization
- ABAC Authorization
- Node Authorization
- Webhook Mode



클러스터 내의 모든 컴포넌트 간의 통신은 TLS 암호화를 사용하여 보안 상태로 진행됨

클러스터에 포함된 모든 Pod는 기본적으로 상호 간 모두 통신 가능하지만, 특정 Network 정책을 사용해서 이를 제한할 수 있음

