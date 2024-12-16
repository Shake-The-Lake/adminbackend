package ch.fhnw.shakethelakebackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveTimeSlotDto {
    @Schema(type = "string", format = "time", example = "08:00:00")
    private LocalTime fromTime;
    @Schema(type = "string", format = "time", example = "09:00:00")
    private LocalTime untilTime;
}
