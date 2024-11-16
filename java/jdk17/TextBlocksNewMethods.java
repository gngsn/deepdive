class TextBlocksNewMethods {
    public static void main(String[] args) {
        allnewmethods();

        stripIndent();
        formatted();
        translateEscapes();
    }

    private static void allnewmethods() {
        System.out.println("%s\\n%s\\n%s"
                .translateEscapes()
                .formatted(
                        "   hello world".stripIndent(),
                        "hello world  ".stripIndent(),
                        "   hello world  ".stripIndent()));
    }

    private static void stripIndent() {
        System.out.println("   hello world".stripIndent());
        System.out.println("hello world  ".stripIndent());
        System.out.println("   hello world  ".stripIndent());
    }

    private static void formatted() {
        String formatted = "1. %s 2. %s 3. %s ".formatted("one", "two", "three");
        System.out.println(formatted); // 1. one 2. two 3. three
    }
    private static void translateEscapes() {
        String str1 = "This is tab \t, next backspace \b,next Single Quotes \' next,Double Quotes \" ";
        String str2 = "This is tab \\t, next backspace \\b,next Single Quotes \\' next,Double Quotes \" ";
        System.out.println(str1);
        System.out.println(str1.translateEscapes());

        System.out.println(str2);
        System.out.println(str2.translateEscapes());
    }
}