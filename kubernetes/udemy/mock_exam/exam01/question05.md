### Q5. Create a service `messaging-service` to expose the `messaging` application within the cluster on port 6379.


Use imperative commands.

- Service: messaging-service
- Port: 6379
- Type: ClusterIp
- Use the right labels

<br>

#### Answer

```Bash
k get nodes -ojson > /opt/outputs/nodes-z3444kd9.json
```

#### Clarification

생성된 파일 확인

```Bash
controlplane ~ ➜  cat /opt/outputs/nodes-z3444kd9.json
{
    "apiVersion": "v1",
    "items": [
        {
            "apiVersion": "v1",
            "kind": "Node",
            "metadata": {
                "name": "controlplane",
                ...
            },
        },
    ...
}
```

kubectl run test-nslookup --image=busybox:1.28 --rm -it --restart=Never