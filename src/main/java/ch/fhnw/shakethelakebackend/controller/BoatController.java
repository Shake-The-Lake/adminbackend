package ch.fhnw.shakethelakebackend.controller;


import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.service.BoatService;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@OpenAPI30

@RequestMapping("/boat")
public class BoatController {
    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    private final BoatService boatService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Boat createBoat(@RequestBody Boat boat) {
        return boatService.createBoat(boat);
    }

    @GetMapping("/{id}")
    public Boat getBoat(@PathVariable Long id) {
        return boatService.getBoat(id);
    }

}
