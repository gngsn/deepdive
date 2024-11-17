package com.gngsn.ch09.crm.v1.logger;

import com.gngsn.ch09.crm.v1.IMessageBus;
import com.gngsn.ch09.crm.v1.event.EmailChangedEvent;
import com.gngsn.ch09.crm.v1.event.IDomainEvent;
import com.gngsn.ch09.crm.v1.event.UserTypeChangedEvent;

import java.util.List;

public class EventDispatcher {
    private final IMessageBus messageBus;
    private final IDomainLogger domainLogger;

    public EventDispatcher(IMessageBus messageBus, IDomainLogger domainLogger) {
        this.domainLogger = domainLogger;
        this.messageBus = messageBus;
    }

    public void Dispatch(List<IDomainEvent> events) {
        for (IDomainEvent ev : events) {
            dispatch(ev);
        }
    }

    public void dispatch(IDomainEvent ev) {
        switch (ev) {
            case EmailChangedEvent emailChangedEvent:
                messageBus.sendEmailChangedMessage(emailChangedEvent.getUserId(), emailChangedEvent.getNewEmail());
                break;

            case UserTypeChangedEvent userTypeChangedEvent:
                domainLogger.userTypeHasChanged(
                        userTypeChangedEvent.getUserId(),
                        userTypeChangedEvent.getOldType(),
                        userTypeChangedEvent.getNewType());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ev);
        }
    }
}