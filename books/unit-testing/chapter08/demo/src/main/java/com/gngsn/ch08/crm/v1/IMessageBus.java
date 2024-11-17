package com.gngsn.ch08.crm.v1;

public interface IMessageBus {
    void sendEmailChangedMessage(int userId, String mail);
}
