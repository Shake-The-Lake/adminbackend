package ch.fhnw.shakethelakebackend.model.dto;

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
public class CreateBookingDto {
    private Boolean isRider;
    private Boolean isManual;
    private Integer pagerNumber;
    private Long personId;
    private Long timeSlotId;
}
