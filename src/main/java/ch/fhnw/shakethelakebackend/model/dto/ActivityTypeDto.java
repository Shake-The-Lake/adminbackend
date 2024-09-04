package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Icon;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link ActivityType}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityTypeDto implements Serializable {
    private Long id;
    private LocalizedString name;
    private LocalizedString description;
    private LocalizedString checklist;
    private Long iconId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private Icon icon;
    private Long eventId;

}
