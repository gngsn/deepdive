# Cluster Roles and Role Bindings

Role 과 RoleBinding 은 Namespace 내에 생성

특정 Namespace를 지정하지 않으면 default Namespace에 생성됨

Pods, Deployments, Services 는 해당 Namespace 내에서 컨트롤 할 수 있지만,
Node 는 Namespace 범위가 아님

Node는 Cluster 단위 (cluster-wide or cluster-scoped) 이기 때문에 특정 Namespace에 한정 짓지 않음

리소스들은 **Namespace** 혹은 **Cluster Scoped**로 분류할 수 있음


<table>
<tr>
    <th>Namespace</th>
    <th>Cluster Scoped</th>
</tr>
<tr>
<td>

```Bash
❯ kubectl api-resources --namespaced=true
NAME                        SHORTNAMES   APIVERSION                     NAMESPACED   KIND
bindings                                 v1                             true         Binding
configmaps                  cm           v1                             true         ConfigMap
endpoints                   ep           v1                             true         Endpoints
events                      ev           v1                             true         Event
limitranges                 limits       v1                             true         LimitRange
persistentvolumeclaims      pvc          v1                             true         PersistentVolumeClaim
pods                        po           v1                             true         Pod
podtemplates                             v1                             true         PodTemplate
replicationcontrollers      rc           v1                             true         ReplicationController
resourcequotas              quota        v1                             true         ResourceQuota
secrets                                  v1                             true         Secret
serviceaccounts             sa           v1                             true         ServiceAccount
services                    svc          v1                             true         Service
controllerrevisions                      apps/v1                        true         ControllerRevision
daemonsets                  ds           apps/v1                        true         DaemonSet
deployments                 deploy       apps/v1                        true         Deployment
replicasets                 rs           apps/v1                        true         ReplicaSet
statefulsets                sts          apps/v1                        true         StatefulSet
localsubjectaccessreviews                authorization.k8s.io/v1        true         LocalSubjectAccessReview
horizontalpodautoscalers    hpa          autoscaling/v2                 true         HorizontalPodAutoscaler
cronjobs                    cj           batch/v1                       true         CronJob
jobs                                     batch/v1                       true         Job
leases                                   coordination.k8s.io/v1         true         Lease
endpointslices                           discovery.k8s.io/v1            true         EndpointSlice
events                      ev           events.k8s.io/v1               true         Event
ingresses                   ing          networking.k8s.io/v1           true         Ingress
networkpolicies             netpol       networking.k8s.io/v1           true         NetworkPolicy
poddisruptionbudgets        pdb          policy/v1                      true         PodDisruptionBudget
rolebindings                             rbac.authorization.k8s.io/v1   true         RoleBinding
roles                                    rbac.authorization.k8s.io/v1   true         Role
csistoragecapacities                     storage.k8s.io/v1              true         CSIStorageCapacity
```

</td>
<td>

```Bash
❯ kubectl api-resources --namespaced=false
NAME                              SHORTNAMES   APIVERSION                        NAMESPACED   KIND
componentstatuses                 cs           v1                                false        ComponentStatus
namespaces                        ns           v1                                false        Namespace
nodes                             no           v1                                false        Node
persistentvolumes                 pv           v1                                false        PersistentVolume
mutatingwebhookconfigurations                  admissionregistration.k8s.io/v1   false        MutatingWebhookConfiguration
validatingwebhookconfigurations                admissionregistration.k8s.io/v1   false        ValidatingWebhookConfiguration
customresourcedefinitions         crd,crds     apiextensions.k8s.io/v1           false        CustomResourceDefinition
apiservices                                    apiregistration.k8s.io/v1         false        APIService
selfsubjectreviews                             authentication.k8s.io/v1          false        SelfSubjectReview
tokenreviews                                   authentication.k8s.io/v1          false        TokenReview
selfsubjectaccessreviews                       authorization.k8s.io/v1           false        SelfSubjectAccessReview
selfsubjectrulesreviews                        authorization.k8s.io/v1           false        SelfSubjectRulesReview
subjectaccessreviews                           authorization.k8s.io/v1           false        SubjectAccessReview
certificatesigningrequests        csr          certificates.k8s.io/v1            false        CertificateSigningRequest
flowschemas                                    flowcontrol.apiserver.k8s.io/v1   false        FlowSchema
prioritylevelconfigurations                    flowcontrol.apiserver.k8s.io/v1   false        PriorityLevelConfiguration
ingressclasses                                 networking.k8s.io/v1              false        IngressClass
runtimeclasses                                 node.k8s.io/v1                    false        RuntimeClass
clusterrolebindings                            rbac.authorization.k8s.io/v1      false        ClusterRoleBinding
clusterroles                                   rbac.authorization.k8s.io/v1      false        ClusterRole
priorityclasses                   pc           scheduling.k8s.io/v1              false        PriorityClass
csidrivers                                     storage.k8s.io/v1                 false        CSIDriver
csinodes                                       storage.k8s.io/v1                 false        CSINode
storageclasses                    sc           storage.k8s.io/v1                 false        StorageClass
volumeattachments                              storage.k8s.io/v1                 false        VolumeAttachment
```

</td>
</tr>
</table>

- Namespaced resource 권한 관리: `roles`, `rolebindings`
- **Non-namespaced resource 권한 관리**: `clusterroles`, `clusterrolebindings`

---

### Cluster Roles

`clusterroles` 는 클러스터 범위라는 것만 제외하면 `roles`과 동일

가령, 클러스터 관리자 역할은 클러스터 내 노드를 조회하고 생성, 삭제할 권한을 줄 수 있음



<table>
<tr>
    <th><code>cluster-admin-role.yaml</code></th>
    <th>Command</th>
</tr>
<tr>
<td>

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: cluster-administrator
rules:
- apiGroups: [""]
  resources: ["nodes"]
  verbs: ["get", "create", "delete", "list"]
```

</td>
<td>

```Bash
❯ kubectl create -f cluster-admin-role.yaml
```

or 

```
❯ kubectl create clusterrole cluster-administrator --resource=nodes --verb=get,create,delete,list
```

</td>
</tr>
</table>

혹은 PV (Persistent Volumes) 와 클레임을 관리하기 위한 Storage Admin을 생성할 수도 있음

이후, 사용자와 연결 시킴

<br>



<table>
<tr>
    <th><code>cluster-admin-role-binding.yaml</code></th>
    <th>Command</th>
</tr>
<tr>
<td>

```yaml
apiVersion: rbac.authorization.k8s.io/v1
# This cluster role binding allows anyone in the "manager" group to read secrets in any namespace.
kind: ClusterRoleBinding
metadata:
  name: cluster-admin-role-binding
subjects:
  - kind: User
    name: cluster-admin
    apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: cluster-administrator
  apiGroup: rbac.authorization.k8s.io
```

</td>
<td>

```Bash
❯ kubectl create -f cluster-admin-role-binding.yaml
```

</td>
</tr>
</table>

혹은 

```
❯ kubectl create clusterrolebinding cluster-admin-role-binding --user=cluster-admin --clusterrole=cluster-administrator
```

사실, 클러스터 역할과 바인딩은 클러스터 범위 리소스에 사용된다고 했지만, 네임스페이스드 리소스를 위한 클러스터 역할도 만들 수 있음

사용자는 네임스페이스의 모든 리소스에 접근할 수 있게 됨

클러스터 역할로 사용자에게 Pod 접속을 승인하면 사용자는 클러스터 내 모든 포드에 접속할 수 있음

쿠버네테스는 클러스터가 처음 설정되면 기본적으로 클러스터 역할을 여러 개 만듦

