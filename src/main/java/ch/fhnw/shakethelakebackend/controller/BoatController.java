package ch.fhnw.shakethelakebackend.controller;


import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.service.BoatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/boat")
public class BoatController {

    private final BoatService boatService;

    @PostMapping()
    public Boat createBoat(@RequestBody Boat boat) {
        return boatService.createBoat(boat);
    }

    @GetMapping("/{id}")
    public Boat getBoat(@PathVariable Long id) {
        return boatService.getBoat(id);
    }

    @PutMapping("/{id}")
    public Boat updateBoat(@PathVariable Long id, @RequestBody Boat boat) {
        return boatService.updateBoat(id, boat);
    }

}
