## HTTP referer 

HTTP 리퍼러(HTTP referer)는 웹 브라우저로 월드 와이드 웹을 서핑할 때, 하이퍼링크를 통해서 각각의 사이트로 방문시 남는 흔적을 말한다.

> 예를 들어 A라는 웹 페이지에 B 사이트로 이동하는 하이퍼링크가 존재한다고 하자. 이때 웹 사이트 이용자가 이 하이퍼링크를 클릭하게 되면 웹 브라우저에서 B 사이트로 참조 주소(리퍼러)를 전송하게 된다. B 사이트의 관리자는 이 전송된 리퍼러를 보고 방문객이 A 사이트를 통해 자신의 사이트에 방문한 사실을 알 수 있다.

Referer 요청 헤더는 현재 요청을 보낸 페이지의 절대 혹은 부분 주소를 포함한다. 만약 링크를 타고 들어왔다면 해당 링크를 포함하고 있는 페이지의 주소가, 다른 도메인에 리소스 요청을 보내는 경우라면 해당 리소스를 사용하는 페이지의 주소가 이 헤더에 포함된다. Referer 헤더는 사람들이 어디로부터 와서 방문 중인지를 인식할 수 있도록 해주며 해당 데이터는 예를 들어, 분석, 로깅, 혹은 캐싱 최적화에 사용될 수도 있다.

그리고 실제로 스프링 시큐리티에서도 로그인 성공시 이동할 TargetUrl 을 구할 때 HTTP Referrer 를 사용해서 구하고 있습니다.

``` java 
// AbstractAuthenticationTargetUrlRequestHandler.java

protected String determineTargetUrl(HttpServletRequest request, ...) {

      --중략

   String targetUrl = null;

   if (targetUrlParameter != null) {
      targetUrl = request.getParameter(targetUrlParameter);

      if (StringUtils.hasText(targetUrl)) {
         logger.debug("Found targetUrlParameter in request: " + targetUrl);

         return targetUrl;
      }
   }

   // HTTP Referrer 사용
   if (useReferer && !StringUtils.hasLength(targetUrl)) {
      targetUrl = request.getHeader("Referer");
      logger.debug("Using Referer header: " + targetUrl);
   }

   if (!StringUtils.hasText(targetUrl)) {
      targetUrl = defaultTargetUrl;
      logger.debug("Using default Url: " + targetUrl);
   }

   return targetUrl;
}
```

그런데 위키백과에서는 HTTP Referrer 는 조작또한 가능하기 때문에 리퍼러 정보를 사용할 때에는 보안에 항상 주의해야 한다고 되어 있다.

<small>참고: https://www.inflearn.com/questions/55237</small>
