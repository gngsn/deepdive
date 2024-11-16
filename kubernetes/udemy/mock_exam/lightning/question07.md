## Question 06.


Create a pod called `secret-1401` in the `admin1401` namespace using the `busybox` image. 
The container within the pod should be called `secret-admin` and should sleep for `4800` seconds.

The container should mount a read-only secret volume called secret-volume at the path `/etc/secret-volume`. 
The secret being mounted has already been created for you and is called `dotfile-secret`.


### Answer

```Bash
controlplane ~ ➜  kubectl run secret-1401 -n admin1401 --image=busybox --dry-run=client -oyaml --command -- sleep 4800 > admin.yaml

controlplane ~ ➜  cat admin.yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: secret-1401
  name: secret-1401
  namespace: admin1401
spec:
  containers:
  - command:
    - sleep
    - "4800"
    image: busybox
    name: secret-1401
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

controlplane ~ ➜  vi admin.yaml
---
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: secret-1401
  name: secret-1401
  namespace: admin1401
spec:
  volumes:
  - name: secret-volume
    # secret volume
    secret:
      secretName: dotfile-secret
  containers:
  - command:
    - sleep
    - "4800"
    image: busybox
    name: secret-admin
    # volumes' mount path
    volumeMounts:
    - name: secret-volume
      readOnly: true
      mountPath: "/etc/secret-volume"      
```