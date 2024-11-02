package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Event}
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
    private LocalDate date;

    private String employeeBarcode;
    private String customerBarcode;

    private Set<Long> activityTypeIds;
    private Set<Long> boatIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private List<BoatDto> boats = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(hidden = true)
    private List<ActivityTypeDto> activityTypes = null;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
    private Date updatedAt;
}
