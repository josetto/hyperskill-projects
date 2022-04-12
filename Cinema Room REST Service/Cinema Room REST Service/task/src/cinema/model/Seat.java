package cinema.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Seat {

    private int row;
    private int column;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int price;
    public Seat(int row, int column) {
        setRow(row);
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        if (row <= 4) {
            this.price = 10;
        } else {
            this.price = 8;
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    public int getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
