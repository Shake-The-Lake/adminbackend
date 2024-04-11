package ch.fhnw.shakethelakebackend.controller;


import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.service.BoatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/boat")
public class BoatController {

    private final BoatService boatService;

    @PostMapping()
    public Boat createBoat(@RequestBody @Valid Boat boat) {
        return boatService.createBoat(boat);
    }

    @GetMapping("/{id}")
    public Boat getBoat(@PathVariable Long id) {
        return boatService.getBoat(id);
    }

    @PutMapping("/{id}")
    public Boat updateBoat(@PathVariable Long id, @RequestBody @Valid Boat boat) {
        return boatService.updateBoat(id, boat);
    }

    @DeleteMapping("/{id}")
    public Boat deleteBoat(@PathVariable Long id) {
        return boatService.deleteBoat(id);
    }

}
