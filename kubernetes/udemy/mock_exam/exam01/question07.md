### Q7. Create a static pod named `static-busybox` on the `controlplane` node that uses the `busybox` image and the command `sleep 1000`.

<br>

#### Answer

```Bash
controlplane ~ ➜  k run static-busybox --image busybox -oyaml --dry-run=client --command -- sleep 1000 > /etc/kubernetes/manifests/static-busybox.yaml
```


#### Clarification

```Bash
controlplane ~ ➜  k get po -w
NAME                          READY   STATUS    RESTARTS   AGE
static-busybox-controlplane   1/1     Running   0          3s
...
```
