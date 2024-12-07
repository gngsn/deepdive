## Question 1.

> ### Create a new service account with the name `pvviewer`. Grant this Service account access to `list` all PersistentVolumes in the cluster by creating an appropriate cluster role called `pvviewer-role` and `ClusterRoleBinding` called `pvviewer-role-binding`. Next, create a pod called `pvviewer` with the `image: redis` and `serviceAccount: pvviewer` in the `default` namespace.

<br>

### Solution

1. create serviceaccount

```Bash
controlplane ~ ➜  kubectl create serviceaccount pvviewer
serviceaccount/pvviewer created
```

2. create role

```Bash
controlplane ~ ➜  kubectl create clusterrole pvviewer-role --resource=persistentvolumes --verb=list
clusterrole.rbac.authorization.k8s.io/pvviewer-role created
```

3. create role-binding

```Bash
controlplane ~ ➜  kubectl create clusterrolebinding pvviewer-role-binding --clusterrole=pvviewer-role --serviceaccount=default:pvviewer
clusterrolebinding.rbac.authorization.k8s.io/pvviewer-role-binding created
```

4. create pod & configure serviceAccount by specifying `serviceAccountName`

```Bash
controlplane ~ ➜  k run pvviewer --image=redis -o yaml --dry-run=client > pvviewer.yaml

controlplane ~ ➜  vi pvviewer.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: pvviewer
  name: pvviewer
spec:
  containers:
  - image: redis
    name: pvviewer
    resources: {}
  serviceAccountName: pvviewer 
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
```

```Bash
controlplane ~ ➜  k apply -f pvviewer.yaml 
pod/pvviewer created
```

check permission

```
controlplane ~ ➜  kubectl auth can-i list pv
Warning: resource 'persistentvolumes' is not namespace scoped

yes
```

<br><br>

---