package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/timeslot")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @Operation(summary = "Create a time slot", description = "Creates a time slot")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a timeslot"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @PostMapping
    public TimeSlotDto createTimeSlot(@RequestBody @Valid CreateTimeSlotDto timeSlot) {
        return timeSlotService.createTimeSlot(timeSlot);
    }

    @Operation(summary = "Get a time slot by id", description = "Returns a time slot as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a timeslot by id"),
        @ApiResponse(responseCode = "404", description = TimeSlotService.TIMESLOT_NOT_FOUND) })
    @GetMapping("/{id}")
    public TimeSlotDto getTimeSlot(@PathVariable Long id) {
        return timeSlotService.getTimeSlotDto(id);
    }

    @Operation(summary = "Update a time slot by id", description = "Updates a time slot as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a timeslot by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @PutMapping("/{id}")
    public TimeSlotDto updateTimeSlot(@PathVariable Long id, @RequestBody @Valid CreateTimeSlotDto timeSlot) {
        return timeSlotService.updateTimeSlot(id, timeSlot);
    }

    @Operation(summary = "Delete a time slot by id", description = "Deletes a time slot as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a timeslot by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found") })
    @DeleteMapping("/{id}")
    public void deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
    }

    @Operation(summary = "Get all time slots", description = "Returns all time slots")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all timeslots") })
    @GetMapping()
    public List<TimeSlotDto> getAllTimeSlots() {
        return timeSlotService.getAllTimeSlots();
    }
}
