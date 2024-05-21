package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.service.EventService;
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
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Create an event", description = "Creates an event")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created an Event"),
        @ApiResponse(responseCode = "404", description = EventService.EVENT_NOT_FOUND) })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public EventDto createEvent(@RequestBody @Valid CreateEventDto createEventDto) {
        return eventService.createEvent(createEventDto);
    }

    @Operation(summary = "Get an event by id", description = "Returns an event as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved an event by id"),
        @ApiResponse(responseCode = "404", description = EventService.EVENT_NOT_FOUND) })
    @GetMapping("/{id}")
    public EventDto getEvent(@PathVariable Long id) {
        return eventService.getEventDto(id);
    }

    @Operation(summary = "Get all events", description = "Returns all events")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all events") })
    @GetMapping()
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Operation(summary = "Delete an event by id", description = "Deletes an event as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted an event by id"),
        @ApiResponse(responseCode = "404", description = EventService.EVENT_NOT_FOUND) })
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @Operation(summary = "Update an event by id", description = "Updates an event as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated an event by id"),
        @ApiResponse(responseCode = "404", description = EventService.EVENT_NOT_FOUND) })
    @PutMapping("/{id}")
    public EventDto updateEvent(@PathVariable Long id, @RequestBody @Valid CreateEventDto createEventDto) {
        return eventService.updateEvent(id, createEventDto);
    }
}
