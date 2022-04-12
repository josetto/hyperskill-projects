package cinema.controller;

import cinema.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    private Room room;

    @GetMapping("/seats")
    public Room getRoomInfo() {
        return room;
    }
}
