package com.gngsn.chapter1.v3;

/**
 * Version3.
 * 관람객이 가지고 올 수 있는 소지품 - 초대장, 현금, 티켓
 */
public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    // v3
    public Long hold(Ticket ticket) {
        if (hasInvitation()) {
            setTicket(ticket);
            return 0L;
        } else {
            setTicket(ticket);
            minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }

    public Bag(long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    // change to private
    private boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    // change to private
    private void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    // change to private
    private void minusAmount(Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(Long amount) {
        this.amount += amount;
    }
}
