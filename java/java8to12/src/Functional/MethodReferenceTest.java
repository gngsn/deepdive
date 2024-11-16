package Functional;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class MethodReferenceTest {
    public static void main(String[] args) {
        UnaryOperator<String> hi = Greeting::hi;
        System.out.println(hi.apply("gngsn"));

        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("gngsn"));

        Supplier<Greeting> greetingSupplier = Greeting::new;
        greetingSupplier.get();

        Function<String, Greeting> greetingFunction = Greeting::new;
        Greeting greeting1 = greetingFunction.apply("gngsn");
        System.out.println(greeting1.getName());

        String[] names = {"gngsn", "whiteship", "toby"};

//      Java 8 이전
//        Arrays.sort(names, new Comparator<String>() {
//            @Override
//            public int compare(String s1, String s2) {
//                return 0;
//            }
//        });
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
    }
}
