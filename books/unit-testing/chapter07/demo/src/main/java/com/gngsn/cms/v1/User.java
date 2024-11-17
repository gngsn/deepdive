package com.gngsn.cms.v1;

public class User {
    private int userId;
    private String email;
    private UserType type;

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


    public void changeEmail(int userId, String newEmail) throws Exception {
        Object[] data = Database.getUserById(userId);
        setUserId(userId);
        setEmail((String) data[1]);
        setType((UserType) data[2]);

        if (getEmail().equals(newEmail))
            return;

        Object[] companyData = Database.getCompany();
        String companyDomainName = (String) companyData[0];
        int numberOfEmployees = (int) companyData[1];

        String emailDomain = newEmail.split("@")[1];
        boolean isEmailCorporate = emailDomain.equals(companyDomainName);


        UserType newType =
                isEmailCorporate ? UserType.EMPLOYEE : UserType.CUSTOMER;

        if (!getType().equals(newType)) {
            int delta = newType == UserType.EMPLOYEE ? 1 : -1;
            int newNumber = numberOfEmployees + delta;
            Database.saveCompany(newNumber);
        }

        setEmail(newEmail);
        setType(newType);

        Database.saveUser(this);

        MessageBus.sendEmailChangedMessage(getUserId(), getEmail());
    }
}


