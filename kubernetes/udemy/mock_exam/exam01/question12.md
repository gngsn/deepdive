### Q10. Expose the `hr-web-app` created in the previous task as a service named `hr-web-app-service`, accessible on port `30082` on the nodes of the cluster.

The web application listens on port `8080`.

- Name: hr-web-app-service
- Type: NodePort
- Endpoints: 2
- Port: 8080
- NodePort: 30082

<br>

#### Answer

```Bash
controlplane ~ ➜  cat pv-analytics.yaml
apiVersion: v1
kind: PersistentVolume
metadata:
name: pv-analytics
spec:
capacity:
storage: 100Mi
accessModes:
- ReadWriteMany
hostPath:
path: /pv/data-analytics
controlplane ~ ➜  k apply -f pv-analytics.yaml
persistentvolume/pv-analytics created

controlplane ~ ➜  k get pv
NAME           CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM   STORAGECLASS   VOLUMEATTRIBUTESCLASS   REASON   AGE
pv-analytics   100Mi      RWX            Retain           Available                          <unset>                          5s
```
