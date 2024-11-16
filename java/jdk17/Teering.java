import java.util.List;

import static java.util.stream.Collectors.*;

public class Teering {
    public static void main(String[] args) {
        List<Integer> grades = List.of(3, 5, 5, 2, 5, 3, 4, 3, 5, 4, 4, 2);

        Double averageGrade = grades.stream()
                .collect(teeing(
                                summingDouble(i -> i),
                                counting(),
                                (sum, n) -> sum / n));

        System.out.println(averageGrade);
    }
}
