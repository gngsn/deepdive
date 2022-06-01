package chapter01.ticket.v2;

import chapter01.ticket.v1.Audience;

public class Theater {
    private TicketSeller seller;

    public Theater(TicketSeller seller) {
        this.seller = seller;
    }

    public void enter(Audience audience) {
        seller.sellTo(audience);
    }
}