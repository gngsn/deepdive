package Functional;

@FunctionalInterface
public interface RunSomething {
//    void doIt();
    int doIt(int num);

    static void printName() {
        System.out.println("gngsn");
    }

    default void printAge() {
        System.out.println("40");
    }
}
