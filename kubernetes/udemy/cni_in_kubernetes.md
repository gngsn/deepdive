# CNI in Kubernetes
_Container Networking Interface_

쿠버네테스는 컨테이너 Network Namespace 생성을 책임짐

### Pod network creation sequence

<br><img src="./img/cni_in_kubernetes_img1.png" width="80%" alt="Pod network creation sequence">Ref. https://tetrate.io/blog/kubernetes-networking/<br>

### CNI workflow

<br><img src="./img/cni_in_kubernetes_img2.png" width="80%" alt="CNI workflow">Ref. https://tetrate.io/blog/kubernetes-networking/<br>

네임스페이스들을 식별하고 적절한 네트워크 플러그인을 불러서 알맞은 네트워크에 붙임

쿠버네티스에서 컨테이너의 생성을 책임지는 컴포넌트는 컨테이너가 생성된 이후 바로 CNI 플러그인을 불러야 함

CNI 플러그인은 클러스터의 각 노드의 kubelet service 내에 설정됨

kubelet 설정 파일을 확인해보면 아래에서 설정 값들을 확인할 수 있음 

<pre><code lang="bash"># kubelet.service
ExecStart=/usr/local/bin/kubelet \\
--config=/var/lib/kubelet/kubelet-config.yaml \\
--container-runtime=remote \\
--container-runtime-endpoint=unix:///var/run/containerd/containerd.sock \\
--image-pull-progress-deadline=2m \\
--kubeconfig=/var/lib/kubelet/kubeconfig \\
<b>--network-plugin=cni \\
--cni-bin-dir=/opt/cni/bin \\
--cni-conf-dir=/etc/cni/net.d</b> \\
--register-node=true \\
--v=2
</code></pre>

동일한 Network Plugin 설정 정보를 kubelet 실행 정보에서 확인할 수 있음

<pre><code lang="bash">$ ps -aux | grep kubelet
root 2095 1.8 2.4 960676 98788 ? Ssl 02:32 0:36 /usr/bin/kubelet --bootstrapkubeconfig=/etc/kubernetes/bootstrap-kubelet.conf --kubeconfig=/etc/kubernetes/kubelet.conf --
config=/var/lib/kubelet/config.yaml --cgroup-driver=cgroupfs <b>--cni-bin-dir=/opt/cni/bin --cni-conf-dir=/etc/cni/net.d --network-plugin=cni</b>
</code></pre>

- `cni-bin-dir` 는 실행 가능한 CNI 플러그인을 지원하는 모든 것을 포함
  - 위 파라미터는 Kubernetes 1.24 부터 제거됨, with management of the CNI no longer in scope for kubelet [[🔗 link](https://kubernetes.io/docs/concepts/extend-kubernetes/compute-storage-net/network-plugins/#installation)]
- `cni-conf-dir` 는 어떤 플러그인이 필요한지 찾을 때 kubelet이 훑어보는 위치   

<br>

|                | `/opt/cni/bin`                                                                                        | `/etc/cni/net.d`                                                                                                                                                                                 | `/var/lib/cni`                                                                                                                                                                                                                                                      |
|----------------|-------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Purpose**    | Contains CNI plugin binaries                                                                          | Contains CNI configuration files.                                                                                                                                                                | Stores runtime network state and parameters.                                                                                                                                                                                                                        |
| **Role**       | Represents the available CNI plugins that can be used for pod networking.                             | Specifies the currently active CNI plugin and its configurations for the Kubernetes cluster.                                                                                                     | Maintains actual network configuration state, such as: <br>- Allocated IP addresses (if the plugin uses host-local IPAM). <br>- Persistent data related to pod network setup.                                                                                       |
| **Key Point**  | The container runtime (e.g., `containerd`, `cri-o`) calls these binaries to configure pod networking. | The container runtime **reads this directory to determine which CNI plugin to use** for setting up pod networks. The configuration also includes options like IPAM settings and plugin chaining. | This directory ensures state persistence (e.g., IP reuse) across pod or node restarts for some CNI plugins. However, not all plugins heavily use this directory—some store state elsewhere (e.g., in etcd for Calico or in Kubernetes resources for AWS VPC CNI).   |

<br>

```
$ ls /opt/cni/bin
bridge dhcp flannel host-local ipvlan loopback macvlan portmap ptp sample tuning
vlan weave-ipam weave-net weave-plugin-2.2.1

$ ls /etc/cni/net.d
10-bridge.conf

# CNI 표준 정의 파일
$ cat /etc/cni/net.d/10-bridge.conf
{
    "cniVersion": "0.2.0",
    "name": "mynet",
    "type": "bridge",
    "bridge": "cni0",
    "isGateway": true,
    "ipMasq": true,
    "ipam": {
        "type": "host-local",
        "subnet": "10.22.0.0/16",
        "routes": [
            { "dst": "0.0.0.0/0" }
        ]
    }
}
```

[CNI Configuration format](https://github.com/containernetworking/cni/blob/main/SPEC.md)

<br>

A network configuration consists of a JSON object with the following keys:

- `cniVersion` (string): [Semantic Version 2.0](https://semver.org/) of CNI specification to which this configuration list and all the individual configurations conform. Currently "1.1.0"
- `cniVersions` (string list): List of all CNI versions which this configuration supports. See version selection below.
- `name` (string): Network name. This should be unique across all network configurations on a host (or other administrative domain). Must start with an alphanumeric character, optionally followed by any combination of one or more alphanumeric characters, underscore, dot (.) or hyphen (-).
- `disableCheck` (boolean): Either true or false. If disableCheck is true, runtimes must not call CHECK for this network configuration list. This allows an administrator to prevent CHECKing where a combination of plugins is known to return spurious errors.
- `plugins` (list): A list of CNI plugins and their configuration, which is a list of plugin configuration objects.

Plugin configuration objects:
Plugin configuration objects may contain additional fields than the ones defined here. The runtime MUST pass through these fields, unchanged, to the plugin, as defined in section 3.

**Required keys:**

- `type` (string): Matches the name of the CNI plugin binary on disk. Must not contain characters disallowed in file paths for the system (e.g. / or \).

**Optional keys, used by the protocol:**

- `capabilities` (dictionary): Defined in section 3

**Reserved keys, used by the protocol**: These keys are generated by the runtime at execution time, and thus should not be used in configuration.

- `runtimeConfig`
- `args`
- Any keys starting with `cni.dev/`

**Optional keys, well-known**: These keys are not used by the protocol, but have a standard meaning to plugins. Plugins that consume any of these configuration keys should respect their intended semantics.

- `ipMasq` (boolean): If supported by the plugin, sets up an IP masquerade on the host for this network. This is necessary if the host will act as a gateway to subnets that are not able to route to the IP assigned to the container.
- `ipam` (dictionary): Dictionary with IPAM (IP Address Management) specific values:
  - `type` (string): Refers to the filename of the IPAM plugin executable. Must not contain characters disallowed in file paths for the system (e.g. / or \).
- `dns` (dictionary, optional): Dictionary with DNS specific values:
  - `nameservers` (list of strings, optional): list of a priority-ordered list of DNS nameservers that this network is aware of. Each entry in the list is a string containing either an IPv4 or an IPv6 address.
  - `domain` (string, optional): the local domain used for short hostname lookups.
  - `search` (list of strings, optional): list of priority ordered search domains for short hostname lookups. Will be preferred over domain by most resolvers.
  - `options` (list of strings, optional): list of options that can be passed to the resolver
  - `Other` keys: Plugins may define additional fields that they accept and may generate an error if called with unknown fields. Runtimes must preserve unknown fields in plugin configuration objects when transforming for execution.