## Question 2.

> ### List the `InternalIP` of all nodes of the cluster. Save the result to a file `/root/CKA/node_ips`. Solution should be in the format: `InternalIP of controlplane<space>InternalIP of node01` (in a single line)

<br>

### Solution

[🔗 JSONPath Support](https://kubernetes.io/docs/reference/kubectl/jsonpath/) 참고

```Bash
controlplane ~ ➜  kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="InternalIP")].address}' 
192.12.122.12 192.12.122.3

controlplane ~ ➜  kubectl get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="InternalIP")].address}' > /root/CKA/node_ips
```

<br>

### Tips.

#### Tip 1. More 페이지로 보여주는 옵션

```
➜ kubectl get nodes -o json | jq | more
```

<br>

#### Tip 2. `jq` 에서 `paths` 만을 보여주는 옵션

```
controlplane ~ ➜  kubectl get nodes -o json | jq -c 'paths'
["apiVersion"]
["items"]
["items",0]
["items",0,"apiVersion"]
["items",0,"kind"]
["items",0,"metadata"]
["items",0,"metadata","annotations"]
...
["items",0,"metadata","annotations","volumes.kubernetes.io/controller-managed-attach-detach"]
["items",0,"metadata","labels"]
...
["items",0,"status","images"]
["items",0,"status","images",0]
["items",0,"status","images",0,"names"]
["items",0,"status","images",0,"names",0]
["items",0,"status","images",0,"names",1]
["items",0,"status","images",0,"sizeBytes"]
["items",0,"status","images",1]
["items",0,"status","images",1,"names"]
["items",0,"status","images",1,"names",0]
["items",0,"status","images",1,"names",1]
```

**`jq` 에서 `paths` 만을 보여주는 옵션 + 특정 키워드 찾기/제외하기**

```
# 특정 키워드 찾기
➜ kubectl get nodes -o json | jq -c 'paths' | grep type

# 특정 키워드 제외하기
➜ kubectl get nodes -o json | jq -c 'paths' | grep type | grep -v condition
```

`-v`: Display only lines that do not match the search pattern.
`-x`: Match only whole lines.

