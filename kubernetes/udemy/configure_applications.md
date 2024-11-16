# Configure Applications

## Environment Variables 

환경 변수를 설정하려면 `env` 속성 사용


```yaml
apiVersion: v1
kind: Pod
metadata:
  name: ubuntu-sleeper-pod
spec:
  containers:
    - name: ubuntu-sleeper
      image: ubuntu-sleeper
      ports:
        - containerPort: 8080
      env:
        - name: APP_COLOR
        - value: pink
```

`env`는 배열로, 각 `name`과 `value` 명시

`name`은 환경 변수 이름, `value`는 해당 환경 변수 값

```Bash
docker run -e APP_COLOR=pink simple-webapp-color
```

위 처럼 직접 값을 입력할 수도 있지만, `ConfigMap`이나 `Secret`의 값을 가져올 수 있음

---

## ConfigMaps

Pod 정의 파일이 많아질 수록, 한 파일에 여러 환경 변수를 관리하기 어려워짐

이러한 데이터들을 Pod 정의 파일에서 분리해 Configuration Map 으로 중앙 관리할 수 있음

ConfigMap은 쿠버네티스에서 Key-Value 쌍의 Config 데이터를 전달하는 데 사용

<br>

```Bash
APP_COLOR: blue
APP_MODE: prod
```

<br>

Pod가 생성되면 Pod에 Config map 값을 주입해 환경 변수로 사용될 수 있게 함

<br>

---

<br>

**ConfigMap 구성 두 단계**

<br>

### STEP 1. Create ConfigMap

첫째, 구성 맵을 생성

다른 쿠버네티스 객체와 동일하게 ConfigMap은 두 가지 방법으로 생성 가능

#### 📌 1. Imperative

<br>

**✔️ --from-literal option**

```Bash
❯ kubectl create configmap \
     <<config-name>> --from-literal=<key>=<value>
```

Key-Value 를 Command Line에 바로 입력

<br>

**Example.**

```Bash
❯ kubectl create configmap \
     app-config --from-literal=APP_COLOR=blue \
                --from-literal=APP_MODE=prod \
```

여러 개를 사용하고 싶다면 `--from-literal` Option 사용

<br>

**✔️ --from-file option**

```Bash
❯ kubectl create configmap \
     <<config-name>> --from-file=<path-to-file>
```

명령줄이 길어질 수 있는데, 이때 `--from-file` 옵션을 통해 파일을 명시할 수 있음

<br>

**Example.**


```Bash
❯ kubectl create configmap \
     app-config --from-file=app_config.properties
```

<br>

#### 📌 2. Declarative

```Bash
kubectl create -f ...
```

선언적 방식은 definition 파일 생성

ConfigMap 정의 파일은 `spec` 대신 `data` 속성을 포함

<br>

**Example.**

_config-map.yaml_

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_COLOR: blue
  APP_MODE: prod
```

Config 파일 이름 지정해서 kubectl 명령 실행

```Bash
kubectl create -f config-map.yaml
```

그럼 지정한 값으로 ConfigMap을 생성함

필요한 만큼 ConfigMap 생성 가능

<table>
<tr>
<th>app-config</th>
<th>mysql-config</th>
<th>redis-config</th>
</tr>
<tr>
<td>
<pre><code lang="bash">APP_COLOR: blue
APP_MODE: prod
</code></pre>
</td>
<td>
<pre><code lang="bash">port: 3306
max_allowed_packet: 128M
</code></pre>
</td>
<td>
<pre><code lang="bash">port: 6379
rdb-compression: yes
</code></pre>
</td>
</tr>
</table>

각각 Application, SQL, Redis 을 위한 Config Map

나중에 이름을 기반으로 Pod 주입을 결정하는데, 이름을 정확히 정의·명시 하는 게 중요

<br>

#### View ConfigMaps

Config Map 확인은 `Kubectl get configmaps` 명령 실행

```Bash
❯ kubectl get configmaps
NAME                          DATA   AGE
app-config                    2      5s
kube-root-ca.crt              1      48d
```

자세하게 보기 위해선 `Kubectl describe configmaps` 명령 실행으로 상세 확인 가능

Data 섹션 하위에 지정한 환경 변수를 확인할 수 있음

<br/>

```Bash
❯ kubectl describe configmaps
Name:         app-config
Namespace:    default
Labels:       <none>
Annotations:  <none>

