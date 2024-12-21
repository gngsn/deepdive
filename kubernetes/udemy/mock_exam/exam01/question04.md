### Q4. Get the list of nodes in JSON format and store it in a file at `/opt/outputs/nodes-z3444kd9.json`.

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