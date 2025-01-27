# Transit Gateway VPC attachments and Routing

## Transit Gateway attachments

- 주의 사항: CIDRs 범위가 겹치면 안됨
- Across region 지원

<br><img src="./img/transit_gateway_vpc_attachments_and_routing_img1.png"><br>

Attachment를 생성한 후에 이 Attachment에 연결할 특정 VPCs 혹은 VPN를 지정해야함 

<br>

_How does Transit Gateway - Full mesh connectivity - really work?_

### Transit Gateway Route tables

_: Transit Gateway default Route table gets associated with all the attachments_

<br><img src="./img/transit_gateway_vpc_attachments_and_routing_img2.png"><br>

- CIDR 범위가 겹치지 않는 VPC A, B, C
- 중간 집중된 transit gateway
  - Transit Gateway를 생성할 때 Default Route Table을 생성함
  - Default Route Table는 연결된 네트워크에서 트래픽이 어떻게 흘러야하는지 통괄함
- 세 개의 Attachment 생성

<br>

### Transit Gateway Route tables

_: Routes are propagated from VPC to Transit Gateway Route table_

VPC 를 생성하면, 이 Route Table에 연결되는 대상이 **자동으로** Rotue Table로 전파됨

<br><img src="./img/transit_gateway_vpc_attachments_and_routing_img3.png"><br>

<table>
<tr>
<th>Before Attached VPCs</th>
<th>After Attached VPCs</th>
</tr>
<tr>
<td>

<table>
<tr><th>TGW RT (default)</th></tr>
<tr><td></td></tr>
<tr><td></td></tr>
<tr><td></td></tr>
</table>

</td>
<td>

<table>
<tr><th>TGW RT (default)</th></tr>
<tr><td>10.0.0.0/16 via att-a</td></tr>
<tr><td>10.1.0.0/16 via att-a</td></tr>
<tr><td>10.2.0.0/16 via att-a</td></tr>
</table>

</td>
</tr>
</table>

<br>

- 이후, VPC A에서 흘러나온 트래픽이 Transit Gateway에 도착하면 다른 VPC로 이동

이때, VPC A 내부에서는 특정 트래픽이 Attachment A를 통해 Transit Gateway로 향한다는 것을 알까?

<br>
<pre>❗️여러 네트워크를 연결하고자 할 때, 반드시 Route를 설정해야하는데, 각 두 종단 모두 설정해야함</pre>

<br>

### VPC Route tables

_: Static routes needs to be added in the VPC Route tables_

각 VPC에서 Transit Gateway로 향하는 라우트를 VPC 내 Route Table에 설정해야함

→ **Static Route**

<br><img src="./img/transit_gateway_vpc_attachments_and_routing_img4.png"><br>

이를 위해, 반드시 각 VPC 내에 Route Entry를 설정해야함

For example, VPC A have to be set ...

<table>
<tr>
<th>Destination</th>
<th>Target</th>
</tr>
<tr>
<td>10.0.0.0/16</td>
<td>local</td>
</tr>
<tr>
<td>10.0.0.0/8</td>
<td>tgw-xxxxx</td>
</tr>
</table>

<br>
<pre>❗VPN / BGP를 연결하고자 한다면, VPN으로 부터 VPC Route table로 설정해야하는 Route가 전파됨(자동 설정됨)</pre>

모든 VPC 마다 위와 같은 Static Route 를 설정해야함

Static Route 설정이 끝났다면, 이제 Traffic이 외부 VPC로 흘러감

<br>

<pre><b>VPC A에서 IP <code>10.1.0.1</code> 으로 트래픽을 보내는 경우</b>를 살펴보면,

<br><img src="./img/transit_gateway_vpc_attachments_and_routing_img5.png" width="60%"><br>

1️⃣ VPC A 내의 Route Table에서 지정된 규칙에 의해 <b><code>tgw-xxxxxx</code></b> 으로 향함
2️⃣ <code>tgw-xxxxxx</code> 로 도착하면 Transit gateway 의 Route Table에서 해당 트래픽이 향해야할 VPC B로 라우팅함
3️⃣ VPC B가 해당 트래픽을 받음
</pre>

<br>
