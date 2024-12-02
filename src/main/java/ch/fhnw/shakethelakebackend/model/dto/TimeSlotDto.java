package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opencsv.bean.CsvIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;
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
    @CsvIgnore
    private Long id;

    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime originalFromTime;

    @Schema(type = "string", format = "time", example = "09:00:00")
    private LocalTime originalUntilTime;

    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime fromTime;

    @Schema(type = "string", format = "time", example = "09:00:00")
    private LocalTime untilTime;

    private Long boatId;

    private Set<Long> bookingIds;

    private Long activityTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private ActivityTypeDto activityType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private Set<BookingDto> bookings;

    private TimeSlotType status;
    //With parameter

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private BoatDto boat;

    private long seatsRider;
    private long seatsViewer;

    private long availableSeats;
    private long availableRiderSeats;
    private long availableViewerSeats;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;

    private String topic;
}
