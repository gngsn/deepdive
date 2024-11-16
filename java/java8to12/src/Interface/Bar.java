package Interface;

public interface Bar extends Foo {
    // 다시 추상화
    @Override
    default void printNameUpperCase() {
        System.out.println("BAR");
    }

    static void printBar() {
        System.out.println("Bar");
    }
}
