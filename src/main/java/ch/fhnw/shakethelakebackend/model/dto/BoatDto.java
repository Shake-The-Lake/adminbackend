package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private Long boatDriverId;
    @NotNull
    private String name;
    @NotNull
    private String type;
    private int seatsRider;
    private int seatsViewer;
    private int slotDurationInMins;
    @NotNull
    private LocalDateTime availableFrom;
    @NotNull
    private LocalDateTime availableUntil;
    private Set<Long> timeSlotIds;
    private Long activityTypeId;
    private Long eventId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<TimeSlotDto> timeSlots = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ActivityTypeDto activityType = null;

}
