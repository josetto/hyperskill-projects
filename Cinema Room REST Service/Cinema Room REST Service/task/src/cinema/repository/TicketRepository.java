package cinema.repository;

import cinema.model.Ticket;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TicketRepository {

    private List<Ticket> tickets;

    public TicketRepository(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }
    public void remove(Ticket ticket) {
        tickets.remove(ticket);
    }

    public Optional<Ticket> getTicketByToken(UUID token) {
        Optional<Ticket> ticket = tickets
                .stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();
        return ticket;
    }
}
