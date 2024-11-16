# TLS in Kubernetes: Certificate Creation

Certificate (인증서)를 생성하기 위해서 다양한 툴들이 존재

- Easy RSA
- OpenSSL
- CF SSL


### Open SSL

<pre>
<b>FYI.</b>
<code lang="Bash">OPENSSL(1ssl)                       OpenSSL                      OPENSSL(1ssl)


NAME
       openssl - OpenSSL command line program

SYNOPSIS
       openssl command [ options ... ] [ parameters ... ]

       openssl no-XXX [ options ]

       openssl -help | -version

DESCRIPTION
       OpenSSL is a cryptography toolkit implementing the Secure Sockets Layer
       (SSL) and Transport Layer Security (TLS) network protocols and related
       cryptography standards required by them.

       The openssl program is a command line program for using the various
       cryptography functions of OpenSSL's crypto library from the shell.  It
       can be used for

        o  Creation and management of private keys, public keys and parameters
        o  Public key cryptographic operations
        o  Creation of X.509 certificates, CSRs and CRLs
        o  Calculation of Message Digests and Message Authentication Codes
        o  Encryption and Decryption with Ciphers
        o  SSL/TLS Client and Server Tests
        o  Handling of S/MIME signed or encrypted mail
        o  Timestamp requests, generation and verification
</code></pre>

<br>

**1. Generate Keys**


OpenSSL 을 통한 키 생성 요청

> **openssl genrsa**: Generation of RSA Private Key. Superseded by openssl-genpkey(1).


비밀키 이름을 지정해서 `ca.key` 생성 RSA 2048 bit 키 생성  


```Bash
❯ openssl genrsa -out ca.key 2048
```

<br>

발급된 `ca.key` 확인

```Bash
❯ openssl rsa -in ca.key -check
RSA key ok
writing RSA key
-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCyCww0wYOQpxgB
kSy0PNAfPKOPIPzBqjFGh4SyMqm31HtliWDKJAKj97Vr1NK9nSfd+DVS8O82TJOU
...
bDeK3YubrXKDviOnwEpB4C66Z4Vc+jyDy3okAC6Ym4+vJdxo8tmrfLFb8UadeuEc
DFv06/XMm9YRyettDkTDYdU=
-----END PRIVATE KEY-----
```

<br>

**2. Certificate Signing Request**

> **openssl req** PKCS#10 X.509 Certificate Signing Request (CSR) Management.

그런 다음 방금 만든 키와 함께 OpenSSL Request 명령을 사용하여 **인증서 서명 요청** 생성

인증서 서명 요청은 요청자의 모든 세부 정보가 포함되어 있지만 서명은 없는 인증서와 같음

```Bash
❯ openssl req -new -key ca.key -subj "/CN=KUBERNETES-CA" -out ca.csr
```

인증서 서명 요청을 위해서는 CN (Common name) 필드에 특정 이름을 명시

CSR 파일은 인증서 인증(sign) 요청을 위한 단축 형식으로,
Private Key로 부터 생성되어 최종적으로 사용할 인증서를 위해 사용됨 

> CSR is short for Certificate Signing Request. A CSR is created from the Private Key and is then further used to create the final certificate.
> 
> - https://www.geekersdigest.com/how-to-create-and-work-with-openssl-ssl-tls-certificates/

이 경우 Kubernetes CA에 대한 인증서를 생성하기 때문에 `KUBERNETES-CA` 라고 명명

생성된 `ca.csr` 정보 확인

<br>

