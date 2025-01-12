# VPC Gateway Endpoints

- **Gateway Endpoint**는 S3 및 DynamoDB와 같은 AWS 서비스에 대한 Private Network 연결을 제공
- Gateway VPC Endpoint를 통할 S3 및 DynamoDB 트래픽을 라우팅하기 위해 라우트 테이블을 수정해야 함
- Amazon S3 엔드포인트를 생성하면 VPC에 Prefix List가 생성됨
- Prefix List는 Amazon S3에서 사용하는 IP 주소의 컬렉션
- Prefix List는 `pl-xxxxxxxx` 형식으로 표시됨, 서브넷 라우팅 테이블 및 보안 그룹에서 사용 가능한 옵션으로 사용됨

<br><img src="./img/vpc_gateway_endpoint_img1.png" width="60%" /><br>

어떻게 Entry를 추가?


## VPC Gateway Endpoint
- Prefix 리스트는 Security Group Outbound Rule 에 추가되어 있어야 함
  (Security Group Outbound Rule에 디폴트 `Allow All` 규칙이 없을 경우)

<br><img src="./img/vpc_gateway_endpoint_img2.png" width="60%" /><br>
