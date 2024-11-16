import java.text.MessageFormat;

//import static java.lang.StringTemplate.STR;

class StringTemplate {

    /* 기존의 String Concatenation 방식들 */
    public void existed() {
        // METHOD 1: `+ operator` produces hard-to-read code
        String x = "something";
        String y = "not much convenient";
        String s1 = x + " plus " + y + " equals " + (x + y);


        // METHOD 2: StringBuilder is verbose

        String s2 = new StringBuilder()
                .append(x)
                .append(" plus ")
                .append(y)
                .append(" equals ")
                .append(x + y)
                .toString();

        // METHOD 3: `String::format` and `String::formatted` separate the format string from the parameters, inviting **arity** and **type mismatches**
        String s3 = String.format("%2$d plus %1$d equals %3$d", x, y, x + y);
        String t = "%2$d plus %1$d equals %3$d".formatted(x, y, x + y);

        // METHOD 4: `java.text.MessageFormat` requires too much ceremony and uses an unfamiliar syntax in the format string
        MessageFormat mf = new MessageFormat("{0} plus {1} equals {2}");
        String s4 = mf.format(x, y, x + y);
    }

    /* Refer to other languages

        C#             $"{x} plus {y} equals {x + y}"
        Visual Basic   $"{x} plus {y} equals {x + y}"
        Python         f"{x} plus {y} equals {x + y}"
        Scala          s"$x plus $y equals ${x + y}"
        Groovy         "$x plus $y equals ${x + y}"
        Kotlin         "$x plus $y equals ${x + y}"
        JavaScript     `${x} plus ${y} equals ${x + y}`
        Ruby           "#{x} plus #{y} equals #{x + y}"
        Swift          "\(x) plus \(y) equals \(x + y)"
    */

    public void templateExpression() {
//        String name = "Joan";
//        String info = STR."My name is \{name}";
//        assert info.equals("My name is Joan");   // true
//
//        // Embedded expressions can be strings
//        String firstName = "Bill";
//        String lastName  = "Duck";
//        String fullName  = STR."\{firstName} \{lastName}";  // "Bill Duck"
//        String sortName  = STR."\{lastName}, \{firstName}"; // "Duck, Bill"
//
//
//        // Embedded expressions can perform arithmetic
//        int x = 10, y = 20;
//        String s1 = STR."\{x} + \{y} = \{x + y}";  // "10 + 20 = 30"
//
//        // Embedded expressions can invoke methods and access fields
//        String s2 = STR."You have a \{getOfferType()} waiting for you!";
//        // "You have a gift waiting for you!"
//        String t = STR."Access at \{req.date} \{req.time} from \{req.ipAddress}";
//        //  "Access at 2022-03-25 15:34 from 8.8.8.8"
    }
}