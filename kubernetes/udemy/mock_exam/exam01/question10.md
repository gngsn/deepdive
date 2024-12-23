### Q10. Expose the `hr-web-app` created in the previous task as a service named `hr-web-app-service`, accessible on port `30082` on the nodes of the cluster.

The web application listens on port `8080`.

- Name: hr-web-app-service
- Type: NodePort
- Endpoints: 2
- Port: 8080
- NodePort: 30082

<br>

#### Answer

1\. `kubectl expose` 명령어로 NodePort Service 객체 생성

```Bash
controlplane ~ ➜  kubectl expose deployment hr-web-app --port=8080 --name=hr-web-app-service --type=NodePort
service/hr-web-app-service exposed
```

2\. 생성된 Service 객체 수정

```Bash
controlplane ~ ➜  kubectl edit svc hr-web-app-service 
service/hr-web-app-service edited
```

3\. 랜덤으로 할당된 NodePort를 조건에 맞게 `30082` 으로 수정

```Bash
apiVersion: v1
kind: Service
metadata:
  labels:
    app: hr-web-app
  name: hr-web-app
  ...
spec:
  type: NodePort
  ports:
  - **nodePort: 30082**     ← 수정
    port: 8080
    protocol: TCP
    targetPort: 8080
  selector:
    app: hr-web-app
  ...
```

#### Clarification

수정된 Service 반영 확인

```Bash  
controlplane ~ ➜  k get svc -owide
NAME                 TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE   SELECTOR
hr-web-app-service   NodePort    172.20.163.69   <none>        8080:30082/TCP   28s   app=hr-web-app
...
```

1. Type이 NodePort 이고, 
2. Port 80, NodePort 30082 로 매핑되어 있으며,
3. hr-web-app label 과 동일한 `app=hr-web-app` 적용 확인