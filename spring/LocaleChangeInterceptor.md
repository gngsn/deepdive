## LocaleChangeInterceptor

[spring.io Link](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/LocaleChangeInterceptor.html)

``` java
public class LocaleChangeInterceptor 
extends Object 
implements HandlerInterceptor {}
```

<br/>

>
> Interceptor that allows for changing the current locale on every request,
> 
> via a configurable request parameter (default parameter name: "locale").

<br/>

구성 가능한 요청 매개 변수(기본 매개 변수 이름: "locale")를 통해 모든 요청의 현재 로케일을 변경할 수 있는 인터셉터입니다.

특정 값으로 Locale을 자동으로 변경하여 LocaleResolver에 담아 줍니다다.

다국어 처리 Interceptor

<br/><br/>

### ✔ 메시지

그럼 실제 메세지는 어디에 저장해야 할까?

바로 메세지 프로퍼티 파일인데 이 메세지 프로퍼티는 WEB-INF/messages 폴더 하위에 저장되어 있다. 즉 저장해야 한다.

기본 메세지는 message.properties 파일

한국어 메세지는 message_ko.properties 파일

영문 메세지는 message_en.properties 파일입니다

그리고 메세지 프로퍼티 파일로부터 값을 얻을 때는

`message.getMessage(메세지 키값, 대체할 값이 있다면 값의 배열, default 값, locale);` 와 같이 사용한다.

JSP의 경우 메시지 메시지 태그를 통해 다음과 같이 가능하다.

alert("<spring:message code="ALERT_MESSAGE"/>")

