# JAVA Regex 사용 시 성능을 위한 팁



정규 표현식(Regex)사용 시 무심코 사용하면 성능상 이슈가 발생할 수 있다.

어떤 경우인지 알아보고 어떻게 개선하는 게 좋을지 알아보자.



``` java
boolean isNumberOrAlphabet(String s){
  return s.matches("[0-9a-zA-Z]");
}
```



기능 관점에서 보면 문제가 없지만, 성능 관점에서 보면 개선할 포인트가 있다.



- String.matches 메서드를 사용하는 것은 **성능이 중요한 상황에서 반복해 사용하기엔 적합하지 않다.**
- 왜냐하면 String.matches() 메서드 내부에서 만드는 정규표현식용 Pattern 인스턴스는 1번 사용되고 버려져서 곧바로 GC의 대상이 되기 때문이다. 즉 **비효율적**이다.





## 해결책

필요한 정규표현식을 표현하는 **불변 객체 Pattern** 인스턴스를 클래스 초기화 과정에서 생성해두고 나중에 재활용하자 〰️ 



``` java
public class regex {
    private static final Pattern isNumberOrAlphabet = Pattern.compile("[0-9a-zA-Z]");

    boolean validateString(String s) {
        return isNumberOrAlphabet.matcher(s).matches();
    }
}
```













