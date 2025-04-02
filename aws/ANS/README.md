# AWS Certified Advanced Networking - Specialty Certification

<br/>

## Section 1. Amazon VPC fundamentals

1. [What is Amazon VPC?](./vpc.md)
2. [VPC Building blocks - core components](./vpc_building_blocks.md)
3. [VPC Addressing (CIDR)](./vpc_addressing_cidr.md)
4. [VPC Route Tables](./vpc_route_tables.md)
5. [IP Addresses - Private vs Public vs Elastic & IPv4 vs IPv6](./ip_addresses.md)
6. [VPC Firewall - Security Group](./vpc_firewall_security_group.md)
7. [VPC Firewall - Network Access Control List (NACL)](./vpc_firewall_nacl.md)
8. [Default VPC](./default_vpc.md)
9. [Hands On: Creating VPC with Public Subnet](./hands_on_creating_vpc_with_public_subnet.md)
10. [Hands On: Add Private subnet](./hands_on_add_private_subnet.md)
11. [NAT Gateway](./nat_gateway.md)
12. [Hands On: Create NAT Gateway](./hands_on_create_nat_gateway.md)
13. [NAT Gateway with High Availability](./nat_gateway_with_high_availability.md)
14. [NAT Instance (EC2 based NAT)](./nat_instance.md)

<br/>

## Section3. Additional VPC features

1. [Extending VPC address space](./extending_vpc_address_space.md)
2. [Elastic Network Interface (ENI) deep dive](./elastic_network_interface.md)
3. [Bring Your Own IP](./bring_your_own_ip.md)

<br/>

## Section 4. VPC DNS and DHCP

1. [How DNS works?](./how_dns_works.md)
2. [Amazon VPC DNS Server Route53 Resolver](./amazon_vpc_dns_server_route53_resolver.md)
3. [VPC DHCP Option Sets](./vpc_dhcp_option_sets.md)
4. [Hands on exercises scenarios](./hands_on_exercises_scenarios.md)

<br/>

## Section 5. Network Performance and Optimization

1. [Basics of Network performance - Bandwidth, Latency, Jitter, Throughput, PPS, MTU](./basics_of_network_performance.md)
2. [Placement Groups and EBS Optimized EC2 instances](./placement_groups_and_ebs_optimized_ec2.md)
3. [Enhanced Networking](./enhanced_networking.md)
4. [DPDK and Elastic Fabric Adapter (EFA)](./dpdk_and_elastic_fabric_adapter_efa.md)
5. [Bandwidth Limits inside and outside of VPC](./bandwidth_limits_inside_and_outside_of_vpc.md)
6. [Network I/O credits](./network_io_credits.md)
7. [Network performance Summary + Exam Essentials](./network_performance_summary.md)

[Quiz 5: Network Performance](quiz_5_network_performance.md)

<br/>

## Section 6. VPC traffic Monitoring, Troubleshooting & Analysis

1. [VPC Traffic Monitoring with VPC Flow logs](./vpc_traffic_monitoring_with_vpc_flow_logs.md) 
2. [VPC Traffic Mirroring](./vpc_traffic_mirroring.md)
3. [VPC Reachability Analyzer](./vpc_reachability_analyzer.md)
4. [Walkthrough: VPC Reachability Analyzer](./walkthrough_vpc_reachability_analyzer.md)
5. [Walkthrough: VPC Network Access Analyzer](./walkthrough_vpc_network_access_analyzer.md)

[Quiz 6: VPC Monitoring & Troubleshooting](./quiz_6_vpc_monitoring_troubleshooting.md)

<br/>

## Section 7: VPC Private Connectivity: VPC Peering

1. [Introduction to VPC private connectivity options](introduction_to_vpc_private_connectivity_options.md)
2. [VPC Peering](./vpc_peering.md)
3. [Hands On: VPC Peering across AWS regions](./hands_on_vpc_peering_across_aws_regions.md)
4. [VPC Peering invalid scenarios](./vpc_peering_invalid_scenarios.md)
    
[Quiz 7: VPC Peering](./quiz_7_vpc_peering.md)

<br>

## Section 8: VPC Private Connectivity:  VPC Gateway Endpoints

1. [Introduction to VPC endpoints](introduction_to_vpc_endpoints.md)
2. [VPC Gateway Endpoint](./vpc_gateway_endpoint.md)
3. [Hands On: VPC gateway endpoint to access S3 bucket](./hands_on_vpc_gateway_endpoint_to_access_s3_bucket.md)
4. [VPC endpoints and S3 bucket policy](./vpc_endpoints_and_s3_bucket_policy.md)
5. [Accessing VPC gateway endpoint from remote network](./accessing_vpc_gateway_endpoint_from_remote_network.md)

[Quiz 8: VPC Endpoints](./quiz_8_vpc_endpoints.md)

<br>

## Section 9: VPC Private Connectivity:  VPC interface endpoint and PrivateLink

