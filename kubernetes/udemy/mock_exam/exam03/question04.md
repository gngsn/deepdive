## Question 4.
### Create a Pod called `non-root-pod`, image: `redis:alpine`

- `runAsUser: 1000`
- `fsGroup: 2000`

<br>

#### Solution

[🔗Configure a Security Context for a Pod or Container](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-the-security-context-for-a-pod) 참고

```Bash
controlplane ~ ➜  k run non-root-pod --image=redis:alpine --dry-run=client -o yaml > non-root-pod.yaml

controlplane ~ ➜  vi non-root-pod.yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: non-root-pod
  name: non-root-pod
spec:
  securityContext:
    runAsUser: 1000
    fsGroup: 2000
  containers:
  - image: redis:alpine
    name: non-root-pod
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

controlplane ~ ➜  k apply -f non-root-pod.yaml 
pod/non-root-pod created
```

