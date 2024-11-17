package com.gngsn.chapter1.v2;

/**
 * Version2.
 * 소극장
 * 입장: 관람객의 가방 안에 초대장을 확인 후, 존재 시 티켓을 가방 안에 넣어주고, 존재하지 않을 시 티켓을 판매 (티켓 금액 차감 + 매표소 금액 증가)
 */
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }
}