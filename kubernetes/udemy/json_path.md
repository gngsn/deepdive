# JSON path

쿠버네티스 프로덕션 환경으로 작업할 때,
Deployment, Pod, ReplicaSet, Service, Secret 등 수백 개의 노드와 수천 개의 개체에 대한 정보를 봐야 함

`Kubectl` utility 를 이용해 이 개체에 대한 정보를 봐야 함

종종 **모든 리소스**의 특정 필드를 보고 싶다는 요구 사항이 있을 수 있음 

수 천개의 리소스에서 해당 데이터를 조회하는 건 까다로움

→ 그래서 Kubectl 는 JSONpath 옵션을 지원

복잡한 조건을 Kubectl를 사용해 큰 데이터셋을 통한 데이터 필터링을 쉬운 작업으로 만드는 것

<br>

### How does Kubectl utility work?

kubectl 는 Kubernetes 의 객체를 읽고 쓰는 데 사용됨

Kubectl 명령 실행마다 Kube API 서버와의 통신을 통해 Kubernetes와 상호 작용함

kube-api-server 는 JSON 를 사용함

그래서 JSON 포맷으로 요청된 정보를 반환

JSON 포맷으로 정보를 받는 kubectl utility 는 사람이 읽기 쉽도록 테이블 형식으로 변환하여 출력

이 때, kube-api-server 에서 받아온 JSON 응답에는 많은 정보가 포함되어 있지만, 가독성을 위해 필요한 세부 사항만 출력

추가적인 세부 사항은 `Kubectl get` 명령과 함께 `-o wide` 옵션 사용

<br>

```
❯ kubectl get all -o wide -A
NAMESPACE            NAME                                             READY   STATUS             RESTARTS         AGE   IP           NODE                 NOMINATED NODE   READINESS GATES
kube-system          pod/coredns-76f75df574-l6tsh                     1/1     Running            2 (8d ago)       70d   10.244.0.4   kind-control-plane   <none>           <none>
kube-system          pod/coredns-76f75df574-s787j                     1/1     Running            2 (8d ago)       70d   10.244.0.2   kind-control-plane   <none>           <none>
kube-system          pod/weave-net-mvjxx                              1/2     CrashLoopBackOff   1858 (73s ago)   42d   172.21.0.2   kind-control-plane   <none>           <none>
...

NAMESPACE     NAME                 TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)                  AGE   SELECTOR
default       service/kubernetes   ClusterIP   10.96.0.1    <none>        443/TCP                  70d   <none>
kube-system   service/kube-dns     ClusterIP   10.96.0.10   <none>        53/UDP,53/TCP,9153/TCP   70d   k8s-app=kube-dns

NAMESPACE     NAME                        DESIRED   CURRENT   READY   UP-TO-DATE   AVAILABLE   NODE SELECTOR            AGE   CONTAINERS        IMAGES                                                     SELECTOR
kube-system   daemonset.apps/kindnet      1         1         1       1            1           kubernetes.io/os=linux   70d   kindnet-cni       docker.io/kindest/kindnetd:v20240202-8f1494ea              app=kindnet
kube-system   daemonset.apps/kube-proxy   1         1         1       1            1           kubernetes.io/os=linux   70d   kube-proxy        registry.k8s.io/kube-proxy:v1.29.2                         k8s-app=kube-proxy
...
```

<br>

가령, 특정 노드에서 사용 가능한 리소스 용량 노드에 설정된 테인트 조건이나 이미지 등, 
추가 내용을 출력하지만 다 보여주는 건 아님 

더 자세한 내용을 보려면 `kubectl describe` 를 통해 확인할 수 있음

하지만, 여러 노드에서는 불가능

이 때, JSON path 쿼리를 이용하면 명령의 출력을 필터링하고 포맷할 수 있음

<br>

### How to JSON path in kubectl

kubectl 의 JSON path 를 사용하기 위해서 아래 네 가지 단계가 필요

1. Identify the **kubectl** command
   - kubectl 명령어를 알아야 함
2. Familiarize with **JSON** output
   - `-o json` 옵션을 통해 JSON Format 출력을 확인
