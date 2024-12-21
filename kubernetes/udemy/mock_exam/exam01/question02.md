### Q2. Deploy a `messaging` pod using the `redis:alpine` image with the labels set to `tier=msg`.

<br>

#### Answer

```Bash
controlplane ~ ➜  k run messaging --image redis:alpine --labels="tier=msg"
pod/messaging created
```

#### Clarification

```Bash
controlplane ~ ➜  k get po --show-labels
NAME        READY   STATUS    RESTARTS   AGE     LABELS
messaging   1/1     Running   0          6m18s   tier=msg
nginx-pod   1/1     Running   0          7m43s   run=nginx-pod
```