1. [Introduction to VPC Interface endpoint](./introduction_to_vpc_interface_endpoint.md)
2. [Hands On: Create VPC interface endpoint and access SQS](./hands_on_create_vpc_interface_endpoint_and_access_sqs.md)
3. [VPC interface endpoint features](./vpc_interface_endpoint_features.md)
4. [VPC Interface endpoint for Customer service (PrivateLink)](./vpc_privateLink.md#vpc-interface-endpoint--accessing-customer-service)
5. [VPC PrivateLink architecture](./vpc_privateLink.md#vpc-privateLink-architecture)
6. [Hands On: VPC PrivateLink to access customer or 3rd party services](./vpc_privateLink.md#hands-on-vpc-privatelink-to-access-customer-or-3rd-party-services)
7. [VPC interface endpoint DNS](./vpc_interface_endpoint_dns.md)
8. [Accessing VPC interface endpoint from remote network](./vpc_interface_endpoint_dns.md#accessing-vpc-interface-endpoint-from-remote-network)
9. [VPC PrivateLink vs VPC Peering](./vpc_privateLink_vs_vpc_peering.md)
10. [VPC PrivateLink Summary](./vpc_privateLink_vs_vpc_peering.md#vpc-privatelink-summary)
11. [Exam Essentials](./vpc_privateLink_vs_vpc_peering.md#exam-essentials)

<br>

## Section 10: VPC Interface endpoint – Accessing Customer service

1. [Introduction to Transit Gateway](./introduction_to_transit_gateway.md)
2. [Transit Gateway VPC attachments and Routing](./transit_gateway_vpc_attachments_and_routing.md)
3. [Hands On: Transit Gateway & VPCs with full routing](./hands_on_transit_gateway.md#hands-on-transit-gateway--vpcs-with-full-routing)
4. [Hands On: Transit Gateway & VPCs with restricted routing](./hands_on_transit_gateway.md#hands-on-transit-gateway--vpcs-with-restricted-routing)
5. [Transit Gateway VPC Network Patterns](./transit_gateway_vpc_network_patterns.md)
6. [Transit Gateway AZ considerations](./transit_gateway_vpc_network_patterns.md#transit-gateway-az-considerations)
7. [Transit Gateway AZ affinity & Appliance mode](./transit_gateway_vpc_network_patterns.md#transit-gateway-az-affinity--appliance-mode)
8. [Transit Gateway Peering](./transit_gateway_peering.md)
9. [Transit Gateway Connect Attachment](./transit_gateway_connect_attachment.md)
10. [Transit Gateway VPN Attachment](./transit_gateway_vpn_attachment.md)
11. [Transit Gateway & Direct Connect](./transit_gateway_with_direct_connect.md)
12. [Transit Gateway Multicast](./transit_gateway_multicast.md)
13. [TGW Architecture: Centralized egress internet](./tgw_architecture_centralized_egress_internet.md)
14. [TGW Architecture: Centralized traffic inspection with Gateway Load Balancer](./tgw_architecture_centralized_egress_traffic_inspection.md)
15. [TGW Architecture: Centralized VPC interface endpoints](./tgw_architecture_centralized_vpc_interface_endpoints.md)
16. [Transit Gateway vs VPC Peering](./transit_gateway_vs_vpc_peering.md)
17. [Transit Gateway Sharing](./transit_gateway_sharing.md)

<br>

## Section 11: Hybrid Network Basics

1. [Introduction to Hybrid networking](./static_routing_vs_dynamic_routing.md#introduction-to-hybrid-networking)
2. [Static Routing vs Dynamic Routing](./static_routing_vs_dynamic_routing.md#vpc-routing---static-vs-dynamic)
3. [How Border Gateway Protocol (BGP) works?](./how_border_gateway_protocol_works.md)
4. [BGP Route selection - ASPATH, LOCAL_PREF, MED](./how_border_gateway_protocol_works.md#bgp-route-selection---aspath-local_pref-med)

<br>

## Section 12: Site-to-Site VPN

1. [Introduction to AWS Site-to-Site VPN](./introduction_to_aws_site_to_site_vpn.md)
2. [Hands On: Setup AWS Site-to-Site VPN](./hands_on_setup_aws_site_to_site_vpn.md)
3. [VPN NAT Traversal (NAT-T)](./vpn_nat_traversal.md)
4. [VPN Route Propagation (Static vs Dynamic)](./vpn_route_propagation_static_vs_dynamic.md)
5. [VPN Transitive Routing scenarios](./vpn_transitive_routing_scenarios.md)
6. [VPN Tunnels - Active/Passive Mode](./vpn_tunnels_active_passive_mode.md)
7. [VPN Dead Peer Detection (DPD)](./vpn_dead_peer_detection.md)
8. [VPN Monitoring](./vpn_monitoring.md)
9. [AWS Site-to-Site VPN Architectures](aws_site_to_site_vpn_architectures.md)
10. [AWS VPN CloudHub](aws_vpn_cloudHub.md)
11. [EC2 based VPN](ec2_based_vpn.md)
12. [AWS Transit VPC architecture using VPN connections](aws_transit_vpc_architecture_using_vpn_connections.md)

<br>

## Section 18: CloudFront

1. [CloudFront Overview](./cloudFront_overview.md)
2. [CloudFront Origins](./cloudFront_origins.md)
3. [CloudFront Origin Headers](./cloudFront_origin_headers.md)
4. [CloudFront Origin Security](./cloudFront_origin_security.md)
5. [CloudFront and HTTPS](./cloudFront_and_https.md)
6. [End-to-End Encryption in CloudFront](./end_to_end_encryption_in_cloudFront.md)
7. [CloudFront Geo Restrictions](./cloudFront_geo_restrictions.md)
8. [CloudFront Functions and Lambda@Edge](./cloudFront_functions_lambda_edge.md)

<br>

[Network Word Dictionary](./network_word_dictionary.md)