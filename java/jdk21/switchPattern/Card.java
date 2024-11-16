package switchPattern;

import static switchPattern.Suit.*;

sealed interface CardClassification permits Suit, Tarot { }

enum Suit implements CardClassification {CLUBS, DIAMONDS, HEARTS, SPADES}

final class Tarot implements CardClassification { }

public class Card {

    public static void main(String[] args) {
        Suit s = HEARTS;

        testforHearts(s);

        exhaustiveSwitchWithoutEnumSupport(s);
    }

    // Prior to Java 21
    static void testforHearts(Suit s) {
        switch (s) {
            case HEARTS -> System.out.println("It's a heart!");
            default -> System.out.println("Some other suit");
        }
    }

    // As of Java 21
    static void exhaustiveSwitchWithoutEnumSupport(CardClassification c) {
        switch (c) {
            case CLUBS -> {
                System.out.println("It's clubs");
            }
            case DIAMONDS -> {
                System.out.println("It's diamonds");
            }
            case HEARTS -> {
                System.out.println("It's hearts");
            }
            case SPADES -> {
                System.out.println("It's spades");
            }
            case Tarot t -> {
                System.out.println("It's a tarot");
            }
        }
    }
}
