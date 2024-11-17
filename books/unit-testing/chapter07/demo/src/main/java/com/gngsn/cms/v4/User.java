package com.gngsn.cms.v4;

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

    /**
     * Version4. 회사 데이터를 처리하는 대신 Company 에게 두가지 중요한 작업을 위임
     * - 이메일이 회사 이메일인지 결정하는 것
     * - 회사의 직원 수를 변경하는것
     */
    public void changeEmail(String newEmail, Company company) {
        if (email.equals(newEmail))
            return;

        UserType newType = company.isEmailCorporate(newEmail) ? UserType.EMPLOYEE : UserType.CUSTOMER;

        if (!getType().equals(newType)) {
            int delta = newType == UserType.EMPLOYEE ? 1 : -1;
            company.changeNumberOfEmployees(delta);
        }

        setEmail(newEmail);
        setType(newType);
    }
}


