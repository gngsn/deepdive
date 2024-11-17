package com.gngsn.ch09.crm.v1;

import com.gngsn.ch09.crm.v1.company.Company;
import com.gngsn.ch09.crm.v1.user.User;

/**
 * chapter09 - v1
 */
public class Database {
    private static Object[] company;
    private static Object[] user;

    public Database() {
    }

    public Object[] getCompany() {
        return company;
    }

    public Object[] getUserById(int id) {
        return user;
    }

    public void saveCompany(Company company) {
        // save Company to Database
        System.out.printf("save Company{%s} to Database%n", company);
    }

    public void saveUser(User user) {
        // save user to Database
        System.out.printf("save User{%s} to Database%n", user.getUserId());
    }
}
