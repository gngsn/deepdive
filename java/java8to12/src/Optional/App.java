package Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class App {
    public static void main(String args[]) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        OnlineClass springBoot = new OnlineClass(1, "spring boot", true);

        // null check - ERROR 를 만들기 쉬운 코드. 근본적으로 null을 return 하는 것 자체가 문제와 null check를 하지 않는 것
//        Progress progress = springBoot.getProgress();
//        if (progress != null) {
//            System.out.println(progress.getStudyDuration());
//        }

        Optional<Progress> progress = springBoot.getProgress();
        progress.ifPresent(p -> System.out.println(p.getStudyDuration()));
    }
}
