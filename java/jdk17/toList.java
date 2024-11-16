import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class toList {
    public static void main(String[] args) {
        oldStyle();
        newStyle();
    }

    private static void oldStyle() {
        List<String> stringList = Stream.of("a", "b", "c").collect(Collectors.toList());
        System.out.println(stringList);
    }
    private static void newStyle() {
        List<String> stringList = Stream.of("a", "b", "c").toList();
        System.out.println(stringList);
    }
}
