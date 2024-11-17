package com.gngsn.ch08.crm.v1;

public class EmailChangedEvent {
    private int userId;
    private String newEmail;

    public int getUserId() {
        return userId;
    }

    public String getNewEmail() {
        return newEmail;
    }
}
