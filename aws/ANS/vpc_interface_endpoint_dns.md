# VPC interface endpoint DNS

VPC Interface Endpoint


- Provisions an ENI that will have a private endpoint interface hostname

- Private DNS settings for Interface endpoint

- The public hostname of a service will resolve to the private Endpoint Interface hostname

- VPC Setting: “Enable DNS hostnames” and “Enable DNS Support” must be 'true’

- Example for Athena:

- Regional: `vpce-0b7d2995e9dfe5418-mwrths3x.athena.us-east-1.vpce.amazonaws.com`

- Zonal: `vpce-0b7d2995e9dfe5418-mwrths3x-us-east-1a.athena.us-east-1.vpce.amazonaws.com`

- Zonal: `vpce-0b7d2995e9dfe5418-mwrths3x-us-east-1b.athena.us-east-1.vpce.amazonaws.com`

- Service DNS: `athena.us-east-1.amazonaws.com` (private DNS name)

- With Private DNS enabled, the consumer VPC can access the endpoint services
using Service's default DNS e.g `ec2.us-east-1.amazonaws.com` instead of using
endpoint specific DNS e.g `vpce-12345-ab.ec2.us-east-1.vpce.amazonaws.com` 