package com.gngsn.ch09.crm.v1.event;

public class EmailChangedEvent implements IDomainEvent {
    private int userId;
    private String newEmail;

    public int getUserId() {
        return userId;
    }

    public String getNewEmail() {
        return newEmail;
    }
}
