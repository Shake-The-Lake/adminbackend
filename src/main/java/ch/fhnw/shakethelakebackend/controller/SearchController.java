package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.service.BookingService;
import ch.fhnw.shakethelakebackend.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * Get search filter parameters
     *
     * @param eventId the id of the event
     * @return the search filter parameters
     */
    @Operation(summary = "Get search filter parameters", description = "Returns all search filter parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all search filter parameters") })
    @GetMapping("{eventId}/parameters")
    public SearchParameterDto getSearchParameters(@PathVariable Long eventId) {
        return searchService.getSearchParameters(eventId);
    }

    /**
     * Get bookings by search
     *
     * @param eventId the id of the event
     * @param personName the person name
     * @param boatId the boat id
     * @param from the date from
     * @param to the date to
     * @param activityId the activity id
     * @return the bookings
     */
    @Operation(summary = "Search bookings", description = "Returns find bookings", parameters = {
        @Parameter(name = "personName", description = "Search by person name", required = false,
            example = "John", schema = @Schema(type = "string")),
        @Parameter(name = "boatId", description = "filters by boat id", required = false,
            example = "1", schema = @Schema(type = "long")),
        @Parameter(name = "from", description = "filters by date from", required = false,
            example = "08:00:00", schema = @Schema(type = "LocalTime")),
        @Parameter(name = "to", description = "filters by date to", required = false,
            example = "09:00:00", schema = @Schema(type = "LocalTime")),
        @Parameter(name = "activityId", description = "filters by activity id", required = false,
            example = "1", schema = @Schema(type = "long")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a booking by id"),
        @ApiResponse(responseCode = "404", description = BookingService.BOOKING_NOT_FOUND, content = @Content(
            schema = @Schema(implementation = String.class))) })
    @GetMapping("/{eventId}")
    public List<SearchDto> getBookingSearch(@PathVariable Long eventId,
        @RequestParam(required = false) Optional<String> personName,
        @RequestParam(required = false) Optional<Long> boatId,
        @RequestParam(required = false) Optional<LocalTime> from,
        @RequestParam(required = false) Optional<LocalTime> to,
        @RequestParam(required = false) Optional<Long> activityId) {
        return searchService.getSearch(eventId, personName, boatId, from, to, activityId);
    }
}
