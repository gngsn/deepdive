package Functional;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class LambdaTest {
    public static void main(String[] args) {
    }

    private void run() {
        int  baseNumber = 10;

        // 로컬 클래스
        class LocalClass {
            void printBaseNumber() {
                int baseNumber = 11;
                System.out.println(baseNumber);
            }
        }

        // 익명 클래스
        Consumer<Integer> integerConsumer = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                int baseNumber = 11;
                System.out.println(integer + baseNumber);
            }
        };


        new LocalClass().printBaseNumber();
        integerConsumer.accept(10);
        // 람다
        IntConsumer printInt = (i) -> System.out.println(i + baseNumber);
        // ERROR
        // IntConsumer printInt = (baseNumber) -> System.out.println(baseNumber);
        printInt.accept(10);

    }
}
