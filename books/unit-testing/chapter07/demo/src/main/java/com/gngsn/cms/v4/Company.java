package com.gngsn.cms.v4;

import java.util.Objects;

public class Company {
    public String DomainName;
    public int NumberOfEmployees;

    public Company(String domainName, int numberOfEmployees) {
        DomainName = domainName;
        NumberOfEmployees = numberOfEmployees;
    }

    /**
     * changeNumberOfEmployees & isEmailCorporate
     * '묻지 말고 말하라' tell don't ask
     * - user 인스턴스는 직원 수를 변경하거나 특정 이메일이 회사 이메일인지 여부를 파악하도록 회사에 말하며, 원시 데이터를 묻지 않고 모든 작업을 자체적으로 수행
     */
    public void changeNumberOfEmployees(int delta) {
        assert NumberOfEmployees + delta >= 0;
        NumberOfEmployees += delta;
    }

    public boolean isEmailCorporate(String email) {
        String emailDomain = email.split("@")[1];
        return Objects.equals(emailDomain, DomainName);
    }

    public String getDomainName() {
        return DomainName;
    }

    public void setDomainName(String domainName) {
        DomainName = domainName;
    }

    public int getNumberOfEmployees() {
        return NumberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        NumberOfEmployees = numberOfEmployees;
    }

    @Override
    public String toString() {
        return "Company{" +
                "DomainName='" + DomainName + '\'' +
                ", NumberOfEmployees=" + NumberOfEmployees +
                '}';
    }
}