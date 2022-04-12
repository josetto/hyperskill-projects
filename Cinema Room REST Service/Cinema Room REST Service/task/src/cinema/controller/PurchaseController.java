package cinema.controller;

import cinema.repository.TicketRepository;
import cinema.model.*;
import cinema.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PurchaseController {

    @Autowired
    private Room room;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatService seatService;

    @PostMapping("/purchase")
    public ResponseEntity purchase(@RequestBody Seat seat) {

        boolean validSeat = seatService.checkSeatBoundaries(seat);
        if (!validSeat) {
            Map<String, String> map = new HashMap<>();
            map.put("error","The number of a row or a column is out of bounds!");
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
        }
        Ticket ticket = new Ticket(seat);
        List<Seat> availableSeats = room.getAvailableSeats();

        if (!availableSeats.contains(seat)) {
            Map<String, String> map = new HashMap<>();
            map.put("error","The ticket has been already purchased!");
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
        }

        availableSeats.remove(seat);
        ticketRepository.add(ticket);
        return new ResponseEntity(ticket, HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity returnTicket(@RequestBody Token token) {
        if(token == null) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Wrong token!");
            return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
        }

        Optional<Ticket> optTicket = ticketRepository.getTicketByToken(token.getToken());

        if(optTicket.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "Wrong token!");
            return new ResponseEntity(map, HttpStatus.BAD_REQUEST);
        }
        Ticket ticket = optTicket.get();
        Seat seat = ticket.getSeat();
        room.getAvailableSeats().add(seat);
        ticketRepository.remove(ticket);

        Map<String, Seat> map = new HashMap<>();
        map.put("returned_ticket", seat);

        return new ResponseEntity(map, HttpStatus.OK);
    }

    @PostMapping("/stats")
    public ResponseEntity stats(@RequestParam(required = false) String password) {
        if(password == null) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "The password is wrong!");
            return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
        }

        if(!"super_secret".equals(password)) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "The password is wrong!");
            return new ResponseEntity(map, HttpStatus.UNAUTHORIZED);
        }

        Statistics stats = new Statistics();
        List<Ticket> purchasedTickets =ticketRepository.getTickets();
        int income = purchasedTickets.stream()
                .mapToInt(ticket -> ticket.getPrice())
                .sum();

        int availableSeats = room.getAvailableSeats().size();
        int purchasedAmount = purchasedTickets.size();

        stats.setCurrent_income(income);
        stats.setAvailableSeats(availableSeats);
        stats.setPurchasedTickets(purchasedAmount);
        return new ResponseEntity(stats, HttpStatus.OK);
    }
}
