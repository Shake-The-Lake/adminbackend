package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.service.BookingService;
import ch.fhnw.shakethelakebackend.service.TimeSlotSubscriptionService;
import ch.fhnw.shakethelakebackend.service.FirebaseService;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final FirebaseService firebaseService;
    private final TimeSlotSubscriptionService timeSlotSubscriptionService;
    private final BookingService bookingService;

    @Operation(summary = "Create a user", description = "Creates a user")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a user"),
        @ApiResponse(responseCode = "404", description = "Failed to create a user",
            content = @Content(schema = @Schema(implementation = String.class))) })
    @PostMapping
    public void createUser(Authentication authentication, CreateUserDto createUserDto,
            @RequestParam(required = false) Optional<Long> eventId,
            @RequestParam(required = false) Optional<String> secret) {
        firebaseService.createUser(authentication, createUserDto, eventId, secret);
    }

    @PostMapping("/subscribe/time-slot")
    public void subscribeForTimeSlot(Authentication authentication, CreateTimeSlotSubscription dto) {
        timeSlotSubscriptionService.subscribeToTimeSlot(
                authentication,
                Long.parseLong(dto.getTimeSlotId()), dto.getExpoToken()
        );
    }

    @GetMapping("/bookings")
    public List<BookingDto> getAllBookingsForUser(Authentication authentication) {
        String uid = ((FirebaseToken) authentication.getCredentials()).getUid();
        return bookingService.getAllBookingsOfUser(uid);
    }

    @PostMapping("/send-notification")
    public void sendNotification(ExpoNotification notification) {
        timeSlotSubscriptionService.sendTestNotificationToAllDevices(notification);
    }
}
