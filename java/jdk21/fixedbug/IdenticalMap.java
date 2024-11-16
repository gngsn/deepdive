package fixedbug;

import java.util.IdentityHashMap;

/**
 * IdentityHashMap 사용해도 String 인 Key 값 비교 시 Identical 비교에 정합성이 맞지 않음
 * 📌 JDK 17
 * Result: assertion passes: false
 * .
 * 📌 JDK 21
 * Result: assertion passes: true
 */
public class IdenticalMap {

    public static void main(String args[]) {
        var users = new IdentityHashMap<String, User>();
        String key = "abc";

        users.put(key, new User("Sun Park"));
        // try to remove an EQUAL but not IDENTICAL combination
        var removed = users.remove(key, new User("Sun Park"));

        // according to the `IdentityHashMap` contract there should've been no removal
        // ❌ up to Java 19: assertion fails
        // ✅ since Java 20: assertion passes
        assert !removed;
        System.out.println("assertion passes: " + !removed);
    }
}

record User(String name) { }
