## Question 3.
### Create a pod called `multi-pod` with two containers.

Container 1: name: `alpha`, image: `nginx`
Container 2: name: `beta`, image: `busybox`, command: `sleep 4800`

Environment Variables:
container 1:
`name: alpha`

Container 2:
`name: beta`

<br>

### Solution

```Bash
apiVersion: v1
kind: Pod
metadata:
name: multi-pod
spec:
containers:
- image: nginx
  name: alpha
  env:
  - name: name
    value: alpha
- image: busybox
  name: beta
  command: ["sleep", "4800"]
  env:
  - name: name
    value: beta
```

### Check

```Bash
controlplane ~ ✖ k describe pods multi-pod 
Name:             multi-pod
Namespace:        default
...
Containers:
  alpha:
    Image:          nginx
    Environment:
      name:  alpha
    ...
  beta:
    Image:         busybox
    Command:
      sleep
      4800
    Environment:
      name:  beta
  ...
```

