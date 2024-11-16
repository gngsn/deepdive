# Control Plane Failure

1. `service kube-apiserver status`
2. `service kube-controller-manager status`
3. `service kube-scheduler status`
4. `service kubelet status`
5. `service kube-proxy status`


### Check Service Logs

1. `kubectl logs kube-apiserver-master -n kube-system` 
2. `sudo journalctl -u kube-apiserver`



### 📌 [kubectl Quick Reference](https://kubernetes.io/docs/reference/kubectl/quick-reference/)

#### Kubectl autocomplete

```
❯ source <(kubectl completion zsh)
❯ alias k=kubectl
❯ complete -o default -F __start_kubectl k
```
