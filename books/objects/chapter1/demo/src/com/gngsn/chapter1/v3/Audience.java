package com.gngsn.chapter1.v3;

/**
 * Version3.
 * 소지품을 보관하기 위한 관람객의 가방
 */
public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Long buy(Ticket ticket) {
        return bag.hold(ticket);
    }
}
