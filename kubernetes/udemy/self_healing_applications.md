# Self Healing Applications

[🔗 Kubernetes - Configure Liveness, Readiness and Startup Probes](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)

Kubernetes는 ReplicaSets와 Replication Controller를 통해 self-healing(자가 치유) 응용 프로그램을 지원

Replication Controller는 Pod 내의 응용 프로그램이 충돌할 때 자동으로 Pod가 다시 생성되도록 도와줌

충분한 응용 프로그램의 복제본이 항상 실행되도록 도와줌

Kubernetes는 Pod 내에서 실행되는 응용 프로그램의 상태를 확인하고,
Liveness and Ready Probe를 통해 필요한 조치를 취할 수 있도록 추가적인 지원을 제공
