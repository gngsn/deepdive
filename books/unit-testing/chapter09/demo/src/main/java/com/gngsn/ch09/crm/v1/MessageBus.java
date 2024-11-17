package com.gngsn.ch09.crm.v1;

public class MessageBus implements IMessageBus{
    private final IBus bus;

    public MessageBus(IBus bus) {
        this.bus = bus;
    }

    @Override
    public void sendEmailChangedMessage(int userId, String newEmail) {
        bus.Send("Type: USER EMAIL CHANGED;\nId: %s;\nNewEmail: %s".formatted(userId, newEmail));
    }
}


