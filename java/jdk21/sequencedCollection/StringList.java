package sequencedCollection;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

/**
 * ❯  $JAVA17HOME/javac sequencedCollection/StringList.java
 * ❯  $JAVA21HOME/javac sequencedCollection/StringList.java
 * sequencedCollection/StringList.java:21: error: getFirst() in StringList cannot implement getFirst() in List
 *     public Optional<String> getFirst() {
 *                             ^
 *   return type Optional<String> is not compatible with String
 *   where E is a type-variable:
 *     E extends Object declared in interface List
 * 1 error
 */
public class StringList extends AbstractList<String> implements List<String> {

    transient String[] elementData;

    @Override
    public String set(int index, String element) {
        return elementData[index] = element;
    }

    @Override
    public String get(int index) {
        return elementData[index];
    }

//    /**
//     * Error since jdk 21
//     */
//    public Optional<String> getFirst() {
//        return size() == 0
//                ? Optional.empty()
//                : Optional.of(get(0));
//    }

    @Override
    public int size() {
        return 0;
    }
}