package com.gngsn.ch09.crm.v1.user;

import com.gngsn.ch09.crm.v1.company.Company;
import com.gngsn.ch09.crm.v1.event.EmailChangedEvent;
import com.gngsn.ch09.crm.v1.event.IDomainEvent;

/**
 * chapter09 - v1
 */
public class User {
    public IDomainEvent domainEvents;
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

    public String canChangeEmail() {
        return null;
    }

    public EmailChangedEvent[] getEmailChangedEvents() {
        return new EmailChangedEvent[0];
    }
}
