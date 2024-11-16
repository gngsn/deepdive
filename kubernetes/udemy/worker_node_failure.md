# Worker Node Failure

Node 의 상태를 확인

```Bash
❯ kubectl get nodes
NAME        STATUS      ROLES   AGE     VERSION
worker-1    Ready       <none>  8d      v1.13.0
worker-2    NotReady    <none>  8d      v1.13.0
```

위 `NotReady` 상태 처럼 비정상 적일 때 원인을 찾아야 함

먼저, 

```Bash
❯ kubectl describe node worker-1
Conditions:
Type            Status  LastHeartbeatTime                   Reason                      Message
----            ------  -----------------                   ------                      -------
OutOfDisk       False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientDisk    kubelet has sufficient disk space available
MemoryPressure  False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientMemory  kubelet has sufficient memory available
DiskPressure    False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasNoDiskPressure    kubelet has no disk pressure
PIDPressure     False   Mon, 01 Apr 2019 14:30:33 +0000     KubeletHasSufficientPID     kubelet has sufficient PID available
Ready           True    Mon, 01 Apr 2019 14:30:33 +0000     KubeletReady                kubelet is posting ready status. AppArmor enabled
```

Status 는 'True', 'False' 혹은 'Unknown' 으로 표시될 수 있음

### Check kubelet status

먼저 kubelet 의 상태를 확인해보고, 

```
❯ service kubelet status
```

만약, kubelet 자체가 문제가 된다면,
journalctl 명령어를 통해 문제를 살펴봐야함

```Bash
❯ sudo journalctl -u kubelet
```

<br>

### Check Certificates

```Bash
❯ openssl x509 -in /var/lib/kubelet/worker-1.crt -text
Certificate:
Data:
Version: 3 (0x2)
Serial Number:
ff:e0:23:9d:fc:78:03:35
Signature Algorithm: sha256WithRSAEncryption
Issuer: CN = KUBERNETES-CA
Validity
Not Before: Mar 20 08:09:29 2019 GMT
Not After : Apr 19 08:09:29 2019 GMT
Subject: CN = system:node:worker-1, O = system:nodes
Subject Public Key Info:
Public Key Algorithm: rsaEncryption
Public-Key: (2048 bit)
Modulus:
00:b4:28:0c:60:71:41:06:14:46:d9:97:58:2d:fe:
a9:c7:6d:51:cd:1c:98:b9:5e:e6:e4:02:d3:e3:71:
58:a1:60:fe:cb:e7:9b:4b:86:04:67:b5:4f:da:d6:
6c:08:3f:57:e9:70:59:57:48:6a:ce:e5:d4:f3:6e:
b2:fa:8a:18:7e:21:60:35:8f:44:f7:a9:39:57:16:
4f:4e:1e:b1:a3:77:32:c2:ef:d1:38:b4:82:20:8f:
11:0e:79:c4:d1:9b:f6:82:c4:08:84:84:68:d5:c3:
e2:15:a0:ce:23:3c:8d:9c:b8:dd:fc:3a:cd:42:ae:
5e:1b:80:2d:1b:e5:5d:1b:c1:fb:be:a3:9e:82:ff:
a1:27:c8:b6:0f:3c:cb:11:f9:1a:9b:d2:39:92:0e:
47:45:b8:8f:98:13:c6:4d:6a:18:75:a4:01:6f:73:
f6:f8:7f:eb:5d:59:94:46:d8:da:37:75:cf:27:0b:
39:7f:48:20:c5:fd:c7:a7:ce:22:9a:33:4a:30:1d:
95:ef:00:bd:fe:47:22:42:44:99:77:5a:c4:97:bb:
37:93:7c:33:64:f4:b8:3a:53:8c:f4:10:db:7f:5f:
2b:89:18:d6:0e:68:51:34:29:b1:f1:61:6b:4b:c6
```
