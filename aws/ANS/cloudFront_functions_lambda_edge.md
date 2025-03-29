# CloudFront Functions and Lambda@Edge
## CloudFront – Customization At The Edge

- 많은 모던 애플리케이션은 엣지에서 특정 형태의 로직을 실행
- Edge Function:
  - CloudFront 배포에 연결하고 작성하는 코드
  - 지연 시간을 최소화하기 위해 사용자 근처에서 실행
  - 캐시 없음, 요청/응답 변경만 가능
  - CloudFront는 두 가지 유형 제공
    - CloudFront Functions
    - Lambda@Edge
- 사용 사례:
  - HTTP 요청 및 응답 조작
  - 애플리케이션에 도달하기 전에 요청 필터링 구현
  - 사용자 인증 및 권한 부여
  - 엣지에서 HTTP 응답 생성
  - A/B 테스트
  - 엣지에서 봇 방지
- 서버를 관리할 필요 없음, 전 세계적으로 배포

<br><img src="./img/cloudFront_functions_lambda_edge_img1.png" width="80%" /><br>

**CloudFront의 전통적인 구조**
- 클라이언트가 Edge Locations과 통신
- Edge Locations는 Regional Edge Cache와 통신
- Regional Edge Cache는 오리진과 통신

**Functions 배포에는 두 개의 레벨이 있음**
- **Lambda@Edge Functions**는 **Regional Edge Cache**에 배포 됨
- **CloudFront Functions**는 **Edge Locations**에 배포 됨


## CloudFront – CloudFront Functions

<table>
<tr>
<td><img src="./img/cloudFront_functions_lambda_edge_img2.png" width="30%" /></td>
<td>

- 가벼운 JavaScript로 작성된 함수
- 대규모, 지연 시간에 민감한 CDN 사용자 정의
- 서브 밀리초(ms) 시작 시간, 초당 수백만 요청
- 엣지 로케이션에서 실행
- 프로세스 기반 격리
- 뷰어 요청 및 응답 변경에 사용
  - **Viewer Request**: 뷰어에서 요청을 받은 후
  - **Viewer Response**: 뷰어로 응답을 전달하기 전
- CloudFront의 네이티브 기능 (CloudFront 내에서 코드 관리)

</td>
</tr>
</table>
<br><br>