Data
====
APP_COLOR:
----
blue
APP_MODE:
----
prod

BinaryData
...
```

<br>

### STEP 2. Inject into Pod

_둘째, 그걸 포드에 주입_

간단한 웹앱 실행 Pod 정의 파일을 예시로 들자면,

<table>
<tr><td>

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: simple-webapp-color
  labels:
    name: simple-webapp-color
spec:
  containers:
  - name: simple-webapp-color
    image: simple-webapp-color
    ports:
    - containerPort: 8080
    envFrom:
    - configMapRef:
        name: app-config
```

</td></tr>
<tr><td>

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  APP_COLOR: blue
  APP_MODE: prod
```

</td></tr>
</table>

환경 변수를 삽입하려면 `containers` 하위에 `envFrom` 새 속성을 추가

`envFrom` 속성은 Array로 다중의 환경 변수를 전달할 수 있음

→ 이전에 생성해둔 ConfigMap 값을 주입하는 방법


---

Pod에 환경변수를 입력하는 방법은 세 가지가 존재 

<table>
<tr>
<th>1. Env: ConfigMap 사용</th>
<th>2. Single Env</th>
<th>3. Volume</th>
</tr>
<tr>
<td>

```yaml
envFrom:
    - configMapRef:
        name: app-config     
```

</td>
<td>

단일 환경 변수로 입력

```yaml
env:
  - name: APP_COLOR
    valueFrom:
      configMapKeyRef:
        name: app-config     
        key: APP_COLOR     
```

</td>
<td>

전체 데이터를 파일로 볼륨에 입력

```yaml
volumes:
  - name: app-config-volume
    configMap: 
      name: app-config
```

</td>
</tr>
</table>

<br>

---

## Secrets

애플리케이션에서 직접 입력할 수 없는 Secret 값을 설정하는 방법?

가령, Database Host, User, Password 등을 설정할 때 어떻게 설정해야 할까

이때, Secret 사용 - 비밀번호나 Key 등 민감한 정보를 저장하는 데 횔용

ConfigMap은 일반 텍스트 형식으로 구성 데이터를 저장하기 때문에 적절하지 않음

인코딩된 형식으로 저장된다는 점만 빼면 ConfigMap 와 비슷

```Bash
DB_Host: RvEtdlSw=
DB_User: THevDhS==
DB_Password: aHrGgsdJ
```

ConfigMap 와 마찬가지로 Secret 구성은 두 단계로 구성

<br>

### STEP 1. Create Secret

가령, 아래와 같은 설정 값을 지정하는 방법?

```Bash
DB_Host: mysql
DB_User: root
DB_Password: paswrd
```

<br>

#### 📌 1. Imperative

`kubectl create secret generic` 명령어를 통해 Secret 지정 가능

<br>

```Bash
kubectl create secret generic \
    <secret-name> --from-literal=<key>=<value>
```

**Example.**

```Bash
kubectl create secret generic \
    app-secret --from-literal=DB_Host=mysql \
               --from-literal=DB_User=root \
               --from-literal=DB_Password=paswrd \
```

<br>

혹은 `--from-file` 옵션으로 파일을 지정할 수 있음

```Bash
kubectl create secret generic \
    <secret-name> --from-file=<path-to-file>
```

**Example.**

```Bash
kubectl create secret generic \
    app-secret --from-file=app_secret.properties
```

<br>

#### 📌 2. Declarative

```Bash
kubectl create -f ...
```

**Example.**

<table>
<tr>
<th><code>secret-data.yaml</code></th>
<th>Command</th>
</tr>
<tr><td>

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
data:
    DB_Host: mysql
    DB_User: root
    DB_Password: paswrd
```

</td><td>

```Bash
kubectl create -f secret-data.yaml
```

</td></tr></table>

Secret 은 민감한 데이터를 저장하기 위해 사용되어 암호화된 포맷으로 저장

명령적 방식 Secret을 생성하려면 인코딩된 형식의 Secret 값을 지정해야 함


```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
data:
    DB_Host: bXlzcWw=
    DB_User: cm9vdA==
    DB_Password: cGFzd3Jk
```

이렇게 인코딩된 형식으로 데이터를 지정

