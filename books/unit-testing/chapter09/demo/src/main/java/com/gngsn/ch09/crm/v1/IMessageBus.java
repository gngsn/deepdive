package com.gngsn.ch09.crm.v1;

public interface IMessageBus {
    void sendEmailChangedMessage(int userId, String mail);
}
