package chapter01.ticket.v2;

import chapter01.ticket.v1.Audience;
import chapter01.ticket.v1.Ticket;
import chapter01.ticket.v1.TicketBooth;

public class TicketSeller {
    private TicketBooth ticketBooth;

    public TicketSeller(TicketBooth ticketBooth) {
        this.ticketBooth = ticketBooth;
    }

//    public TicketBooth getTicketBooth() {
//        return ticketBooth;
//    }

    /**
     * v2: from v1 Theater Class - enter()
     *
     * @param audience
     */
    public void sellTo(Audience audience) {
        Ticket ticket = ticketBooth.getTicket();

        if (audience.getBag().hasInvitation()) {
            audience.getBag().setTicket(ticket);
        } else  {
            audience.getBag().minusCash(ticket.getFee());
            ticketBooth.plusPrice(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
