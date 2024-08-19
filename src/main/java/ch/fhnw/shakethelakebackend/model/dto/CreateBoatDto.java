package ch.fhnw.shakethelakebackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Boat}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoatDto implements Serializable {
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private String operator;
    @NotNull
    private int seatsRider;
    @NotNull
    private int seatsViewer;
    @NotNull
    private int slotDurationInMins;
    @NotNull
    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime availableFrom;
    @NotNull
    @Schema(type = "string", format = "time", example = "09:00:00")
    private LocalTime availableUntil;
    @NotNull
    private Long eventId;
}
