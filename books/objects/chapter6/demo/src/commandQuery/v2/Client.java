package commandQuery.v2;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Client {
    public static void main(String[] args) {
        RecurringSchedule schedule = new RecurringSchedule("회의", DayOfWeek.WEDNESDAY,
                LocalTime.of(10, 30), Duration.ofMinutes(30));

        // 2019년 5월 8일 목요일 10:30 ~ 11:00 동안 진행되는 회의
        Event wedMeeting = new Event("수요일 회의", LocalDateTime.of(2019, 5, 8, 10, 30), Duration.ofMinutes(30));

        assert wedMeeting.isSatisfied(schedule);

        // 2019년 5월 9일 목요일 10:30 ~ 11:00 동안 진행되는 회의
        Event thuMeeting = new Event("목요일 회의", LocalDateTime.of(2019, 5, 9, 10, 30), Duration.ofMinutes(30));

        assert !thuMeeting.isSatisfied(schedule);
        assert !thuMeeting.isSatisfied(schedule);

        if (!thuMeeting.isSatisfied(schedule)) {
            thuMeeting.reschedule(schedule);
        }

        assert thuMeeting.isSatisfied(schedule);
    }
}
