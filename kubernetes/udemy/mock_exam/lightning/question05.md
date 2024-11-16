## Question 05.

A new deployment called `alpha-mysql` has been deployed in the alpha namespace. 
However, the pods are not running. Troubleshoot and fix the issue. 
The deployment should make use of the persistent volume `alpha-pv` to be mounted at `/var/lib/mysql`
and should use the environment variable `MYSQL_ALLOW_EMPTY_PASSWORD=1` to make use of an empty root password.

<small>Important: Do not alter the persistent volume.</small>

### Answer


```Bash
controlplane ~ ➜  k get deploy alpha-mysql -n alpha -o json
{
    "apiVersion": "apps/v1",
    "kind": "Deployment",
    "metadata": {
        "labels": {
            "app": "alpha-mysql"
        },
        "name": "alpha-mysql",
        "namespace": "alpha",
    },
    "spec": {
        "replicas": 1,
        "revisionHistoryLimit": 10,
        "selector": {
            "matchLabels": {
                "app": "alpha-mysql"
            }
        },
        "strategy": {
            "rollingUpdate": {
                "maxSurge": "25%",
                "maxUnavailable": "25%"
            },
            "type": "RollingUpdate"
        },
        "template": {
            "metadata": {
                "creationTimestamp": null,
                "labels": {
                    "app": "alpha-mysql"
                }
            },
            "spec": {
                "containers": [
                    {
                        "env": [
                            {
                                "name": "MYSQL_ALLOW_EMPTY_PASSWORD",
                                "value": "1"
                            }
                        ],
                        "image": "mysql:5.6",
                        "imagePullPolicy": "Always",
                        "name": "mysql",
                        "ports": [
                            {
                                "containerPort": 3306,
                                "protocol": "TCP"
                            }
                        ],
                        "resources": {},
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File",
                        "volumeMounts": [
                            {
                                "mountPath": "/var/lib/mysql",
                                "name": "mysql-data"
                            }
                        ]
                    }
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": {},
                "terminationGracePeriodSeconds": 30,
                "volumes": [
                    {
                        "name": "mysql-data",
                        "persistentVolumeClaim": {
                            "claimName": "mysql-alpha-pvc"
                        }
                    }
                ]
            }
        }
    },
    "status": {
        "conditions": [
            {
                "lastTransitionTime": "2024-07-21T21:10:12Z",
                "lastUpdateTime": "2024-07-21T21:10:12Z",
                "message": "Deployment does not have minimum availability.",
                "reason": "MinimumReplicasUnavailable",
                "status": "False",
                "type": "Available"
            },
            {
                "lastTransitionTime": "2024-07-21T21:10:12Z",
                "lastUpdateTime": "2024-07-21T21:10:12Z",
                "message": "ReplicaSet \"alpha-mysql-5b944d484\" is progressing.",
                "reason": "ReplicaSetUpdated",
                "status": "True",
                "type": "Progressing"
            }
        ],
        "observedGeneration": 1,
        "replicas": 1,
        "unavailableReplicas": 1,
        "updatedReplicas": 1
    }
}

controlplane ~ ✖ vi mysql-alpha-pvc.yml
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-alpha-pvc
  namespace: alpha
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
  storageClassName: slow

controlplane ~ ➜  k apply -f mysql-alpha-pvc.yml
persistentvolumeclaim/mysql-alpha-pvc created
```