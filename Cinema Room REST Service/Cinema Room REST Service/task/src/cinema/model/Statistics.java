package cinema.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistics {
    private int current_income;
    @JsonProperty("number_of_available_seats")
    private int availableSeats;
    @JsonProperty("number_of_purchased_tickets")
    private int purchasedTickets;

    public Statistics(int current_income, int availableSeats, int purchasedTickets) {
        this.current_income = current_income;
        this.availableSeats = availableSeats;
        this.purchasedTickets = purchasedTickets;
    }

    public Statistics() {
    }

    public int getCurrent_income() {
        return current_income;
    }

    public void setCurrent_income(int current_income) {
        this.current_income = current_income;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getPurchasedTickets() {
        return purchasedTickets;
    }

    public void setPurchasedTickets(int purchasedTickets) {
        this.purchasedTickets = purchasedTickets;
    }
}
