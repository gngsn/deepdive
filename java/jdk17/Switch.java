public class Switch {
    public static void main(String[] args) {
        traditionalCase();
        newCase();
        yieldResult();
    }

    public static void traditionalCase() {
        int num = 0;
        Day day = Day.FRIDAY;

        switch (day) {
            case MONDAY:
            case FRIDAY:
            case SUNDAY:
                num = 6;
                break;
            case TUESDAY:
                num = 7;
                break;
            case THURSDAY:
            case SATURDAY:
                num = 8;
                break;
            case WEDNESDAY:
                num = 9;
                break;
            default:
                throw new IllegalStateException("Invalid day: " + day);
        }

        System.out.println(num);
    }
    public static void newCase() {
        Day day = Day.WEDNESDAY;

        int num = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case TUESDAY -> 7;
            case WEDNESDAY -> 9;
            case THURSDAY, SATURDAY -> 8;
        };

        System.out.println(num);
    }

    public static void errorNewCase() {
        Day day = Day.WEDNESDAY;

        int num = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            default -> 0;
                // ERROR! Group doesn't contain a yield statement
        };

        System.out.println(num);
    }

    public static void yieldResult() {
        String s = "Bar";

        int result = switch (s) {
            case "Foo":
                yield 1;
            case "Bar":
                yield 2;
            default:
                System.out.println("Neither Foo nor Bar, hmmm...");
                yield 0;
        };

        System.out.println(result);
    }

    public enum Day { SUNDAY, MONDAY, TUESDAY,
        WEDNESDAY, THURSDAY, FRIDAY, SATURDAY; }

}
