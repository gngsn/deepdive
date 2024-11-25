# Hands on exercises scenarios

## Scenario 1: VPC DNS with Route53 Private Hosted zone

<small><i>Using Amazon Route53 DNS resolver and Amazon Route53 Private hosted zone</i></small>

<br><img src="./img/hands_on_exercises_scenarios_img1.png" width="80%" /><br>

### Steps

<br><img src="./img/hands_on_exercises_scenarios_img2.png" width="60%" /><br>

1. Create a VPC with Public & Private subnet
2. (Optional) Create DHCP Option set with domain as corp.internal and associate with your VPC
3. Launch one EC2 instance in Public subnet (say app) and one instance in Private subnet (say db).
4. Allow SSH and ICMP IPv4 in the security group
5. Create Route 53 Private hosted zone and associate with the VPC
6. Create A records for ec2 instances pointing to their Private IPs
7. SSH into Public EC2 instance and ping to other instance using it’s DNS name

<br>

## Scenario 2: Using custom DNS server

<small><i>VPC DNS with custom DNS server</i></small>

<br><img src="./img/hands_on_exercises_scenarios_img3.png" width="80%" /><br>

### Step 1 – Setup a VPC and launch instances

<br><img src="./img/hands_on_exercises_scenarios_img4.png" width="60%" /><br>

**1.1.** Create a VPC with public and private subnets

- Launch DNS server ec2 instance:
  - Security group to allow UDP 53 from

**1.2.** VPC CIDR, SSH (22)

- Launch an app server & db server ec2 instances:
  - Security group to allow SSH (22), ICMP IPv4 All (ping)

### Step 2a – Setup DNS server

**2a.1.** Login to DNS server

**2a.2.** Install DNS server packages

```
$sudo su
$yum update –y
$yum install bind bind-utils –y
```

### Step 2b – Configure DNS server

**2a.3.** Create file /var/named/corp.internal.zone

<pre lang="bash">
$TTL 86400
@ IN SOA ns1.corp.internal. root.corp.internal. (
  2013042201 ;Serial
  3600  ;Refresh
  1800   ;Retry
  604800  ;Expire
  86400 ;Minimum  TTL
)
; Specify our two nameservers
  IN NS dnsA. corp.internal.
  IN NS dnsB. corp.internal.
; Resolve nameserver hostnames to IP, replace with your two droplet IP addresses. 
dnsA IN A 1.1.1.1
dnsB IN A 8.8.8.8

; Define hostname -> IP pairs which you wish to resolve 
@ IN A 10.10.0.<b>x</b>
app IN A 10.10.0.<b>x</b>
db IN A 10.10.1.<b>x</b>
</pre>

`x` → IP 에 맞게 수정

### Step 2c – Configure DNS server

**2a.4.** Create file `/etc/named.conf`

`X` → with your DNS server IP 에 맞게 수정

<pre lang="bash">
options {
  directory "/var/named";
  dump-file "/var/named/data/cache_dump.db";
  statistics-file "/var/named/data/named_stats.txt"; 
  memstatistics-file "/var/named/data/named_mem_stats.txt"; 
  allow-query { any; };
  <b>allow-transfer { localhost; 10.10.0.X; };</b>
  recursion yes; forward first; 
  forwarders {
    10.10.0.2; 
  };
  dnssec-enable yes;
  dnssec-validation yes;
  dnssec-lookaside auto;
  /* Path to ISC DLV key */
  bindkeys-file "/etc/named.iscdlv.key"; 
  managed-keys-directory "/var/named/dynamic";
};
zone "corp.internal" IN {
  type master;
  file "corp.internal.zone"; 
  allow-update { none; };
};
</pre>

### Step 2d – Configure DNS server

Restart **named** service

```
$ service named restart
$ chkconfig named on
```

### STEP 3 – Create DHCP Option set

<br><img src="./img/hands_on_exercises_scenarios_img4.png" width="60%" /><br>

- Create new DHCP Option set
  1. Set **Domain name** = `corp.internal`
  2. Set name server = <code>10.10.0.<b>X</b></code> (DNS server IP)
- Reboot DNS server and App server (to update DHCP)

### STEP 4 – Verify custom domain names

1. Login to App server
2. Check `/etc/resolv.conf`
3. Resolve DNS for db server
   
```
$ nslooup db.corp.internal
$ nslookup db
$ ping db
```
