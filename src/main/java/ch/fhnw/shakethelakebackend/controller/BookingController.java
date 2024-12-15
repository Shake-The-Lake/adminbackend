package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.service.BookingService;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    /**
     * Create a booking
     *
     * @param booking the booking to create
     * @return the created booking
     */
    @Operation(summary = "Create a booking", description = "Creates a booking")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a booking"),
        @ApiResponse(responseCode = "404", description = "Related Entity not found", content = @Content(
            schema = @Schema(implementation = String.class))) })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public BookingDto createBooking(@RequestBody @Valid CreateBookingDto booking) {
        return bookingService.createBooking(booking);
    }

    /**
     * Get a booking by id
     *
     * @param id the id of the booking
     * @param expand expand the response with more details from related objects
     * @return the booking
     */
    @Operation(summary = "Get a booking by id", description = "Returns a booking as per the id", parameters = {
        @Parameter(name = "expand", description = "Expand the response with more details from related objects",
            required = false, example = "person,timeSlot,timeSlot.boat", schema = @Schema(type = "string")) })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a booking by id"),
        @ApiResponse(responseCode = "404", description = BookingService.BOOKING_NOT_FOUND, content = @Content(
            schema = @Schema(implementation = String.class))) })
    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id, @RequestParam(required = false) Optional<String> expand) {
        return bookingService.getBookingWithDetails(id, expand);
    }

    /**
     * Update a booking by id
     *
     * @param id the id of the booking
     * @param booking the booking to update
     * @return the updated booking
     */
    @Operation(summary = "Update a booking by id", description = "Updates a booking as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a booking by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found", content = @Content(
            schema = @Schema(implementation = String.class))) })
    @PutMapping("/{id}")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody @Valid CreateBookingDto booking) {
        return bookingService.updateBooking(id, booking);
    }

    /**
     * Delete a booking by id
     *
     * @param id the id of the booking
     */
    @Operation(summary = "Delete a booking by id", description = "Deletes a booking as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a booking by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found", content = @Content(
            schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "This is still related to other entites") })
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    /**
     * Get all bookings
     *
     * @return all bookings
     */
    @Operation(summary = "Get all bookings", description = "Returns all bookings")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all bookings") })
    @GetMapping()
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
