package ch.fhnw.shakethelakebackend.model.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveTimeSlotDto {
    @Min(value = 0L, message = "The value must be positive")
    private long minutes;
}
