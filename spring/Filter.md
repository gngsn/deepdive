## Filter

말그대로 요청과 응답을 거른뒤 정제하는 역할을 한다.

서블릿 필터는 DispatcherServlet 이전에 실행이 되는데 필터가 동작하도록 지정된 자원의 앞단에서 요청내용을 변경하거나,  여러가지 체크를 수행할 수 있다.

또한 자원의 처리가 끝난 후 응답내용에 대해서도 변경하는 처리를 할 수가 있다.

보통 web.xml에 등록하고, 일반적으로 인코딩 변환 처리, XSS방어 등의 요청에 대한 처리로 사용된다.

<br/>

**[ 필터의 실행메서드 ]**

ㆍinit() - 필터 인스턴스 초기화

ㆍdoFilter() - 전/후 처리

ㆍdestroy() - 필터 인스턴스 종료

<br/>

```java
public interface Filter { 
	public default void init(FilterConfig filterConfig) throws ServletException {} 

	public void doFilter(ServletRequest request, ServletResponse response, 
		FilterChain chain) throws IOException, ServletException; 

	public default void destroy() {} 
}
```

<br/>

[: Interface Filter](https://docs.oracle.com/cd/E17802_01/products/products/servlet/2.3/javadoc/javax/servlet/Filter.html)


<br/>

## 적용 예시 

### ConfigurableSiteMeshFilter

<br/>

``` java
public class SiteMeshFilterConfiguration extends ConfigurableSiteMeshFilter {	

	/**	 
	* Sitemesh 설정	 
	*
	* @param builder	 */
    @Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

		builder.addDecoratorPath("/*", "/WEB-INF/decorator/maindeco.jsp");

	}
}
```