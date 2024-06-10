package ch.fhnw.shakethelakebackend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime availableFrom;
    @NotNull
    private LocalDateTime availableUntil;
    @NotNull
    private Long activityTypeId;
    @NotNull
    private Long eventId;
}
