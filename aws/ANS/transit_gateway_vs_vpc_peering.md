## VPC Peering vs Transit Gateway

|                                       | VPC Peering                                                                                                      | Transit Gateway                                                                          |
|---------------------------------------|------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| **Architecture**                      | One-One connection – Full Mesh                                                                                   | Hub and Spoke with multiple attachments (e.g. 5,000개의 VPC들을 서로 연결하는 Hub and Spoke Model) |
| **Hybrid Connectivity**               | Not supported                                                                                                    | Supported hybrid connectivity via VPN and DX                                             |
| **Complexity**                        | Simple for fewer VPCs, Complex as numberof VPCs increase                                                         | Simple for any number of VPCs and hybrid network connectivity                            |
| **Scale**                             | 125 peering / VPC                                                                                                | 5000 attachments / TGW                                                                   |
| **Latency**                           | Lowest                                                                                                           | Additional Hop                                                                           |
| **Bandwidth**                         | No limit                                                                                                         | 50 Gbps / attachment                                                                     |
| **Ref Security Group**                | Supported                                                                                                        | Not supported                                                                            |                                  
| **Subnet Connectivity**               | For all subnets across AZs                                                                                       | ⭐️⭐️⭐️ Only subnets within the same AZ in which TGW attachment is created                |
| **Transitive Routing e.g IGW access** | Not supported                                                                                                    | Supported                                                                                |
| **TCO** (How much it cost)            | Lowest – Only Data transfer cost (free within same AZ, $0.01 across AZs in each direction, $0.02 across regions) | Per attachment cost + Data transfer cost (e.g N. Virginia: $0.05 per hour + $0.02 / GB)  |

Transit Gateway cost - 데이터을 전송하고 있지 않아도, Attachment 비용이 시간 당 $0.05이기 때문에 지속적으로 비용 부과
