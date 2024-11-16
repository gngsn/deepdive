## Question 04.

Create a new deployment called `nginx-deploy`, with image `nginx:1.16` and `1` replica. 
Next upgrade the deployment to version `1.17` using `rolling update`.



### Answer

```Bash
controlplane ~ ✖ kubectl create deployment  nginx-deploy --image=nginx:1.16
deployment.apps/nginx-deploy created

controlplane ~ ➜  k get deploy
NAME           READY   UP-TO-DATE   AVAILABLE   AGE
gold-nginx     1/1     1            1           9m37s
nginx-deploy   1/1     1            1           11s

controlplane ~ ➜  kubectl set image deployment/nginx-deploy nginx=nginx:1.17 --record
Flag --record has been deprecated, --record will be removed in the future
deployment.apps/nginx-deploy image updated

controlplane ~ ➜  k get deploy -o wide
NAME           READY   UP-TO-DATE   AVAILABLE   AGE     CONTAINERS   IMAGES         SELECTOR
gold-nginx     1/1     1            1           9m53s   nginx        nginx:latest   app=gold-nginx
nginx-deploy   1/1     1            1           27s     nginx        nginx:1.17     app=nginx-deploy
```