3. Form the **JSON Path** query
   - `.items[0].spec.containers[0].image`
   - `$` 표시가 필수는 아님 kubectl 이 추가해줌
4. Use the **JSON Path** query with **kubectl** command
   - `-o=jsonpath='{ ... }'` 옵션 사용해서 쿼리
   - `-o=jsonpath='{ .items[0].spec.containers[0].image }'`
   - 반드시 `'{ ... }'` 내에 Json Query를 작성해야 함

<br>

```Bash
❯ kubectl get nodes -o=jsonpath='{.items[*].metadata.name}' -A
kind-control-plane

❯ kubectl get nodes -o=jsonpath='{.items[*].status.nodeInfo.architecture}'
arm64

❯ kubectl get nodes -o=jsonpath='{.items[*].status.capacity.cpu}'
10
```

두 개의 쿼리를 한 번에 질의하고 싶다면 아래와 같이 붙여서 질의 하면 됨

```
❯ kubectl get nodes -o=jsonpath='{.items[*].metadata.name}{.items[*].status.capacity.cpu}'
kind-control-plane10
```

하지만 위 처럼 질의하면 보기가 힘듦

`{"\n"}` 혹은 `{"\t"}` 등을 사용해서 포맷을 설정할 수 있음

```
❯ kubectl get nodes -o=jsonpath='{.items[*].metadata.name}{"\n"}{.items[*].status.capacity.cpu}'
kind-control-plane
10
```

### Loops - Range

원하는 출력 방식은 아래와 같음

```Bash
# as is
master  node01
4       4

# to be
master  4
node01  4
```

이를 위해서 Loop 절이 필요

<table><tr>
<td><pre>FOR EACH NODE
    PRINT NODE NAME \t PRINT CPU COUNT \n 
END FOR</pre></td>
<td><pre>{range .items[*]}
    {.metadata.name}{"\t"}{.status.capacity.cpu}{"\n"} 
{end}</pre></td>
</tr>
<tr>
<td colspan="2">
<pre><code>❯ kubectl get nodes -o=jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.capacity.cpu}{"\n"}{end}'</code></pre>
</td></tr></table>

<br>

### JSON path for Custom Column Names

혹은 Column 명을 지정할 수도 있음

```Bash
❯ kubectl get nodes -o=custom-columns=<COLUMN NAME>:<JSON PATH>
```

가령 예를 들면,

```Bash
❯ kubectl get nodes -o=jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.status.capacity.cpu}{"\n"}{end}' -o=custom-columns=NODE:.metadata.name,CPU:.statis.capacity.cpu
NODE                 CPU
kind-control-plane   <none>
```

<br>

### JSON path for Sort

출력 시 특정 값을 기준으로 정렬을 할 수도 있는데, JSON Path 에서 명시했던 경로를 입력하면 됨

가령, Metadata 의 name 으로 정렬하고자 한다면 아래와 같이 작성할 수 있음

```Bash
❯ kubectl get nodes --sort-by=.metadata.name
NAME     STATUS   ROLES    AGE   VERSION
master   Ready    master   5m    v1.11.3
node01   Ready    <none>   5m    v1.11.3
```

혹은, CPU 의 capacity 으로 정렬하려면 아래와 같이 작성할 수 있음

```Bash
❯ kubectl get nodes --sort-by=.status.capacity.cpu
NAME     STATUS   ROLES    AGE   VERSION
master   Ready    master   5m    v1.11.3
node01   Ready    <none>   5m    v1.11.3
```

---

## Practice

### Question 1.

Get the list of nodes in JSON format and store it in a file at `/opt/outputs/nodes.json`

#### Answer.

```Bash
controlplane ~ ➜  k get nodes -o json > /opt/outputs/nodes.json
{
    "apiVersion": "v1",
    "items": [
        {
            "apiVersion": "v1",
            "kind": "Node",
            "metadata": {
            ...
            }
        }
    ]
}
```

<br>

### Question 2.

Get the details of the node node01 in json format and store it in the file `/opt/outputs/node01.json`

#### Answer.

