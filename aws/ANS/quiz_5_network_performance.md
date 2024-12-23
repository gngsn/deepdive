# Quiz 5: Network Performance

---
### Question 1.

> You are supporting a customer that executes tightly coupled High Performance Computing (HPC) workloads. What Virtual Private Cloud (VPC) option provides high-throughput, low-latency, and high packet-per-second performance?

### Answer.

- Placement Groups

### Explanation.

Placement groups are designed to provide the highest performance network between Amazon Elastic Compute Cloud (Amazon EC2) instances

---

### Question 2.

> Voice calls to international numbers from inside your company must go through an opensource Session Border Controller (SBC) installed on a custom Linux Amazon Machine Image (AMI) in your Virtual Private Cloud (VPC) public subnet. The SBC handles the real-time media and voice signaling. International calls often have garbled voice, and it is difficult to understand what people are saying. What may increase the quality of international voice calls?

### Answer.

✅ Enable enhanced networking on the instance.

❌ Place the SBC in a placement group to reduce latency

### Explanation.

- Enhanced networking can help reduce jitter and network performance.
- Placement groups and lower latency will not assist with flows leaving the VPC. 
- Network interfaces do not affect network performance. 
- An Application Load Balancer will not assist with performance issues


---

### Question 3.

> Your big data team is trying to determine why their proof of concept is running slowly. For the demo, they are trying to ingest 1 TB of data from Amazon Simple Storage Service (Amazon S3) on their c4.8xl instance. They have already enabled enhanced networking. What should they do to increase Amazon S3 ingest rates?

### Answer.

✅ Split the data ingest on more than one instance, such as two c4.4xl instances.

❌ Place the instance in a placement group and use an Amazon S3 endpoint.

### Explanation.

- Using more than one instance will increase the performance because any given flow to Amazon S3 will be limited to 5 Gbps. 
- Moving the instance will not increase Amazon S3 bandwidth. 
- Placement groups will not increase Amazon S3 bandwidth either. 
- Amazon S3 cannot be natively placed behind a Network Load Balancer.

---

### Question 4.

> Your database instance running on an r4.large instance seems 
> to be dropping Transmission Control Protocol (TCP) packets 
> based on a packet capture from a host with which it was communicating. 
> During initial performance baseline tests, the instance was able 
> to handle peak load twice as high as its current load. 
> What could be the issue?

### Answer.

The r4.large instance may have accumulated network credits before load testing, which would allow higher peak values.

### Explanation.

R4 instances use network Input/Output (I/O) credits that allow higher bandwidths when credits are available, which may affect baseline performance tests


---

### Question 5.

> Your development team is testing the performance of a new application using enhanced networking. They have updated the kernel to the latest version that supports the Elastic Network Adapter (ENA) driver. What is the other requirement for support?

### Answer.

Flag the Amazon Machine Image (AMI) for enhanced networking support

### Explanation.

Operating systems must support the appropriate network driver for the correct instance type. The AMI or instance must have enaSupport enabled in addition to having driver support




