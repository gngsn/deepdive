## DNS in kubernetes

한 클러스터에 세 개의 노드가 있고, 그 내부에는 서비스와 Pod가 배포되어 있음

각각 노드는 이름과 IP가 할당되어 있음

이 때, 클러스터 내 서비스나 Pod 처럼 서로 다른 컴포넌트는 어떻게 통신할까?

쿠버네테스는 클러스터를 배포할 때 Built-in DNS 서버를 디폴트로 배포함

(하지만, 쿠버네티스를 직접 설정한다면 직접 설정해야 함)

<table>
<tr>
<td>
<pre><code>
 +----------------------- Cluster --------------------+
 |                                                    |
 |      🔵                   🟠             🟪         |
 |     Pod A                Pod B      web-service    |
 |  10.244.1.5         10.107.37.188  10.107.37.188   |
 +----------------------------------------------------+
</code></pre>
</td>
<td>

<table>
<tr>
<th>Hostname</th>
<th>IP Address</th>
</tr>
<tr>
<td>web-service</td>
<td>10.107.37.188</td>
</tr>
</table>

</td>
</tr>
</table>

쿠버네티스 DNS 서비스는 서비스가 생성될 때마다 Record를 생성함

Record는 Service 이름과 IP 주소를 매핑함

컴포넌트들 간 호출을 하고 싶다면 대상 컴포넌트의 이름을 명시하면 되는데,
동일한 네임스페이스 안에서는 서로 이름으로만 호출하고,
다른 네임스페이스에서 호출하려면 풀네임을 사용함

<br>

**1. 동일한 Namespace 컴포넌트**

<table>
<tr>
<td>
<pre><code> +------------------------- Cluster ------------------------+
 |  +--------------------- Namespace --------------------+  |
 |  |                                                    |  |
 |  |      🔵                   🟠             🟪         |  |
 |  |     Pod A                Pod B      web-service    |  |
 |  |  10.244.1.5         10.107.37.188  10.107.37.188   |  |
 |  +----------------------------------------------------+  |
 +----------------------------------------------------------+
</code></pre>
</td>
<td>

<table>
<tr>
<th>Hostname</th>
<th>IP Address</th>
</tr>
<tr>
<td>web-service</td>
<td>10.107.37.188</td>
</tr>
</table>

</td>
</tr>
</table>

컴포넌트들이 같은 노드에 있던 다른 노드에 있던 간에 동일한 Namespace에 있다면, 단순히 이름으로 접근할 수 있음

가령, 🔵 Pod는 🟪 `web-service`에 접근할 때 아래와 같이 이름으로 접근할 수 있음

```bash
❯ curl http://web-service
Welcome to NGINX!
```

<br>

## 2. 서로 다른 Namespace 컴포넌트

서로 다른 Namespace에 있다면 반드시 DNS 전체 이름을 명시해야 함

```
 +------------------------- Cluster ------------------------+
 |  +---------------+  +---------------------------------+  |
 |  | Default       |  |                          Apps   |  |
 |  | Namespace     |  |                      Namespace  |  |
 |  |               |  |                                 |  |
 |  |       🔵      |  |          🟠            🟪        |  |
 |  |     Pod A     |  |        Pod B      web-service   |  |
 |  |  10.244.1.5   |  |   10.107.37.188  10.107.37.188  |  |
 |  +---------------+  +---------------------------------+  |
 +----------------------------------------------------------+
```


`web-service`가 별도의 네임스페이스에 있다고 하면, 서비스 이름을 네임스페이스의 sub-domain으로 명시함

가령, 위 예시에서는 `web-service.apps` 로 언급해야 함

```Bash
❯ curl http://web-service.apps
Welcome to NGINX!
```

<br>

### Service DNS records

모든 서비스는 SVC라는 다른 하위 도메인으로 함께 묶임

모든 서비스는 `svc`라는 또 다른 상위 도메인에 그룹화 되어 하위 도메인으로 구성됨

<table>
<tr>
<th>Hostname</th>
<th>Namespace</th>
<th>Type</th>
<th>Root</th>
<th>IP Address</th>
</tr>
<tr>
<td>web-service</td>
<td>apps</td>
<td>svc</td>
<td>cluster.local</td>
<td>10.107.37.188</td>
</tr>
</table>

그래서 접근하고자 하는 서비스 이름은 `web-serivce.apps.svc` 로 접근할 수 있음

마지막으로, 모든 Service와 Pod는 클러스터의 Root 도메인으로 묶이는데,`local`이 Default 임  

<table>
<tr>
<th>Root</th>
<td>cluster.local</td>
</tr>
<tr>
<th>Type</th>
<td>svc</td>
</tr>
<tr>
<th>Namespace</th>
<td>apps</td>
</tr>
<tr>
<th>Name</th>
<td>web-service</td>
</tr>
</table>

그래서 최종적으로는 `web-service.apps.svc.cluster.local` 로 접근 가능하며, 해당 주소가 서비스의 풀네임임

```Bash
❯ curl http://web-service.apps.svc.cluster.local
Welcome to NGINX!
```

<br>

### POD DNS Records

Service와 다르게, Pod 의 Record는 자동적으로 생성되지 않음

(생성하는 방법은 다음 장에서 정리)

Pod의 Record가 생성될 때에는, Pod의 이름이 아니라 IP 주소에서 Dot(`.`)을 Dash(`-`)로 치환한 형태로 지정됨

<table>
<tr>
<th>Root</th>
<td colspan="2">cluster.local</td>
</tr>
<tr>
<th>Type</th>
<td>svc</td>
<td>pod</td>
</tr>
<tr>
<th>Namespace</th>
<td>apps</td>
<td>apps</td>
</tr>
<tr>
<th>Name</th>
<td>web-service</td>
<td>10-244-2-5</td>
</tr>
</table>

가령, `10.244.2.5`의 IP 주소를 가진 Pod의 이름은 `10-244-2-5`가 되는 것

```
 +------------------------- Cluster ------------------------+
 |  +---------------+  +---------------------------------+  |
 |  | Default       |  |                          Apps   |  |
 |  | Namespace     |  |                      Namespace  |  |
 |  |               |  |                                 |  |
 |  |       🔵      |  |          🟠            🟪        |  |
 |  |     Pod A     |  |        Pod B      web-service   |  |
 |  |  10.244.1.5   |  |   10.107.37.188  10.107.37.188  |  |
 |  +---------------+  +---------------------------------+  |
 +----------------------------------------------------------+
```

위 예시의 컴포넌트의 풀네임을 정리해보면, 아래와 같음

<table>
<tr>
<th>Hostname</th>
<th>Namespace</th>
<th>Type</th>
<th>Root</th>
<th>IP Address</th>
</tr>
<tr>
<td>web-service</td>
<td>apps</td>
<td>svc</td>
<td>cluster.local</td>
<td>10.107.37.188</td>
</tr>
<tr>
<td>10-244-2-5</td>
<td>apps</td>
<td>pod</td>
<td>cluster.local</td>
<td>10.244.2.5</td>
</tr>
<tr>
<td>10-244-1-5</td>
<td>default</td>
<td>pod</td>
<td>cluster.local</td>
<td>10.244.1.5</td>
</tr>
</table>

