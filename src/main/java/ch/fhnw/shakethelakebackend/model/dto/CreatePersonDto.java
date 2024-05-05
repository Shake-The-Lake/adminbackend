package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.PersonType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Person}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePersonDto implements Serializable {
    @NotNull
    private PersonType personType;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @Email(message = "Email should be valid")
    @Schema(description = "Persons's email address", example = "user@example.com")
    @NotBlank
    private String emailAddress;
    @NotNull
    private String phoneNumber;
}
