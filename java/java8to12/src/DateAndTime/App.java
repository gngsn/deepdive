package DateAndTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat();

        // 이름이 작명이 되어 있지 않고, 사용하기 번거로움

        // date 인데 time을 가져온다고? 좀 이상함
        Date date1 = new Date();
        long time = date1.getTime();

        System.out.println(date1);
        System.out.println(time);

        // mutable 한 객체 -> multi-thread 환경에서 사용하기 어렵다.
        Thread.sleep(1000 * 3);
        Date after3Second = new Date();
        System.out.println(after3Second);
        after3Second.setTime(time);
        System.out.println(after3Second);

//        Calendar gngsnBirthDay = new GregorianCalendar(1998, 8, 12);
        Calendar gngsnBirthDay = new GregorianCalendar(1998, GregorianCalendar.AUGUST, 12);
    }
}
