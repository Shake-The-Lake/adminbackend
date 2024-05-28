package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.service.BoatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/boat")
public class BoatController {

    private final BoatService boatService;

    @Operation(summary = "Create a boat", description = "Creates a boat")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a boat"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public BoatDto createBoat(@RequestBody @Valid CreateBoatDto getBoatDto) {
        return boatService.createBoat(getBoatDto);
    }

    @Operation(summary = "Get a boat by id", description = "Returns a boat as per the id", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects", required = false, example = "timeSlots,activityType", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a boat by id"),
        @ApiResponse(responseCode = "404", description = BoatService.BOAT_NOT_FOUND) })
    @GetMapping("/{id}")
    public BoatDto getBoat(@PathVariable Long id, @RequestParam(required = false) Optional<String> expand) {
        return boatService.getBoatWithDetails(id, expand);
    }

    @Operation(summary = "Update a boat by id", description = "Updates a boat as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a boat by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @PutMapping("/{id}")
    public BoatDto updateBoat(@PathVariable Long id, @RequestBody @Valid CreateBoatDto getBoatDto) {
        return boatService.updateBoat(id, getBoatDto);
    }

    @Operation(summary = "Delete a boat by id", description = "Deletes a boat as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a boat by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @DeleteMapping("/{id}")
    public void deleteBoat(@PathVariable Long id) {
        boatService.deleteBoat(id);
    }

    @Operation(summary = "Get all boats", description = "Returns a list of all boats", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects", required = false, example = "timeSlots, activityType", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all boats") })
    @GetMapping()
    public List<BoatDto> getAllBoats(@RequestParam(required = false) Optional<String> expand) {
        return boatService.getBoatsWithDetails(expand);
    }

}
