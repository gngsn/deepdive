## BCryptPasswordEncoder

*스프링 시큐리티(Spring Seurity) 프레임워크에서 제공하는 클래스 중 하나로 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스*

- BCryptPasswordEncoder는 BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 인코딩해주는 메서드와 사용자의 의해 제출된 비밀번호와 저장소에 저장되어 있는 비밀번호의 일치 여부를 확인해주는 메서드를 제공합니다.
- PasswordEncoder 인터페이스를 구현한 클래스입니다.
- 생성자의 인자 값(verstion, strength, SecureRandom instance)을 통해서 해시의 강도를 조절할 수 있습니다.

BCryptPasswordEncoder는 위에서 언급했듯이 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 제공합니다. 기본적으로 웹 개발함에 있어서 사용자의 비밀번호를 데이터베이스에 저장하게 됩니다. 허가되지 않은 사용자가 접근하지 못하도록 기본적인 보안이 되어 있을 것입니다. 하지만 기본적 보안이 되어 있더라도, 만약 그 보안이 뚫리게 된다면 비밀번호 데이터는 무방비하게  노출됩니다. 이런 경우를 대비해 BCryptPasswordEncoder에서 제공하는 메서드를 활용하여 비밀번호를 암호화 함으로써 비밀번호 데이터가 노출되더라도 확인하기 어렵도록 만들어 줄 수 있습니다.

## **Bcrypt**

```jsx

bcrypt.hashpw(password, bcrypt.gensalt())

```

- 1999년에 publish된 password-hashing function이다.
- Blowfish 암호를 기반으로 설계된 암호화 함수이며 현재까지 사용중인 **가장 강력한 해시 메커니즘** 중 하나이다.
- 보안에 집착하기로 유명한 OpenBSD에서 사용하고 있다.
- .NET 및 Java를 포함한 많은 플랫폼,언어에서 사용할 수 있다.
- 반복횟수를 늘려 연산속도를 늦출 수 있으므로 연산 능력이 증가하더라도 brute-forece 공격에 대비할 수 있다.

출처: [https://inpa.tistory.com/entry/NODE-📚-bcrypt-모듈-원리-사용법#bcrypt](https://inpa.tistory.com/entry/NODE-%F0%9F%93%9A-bcrypt-%EB%AA%A8%EB%93%88-%EC%9B%90%EB%A6%AC-%EC%82%AC%EC%9A%A9%EB%B2%95#bcrypt) [👨‍💻 Dev Scroll]

[OKKY | 패스워드 해시화에 BCrypt 썼는데 감리에서 걸렸습니다](https://okky.kr/article/910994)

[What is the difference between SHA-3 and SHA-256?](https://crypto.stackexchange.com/questions/68307/what-is-the-difference-between-sha-3-and-sha-256)

## 정의

```java
public class BCryptPasswordEncoder 
  implements PasswordEncoder {}
```

BCryptPasswordEncoder는 스프링 시큐리티 5.4.2부터는 3개의 메서드, 그 이전 버전은 2개의 메서드를 가집니다. 공통적으로 encdoe(), matchers() 메서드에 upgradeEncoding() 메서드가 추가되었습니다.

## 메소드

### ✔ **encode(java.lang.CharSequence rawPassword)**

> *encode(java.lang.CharSequence rawPassword)

: Encode the raw password*
> 

패스워드를 암호화해주는 메서드입니다. 

encde() 메서드는 SHA-1, 8바이트로 결합된 해쉬, 랜덤 하게 생성된 솔트(salt)를 지원합니다.

- 매개변수는 java.lang.CharSequence타입의 데이터를 입력해주면 됩니다. (CharSequence를 구현하고 있는 java.lang의 클래스는 String, StringBuffer, StringBuilder가 있습니다.)
- 반환 타입은 String 타입입니다.
- 똑같은 비밀번호를 해당 메서드를 통하여 인코딩하더라도 매번 다른 인코딩 된 문자열을 반환합니다.

### ✔ ****matchers(CharSequence rawPassword, String encodePassword)****

> *matchers(java.lang.CharSequence rawPassword, java.lang.String encodePassword)*
> 

BOOLEAN	matches(java.lang.CharSequence rawPassword, java.lang.String encodedPassword)	
Verify the encoded PASSWORD obtained FROM STORAGE matches the submitted raw PASSWORD AFTER it too IS encoded.
BOOLEAN	upgradeEncoding(java.lang.String encodedPassword)	
RETURNS TRUE IF the encoded PASSWORD should be encoded again FOR better SECURITY, ELSE false.
