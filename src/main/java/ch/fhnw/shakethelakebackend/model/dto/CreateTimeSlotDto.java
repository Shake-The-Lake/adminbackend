package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.TimeSlot}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTimeSlotDto {
    private LocalTime fromTime;
    private LocalTime untilTime;
    private TimeSlotType status;
    private Long boatId;
    private Long activityTypeId;
}
