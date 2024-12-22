## Question 02.

Print the names of all deployments in the admin2406 namespace in the following format:

```
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
```

`<deployment name>   <container image used>   <ready replica count>   <Namespace>`. 
The data should be sorted by the increasing order of the `deployment name`.

Example:

```
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
deploy0   nginx:alpine   1   admin2406
```
Write the result to the file `/opt/admin2406_data`.


### Answer

```Bash
controlplane ~ ➜  cat column-template.txt 
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
.metadata.name  .spec.template.spec.containers[*].image .status.readyReplicas .metadata.namespace

controlplane ~ ➜  kubectl get deploy -o=jsonpath='{range .items[*]}{.metadata.name}{"\t"} {range .spec.template.spec.containers[*]} {.image}{"\n"} {end} {.status.readyReplicas}{"\t"} {.metadata.namespace}{"\t"} {end}' \
    -o custom-columns-file=column-template.txt \
    --sort-by=.metadata.name
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
deploy1      nginx             1                admin2406
deploy2      nginx:alpine      1                admin2406
deploy3      nginx:1.16        1                admin2406
deploy4      nginx:1.17        1                admin2406
deploy5      nginx:latest      1                admin2406
```

```
controlplane ~ ➜ kubectl -n admin2406 get deployment -o custom-columns=DEPLOYMENT:.metadata.name,CONTAINER_IMAGE:.spec.template.spec.containers[].image,READY_REPLICAS:.status.readyReplicas,NAMESPACE:.metadata.namespace --sort-by=.metadata.name > /opt/admin2406_data
```


```
controlplane ~ ➜  k get deploy -ojson | jq -c 'paths' | grep 'image\|replica\|namespace' -i
["items",0,"metadata","namespace"]
["items",0,"spec","replicas"]
["items",0,"spec","template","spec","containers",0,"image"]
["items",0,"spec","template","spec","containers",0,"imagePullPolicy"]
["items",0,"status","availableReplicas"]
["items",0,"status","readyReplicas"]
["items",0,"status","replicas"]
["items",0,"status","updatedReplicas"]

controlplane ~ ➜  cat template.txt 
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
metadata.name spec.template.spec.containers[*].image  spec.readyReplicas metadata.namespace 

controlplane ~ ➜  k get deploy --sort-by=.metadata.name -o custom-columns-file=template.txt > /opt/admin2406_data

controlplane ~ ➜  cat /opt/admin2406_data
DEPLOYMENT   CONTAINER_IMAGE   READY_REPLICAS   NAMESPACE
gold-nginx   nginx:latest      1                default
```
