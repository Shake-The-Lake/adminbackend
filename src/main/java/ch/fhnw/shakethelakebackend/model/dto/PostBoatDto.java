package ch.fhnw.shakethelakebackend.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostBoatDto implements Serializable {
    private Long boatDriverId;
    @NotNull
    private String name;
    @NotNull
    private String type;
    @NotNull
    private int seatsRider;
    @NotNull
    private int seatsViewer;
    @NotNull
    private int slotDurationInMins;
    @NotNull
    private LocalDateTime availableFrom;
    @NotNull
    private LocalDateTime availableUntil;
}
