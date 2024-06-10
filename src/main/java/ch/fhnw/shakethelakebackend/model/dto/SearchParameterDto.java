package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchParameterDto implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BoatDto> boats;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ActivityTypeDto> activities;

}
