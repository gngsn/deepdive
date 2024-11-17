package com.gngsn.chapter1.v3;

import java.util.ArrayList;
import java.util.List;

/**
 * Version3.
 * 관람객이 소극장에 입장하기 위한 매표소 - 티켓 교환 or 구매
 */
public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public void sellTicketTo(Audience audience) {
        plusAmount(audience.buy(getTickets()));
    }

    public Ticket getTickets() {
        return tickets.remove(0);
    }

    public void minusAmount(Long amount) {
        this.amount -= amount;
    }

    private void plusAmount(Long amount) {
        this.amount += amount;
    }

}
