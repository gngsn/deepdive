## Question 6.

> ### Taint the worker node `node01` to be Unschedulable.
> ### Once done, create a pod called `dev-redis`, image `redis:alpine`, to ensure workloads are not scheduled to this worker node.
> ### Finally, create a new pod called `prod-redis` and image: `redis:alpine` with toleration to be scheduled on `node01`.
>
> key: `env_type`, value: `production`, operator: `Equal` and effect: `NoSchedule`

<br>

### Solution

```Bash
controlplane ~ ➜  kubectl taint nodes node01 env_type=production:NoSchedule
node/node01 tainted

controlplane ~ ➜  k run dev-redis --image=redis:alpine
pod/dev-redis created

controlplane ~ ➜  k get pods -o wide
NAME           READY   STATUS    RESTARTS   AGE   IP             NODE           NOMINATED NODE   READINESS GATES
dev-redis      1/1     Running   0          6s    10.244.0.4     controlplane   <none>           <none>
...

controlplane ~ ➜  k run prod-redis --image=redis:alpine --dry-run=client -o yaml > prod-redis.yaml

controlplane ~ ➜  vi prod-redis.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: prod-redis
  name: prod-redis
spec:
  containers:
  - image: redis:alpine
    name: prod-redis
    resources: {}
  tolerations:
  - key: "env_type"
    operator: "Equal"
    value: "production"
    effect: "NoSchedule"
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

controlplane ~ ➜  k apply -f prod-redis.yaml 
pod/prod-redis created

controlplane ~ ➜  k get pod -o wide -w
NAME           READY   STATUS    RESTARTS   AGE    IP             NODE           NOMINATED NODE   READINESS GATES
dev-redis      1/1     Running   0          2m7s   10.244.0.4     controlplane   <none>           <none>
prod-redis     1/1     Running   0          6s     10.244.192.6   node01         <none>           <none>
...
```

