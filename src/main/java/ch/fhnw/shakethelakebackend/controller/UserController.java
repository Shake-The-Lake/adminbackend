package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.service.FirebaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final FirebaseService firebaseService;

    @Operation(summary = "Create a user", description = "Creates a user")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created a user"),
        @ApiResponse(responseCode = "404", description = "Failed to create a user",
            content = @Content(schema = @Schema(implementation = String.class))) })
    @PostMapping
    public void createUser(Authentication authentication, CreateUserDto createUserDto) {
        firebaseService.createUser(authentication.getName(), createUserDto);
    }
}
