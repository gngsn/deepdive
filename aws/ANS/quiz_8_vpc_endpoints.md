# Quiz 8: VPC Endpoints

### Question 1.

> Which of the following is a security benefit of Amazon VPC endpoints?
>
> 1. VPC endpoints provide private connectivity there by blocking access to the AWS services and API endpoints over the internet.
> 2. VPC endpoints allows the private communication between VPCs there by reducing the attack surface
> 3. VPC endpoints provide greater availability and reliability than public endpoints, which increases security by limiting access for Distributed Denial of Service (DDoS) and other attacks.
> 4. VPC endpoints provide private access to AWS services without instances requiring outbound Internet access.


### Answer.

- 4. VPC endpoints provide private access to AWS services without instances requiring outbound Internet access.

### Explanation.

There by restricting private instances go to internet and download malacious packages which may harm the system/network.

---

### Question 2.

> You have configured a new Amazon S3 endpoint in your VPC. You have created a public Amazon S3 bucket so that you can test connectivity. You can download objects from your laptop but not from instances in the VPC. Which of the following could **NOT** be a problem?
>
> 1. Domain Name System (DNS) was not enabled for the VPC, so you must enable DNS.
> 2. There are not enough free IP addresses in your subnet, so you must choose a larger subnet or remove unused interfaces and IP addresses.
> 3. The VPC endpoint security policy is blocking access to specified S3 bucket.
> 4. The route to the Amazon S3 prefix list is not in the routing table for the instance's subnet

### Answer.

- 4. VPC endpoints provide private access to AWS services without instances requiring outbound Internet access.

### Explanation.

DNS must be enabled for Amazon S3 endpoints to function. Amazon S3 endpoints do not require IP addresses. Access to S3 bucket can be restricted using Gateway endpoint policy. Amazon S3 endpoints do require a route in the routing table.


> FYI. There is no additional charge for using gateway endpoints.

https://jayendrapatil.com/aws-vpc-endpoints/

---

### Question 3.

> You created a new endpoint for your VPC that does not have Internet connectivity. Your instance cannot connect to Amazon S3. What could be the problem?
>
> 1. There is no route in your route table to the Amazon S3 VPC endpoint
> 2. The Amazon S3 bucket is in another region.
> 3. Your bucket access list is not properly configured.
> 4. The VPC endpoint does not have the proper AWS Identity and Access Management (IAM) policy attached to it.
> 5. All of the above

### Answer.

- 5. All of the above

### Explanation.

For VPC gateway endpoint you need to add the route entry in the subnet route table, also the IAM policy for the endpoint and bucket policy for S3 should allow the access from the VPC. VPC endpoint works for the AWS services in the same AWS region.