```Bash
controlplane ~ ➜  k get nodes -o json > /opt/outputs/nodes.json
{
    "apiVersion": "v1",
    "kind": "Node",
    "metadata": {
        "annotations": {
            "flannel.alpha.coreos.com/backend-data": "{\"VNI\":1,\"VtepMAC\":\"b2:e0:b0:6d:27:53\"}",
            ...
        }
    }
}
```

<br>

### Question 3.

Use JSON PATH query to fetch node names and store them in `/opt/outputs/node_names.txt`.

<small>Remember the file should only have node names.</small>

#### Answer.

```Bash
controlplane ~ ➜  k get node -o=jsonpath='{.items[*].metadata.name}' > /opt/outputs/node_names.txt
controlplane node01
```

<br>

### Question 4.

Use JSON PATH query to retrieve the `osImage`s of all the nodes and store it in a file `/opt/outputs/nodes_os.txt`.

<small>The `osImage`s are under the `nodeInfo` section under `status` of each node.</small>

#### Answer.

<pre><code lang="bash">controlplane ~ ✖ k get node -o json
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
            "spec": {
                ...
            },
            "status": {
                ...
                "nodeInfo": {
                    "architecture": "amd64",
                    "bootID": "04244f48-6655-41d9-8a84-2ae042385286",
                    "containerRuntimeVersion": "containerd://1.6.26",
                    "kernelVersion": "5.4.0-1106-gcp",
                    "kubeProxyVersion": "v1.30.0",
                    "kubeletVersion": "v1.30.0",
                    "machineID": "19d93cf879df4a7dbff7fb9eabd1279f",
                    "operatingSystem": "linux",
                    <b>"osImage": "Ubuntu 22.04.4 LTS",</b>
                    "systemUUID": "aac6731f-b99d-f878-0d80-8d7ce835acab"
                }
            }
        },
        {
            "apiVersion": "v1",
            "kind": "Node",
            "metadata": {
                "name": "node01",
                ...
            },
            "spec": {
                ...
            },
            "status": {
                ...
                "nodeInfo": {
                    "architecture": "amd64",
                    "bootID": "72a6c803-18e2-4db2-9109-fe56180d567a",
                    "containerRuntimeVersion": "containerd://1.6.26",
                    "kernelVersion": "5.4.0-1106-gcp",
                    "kubeProxyVersion": "v1.30.0",
                    "kubeletVersion": "v1.30.0",
                    "machineID": "7069a4aa6b534f58bf55a7749d795fd3",
                    "operatingSystem": "linux",
                    <b>"osImage": "Ubuntu 22.04.4 LTS",</b>
                    "systemUUID": "82ad48cc-cf0c-c0bf-7b55-386f5983f9a3"
                }
            }
        }
    ]
}

controlplane ~ ➜  k get node -o=jsonpath='{.items[*].status.nodeInfo.osImage}' > /opt/outputs/nodes_os.txt
Ubuntu 22.04.4 LTS Ubuntu 22.04.4 LTS
</code></pre>

<br>

### Question 5.

A `kube-config` file is present at `/root/my-kube-config`. Get the user names from it and store it in a file `/opt/outputs/users.txt`.

<small>Use the command `kubectl config view --kubeconfig=/root/my-kube-config` to view the custom kube-config.</small>

#### Answer.

```Bash
controlplane ~ ➜  kubectl config view --kubeconfig=/root/my-kube-config -o json
{
    "kind": "Config",
    "apiVersion": "v1",
    "preferences": {},
    "clusters": [
        {
            "name": "development",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "kubernetes-on-aws",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "production",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "test-cluster-1",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        }
    ],
    "users": [
        {
            "name": "aws-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/aws-user/aws-user.crt",
                "client-key": "/etc/kubernetes/pki/users/aws-user/aws-user.key"
            }
        },
        {
            "name": "dev-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/dev-user/developer-user.crt",
                "client-key": "/etc/kubernetes/pki/users/dev-user/dev-user.key"
            }
        },
        {
            "name": "test-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/test-user/test-user.crt",
                "client-key": "/etc/kubernetes/pki/users/test-user/test-user.key"
            }
        }
    ],
    "contexts": [
        {
            "name": "aws-user@kubernetes-on-aws",
            "context": {
                "cluster": "kubernetes-on-aws",
                "user": "aws-user"
            }
        },
        {
            "name": "research",
            "context": {
                "cluster": "test-cluster-1",
                "user": "dev-user"
            }
        },
        {
            "name": "test-user@development",
            "context": {
                "cluster": "development",
                "user": "test-user"
            }
        },
        {
            "name": "test-user@production",
            "context": {
                "cluster": "production",
                "user": "test-user"
            }
        }
    ],
    "current-context": "test-user@development"
}

controlplane ~ ➜  kubectl config view --kubeconfig=/root/my-kube-config -o=jsonpath='{.users[*].name}' > /opt/outputs/users.txt
aws-user dev-user test-user
```

