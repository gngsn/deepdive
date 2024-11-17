package com.gngsn.chapter1.v2;

/**
 * Version2.
 * 매표소에서 초대장을 티켓으로 교환 or 판매하는 판매원
 * - 자신이 일하는 매표소를 알아야 함
 */
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    /*
      외부에서 알 필요 없음 - 접근 차단
    public TicketOffice getTicketOffice() {
        return ticketOffice;
    }
     */

    public void sellTo(Audience audience) {
        ticketOffice.plusAmount(audience.buy(ticketOffice.getTickets()));
    }
}