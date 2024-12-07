### Q1. Take a backup of the etcd cluster and save it to `/opt/etcd-backup.db`.

<br>

#### Answer

Kubernetes 공식 문서에서 `etcd backup` 검색 후 가장 상단의 `Operating etcd clusters for Kubernetes | Kubernetes` 참고

https://kubernetes.io/docs/tasks/administer-cluster/configure-upgrade-etcd/#snapshot-using-etcdctl-options

```Bash
controlplane ~ ✖ cat /etc/kubernetes/manifests/etcd.yaml | grep '\-\-'
    - --advertise-client-urls=https://192.23.197.8:2379
    - --cert-file=/etc/kubernetes/pki/etcd/server.crt
    - --client-cert-auth=true
    - --data-dir=/var/lib/etcd
    - --experimental-initial-corrupt-check=true
    - --experimental-watch-progress-notify-interval=5s
    - --initial-advertise-peer-urls=https://192.23.197.8:2380
    - --initial-cluster=controlplane=https://192.23.197.8:2380
    - --key-file=/etc/kubernetes/pki/etcd/server.key
    - --listen-client-urls=https://127.0.0.1:2379,https://192.23.197.8:2379
    - --listen-metrics-urls=http://127.0.0.1:2381
    - --listen-peer-urls=https://192.23.197.8:2380
    - --name=controlplane
    - --peer-cert-file=/etc/kubernetes/pki/etcd/peer.crt
    - --peer-client-cert-auth=true
    - --peer-key-file=/etc/kubernetes/pki/etcd/peer.key
    - --peer-trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
    - --snapshot-count=10000
    - --trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
```

위 내용 참고해서 `ETCDCTL_API` 명령어 실행

```
controlplane ~ ✖ ETCDCTL_API=3 etcdctl --endpoints=https://127.0.0.1:2379 \
 --cacert=/etc/kubernetes/pki/etcd/ca.crt \
 --cert=/etc/kubernetes/pki/etcd/server.crt \
 --key=/etc/kubernetes/pki/etcd/server.key \
 snapshot save /opt/etcd-backup.db
Snapshot saved at /opt/etcd-backup.db
```

