### Q6. Create a deployment named hr-web-app using the image kodekloud/webapp-color with 2 replicas.

<br>

#### Answer

```Bash
controlplane ~ ➜  k create deploy hr-web-app --image kodekloud/webapp-color --replicas=2
deployment.apps/hr-web-app created
```


#### Clarification

```Bash
controlplane ~ ➜  k get deploy -owide
NAME         READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS     IMAGES                   SELECTOR
hr-web-app   2/2     2            2           54s   webapp-color   kodekloud/webapp-color   app=hr-web-app
```
