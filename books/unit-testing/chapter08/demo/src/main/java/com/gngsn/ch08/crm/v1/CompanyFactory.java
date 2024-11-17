package com.gngsn.ch08.crm.v1;

/**
 * chapter08 - v1
 */
public class CompanyFactory {
    public static Company create(Object[] data) {
        assert data.length >= 2;

        String companyDomainName = (String) data[0];
        int numberOfEmployees = (int) data[1];

        return new Company(companyDomainName, numberOfEmployees);
    }
}
