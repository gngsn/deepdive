package com.gngsn.cms.v4;

class Database {
    private static Object[] company;
    private static Object[] user;

    public static Object[] getCompany() {
        return company;
    }

    public static Object[] getUserById(int id) {
        return user;
    }

    public static void saveCompany(Company company) {
        // save Company to Database
        System.out.printf("save Company{%s} to Database%n", company);
    }

    public static void saveUser(User user) {
        // save user to Database
        System.out.printf("save User{%s} to Database%n", user.getUserId());
    }
}
