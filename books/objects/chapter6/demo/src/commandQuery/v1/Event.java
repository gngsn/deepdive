package commandQuery.v1;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 반복 일정(recurring schedule)을 만족하는 특정 일자와 시간에 발생하는 사건
 */
public class Event {
    private String subject;
    private LocalDateTime from;
    private Duration duration;

    public Event(String subject, LocalDateTime from, Duration duration) {
        this.subject = subject;
        this.from = from;
        this.duration = duration;
    }

    /**
     * - 개념적인 Query Method: Event가 RecurringSchedule의 조건에 부합하는지를 판단
     * - 부수효과를 가지는 Command Method: Event가 RecurringSchedule의 조건에 부합하지 않을 경우, Event 상태를 조건에 부합하도록 변경
     */
    public boolean isSatisfied(RecurringSchedule schedule) {
        if (from.getDayOfWeek() != schedule.getDayOfWeek() ||
                !from.toLocalTime().equals(schedule.getFrom()) ||
                !duration.equals(schedule.getDuration())) {
            reschedule(schedule);
            return false;
        }
        return true;
    }

    /**
     * Event 객체의 상태를 수정
     */
    private void reschedule(RecurringSchedule schedule) {
        from = LocalDateTime.of(from.toLocalDate().plusDays(daysDistance(schedule)), schedule.getFrom());
        duration = schedule.getDuration();
    }

    private long daysDistance(RecurringSchedule schedule) {
        return schedule.getDayOfWeek().getValue() - from.getDayOfWeek().getValue();
    }
}