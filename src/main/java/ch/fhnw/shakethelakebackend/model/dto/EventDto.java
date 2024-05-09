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
import java.util.Set;

/**
 * DTO for {@link Event}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto implements Serializable {
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    // TODO: not in mvp.
    // private Long locationId;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private String customerCode;
    @NotNull
    private String employeeCode;
    @NotNull
    private LocalDateTime customerOnlyTime;
    private boolean isStarted;
    @NotNull
    private LocalDateTime startedAt;
    @NotNull
    private LocalDateTime endedAt;
    private Set<Long> activityTypeIds;
    private Set<Long> boatIds;
}
