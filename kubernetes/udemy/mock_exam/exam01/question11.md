### Use JSON PATH query to retrieve the `osImages` of all the nodes and store it in a file `/opt/outputs/nodes_os_x43kj56.txt`.


The `osImage` are under the `nodeInfo` section under `status` of each node.



<br>

#### Answer

```Bash
controlplane ~ ➜  k get node -ojson | jq -c 'paths' | grep osImage
["items",0,"status","nodeInfo","osImage"]

controlplane ~ ➜  k get node -ojsonpath='{.items[*].status.nodeInfo.osImage}' > /opt/outputs/nodes_os_x43kj56.txt
```

#### Clarification

생성된 Pod 확인

```Bash
controlplane ~ ➜  cat /opt/outputs/nodes_os_x43kj56.txt
Ubuntu 22.04.4 LTS
```
