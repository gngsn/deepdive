package Optional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionalAPI {
    public static void main(String args[]) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        Optional<OnlineClass> optional = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .findFirst();

//        optional.ifPresent(oc -> {
//            System.out.println(oc.getTitle());
//        });

        boolean present = optional.isPresent();
        System.out.println(present);

        // ifPresent() - spring으로 시작하는 수업이 있으면 id를 출력하라.
        optional.ifPresent(oc -> {
            System.out.println(oc.getId());
        });

        // orElse() - JPA로 시작하는 수업이 없다면 비어있는 수업을 리턴하라.
        OnlineClass onlineClass = optional.orElse(createNewClass());
        System.out.println(onlineClass.getTitle());

        // orElse() - JPA로 시작하는 수업이 없다면 비어있는 수업을 리턴하라.
        OnlineClass onlineClass2 = optional.orElseGet(OptionalAPI::createNewClass);
        System.out.println(onlineClass2.getTitle());

        // orElseThrow() - Optional에 값이 있으면 가져오고 없는 경우 에러를 던져라.
//        OnlineClass onlineClass3 = optional.orElseThrow(()-> new IllegalArgumentException());
        OnlineClass onlineClass3 = optional.orElseThrow(IllegalArgumentException::new);
        System.out.println(onlineClass3.getTitle());

        // filter() - spring으로 시작하는 수업이 열려있는 수업인지 확인
        Optional<OnlineClass> onlineClass4 = optional.filter(OnlineClass::isClosed);
        System.out.println(onlineClass4.isPresent());

        // map() - spring으로 시작하는 수업이 열려있는 수업인지 확인
        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println(integer.isPresent());

        // 만약 map으로 꺼내는 타입이 Optional 이라면? 예를 들어 progress는 Optinal을 반환하기로 했음.
        Optional<Optional<Progress>> progress = optional.map(OnlineClass::getProgress);
        Optional<Progress> progress1 = progress.orElse(Optional.empty());

        // 위와 같은 경우는 굉장히 문제가 있어보임. 따라서 아래와 같은
        Optional<Progress> progress2 = optional.flatMap(OnlineClass::getProgress);





    }

    private static OnlineClass createNewClass() {
        System.out.println("creating new online class");
        return new OnlineClass(10, "New Class", false);
    }
}
