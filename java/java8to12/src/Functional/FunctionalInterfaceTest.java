package Functional;

import java.util.function.*;

public class FunctionalInterfaceTest {
    public static void main(String[] args) {
//        Functional.RunSomething runSomething = () -> System.out.println("Hello");
//        Functional.RunSomething runSomething = new Functional.RunSomething() {
//            int baseNumber = 10;
//
//            @Override
//            public int doIt(int num) {
//                return num + baseNumber;
//            }
//        };

        int baseNumber = 10;
        RunSomething runSomething = (num) -> num + baseNumber;

        System.out.println(runSomething.doIt(1));

        // Function<T, R> - 1
        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));

        // Function<T, R> - 2
        Function<Integer, Integer> plus11 = (i) -> i + 11;
        System.out.println(plus11.apply(1));

        Function<Integer, Integer> multiply2 = (i) -> i * 2;
        System.out.println(multiply2.apply(1));


        // 함수의 조합 - 입력값을 가지고 뒤에 오는 함수에 적용함. 그 결과값을 다시 앞의 함수의 입력값으로 사용함
        System.out.println(plus11.compose(multiply2).apply(1));  // 13
        System.out.println(plus11.andThen(multiply2).apply(1));  // 24


        BiFunction<Integer, Integer, Integer> multiply = (i, j) -> i * j;
        System.out.println(multiply.andThen(multiply2).apply(2, 3));  // 12


        Consumer<Integer> printT = (i) -> System.out.println(i);
        printT.accept(2);

        Supplier<Integer> get10 = () -> 10;
        System.out.println(get10.get());  // 10

        Predicate<String> startsWithGngsn = (s) -> s.startsWith("gngsn");
        Predicate<Integer> isEven = (i) -> i % 2 == 0;
        Predicate<Integer> le10 = (i) -> i < 10;

        System.out.println(startsWithGngsn.test("gngsntrue"));  // true
        System.out.println(startsWithGngsn.test("falsegngsn"));  // false

        System.out.println(isEven.test(10));  // true
        System.out.println(isEven.test(11));  // false

        System.out.println(isEven.and(le10).test(12));  // false
        System.out.println(isEven.or(le10).test(12));  // true

        UnaryOperator<Integer> plus11U = (i) -> i + 11;
        UnaryOperator<Integer> multiply2U = (i) -> i * 2;

        System.out.println(plus11U.compose(multiply2U).apply(1));  // 13
        BinaryOperator<Integer> multiplyB = (i, j) -> i * j;
        System.out.println(multiply.andThen(multiply2).apply(2, 3));  // 12

    }
}
