package com.gngsn.chapter1.v2;

/**
 * Version2.
 * 소지품을 보관하기 위한 관람객의 가방
 */
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    /*
    public Bag getBag() {
        return bag;
    }
     */

    public Long buy(Ticket ticket) {
        if (bag.hasInvitation()) {
            bag.setTicket(ticket);
            return 0L;
        } else {
            bag.setTicket(ticket);
            bag.minusAmount(ticket.getFee());
            return ticket.getFee();
        }
    }
}
