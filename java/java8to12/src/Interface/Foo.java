package Interface;

public interface Foo {

    void printName();

//    void printNameUpperCase();  구현하지 않으면 ERROR

    /*
    * @ImplSpec 이 구현체는 getName() 으로 가져온 문자열을 대문자로 바꿔 출력한다.
    * */
    default void printNameUpperCase() {                 //구현체를 제공해서 메소드를 추가해도 하위 클래스애서 수정하지 않아도 됨.  but 하위 모든 클래스가 아래 기능을 가지게 됨.
       System.out.println(getName().toUpperCase());
    }

//    default String toString() {}

    String getName();

}
