class TextBlocks {
    public static void main(String[] args) {
        oldStyle();
        jsonBlock();
        emptyBlock();
        escapeSequence();
    }

    private static void oldStyle() {
        String text = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 45,\n" +
                "  \"address\": \"Doe Street, 23, Java Town\"\n" +
                "}";
        System.out.println(text);
        System.out.println(text.translateEscapes());
    }

    private static void jsonBlock() {
        String text = """
                        {
                          "name": "John Doe",
                          "age": 45,
                          "address": "Doe Street, 23, Java Town"
                        }
                """;
        System.out.println(text);
    }


    private static void emptyBlock() {
        String text = """
                """;
        System.out.println("||" + text + "||");
    }

    private static void escapeSequence() {
        String text1 = """
                abc\
                def\
                ghi\
                """;
        String text2 = """
                abc\sdef\sghi""";
        System.out.println("||" + text1 + "||"); // ||abcdefghi||
        System.out.println("||" + text2 + "||"); // ||abc def ghi||
    }
}