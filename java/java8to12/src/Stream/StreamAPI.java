package Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamAPI {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "spring api development", false));

        System.out.println("spring 으로 시작하는 수업");
        springClasses.stream()
                .filter(c -> c.getTitle().startsWith("spring"))
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("closed 되지 않은 수");
        springClasses.stream()
//                .filter(oc -> !oc.isClosed())
                .filter(Predicate.not(OnlineClass::isClosed))
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("수업 이름만 모아서 스트림 만들기");
        springClasses.stream()
                .map(OnlineClass::getTitle)
                // 이때는 String Type 이 들어옴
                .forEach(System.out::println);


        List<OnlineClass> javaClasses = new ArrayList<>();
        javaClasses.add(new OnlineClass(1, "The Java, Test", true));
        javaClasses.add(new OnlineClass(2, "The Java,  Code manipulation", true));
        javaClasses.add(new OnlineClass(3, "The Java, 8 to 12", false));

        List<List<OnlineClass>> gngsnEvents = new ArrayList<>();
        gngsnEvents.add(springClasses);
        gngsnEvents.add(javaClasses);


        System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
        gngsnEvents.stream()
                .flatMap(Collection::stream)
                .forEach(oc -> System.out.println(oc.getId()));

        System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10까지만");
        Stream.iterate(10, i -> i + 1)  // 중개 오퍼레이션이기 때문에 실행되지 않고, 종료 오퍼레이션을 추가해야함
                .skip(10)
                .limit(10)
                .forEach(System.out::println);

        System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
        boolean test = javaClasses.stream().anyMatch(oc -> oc.getTitle().contains("Test"));
        System.out.println("test : " + test);

        System.out.println("스프링 수업 중에 제목이 spring이 들어간 것만 모아서 List로 만들기");
        List<String> spring = springClasses.stream()
                .filter(oc -> oc.getTitle().contains("spring"))
                .map(OnlineClass::getTitle)
                .collect(Collectors.toList());

        spring.forEach(System.out::println);

    }
}
