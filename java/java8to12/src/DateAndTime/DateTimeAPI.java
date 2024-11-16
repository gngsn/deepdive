package DateAndTime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeAPI {
    public static void main(String[] args) {
        System.out.println("----- Instant -----");
        Instant instant = Instant.now();
        System.out.println(instant);  // 기준시 UTC, GMT

        ZoneId zone = ZoneId.systemDefault();
        System.out.println(zone);
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        System.out.println(zonedDateTime);

        System.out.println("----- LocalDateTime -----");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        LocalDateTime birthday = LocalDateTime.of(1998, Month.AUGUST, 12, 12, 0, 0);
        System.out.println(birthday);

        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        System.out.println(nowInKorea);

        // instant 에서 알아낸 것과 똑같겠죠 ~
        Instant nowInstant = Instant.now();
        ZonedDateTime zonedDateTime1 = nowInstant.atZone(ZoneId.of("Asia/Seoul"));
        System.out.println(zonedDateTime1);

        // zonedDateTime -> Instant
        zonedDateTime1.toInstant();


        System.out.println("----- LocalDateTime -----");

        LocalDate today = LocalDate.now();
        LocalDate thisYearBirthday = LocalDate.of(2022, Month.AUGUST, 12);
        System.out.println(today);
        System.out.println(thisYearBirthday);

        System.out.println("----- Period -----");

        Period period = Period.between(today, thisYearBirthday);
        System.out.println(period.getDays());

        Period until = today.until(thisYearBirthday);
        System.out.println(until.get(ChronoUnit.DAYS));

        // Total Days 구하고 싶다면!
        LocalDate birthDay = LocalDate.of(1998, Month.AUGUST, 12);
        long date = ChronoUnit.DAYS.between(birthDay, today);
        System.out.println(date);

        System.out.println("----- Duration -----");

        Instant instantNow = Instant.now();
        Instant plus = instantNow .plus(10, ChronoUnit.SECONDS);
        Duration between = Duration.between(instantNow, plus);
        System.out.println(between.getSeconds());

        System.out.println("----- Formatting -----");

        System.out.println(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        System.out.println(now.format(MMddyyyy));

        LocalDate parse = LocalDate.parse("08/12/1998", MMddyyyy);
        System.out.println(parse);

        System.out.println("----- Legacy API -----");
        Date date1 = new Date();
        Instant instant1 = date1.toInstant();
        Date newDate = Date.from(instant);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        ZonedDateTime zonedDateTime2 = gregorianCalendar.toZonedDateTime();
        LocalDateTime dateTime = zonedDateTime2.toLocalDateTime();

        // 반대
        GregorianCalendar.from(zonedDateTime2);

        // 예전 API에서 최근 API로 변경
        ZoneId zoneId = TimeZone.getTimeZone("PST").toZoneId();

        // 최근 API에서 예전 API로 변경
        TimeZone.getTimeZone(zoneId);

    }
}
