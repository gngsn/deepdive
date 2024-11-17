package com.gngsn.ch08.crm.v1;

import java.util.Objects;

/**
 * chapter08 - v1
 */
public class Company {
    public String DomainName;
    public int NumberOfEmployees;

    public Company(String domainName, int numberOfEmployees) {
        DomainName = domainName;
        NumberOfEmployees = numberOfEmployees;
    }

    public int getNumberOfEmployees() {
        return NumberOfEmployees;
    }

    public void changeNumberOfEmployees(int delta) {
        assert NumberOfEmployees + delta >= 0;
        NumberOfEmployees += delta;
    }

    public boolean isEmailCorporate(String email) {
        String emailDomain = email.split("@")[1];
        return Objects.equals(emailDomain, DomainName);
    }

    @Override
    public String toString() {
        return "Company{" +
                "DomainName='" + DomainName + '\'' +
                ", NumberOfEmployees=" + NumberOfEmployees +
                '}';
    }
}