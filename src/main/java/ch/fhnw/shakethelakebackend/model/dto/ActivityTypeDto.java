package ch.fhnw.shakethelakebackend.model.dto;

import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
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
public class ActivityTypeDto implements Serializable {
    private Long id;
    private LocalizedString name;
    private LocalizedString description;
    private LocalizedString checklist;
    private String icon;
    private Long eventId;
}
