## Question 06.

Take the backup of ETCD at the location `/opt/etcd-backup.db` on the `controlplane` node.

### Answer

```Bash
controlplane ~ ✖ export ETCDCTL_API=3

controlplane ~ ➜  etcdctl snapshot save --cacert=/etc/kubernetes/pki/etcd/ca.crt --cert=/etc/kubernetes/pki/etcd/server.crt --key=/etc/kubernetes/pki/etcd/server.key --endpoints=127.0.0.1:2379 /opt/etcd-backup.db

Snapshot saved at /opt/etcd-backup.db
```