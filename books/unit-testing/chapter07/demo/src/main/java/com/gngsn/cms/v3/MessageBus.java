package com.gngsn.cms.v3;

class MessageBus {

    public static void sendEmailChangedMessage(int userId, String email) {
        System.out.printf("Sending Email from %s to %s%n", userId, email);
    }
}
