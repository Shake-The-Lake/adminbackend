package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.service.ActivityTypeService;
import ch.fhnw.shakethelakebackend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@AllArgsConstructor
@RequestMapping("/activitytype")
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;

    @Operation(summary = "Get all activity types", description = "Returns all activity types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all activity types") })
    @GetMapping
    public List<ActivityTypeDto> getAllActivityTypes() {
        return activityTypeService.getAllActivityTypes();
    }

    @Operation(summary = "Get an activity type by id", description = "Returns an activity type as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved an activity type by id"),
        @ApiResponse(responseCode = "404", description = ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND,
            content = @Content(schema = @Schema(implementation = String.class, example = "Event not found"))) })
    @GetMapping("/{id}")
    public ActivityTypeDto getActivityType(
        @PathVariable Long id) {
        return activityTypeService.getActivityTypeDto(
            id);
    }

    @Operation(summary = "Create an activity type", description = "Creates an activity type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully created an activity type"),
        @ApiResponse(responseCode = "404", description = EventService.EVENT_NOT_FOUND,
            content = @Content(schema = @Schema(implementation = String.class, example = "Event not found"))) })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ActivityTypeDto createActivityType(
        @RequestBody @Valid CreateActivityTypeDto createActivityTypeDto) {
        return activityTypeService.createActivityType(
            createActivityTypeDto);
    }

    @Operation(summary = "Update an activity type", description = "Updates an activity type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated an activity type"),
        @ApiResponse(responseCode = "404", description = ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND) })
    @PutMapping("/{id}")
    public ActivityTypeDto updateActivityType(
        @PathVariable Long id,
        @RequestBody @Valid CreateActivityTypeDto createActivityTypeDto) {
        return activityTypeService.updateActivityType(
            id, createActivityTypeDto);
    }

    @Operation(summary = "Delete an activity type", description = "Deletes an activity type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted an activity type"),
        @ApiResponse(responseCode = "404", description = ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND) })
    @DeleteMapping("/{id}")
    public void deleteActivityType(
        @PathVariable Long id) {
        activityTypeService.deleteActivityType(
            id);
    }
}
