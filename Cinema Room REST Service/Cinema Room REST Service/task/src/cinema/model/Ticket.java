package cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Ticket {

    private UUID token = UUID.randomUUID();
    @JsonIgnore
    private int row;
    @JsonIgnore
    private int column;
    @JsonIgnore
    private int price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "ticket")
    private Seat seat;
    public Ticket(Seat seat) {
        setSeat(seat);
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        int row = seat.getRow();
        if (row <= 4) {
            this.price = 10;
        } else {
            this.price = 8;
        }
        this.seat = seat;
        this.row = seat.getRow();
        this.column = seat.getColumn();
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(token, ticket.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
