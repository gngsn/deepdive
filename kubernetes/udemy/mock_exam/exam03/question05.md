## Question 5. 

> ### We have deployed a new pod called `np-test-1` and a service called `np-test-service`. Incoming connections to this service are not working. Troubleshoot and fix it. Create NetworkPolicy, by the name `ingress-to-nptest` that allows incoming connections to the service over port `80`.
>
> Important: Don't delete any current objects deployed.

<br>

#### Solution

[Network Policies](https://kubernetes.io/docs/concepts/services-networking/network-policies/)

```Bash
controlplane ~ ➜  vi ingress-to-nptest.yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: ingress-to-nptest
spec:
  podSelector:
    matchLabels:
      run: np-test-1
  ingress:
  - from:
    ports:
    - protocol: TCP
      port: 80
  policyTypes:
  - Ingress

controlplane ~ ➜  k apply -f ingress-to-nptest.yaml 
networkpolicy.networking.k8s.io/allow-all-ingress created
```

### Check

```Bash
controlplane ~ ➜  k describe networkpolicy ingress-to-nptest
Name:         ingress-to-nptest
Namespace:    default
Created on:   2024-07-28 10:03:17 +0000 UTC
Labels:       <none>
Annotations:  <none>
Spec:
  PodSelector:     run=np-test-1
  Allowing ingress traffic:
    To Port: 80/TCP
    From: <any> (traffic not restricted by source)
  Not affecting egress traffic
  Policy Types: Ingress
```

### Ensure to allow ingress from `80` port


```Bash
k run curl --image=busybox --rm --restart=Never -it -- wget -qO- 10.244.192.1:80
<!DOCTYPE html>
<html>
...
<p><em>Thank you for using nginx.</em></p>
</body>
</html>
pod "curl" deleted
```

