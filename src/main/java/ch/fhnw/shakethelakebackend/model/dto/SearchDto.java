package ch.fhnw.shakethelakebackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    private PersonDto person;
    private BoatDto boat;
    private TimeSlotDto timeSlot;
    private ActivityTypeDto activityType;
}
