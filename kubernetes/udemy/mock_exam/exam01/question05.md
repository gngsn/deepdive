### Q5. Create a service `messaging-service` to expose the `messaging` application within the cluster on port `6379`.


Use imperative commands.

- Service: messaging-service
- Port: 6379
- Type: ClusterIp
- Use the right labels

<br>

#### Answer

```Bash
controlplane ~ ✖ k expose pod  messaging --port 6379 --name=messaging-service
service/messaging-service exposed

controlplane ~ ➜  k get svc messaging-service -oyaml
apiVersion: v1
kind: Service
metadata:
  creationTimestamp: "2024-12-21T12:20:17Z"
  labels:
    tier: msg
  name: messaging-service
  namespace: default
  resourceVersion: "4934"
  uid: c031b262-fbdf-4bc8-94aa-2f8231f532eb
spec:
  clusterIP: 172.20.37.49
  clusterIPs:
  - 172.20.37.49
  internalTrafficPolicy: Cluster
  ipFamilies:
  - IPv4
  ipFamilyPolicy: SingleStack
  ports:
  - port: 6379
    protocol: TCP
    targetPort: 6379
  selector:
    tier: msg
  sessionAffinity: None
  type: ClusterIP
status:
  loadBalancer: {}
```

#### Clarification

생성된 파일 확인

```Bash
controlplane ~ ➜  k describe svc messaging-service 
Name:                     messaging-service
Namespace:                default
Labels:                   tier=msg
Annotations:              <none>
Selector:                 tier=msg
Type:                     ClusterIP
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       172.20.37.49
IPs:                      172.20.37.49
Port:                     <unset>  6379/TCP
TargetPort:               6379/TCP
Endpoints:                172.17.0.4:6379
Session Affinity:         None
Internal Traffic Policy:  Cluster
Events:                   <none>

controlplane ~ ✖ k run lookup -it --rm --restart=Never --image busybox -- wget -q 172.17.0.5:6379
wget: error getting response
pod "lookup" deleted
pod default/lookup terminated (Error)

controlplane ~ ✖ k run lookup -it --rm --restart=Never --image busybox -- wget -q 172.17.0.5:6380
wget: can't connect to remote host (172.17.0.5): Connection refused
pod "lookup" deleted
pod default/lookup terminated (Error)
```
