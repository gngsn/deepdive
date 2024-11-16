package recordPatterns;


class A {}
class B extends A {}
sealed interface I permits C, D {}
final class C implements I {}
final class D implements I {}
record Pair<T>(T x, T y) {}

public class Switch {
    public static void main(String[] args) {
        Pair<A> p1 = new Pair(new A(), new B());
        Pair<I> p2 = new Pair(new C(), new D());

        // Error: 'switch' statement does not cover all possible input values
//        switch (p1) {
//            case Pair<A>(A a, B b) -> System.out.println(a);
//            case Pair<A>(B b, A a) -> System.out.println(a);
//        }

        switch (p2) {
            case Pair<I>(I i, C c) -> System.out.println(i);
            case Pair<I>(I i, D d) -> System.out.println(d);
        }
    }
}
