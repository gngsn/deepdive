# Quiz 7: VPC Peering

### Question 1.

> You have created three Virtual Private Clouds (VPCs) named A, B, and C.
> VPC A is peered with VPC B. VPC B is peered with VPC C.
> Which statement is true about this peering arrangement?
>
> 1. Instances in VPC A can reach instances in VPC C by default.
> 2. Instances in VPC A can reach instances in VPC C if the correct routes are configured
> 3. Instances in VPC A can reach instances in VPC C if they use a proxy instance in VPC B.
> 4. Instances in VPC A can reach instances in VPC C if they set their routes to an instance in VPC B.

### Answer.

- 3

### Explanation.

Transitive routing prevents instances from communicating across transitively peered VPCs.
If instances are configured to use a proxy, then the destination IP on each hop is an instance in the peered VPC.
You cannot define a route to a network interface in a peered VPC.

---

### Question 2.

> You have two Amazon EC2 instances in two different VPCs that have a peering connection.
> Both VPCs are in the same Availability Zone.
> What charge will you see on your bill for data transfer between those two instances?
>
> 1. $0.00 per GB in each direction
> 2. $0.01 per GB in each direction
> 3. $0.02 per GB in each direction
> 4. $0.04 per GB in each direction

### Answer.

- 1

### Explanation.

[Amazon VPC Pricing](https://aws.amazon.com/about-aws/whats-new/2021/05/amazon-vpc-announces-pricing-change-for-vpc-peering/)

Starting May 1st 2021, all data transfer over a VPC Peering connection that stays within an Availability Zone (AZ) is now free.
All data transfer over a VPC Peering connection that crosses Availability Zones will continue to be charged at the standard in-region data transfer rates.
You can use the Availability Zone-ID to uniquely and consistently identify an Availability Zone across different AWS accounts.

---

### Question 3.

> A previous network administrator implemented a transit VPC architecture to facilitate communication between multiple AWS networks and on-premises resources.
> Over time, the transit VPC Amazon EC2 instance network bandwidth has become saturated with cross-region traffic.
> What highly available design change should you recommend for this network?
>
> 1. Migrate cross-region traffic to a point-to-point VPN connection between an Amazon EC2 instance in each VPC.
> 2. Disable route propagation on your VPC route tables to disable cross-region traffic.
> 3. Leverage VPC Peering connections between VPCs across regions.
> 4. Implement network ACLs to rate limit cross-region traffic

### Answer.

- 3

### Explanation.

VPC peering is managed AWS connection thereby providing consistent bandwidth and lowest latency across AWS regions.
