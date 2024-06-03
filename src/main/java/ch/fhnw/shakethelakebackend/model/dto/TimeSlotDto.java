package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.TimeSlot}
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotDto {
    private Long id;
    private LocalDateTime fromTime;
    private LocalDateTime untilTime;
    private Long boatId;
    private Set<Long> bookingIds;
    private TimeSlotType status;
    //With parameter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String boatName;
}
