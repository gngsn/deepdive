import java.text.NumberFormat;
import java.util.Locale;

public class CompactNumber {
    public static void main(String[] args) {
        enLocaleShort();
        krLocaleShort();
        enLocaleLong();
    }

    private static void enLocaleShort() {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        System.out.println(fmt.format(1_000));
        System.out.println(fmt.format(10_000));
        System.out.println(fmt.format(1_000_000));
    }

    private static void krLocaleShort() {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.KOREA, NumberFormat.Style.SHORT);

        System.out.println(fmt.format(1_000));
        System.out.println(fmt.format(10_000));
        System.out.println(fmt.format(1_000_000));
    }

    private static void enLocaleLong() {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        System.out.println(fmt.format(1_000));
        System.out.println(fmt.format(10_000));
        System.out.println(fmt.format(1_000_000));
    }
}
