package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.service.BoatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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

    /**
     * Create a boat
     *
     * @param getBoatDto the boat to create
     * @return the created boat
     */
    @Operation(summary = "Create a boat", description = "Creates a boat")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a boat"),
        @ApiResponse(responseCode = "404", description = "Related entity not found",
            content = @Content(mediaType = "",
            schema = @Schema(implementation = String.class))) })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public BoatDto createBoat(@RequestBody @Valid CreateBoatDto getBoatDto) {
        return boatService.createBoat(getBoatDto);
    }

    /**
     * Get a boat by id
     *
     * @param id the id of the boat
     * @param expand expand the response with more details from related objects
     * @return the boat
     */
    @Operation(summary = "Get a boat by id", description = "Returns a boat as per the id", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects",
            required = false,
            example = "timeSlots", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a boat by id"),
        @ApiResponse(responseCode = "404", description = BoatService.BOAT_NOT_FOUND, content = @Content(mediaType = "",
            schema = @Schema(implementation = String.class))) })
    @GetMapping("/{id}")
    public BoatDto getBoat(@PathVariable Long id, @RequestParam(required = false) Optional<String> expand) {
        return boatService.getBoatWithDetails(id, expand);
    }

    /**
     * Update a boat by id
     *
     * @param id the id of the boat
     * @param getBoatDto the boat to update
     * @return the updated boat
     */
    @Operation(summary = "Update a boat by id", description = "Updates a boat as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a boat by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found",
            content = @Content(mediaType = "",
            schema = @Schema(implementation = String.class))) })
    @PutMapping("/{id}")
    public BoatDto updateBoat(@PathVariable Long id, @RequestBody @Valid CreateBoatDto getBoatDto) {
        return boatService.updateBoat(id, getBoatDto);
    }

    /**
     * Delete a boat by id
     *
     * @param id the id of the boat
     */
    @Operation(summary = "Delete a boat by id", description = "Deletes a boat as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a boat by id"),
        @ApiResponse(responseCode = "409", description = "This is still related to other entites") })
    @DeleteMapping("/{id}")
    public void deleteBoat(@PathVariable Long id) {
        boatService.deleteBoat(id);
    }

    /**
     * Get all boats
     *
     * @param expand expand the response with more details from related objects
     * @return all boats
     */
    @Operation(summary = "Get all boats", description = "Returns a list of all boats", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects",
            required = false,
            example = "timeSlots", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all boats") })
    @GetMapping()
    public List<BoatDto> getAllBoats(@RequestParam(required = false) Optional<String> expand) {
        return boatService.getBoatsWithDetails(expand);
    }

}
