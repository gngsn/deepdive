package com.gngsn.cms.v3;

public class User {
    private int userId;
    private String email;
    private UserType type;

    public User(int userId, String email, UserType type) {
        this.userId = userId;
        this.email = email;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }


    public int changeEmail(String newEmail, String companyDomainName, int numberOfEmployees) throws Exception {
        if (email.equals(newEmail))
            return numberOfEmployees;

        String emailDomain = newEmail.split("@")[1];
        boolean isEmailCorporate = emailDomain.equals(companyDomainName);

        UserType newType = isEmailCorporate ? UserType.EMPLOYEE : UserType.CUSTOMER;

        if (!getType().equals(newType)) {
            int delta = newType == UserType.EMPLOYEE ? 1 : -1;
            int newNumber = numberOfEmployees + delta;
            Database.saveCompany(newNumber);
        }

        setEmail(newEmail);
        setType(newType);

        return numberOfEmployees;
    }
}


