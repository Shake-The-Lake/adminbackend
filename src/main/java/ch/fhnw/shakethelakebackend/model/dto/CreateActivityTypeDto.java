package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.ActivityType}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateActivityTypeDto implements Serializable {
    @NotNull
    private LocalizedString name;
    @NotNull
    private LocalizedString description;
    @NotNull
    private LocalizedString checklist;
    @NotNull
    private Long iconId;
    @NotNull
    private Long eventId;
}
