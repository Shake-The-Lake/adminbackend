package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
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

    @CsvBindByName(column = "From")
    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime fromTime;

    @Schema(type = "string", format = "time", example = "09:00:00")
    @CsvBindByName(column = "To")
    private LocalTime untilTime;

    @CsvIgnore
    private Long boatId;

    @CsvIgnore
    private Set<Long> bookingIds;

    @CsvIgnore
    private Long activityTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private ActivityTypeDto activityType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private Set<BookingDto> bookings;

    @CsvBindByName(column = "Type")
    private TimeSlotType status;
    //With parameter

    @CsvBindByName(column = "Boat")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private BoatDto boat;

    private long seatsRider;
    private long seatsViewer;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long availableSeats = -1;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long availableRiderSeats = -1;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long availableViewerSeats = -1;
}
