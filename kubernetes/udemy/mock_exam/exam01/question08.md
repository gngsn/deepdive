### Q8. Create a POD in the `finance` namespace named `temp-bus` with the image `redis:alpine`.

<br>

#### Answer

```Bash
controlplane ~ ➜ k run temp-bus -n finance --image redis:alpine
pod/temp-bus created
```

#### Clarification

생성된 Pod 확인

```Bash
controlplane ~ ➜  k describe po -n finance
Name:             temp-bus
Namespace:        finance
...
Containers:
  temp-bus:
    Image:          redis:alpine
    ...
```