```Bash
❯ openssl req -noout -text -in ca.csr
Certificate Request:
    Data:
        Version: 1 (0x0)
        Subject: CN=KUBERNETES-CA
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:b2:...:b4:3c:
                    ...
                Exponent: 65537 (0x10001)
        Attributes:
            (none)
            Requested Extensions:
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        65:4e:82:...:50:7a:52:
        ...

❯ cat ca.csr
-----BEGIN CERTIFICATE REQUEST-----
MIICXTCCAUUCAQAwGDEWMBQGA1UEAwwNS1VCRVJORVRFUy1DQTCCASIwDQYJKoZI
hvcNAQEBBQADggEPADCCAQoCggEBALILDDTBg5CnGAGRLLQ80B88o48g/MGqMUaH
...
yYcdXFnXWTTC0/sy+shQQGa4A/qg/W6NceKe0/4N3Te/6HeS4evSa4zlQppKueq0
tM4xOZXAp8vl36DouM2qpF3R6lq/21yb3eO4GFCP6cGY
-----END CERTIFICATE REQUEST-----
```

<br>

**3. Sign Certificates**

마지막으로 OpenSSL x509 명령에, 이전 명령에서 생성한 인증서를 명시하여 서명 요청 전송

```Bash
❯ openssl x509 -req -in ca.csr -signkey ca.key -out ca.crt
Certificate request self-signature ok
subject=CN=KUBERNETES-CA
```

이는 CA 자체를 위한 것으로, CA는 첫 번째 단계에서 생성한 자체 개인 키를 사용하여 CA에 의해 자체 서명됨

다른 모든 인증서에는 CA 키 페어를 써서 서명

```Bash
❯ openssl x509 -text -in ca.crt
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            7d:cc:...:3b:75:e6
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=KUBERNETES-CA
        Validity
            Not Before: May  5 16:28:54 2024 GMT
            Not After : Jun  4 16:28:54 2024 GMT
        Subject: CN=KUBERNETES-CA
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    00:b2:...:2c:b4:3c:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Subject Key Identifier:
                9C:30:...:50:F4:3A
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        71:8a:8f:...:5f:27:
        ...
-----BEGIN CERTIFICATE-----
MIIC3zCCAcegAwIBAgIUfcy8HfmVcw2boU9s/I2Yxao7deYwDQYJKoZIhvcNAQEL
BQAwGDEWMBQGA1UEAwwNS1VCRVJORVRFUy1DQTAeFw0yNDA1MDUxNjI4NTRaFw0y
...
WkOioG3CW8OdGQtyJ6MlMfBXsEiShO6S2o39NfA9axo2csqeGErgnHEe4ehCtedJ
kkBVtvyg7lgmr7TJ9AEeQ0usQw==
-----END CERTIFICATE-----

```

CA는 이제 개인 키와 Root 인증서 파일을 준비 완료

<br>

### Client Certificate Creation

<br>

**1. Generate Keys**

관리자용 개인 키 생성은 OpenSSL 명령의 동일한 프로세스를 따름

```Bash
❯ openssl genrsa -out admin.key 2048
```

어드민 용 개인 키가 잘 생성되었는지 확인

```Bash
❯ openssl rsa -in admin.key -check
RSA key ok
writing RSA key
-----BEGIN PRIVATE KEY-----
MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDw4zX/EcWYFNZM
u5bZoYRwwDt9i22+AWzswmk9pw3UR/wghKOg2tr65hYC/OGHaTVU1jxeeHo/EYZ3
...
vmHR7uPwvXmKYk05lItGk+OLusA7xR0F6wHk2lxLglhgczKNfqikQCN5p1sg3RSC
Bqq7sgJ1Ve9g7Wah0Gh9GKYqXg==
-----END PRIVATE KEY-----
```

<br>

**2. Certificate Signing Request**

CSR을 생성하는데, 이 때 관리자 이름인 Kube Admin을 지정

```Bash
❯ openssl req -new -key admin.key -subj "/CN=kube-admin" -out admin.csr
```

CN 명은 어떤 이름으로도 지정할 순 있지만, kubectl 명령 시 kubectl 인증을 위한 이름 임을 명심

그래서 감사 로그 (Audit log)나 그 밖에 상황에서 보게될 이름

즉 이를 감안하여 해당 명령어에 지정한 이름을 사용해야 함

