package Interface;

public class Main {
    public static void main(String[] args) {
        Foo foo = new DefaultFoo("gngsn");
        foo.printName();
        foo.printNameUpperCase();

        Bar.printBar();

    }
}
