### Q1. Deploy a pod named nginx-pod using the nginx:alpine image.

<br>

#### Answer

`kubectl run` 명령어로 Pod 생성 & 실행

```Bash
controlplane ~ ➜  k run nginx-pod --image nginx:alpine
pod/nginx-pod created
```

#### Clarification

실제 정확한 이름/이미지로 생성되었는지 검증

```Bash
controlplane ~ ➜  k get po -owide
NAME        READY   STATUS    RESTARTS   AGE     IP           NODE           NOMINATED NODE   READINESS GATES
messaging   1/1     Running   0          2m37s   172.17.0.5   controlplane   <none>           <none>
```
