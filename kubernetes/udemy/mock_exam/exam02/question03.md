## Question 03.

> Create a new pod called `super-user-pod` with image `busybox:1.28`. Allow the pod to be able to set `system_time`.
>
> The container should sleep for 4800 seconds.
>
> - **Pod**: `super-user-pod`
> - **Container Image**: `busybox:1.28`
> - Is `SYS_TIME` capability set for the container?

<br>

### Answer

**Security Context** 관련 문제

→ Kubernetes 공식 문서에 `Security Capabilities` 검색

[Security Context: Set capabilities for a Container](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-capabilities-for-a-container) 내용 확인

```Bash
controlplane ~ ➜  k run super-user-pod --image=busybox:1.28 --dry-run=client -o yaml --command -- sleep 4800 > super-user-pod.yaml 
controlplane ~ ➜  vi super-user-pod.yaml
```

<pre><code lang="yaml">apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: super-user-pod
  name: super-user-pod
spec:
  containers:
  - command:
    - sleep
    - "4800"
    image: busybox:1.28
    name: super-user-pod
    resources: {}
    <b>securityContext:           # ← 추가
      capabilities:
        add: ["SYS_TIME"]</b>
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
</code></pre>
