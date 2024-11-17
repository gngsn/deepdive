package com.gngsn.ch08.crm.v1;

/**
 * chapter08 - v1
 */
class MessageBus implements IMessageBus {

    public void sendEmailChangedMessage(int userId, String email) {
        System.out.printf("Sending Email from %s to %s%n", userId, email);
    }
}
