package commandQuery.v1;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Client {
    public static void main(String[] args) {
        // 매주 수요일 10시 30분부터 30분 동안 열리는 회의
        RecurringSchedule schedule = new RecurringSchedule("회의", DayOfWeek.WEDNESDAY,
                LocalTime.of(10, 30), Duration.ofMinutes(30));

        // 2019년 5월 8일 수요일 10:30 ~ 11:00 동안 진행되는 회의
        Event meeting = new Event("회의",
                LocalDateTime.of(2019, 5, 8, 10, 30),
                Duration.ofMinutes(30));

        assert meeting.isSatisfied(schedule);

        meeting = new Event("회의", LocalDateTime.of(2019, 5, 9, 10, 30), Duration.ofMinutes(30));

        assert !meeting.isSatisfied(schedule);
        assert meeting.isSatisfied(schedule);
    }
}
