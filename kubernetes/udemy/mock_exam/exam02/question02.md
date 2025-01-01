## Question 02.

> Create a Pod called `redis-storage` with image: `redis:alpine` with a Volume of type `emptyDir` that lasts for the life of the Pod.
>
> Specs on the below.
>
> - Pod named 'redis-storage' created
> - Pod 'redis-storage' uses Volume type of emptyDir
> - Pod 'redis-storage' uses volumeMount with mountPath = /data/redis

<br>

### Answer


```Bash
controlplane ~ ➜  k run redis-storage --image=redis:alpine --dry-run=client -o yaml > redis-storage.yaml 
controlplane ~ ➜  vi redis-storage.yaml
```

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
name: redis-storage
spec:
  containers:
  - image: redis:alpine
    name: redis-storage-container
    volumeMounts:
    - mountPath: /data/redis
      name: redis-volume
  <b>volumes:               # ← 추가
    - name: redis-volume
      emptyDir: {}</b>
</code></pre>

