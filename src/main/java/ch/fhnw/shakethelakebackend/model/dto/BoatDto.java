package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Boat}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoatDto {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private String operator;
    private int seatsRider;
    private int seatsViewer;
    private int slotDurationInMins;
    @NotNull
    private ZonedDateTime availableFrom;
    @NotNull
    private ZonedDateTime availableUntil;
    private Set<Long> timeSlotIds;
    private Long eventId;
    //With parameter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private Set<TimeSlotDto> timeSlots = null;

}
