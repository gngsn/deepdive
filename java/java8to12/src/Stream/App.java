package Stream;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String args[]) {
        List<String> names = new ArrayList<>();
        names.add("gngsn");
        names.add("whiteship");
        names.add("toby");
        names.add("foo");

        Stream<String> stringStream = names.stream().map(String::toUpperCase);

        System.out.println("------- names -------");
        names.forEach(System.out::println);   // names 데이터는 변경되지 않는다.
        System.out.println("\n\n------ stringStream -------");
        stringStream.forEach(System.out::println);


        // 중개 오퍼레이션 - 아래 출력 안됨 -> 실행을 안함.
        System.out.println("\n\n------ intermediate operation -------");
        names.stream().map(s -> {
            System.out.println(s);
            return s.toUpperCase();
        });

        System.out.println("\n\n------ intermediate operation 2 -------");
        List<String> collect = names.stream().map(s -> {
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList());

//        names.forEach(System.out::println);


        // parallel stream
        System.out.println("\n\n------ parellel stream -------");
        List<String> parellelCollect = names.parallelStream()
                                            .map(String::toUpperCase)
                                            .collect(Collectors.toList());
        parellelCollect.forEach(System.out::println);


        System.out.println("\n\n------ parellel stream 2 -------");
        List<String> parellelCollect2 = names.parallelStream() .map(s -> {
            System.out.println(s
                    + " " + Thread.currentThread().getName());
            return s.toUpperCase();
        }).collect(Collectors.toList());

//        parellelCollect2.forEach(System.out::println);
    }
}