<br>

### Question 6.

A set of Persistent Volumes are available. Sort them based on their capacity and store the result in the file `/opt/outputs/storage-capacity-sorted.txt`.

#### Answer.

```Bash
ontrolplane ~ ➜  k get pv -o json
{
    "apiVersion": "v1",
    "items": [
        {
            "apiVersion": "v1",
            "kind": "PersistentVolume",
            "metadata": {
                "creationTimestamp": "2024-07-21T07:25:30Z",
                "finalizers": [
                    "kubernetes.io/pv-protection"
                ],
                "name": "pv-log-1",
                "resourceVersion": "2072",
                "uid": "0278ae3a-8bc4-4598-bed5-ca75929755f1"
            },
            "spec": {
                "accessModes": [
                    "ReadWriteMany"
                ],
                "capacity": {
                    "storage": "100Mi"
                },
                "hostPath": {
                    "path": "/pv/log",
                    "type": ""
                },
                "persistentVolumeReclaimPolicy": "Retain",
                "volumeMode": "Filesystem"
            },
            "status": {
                "lastPhaseTransitionTime": "2024-07-21T07:25:30Z",
                "phase": "Available"
            }
        },
        {
            "apiVersion": "v1",
            "kind": "PersistentVolume",
            "metadata": {
                "creationTimestamp": "2024-07-21T07:25:30Z",
                "finalizers": [
                    "kubernetes.io/pv-protection"
                ],
                "name": "pv-log-2",
                "resourceVersion": "2074",
                "uid": "00f2f546-7dd4-44b5-82c5-50781daebbe0"
            },
            "spec": {
                "accessModes": [
                    "ReadWriteMany"
                ],
                "capacity": {
                    "storage": "200Mi"
                },
                "hostPath": {
                    "path": "/pv/log",
                    "type": ""
                },
                "persistentVolumeReclaimPolicy": "Retain",
                "volumeMode": "Filesystem"
            },
            "status": {
                "lastPhaseTransitionTime": "2024-07-21T07:25:30Z",
                "phase": "Available"
            }
        },
        {
            "apiVersion": "v1",
            "kind": "PersistentVolume",
            "metadata": {
                "creationTimestamp": "2024-07-21T07:25:30Z",
                "finalizers": [
                    "kubernetes.io/pv-protection"
                ],
                "name": "pv-log-3",
                "resourceVersion": "2076",
                "uid": "9e8c119c-9759-45d3-a382-3ad07dbba9aa"
            },
            "spec": {
                "accessModes": [
                    "ReadWriteMany"
                ],
                "capacity": {
                    "storage": "300Mi"
                },
                "hostPath": {
                    "path": "/pv/log",
                    "type": ""
                },
                "persistentVolumeReclaimPolicy": "Retain",
                "volumeMode": "Filesystem"
            },
            "status": {
                "lastPhaseTransitionTime": "2024-07-21T07:25:30Z",
                "phase": "Available"
            }
        },
        {
            "apiVersion": "v1",
            "kind": "PersistentVolume",
            "metadata": {
                "creationTimestamp": "2024-07-21T07:25:30Z",
                "finalizers": [
                    "kubernetes.io/pv-protection"
                ],
                "name": "pv-log-4",
                "resourceVersion": "2078",
                "uid": "52cc21c4-b90f-446c-b83c-4cfc7be4046e"
            },
            "spec": {
                "accessModes": [
                    "ReadWriteMany"
                ],
                "capacity": {
                    "storage": "40Mi"
                },
                "hostPath": {
                    "path": "/pv/log",
                    "type": ""
                },
                "persistentVolumeReclaimPolicy": "Retain",
                "volumeMode": "Filesystem"
            },
            "status": {
                "lastPhaseTransitionTime": "2024-07-21T07:25:30Z",
                "phase": "Available"
            }
        }
    ],
    "kind": "List",
    "metadata": {
        "resourceVersion": ""
    }
}

controlplane ~ ➜  k get pv --sort-by=.spec.capacity.storage > /opt/outputs/storage-capacity-sorted.txt
NAME       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM   STORAGECLASS   VOLUMEATTRIBUTESCLASS   REASON   AGE
pv-log-4   40Mi       RWX            Retain           Available                          <unset>                          14m
pv-log-1   100Mi      RWX            Retain           Available                          <unset>                          14m
pv-log-2   200Mi      RWX            Retain           Available                          <unset>                          14m
pv-log-3   300Mi      RWX            Retain           Available                          <unset>                          14m
```

