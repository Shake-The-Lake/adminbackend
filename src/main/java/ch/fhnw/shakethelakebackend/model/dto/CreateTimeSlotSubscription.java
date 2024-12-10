package ch.fhnw.shakethelakebackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTimeSlotSubscription {
    private String timeSlotId;
    private String expoToken;
}
