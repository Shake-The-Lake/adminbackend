package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.MoveTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.service.TimeSlotService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/timeslot")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    /**
     * Creates a time slot
     *
     * @param timeSlot the time slot to create
     * @return the created time slot
     */
    @Operation(summary = "Create a time slot", description = "Creates a time slot")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a timeslot"),
        @ApiResponse(responseCode = "404", description = "Related entity not found",
            content = @Content(schema = @Schema(implementation = String.class))) })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeSlotDto createTimeSlot(@RequestBody @Valid CreateTimeSlotDto timeSlot) {
        return timeSlotService.createTimeSlot(timeSlot);
    }

    /**
     * Get a time slot by id
     *
     * @param id the id of the time slot
     * @param expand expand the response with more details from related objects
     * @return the time slot
     */
    @Operation(summary = "Get a time slot by id", description = "Returns a time slot as per the id", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects",
            required = false,
            example = "activityType,boat,bookings", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a timeslot by id"),
        @ApiResponse(responseCode = "404", description = TimeSlotService.TIMESLOT_NOT_FOUND,
            content = @Content(schema = @Schema(implementation = String.class))) })
    @GetMapping("/{id}")
    public TimeSlotDto getTimeSlot(@PathVariable Long id, Optional<String> expand) {
        return timeSlotService.getTimeSlotDto(id, expand);
    }

    /**
     * Move a time slot by id including the timeslot that follow
     *
     * @param id the id of the time slot
     * @param timeSlot the moveTimeSlotDto
     * @return the moved time slot
     */
    @Operation(summary = "Move a time slot by id with its successing timeSlots",
        description = "Moves a time slot as per the id with its successing timeSlots")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully moved timeslots"),
        @ApiResponse(responseCode = "404", description = TimeSlotService.TIMESLOT_NOT_FOUND,
            content = @Content(schema = @Schema(implementation = String.class))) })
    @PostMapping("/{id}/move")
    public List<TimeSlotDto> moveTimeSlot(@PathVariable Long id, @RequestBody @Valid MoveTimeSlotDto timeSlot) {
        return timeSlotService.moveTimeSlot(id, timeSlot);
    }

    /**
     * Update a time slot by id
     *
     * @param id the id of the time slot
     * @param timeSlot the time slot to update
     * @return the updated time slot
     */
    @Operation(summary = "Update a time slot by id", description = "Updates a time slot as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a timeslot by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found",
            content = @Content(schema = @Schema(implementation = String.class))) })
    @PutMapping("/{id}")
    public TimeSlotDto updateTimeSlot(@PathVariable Long id, @RequestBody @Valid CreateTimeSlotDto timeSlot) {
        return timeSlotService.updateTimeSlot(id, timeSlot);
    }

    /**
     * Delete a time slot by id
     *
     * @param id the id of the time slot
     */
    @Operation(summary = "Delete a time slot by id", description = "Deletes a time slot as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a timeslot by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "This is still related to other entites") })
    @DeleteMapping("/{id}")
    public void deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
    }

    /**
     *
     * Get all time slots
     *
     * @param expand response with more details from related objects
     * @param eventId filter the response with eventId
     * @return all time slots
     */
    @Operation(summary = "Get all time slots", description = "Returns all time slots", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects",
            required = false,
            example = "activiyType", schema = @Schema(type = "string")),
        @Parameter(name = "eventId", description = "Filter the response with eventId",
            required = false,
            example = "1", schema = @Schema(type = "long")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all timeslots") })
    @GetMapping()
    public List<TimeSlotDto> getAllTimeSlots(Optional<String> expand, Optional<Long> eventId) {
        return timeSlotService.getAllTimeSlots(expand, eventId);
    }
}
