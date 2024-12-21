### Q8. Create a POD in the `finance` namespace named `temp-bus` with the image `redis:alpine`.

<br>

#### Answer

```Bash
controlplane ~ ✖ k describe po orange
Name:             orange
...
Init Containers:
  init-myservice:
    Command:
      sh
      -c
      sleeeep 2;
    State:          Terminated
      Reason:       Error
...
Events:
  Type     Reason     Age                From               Message
  ----     ------     ----               ----               -------
  Warning  BackOff    8s (x3 over 26s)   kubelet            Back-off restarting failed container init-myservice in pod orange_default(d7537a4b-7f8f-43b7-84f1-58497de7ddda)
```

원인 파악 완료 → Pod InitContainer 수정

```
apiVersion: v1
kind: Pod
metadata:
  name: orange
spec:
  containers:
  initContainers:
  - command:
    - sh
    - -c
    - sleep 2
  ...
```

#### Clarification

생성된 Pod 확인

```Bash
controlplane ~ ➜  k get po
NAME                          READY   STATUS    RESTARTS   AGE
orange                        1/1     Running   0          68s
...
```