<br>

**3. Sign Certificates**

마지막으로, `openSSL x509` 명령어를 사용해서 서명된 인증서 생성

```Bash
❯ openssl x509 -req -in admin.csr -CA ~/ca.crt -CAkey ~/ca.key -out admin.crt
Certificate request self-signature ok
subject=CN=kube-admin
```

하지만 이번엔 CA 인증서 (`ca.crt`)와 CA 비밀 키(`ca.key`)를 입력해야 함

당신은 CA 키 쌍을 통해 인증서에 서명할 수 있고, 이는 클러스터 내 인증서를 유효하게 만듦 

서명된 인증서는 `-out` 옵션으로 입력한 `admin.crt` 파일에 입력됨

<br>

```Bash
❯ openssl x509 -text -in admin.crt
Certificate:
    Data:
        Version: 3 (0x2)
        Serial Number:
            39:39:...:42:94:a5
        Signature Algorithm: sha256WithRSAEncryption
        Issuer: CN=KUBERNETES-CA
        Validity
            Not Before: May  6 06:09:30 2024 GMT
            Not After : Jun  5 06:09:30 2024 GMT
        Subject: **CN=kube-admin**
        Subject Public Key Info:
            Public Key Algorithm: rsaEncryption
                Public-Key: (2048 bit)
                Modulus:
                    ...
                Exponent: 65537 (0x10001)
        X509v3 extensions:
            X509v3 Subject Key Identifier:
                1A:0A:...:8A:B6
            X509v3 Authority Key Identifier:
                9C:30:...:50:F4:3A
    Signature Algorithm: sha256WithRSAEncryption
    Signature Value:
        25:18:...:9f
-----BEGIN CERTIFICATE-----
MIIC/TCCAeWgAwIBAgIUOTmll38f6oMrqfx/eqVfzURClKUwDQYJKoZIhvcNAQEL
BQAwGDEWMBQGA1UEAwwNS1VCRVJORVRFUy1DQTAeFw0yNDA1MDYwNjA5MzBaFw0y
...
nw==
-----END CERTIFICATE-----
```

---

클라이언트 인증서를 생성하는 위 전체 과정은,
인증서는 사용자 ID이고 키는 암호 같다는 면에서,
새 사용자 계정을 생성하는 것과 유사함

하지만 단순한 사용자 이름과 암호보다 보안이 훨씬 더 철저함 

위의 어드민 사용자를 생성하는 과정이 아닌 일반 사용자를 생성하는 것은 구별할 필요가 있음 

사용자 계정을 구분하기 위해서는 세분화된 특정 그룹로 사용자를 관리

실제로, 위의 과정에서 어드민 권한 (`Administerative Privileges`)을 가진 그룹인 **System Master** 에 추가 


_⚠️ 그룹과 관련한 내용은 TODO 로 남겨두지만, 꼭 명심하고 넘어가야 할 것은, 
사용자 생성 시 반드시 그룹 정보를 추가해야 한다는 것_


```Bash
❯ openssl req -new -key admin.key -subj "/CN=kube-admin**/O=systemLmasters**" -out admin.csr
```

위처럼 `-O` 옵션에 그룹을 추가하면 되고, 이후 인증 과정을 완료하면 어드민 권한을 가진 유저를 생성할 수 있음

<br>

이 밖에도 Kubernetes Control Plane 에 존재하는 시스템 컴포넌트에 대한 CA 인증이 키 페어가 필요

모든 시스템 컴포넌트는 이름 앞에 키워드 시스템을 붙여야 함

- Kube Scheduler: `scheduler.key`, `scheduler.csr`, `scheduler.crt` (SYSTEM:KUBE-SCHEDULER)
- Kube Controller Manager: `controller-manager.key`, `controller-manager.csr`, `controller-manager.crt` (SYSTEM:KUBE-CONTROLLER-MANAGER)
- Kube Proxy: `kube-proxy.key`, `kube-proxy.csr`, `kube-proxy.crt` (SYSTEM:KUBE-PROXY)

