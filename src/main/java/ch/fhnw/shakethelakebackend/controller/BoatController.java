package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.PostBoatDto;
import ch.fhnw.shakethelakebackend.service.BoatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/boat")
public class BoatController {

    private final BoatService boatService;

    @Operation(summary = "Create a boat", description = "Creates a boat")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully created a boat")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public BoatDto createBoat(@RequestBody @Valid PostBoatDto getBoatDto) {
        return boatService.createBoat(getBoatDto);
    }

    @Operation(summary = "Get a boat by id", description = "Returns a boat as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved a boat by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The boat was not found")
    })
    @GetMapping("/{id}")
    public BoatDto getBoat(@PathVariable Long id) {
        return boatService.getBoatDto(id);
    }


    @Operation(summary = "Update a boat by id", description = "Updates a boat as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated a boat by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The boat was not found")
    })
    @PutMapping("/{id}")
    public BoatDto updateBoat(@PathVariable Long id, @RequestBody @Valid PostBoatDto getBoatDto) {
        return boatService.updateBoat(id, getBoatDto);
    }

    @Operation(summary = "Delete a boat by id", description = "Deletes a boat as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted a boat by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The boat was not found")
    })
    @DeleteMapping("/{id}")
    public void deleteBoat(@PathVariable Long id) {
        boatService.deleteBoat(id);
    }

    @Operation(summary = "Get all boats", description = "Returns a list of all boats")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all boats")
    })
    @GetMapping()
    public List<BoatDto> getAllBoats() {
        return boatService.getAllBoats();
    }

}
