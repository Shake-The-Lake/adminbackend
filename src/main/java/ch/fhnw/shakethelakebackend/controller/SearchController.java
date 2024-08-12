package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Get search filter parameters", description = "Returns all search filter parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all search filter parameters") })
    @GetMapping("/parameters")
    public SearchParameterDto getSearchParameters() {
        return searchService.getSearchParameters();
    }

    @Operation(summary = "Search bookings", description = "Returns find bookings", parameters = {
        @Parameter(name = "personName", description = "Search by person name", required = false,
            example = "John Doe", schema = @Schema(type = "string")),
        @Parameter(name = "boatName", description = "filters by boat name", required = false,
            example = "Boat 1", schema = @Schema(type = "string")),
        @Parameter(name = "from", description = "filters by date from", required = false,
            example = "2024-06-09T15:13:32.297Z", schema = @Schema(type = "ZonedDateTime")),
        @Parameter(name = "to", description = "filters by date to", required = false,
            example = "2024-06-09T16:13:32.297Z", schema = @Schema(type = "ZonedDateTime")),
        @Parameter(name = "activity", description = "filters by activity it", required = false,
            example = "1", schema = @Schema(type = "long")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a booking by id"),
        @ApiResponse(responseCode = "404", description = BookingService.BOOKING_NOT_FOUND, content = @Content(
            schema = @Schema(implementation = String.class))) })
    @GetMapping()
    public List<BookingDto> getBookingSearch(@RequestParam(required = false) Optional<String> personName,
        @RequestParam(required = false) Optional<String> boatName,
        @RequestParam(required = false) Optional<ZonedDateTime> from,
        @RequestParam(required = false) Optional<ZonedDateTime> to,
        @RequestParam(required = false) Optional<Long> activity) {
        return searchService.getSearch(personName, boatName, from, to, activity);
    }
}