일반 텍스트에서 인코딩된 형식으로 데이터를 변환하려면 `echo -n '<<string>>' | base64` 입력


```Bash
❯ echo -n 'mysql' | base64
bXlzcWw=
❯ echo -n 'root' | base64
cm9vdA==
❯ echo -n 'paswrd' | base64
cGFzd3Jk
```

<br>

#### View Secrets

secret을 확인을 위해서는 `kubectl get secrets` 명령어 사용

```Bash
❯ kubectl get secrets
NAME                                         TYPE                 DATA   AGE
app-secret                                   Opaque               3      4s
```

```Bash
Name:         app-secret
Namespace:    default
Labels:       <none>
Annotations:  <none>

Type:  Opaque

Data
====
DB_Host:      5 bytes
DB_Password:  6 bytes
DB_User:      4 bytes

```

값을 보고 싶다면, YAML 포맷으로 확인

```yaml
❯ kubectl get secrets app-secret -o yaml
apiVersion: v1
data:
  DB_Host: bXlzcWw=
  DB_Password: cGFzd3Jk
  DB_User: cm9vdA==
kind: Secret
metadata:
  name: app-secret
  namespace: default
...
```

<br>

#### Decode Secret

```Bash
❯ echo -n 'bXlzcWw=' | base64 --decode
mysql
❯ echo -n 'cm9vdA==' | base64 --decode
root
❯ echo -n 'cGFzd3Jk' | base64 --decode
paswrd
```

<br>

### STEP 2. Inject into Pod

간단한 웹앱 실행 Pod 정의 파일을 예시로 들자면,

<table>
<tr>
<th><code>pod-definition.yaml</code></th>
<th><code>secret-data.yaml</code></th>
</tr>
<tr><td>

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: simple-webapp-color
  labels:
    name: simple-webapp-color
spec:
  containers:
  - name: simple-webapp-color
    image: simple-webapp-color
    ports:
    - containerPort: 8080
    envFrom:
    - secretRef:
        name: app-secret
```

</td></tr>
<tr><td>

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secret
data:
  DB_Host: bXlzcWw=
  DB_Password: cGFzd3Jk
  DB_User: cm9vdA==
```

</td></tr>
</table>

위와 같이 생성한 Secret 객체를 입력할 수 있음


<table>
<tr>
<th>1. Env: Secret 사용</th>
<th>2. Single Env</th>
<th>3. Volume</th>
</tr>
<tr>
<td>

```yaml
envFrom:
    - secretRef:
        name: app-secret     
```

</td>
<td>

단일 Secret 로 입력

```yaml
env:
  - name: DB_Password
    valueFrom:
      secretKeyRef:
        name: app-secret     
        key: DB_Password     
```

</td>
<td>

전체 데이터를 파일로 볼륨에 입력

```yaml
volumes:
  - name: app-secret-volume
    configMap: 
      name: app-secret
```

</td>
</tr>
</table>

Pod에 Secret을 Volume 형식으로 입력하는 방법은,
Secret 각각의 속성은 파일로 생성됨

```Bash
ls /opt/app-secret-volumes
DB_Host     DB_Password     DB_User
```

각각의 파일을 확인해보면, 인코딩 되지 않은 Secret이 그대로 저장되어 있음

암호화되어 있지 않기 때문에, 누구든 기밀문서로 만든 파일을 볼 수 있고 Secret 을 얻을 수 있음 

**❌ Secrets are **not Encrypted**. Only encoded**
  - Do not check-in Secret objects to SCM along with code

Secret을 앱에서 확인하거나 Git을 통해 업로드 하지 마라

**❌ Secrets are not encrypted in ETCD.**
  ✅Enable encryption at rest


**❌ Anyone able to create pods/deployments in the same namespace can access the secrets**

Pod를 만들거나 배포할 수 있는 사람은 누구나 동일한 Namespace 의 Secret에 접근 가능

Role-based Access Control 를 구성해 액세스 제한을 고려할 필요가 있음 

**✅Consider third-party secrets store providers AWS Provider, Azure Provider, GCP Provider, Vault Provider**

서드파티 암호 공급자 고려; Secret이 Etcd가 아닌 외부 Secret Provider에 저장되고 공급자는 보안의 대부분을 처리
