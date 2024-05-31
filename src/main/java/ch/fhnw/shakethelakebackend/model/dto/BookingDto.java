package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Booking}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private Boolean isRider;
    private Boolean isManual;
    private Integer pagerNumber;
    private Long personId;
    private Long timeSlotId;
    //With parameter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PersonDto person = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TimeSlotDto timeSlot = null;

}
