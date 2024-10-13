package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for search parameters
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchParameterDto implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BoatDto> boats;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ActivityTypeDto> activityTypes;

}
