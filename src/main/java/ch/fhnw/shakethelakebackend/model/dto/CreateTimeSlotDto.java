package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime fromTime;
    @Schema(type = "string", format = "time", example = "09:00:00")
    private LocalTime untilTime;
    private TimeSlotType status;
    private Long boatId;
    private Long activityTypeId;
}
