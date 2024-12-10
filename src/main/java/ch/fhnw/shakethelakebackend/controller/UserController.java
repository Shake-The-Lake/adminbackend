package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.service.ExpoNotificationService;
import ch.fhnw.shakethelakebackend.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final FirebaseService firebaseService;
    private final ExpoNotificationService expoNotificationService;

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
        expoNotificationService.subscribeToTimeSlot(
                authentication,
                Long.parseLong(dto.getTimeSlotId()), dto.getExpoToken()
        );
    }

    @PostMapping("/send-notification")
    public void sendNotification(ExpoNotification notification) {
        expoNotificationService.sendTestNotificationToAllDevices(notification);
    }
}
