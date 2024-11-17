package com.gngsn.ch09.crm.v1.user;

/**
 * chapter09 - v1
 */
public class UserFactory {
    public static User create(Object[] data) {
        assert data.length >= 3;

        int id = (int)data[0];
        String email = (String)data[1];
        UserType type = (UserType)data[2];

        return new User(id, email, type);
    }

    public static User create(int id, String email, UserType type) {
        return new User(id, email, type);
    }
}
