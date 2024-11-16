## JAVA의 String Class

String 연산자의 내부 구성?  

StringBuffer 를 new 연산자로 **생성**한 후, **append**메서드로 String 추가해줌.

연산자를 많이 사용할 경우, new연산자를 사용하기 때문에 시간이 많이 걸림.



``` java
String str = new String();
for (int i=0; i<50; i++)
  str += "*";

System.out.println(str);
// **************************************************
```



값을 더해줄 땐, String클래스에 + 연산자를 사용하지말고, StringBuffer Class를 사용하자.

