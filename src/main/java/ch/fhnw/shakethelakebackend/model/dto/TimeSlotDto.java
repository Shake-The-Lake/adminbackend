package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.enums.TimeSlotType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
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
    @CsvIgnore
    private Long id;
    @CsvBindByName(column = "From")
    private LocalDateTime fromTime;
    @CsvBindByName(column = "To")
    private LocalDateTime untilTime;
    @CsvIgnore
    private Long boatId;
    @CsvIgnore
    private Set<Long> bookingIds;
    private TimeSlotType status;
    //With parameter
    @CsvBindByName(column = "Boat")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String boatName;
}
