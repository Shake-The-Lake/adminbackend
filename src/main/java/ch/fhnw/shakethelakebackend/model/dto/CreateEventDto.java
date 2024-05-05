package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.Event;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Event}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventDto implements Serializable {
    @NotNull
    private LocalDateTime customerOnlyTime;
    @NotNull
    private String title;
    @NotNull
    private String description;
    //Long locationId;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private String customerCode;
    @NotNull
    private String employeeCode;
    private boolean isStarted;
    @NotNull
    private LocalDateTime startedAt;
    @NotNull
    private LocalDateTime endedAt;
}
