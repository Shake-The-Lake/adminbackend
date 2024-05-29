package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Create a booking", description = "Creates a booking")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a booking"),
        @ApiResponse(responseCode = "404", description = "Related Entity not found", content = @Content(
            schema = @Schema(implementation = String.class))) })
    @PostMapping()
    public BookingDto createBooking(@RequestBody @Valid CreateBookingDto booking) {
        return bookingService.createBooking(booking);
    }

    @Operation(summary = "Get a booking by id", description = "Returns a booking as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved a booking by id"),
        @ApiResponse(responseCode = "404", description = BookingService.BOOKING_NOT_FOUND,
            content = @Content(schema = @Schema(implementation = String.class))) })
    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id) {
        return bookingService.getBookingDto(id);
    }

    @Operation(summary = "Update a booking by id", description = "Updates a booking as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully updated a booking by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found", content = @Content(
            schema = @Schema(implementation = String.class))) })
    @PutMapping("/{id}")
    public BookingDto updateBooking(@PathVariable Long id, @RequestBody @Valid CreateBookingDto booking) {
        return bookingService.updateBooking(id, booking);
    }

    @Operation(summary = "Delete a booking by id", description = "Deletes a booking as per the id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully deleted a booking by id"),
        @ApiResponse(responseCode = "404", description = "Related entity not found", content = @Content(
            schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "This is still related to other entites") })
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @Operation(summary = "Get all bookings", description = "Returns all bookings")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully retrieved all bookings") })
    @GetMapping()
    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
