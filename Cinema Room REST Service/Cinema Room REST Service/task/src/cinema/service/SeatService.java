package cinema.service;

import cinema.model.Seat;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    public boolean checkSeatBoundaries(Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();
        if((row > 9 || row < 1) || (column > 9 || column < 1)) {
            return false;
        }
        return true;
    }
}
