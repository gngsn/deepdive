package Interface;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChangeMethod {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("gngsn");
        names.add("whiteship");
        names.add("toby");
        names.add("foo");

//        names.forEach(s -> System.out.println(s));
//        names.forEach(System.out::println);

//        Spliterator<String> spliterator = names.spliterator();
//        Spliterator<String> spliterator1 = spliterator.trySplit();
//        while(spliterator.tryAdvance(System.out::println));
//        System.out.printf("==================");
//        while(spliterator1.tryAdvance(System.out::println));

//        Stream stream = names.stream().map(String::toUpperCase)
//                .filter(s -> s.startsWith("G"));
//
//        stream.forEach(System.out::println);
//        System.out.println(stream.toString());

//        names.removeIf(s -> s.startsWith("g"));
//        System.out.printf(names.toString());

        Comparator<String> compareToIgnoreCase = String::compareToIgnoreCase;
        names.sort(compareToIgnoreCase.reversed());
    }
}
