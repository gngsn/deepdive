package com.gngsn.ch09.crm.v1.event;

import com.gngsn.ch09.crm.v1.user.UserType;

public class UserTypeChangedEvent implements IDomainEvent {

    private final int userId;
    private final UserType oldType;
    private final UserType newType;

    public UserTypeChangedEvent(int userId, UserType oldType, UserType newType) {
        this.userId = userId;
        this.oldType = oldType;
        this.newType = newType;
    }

    public int getUserId() {
        return userId;
    }

    public UserType getOldType() {
        return oldType;
    }

    public UserType getNewType() {
        return newType;
    }
}