<br>

### Question 7. 

That was good, but we don't need all the extra details. Retrieve just the first 2 columns of output and store it in `/opt/outputs/pv-and-capacity-sorted.txt`.

<small>The columns should be named NAME and CAPACITY. Use the custom-columns option and remember, it should still be sorted as in the previous question.</small>

#### Answer.

```Bash
❯ kubectl get pv -o=jsonpath='{range .items[*]}{.metadata.name}{"\t"}{.spec.capacity.storage}{"\n"}{end}' -o=custom-columns=NAME:.metadata.name,CAPACITY:.spec.capacity.storage --sort-by=.spec.capacity.storage
NAME       CAPACITY
pv-log-4   40Mi
pv-log-1   100Mi
pv-log-2   200Mi
pv-log-3   300Mi
```

<br>

### Question 8. 

Use a JSON PATH query to identify the context configured for the `aws-user` in the `my-kube-config` context file and store the result in `/opt/outputs/aws-context-name`.

#### Answer.

```Bash
controlplane ~ ➜  k config view --kubeconfig=my-kube-config -o json
{
    "kind": "Config",
    "apiVersion": "v1",
    "preferences": {},
    "clusters": [
        {
            "name": "development",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "kubernetes-on-aws",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "production",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        },
        {
            "name": "test-cluster-1",
            "cluster": {
                "server": "KUBE_ADDRESS",
                "certificate-authority": "/etc/kubernetes/pki/ca.crt"
            }
        }
    ],
    "users": [
        {
            "name": "aws-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/aws-user/aws-user.crt",
                "client-key": "/etc/kubernetes/pki/users/aws-user/aws-user.key"
            }
        },
        {
            "name": "dev-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/dev-user/developer-user.crt",
                "client-key": "/etc/kubernetes/pki/users/dev-user/dev-user.key"
            }
        },
        {
            "name": "test-user",
            "user": {
                "client-certificate": "/etc/kubernetes/pki/users/test-user/test-user.crt",
                "client-key": "/etc/kubernetes/pki/users/test-user/test-user.key"
            }
        }
    ],
    "contexts": [
        {
            "name": "aws-user@kubernetes-on-aws",
            "context": {
                "cluster": "kubernetes-on-aws",
                "user": "aws-user"
            }
        },
        {
            "name": "research",
            "context": {
                "cluster": "test-cluster-1",
                "user": "dev-user"
            }
        },
        {
            "name": "test-user@development",
            "context": {
                "cluster": "development",
                "user": "test-user"
            }
        },
        {
            "name": "test-user@production",
            "context": {
                "cluster": "production",
                "user": "test-user"
            }
        }
    ],
    "current-context": "test-user@development"
}

ontrolplane ~ ➜  k config view --kubeconfig=my-kube-config -o=jsonpath='{.contexts[?(@.context.user=="aws-user")].name}' > /opt/outputs/aws-context-name
aws-user@kubernetes-on-aws
```

