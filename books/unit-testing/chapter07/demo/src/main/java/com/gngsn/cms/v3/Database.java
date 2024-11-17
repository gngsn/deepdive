package com.gngsn.cms.v3;

class Database {
    private static Object[] company;
    private static Object[] user;

    public static Object[] getCompany() {
        return company;
    }

    public static Object[] getUserById(int id) {
        return user;
    }

    public static void saveCompany(int number) {
        // save Company to Database
        System.out.printf("save Company{%s} to Database%n", number);
    }

    public static void saveUser(User user) {
        // save user to Database
        System.out.printf("save User{%s} to Database%n", user.getUserId());
    }
}
