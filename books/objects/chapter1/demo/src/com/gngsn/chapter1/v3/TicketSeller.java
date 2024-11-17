package com.gngsn.chapter1.v3;

/**
 * Version3.
 * 매표소에 고객을 전달하는 판매원
 */

public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}