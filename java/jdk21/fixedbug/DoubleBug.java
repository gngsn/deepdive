package fixedbug;

/**
 * setting:
 *   - java-17-home: JAVA17HOME=~/Library/Java/JavaVirtualMachines/temurin-17.0.8/Contents/Home/bin
 *   - java-21-home: JAVA21HOME=~/Library/Java/JavaVirtualMachines/openjdk-21/Contents/Home/bin
 *   - working-dir: cd TIL/Java/jdk21/fixedbug
 *
 * 📌 JDK 17
 * compile: $JAVA17HOME/javac DoubleBug.java
 * running: $JAVA17HOME/java DoubleBug
 * -
 * Result: 9.999999999999999E22
 *
 * 📌 JDK 21
 * compile: JAVA21HOME/javac DoubleBug.java
 * running: JAVA21HOME/java DoubleBug
 * -
 * Result: 1.0E23
 */
public class DoubleBug {
    public static void main(String[] args) {
        String str = Double.toString(1e23);
        System.out.println(str);
    }
}