---

### Usage

생성한 인증서를 통해 kube-apiserver 에 인증된 요청을 보낼 수 있음

인증서를 적용해서 요청을 보내는 방법은 두 가지

<br>

**1. Curl Command with `key`·`cert`·`cacert` options** 

```Bash
❯ curl https://kube-apiserver:6443/api/v1/pods --key admin.key --cert admin.crt --cacert ca.crt
``` 

<br>

**2. `kube-config.yaml`**

server Endpoint 와 Certificate, 그리고 어드민 키 페어를 지정

Kubernetes 에서 가장 많이 사용되는 방식

```Bash
apiVersion: v1
clusters:
- cluster:
    certificate-authority: ca.crt
    server: https://kube-apiserver:6443
  name: kubernetes
kind: Config
users:
- name: kubernetes-admin
  user:
    client-certificate: admin.crt
    client-key: admin.key
```

클라이언트가 서버에서 보낸 인증서를 유효성 검사하기 위해서, 혹은 그 반대의 경우
**퍼블릭 인증 기관에 인증(authority)된 인증서**의 사본이 필요

가령, 웹 응용 프로그램의 경우엔 사용자 브라우저에 이미 설치돼 있는 것

쿠버네티스에서도 컴포넌트끼리 서로를 확인하려면 CA의 루트 인증서 복사본이 필요

서버나 클라이언트가 인증서를 갖고 구성할 때마다 CA Root 인증서도 지정해야 함

<br><img src="./img/tls_in_kubernetes_certificate_creation_img1.png" width="70%" >
→ 위 이미지의 각 컴포넌트 마다 ca.cert를 지정해야함

<br>

### Server Side Certificate

<br>

#### Setting: ETCD servers

ETCD 인증서를 생성하는 절차는 이전과 동일하며, ETCD-SERVER 라고 이름을 붙여보자

<br><img src="./img/tls_in_kubernetes_certificate_creation_img2.png" width="70%" ><br>

ETCD 서버는 고가용성을 보장하는 환경에서 다중 서버에 걸친 클러스터로 배포될 수 있는데,

이 경우, 클러스터 내 다른 멤버 간의 보안된 통신을 하려면 추가 피어 인증서를 생성해야 함
→ ETCD-PEER

```Bash
    - etcd
    - --advertise-client-urls=https://127.0.0.1:2379
    - **--key-file=/path-to-certs/etcdserver.key**
    - **--cert-file=/path-to-certs/etcdserver.crt**
    - --client-cert-auth=true
    - --data-dir=/var/lib/etcd
    - --initial-advertise-peer-urls=https://127.0.0.1:2380
    - --initial-cluster="master=https://127.0.0.1:2380"
    - --listen-client-urls=https://127.0.0.1:2379
    - --listen-peer-urls=https://127.0.0.1:2380
    - --name=master
    - **--peer-cert-file=/path-to-certs/etcd/peer.crt**
    - **--peer-client-cert-auth=true**
    - **--peer-key-file=/etc/kubernetes/pki/etcd/peer.key**
    - **--peer-trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt**
    - --snapshot-count=10000
    - **--trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt**
```

<br>

인증서가 생성되면 ETCD 서버를 시작하는 동안 지정

- `--key-file`: Etcd 서버 key 파일 지정 옵션
- `--cert-file`: Etcd 서버 인증서 파일 지정 옵션
- `--peer-*`: Etcd Peer 인증서 파일 지정 옵션
- `--trusted-ca-file`: CA Root 인증서 파일 지정 옵션


<br>

#### Setting: Kube API Server

종종, kubernetes와 kube-apiserver 가 하는 일을 혼동하는 사람들이 존재해서 kube-apiserver를
<u>① `kubernetes`</u> 를 부르는 경우가 있음

혹은 <u>② `kubernetes.default`</u>, <u>③ `kubernetes.default.svc`</u>, 또 풀네임 <u>④ `kubernetes.default.svc.cluster.local`</u> 으로 부르는 사람도 있음 

혹은, <u>⑤ `IP 주소`</u>를 직접 부르는 사람도 있음

때문에, 이 모든 이름을 kube-apiserver 를 생성할 때 인증서에 등록해두어야 함

등록하는 방법은 다음과 같음

1. openssl req 명령어로 인증 요청서 생성 시, 이름을 지정한 `cnf` 파일을 입력

cnf 파일 작성

```Bash
[req]
req_extenstions = v3_req
distinguished_name = req_distinguished_name
[ v3_req ]
basicConstraints = CA:FALSE
keyUsage = nonRequdiation,
subjectAltName = @alt_names
[alt_names]
DNS.1 = kubernetes
DNS.2 = kubernetes.default
DNS.3 = kubernetes.default.svc
DNS.4 = kubernetes.default.svc.cluster.local
IP.1 = 10.96.0.1
IP.2 = 172.17.0.87
```

<br>

`openssl req` 명령에 `config` 옵션 값으로 지정

```Bash
openssl req -new -key apiserver.key -subj "\CN=kube-apiserver" -out apiserver.csr \
    -config openssl.cnf
```

<br><img src="./img/tls_in_kubernetes_certificate_creation_img3.png" width="70%" ><br>

이후, 해당 요청서를 인증서 발급 시 사용  

```Bash
openssl x509 -req -in apiserver.csr -CA ca.crt -CAkey ca.key -out apiserver.crt
```

API 서버가 ETCD 및 kubelet 서버에 대해서는, 클라이언트로서 통신하기 때문에 API 클라이언트 인증서를 고려해야 함

클라이언트로서 요청할 때 필요한 인증서의 위치는 Kube-apiserver 실행 파일이나 서비스 구성 파일을 통해 전달

_(모든 구성 요소가 클라이언트를 확인하기 위해 CA 인증서가 필요하다는 것을 명심)_

<br><img src="./img/tls_in_kubernetes_certificate_creation_img4.png" width="70%" ><br>

- `--client-ca-file` 옵션: CA 파일 입력
- `--tls-cert-file` 옵션: kube-apiserver CA 인증서 입력
- `--tls-private-file` 옵션: kube-apiserver 클라이언트 인증서 입력

- `--etcd-*` 옵션: Etcd 연결을 위한 인증서
- `--kubelet-*` 옵션: Kubelet 연결을 위한 인증서


<br>

#### Setting: Kubelet Server


ETCD 서버는 각 노드에서 노드 관리를 담당하기 위한 HTTP API Server

노드를 모니터링하고 어떤 Pod가 어떤 노드 위에 위치하는 지에 대한 정보를 보냄

클러스터의 각 노드에 대해 키 인증서 쌍이 필요

해당 인증서의 이름은, 각 노드 이름을 따서 지음: `node01`, `node02`, `node03` 등

<br><img src="./img/tls_in_kubernetes_certificate_creation_img5.png" width="70%" ><br>

Kubelet Config 파일에 입력해야 하며, 클러스터 내 모든 노드에 대해 설정 필요

<br><img src="./img/tls_in_kubernetes_certificate_creation_img6.png" width="70%" ><br>

<br>

**btw `kube-apiserver`**

kube-apiserver 와 통신 시 사용할 인증서도 필요

노드 또한 시스템 컴포넌트 (kube-scheduler 나 controller-manager 같이) 중 하나이기 때문에,

`SYSTEM` prefix 다음에 노드 명을 입력: `system:node:node01`, `system:node:node02`, `system:node:node03` 등 

각 노드마다 사용 권한을 다르게 주기 위해, 그룹 이름을 명시하는 방법을 따름

따라서, 시스템 노드라는 그룹에 노드를 추가해야 함

