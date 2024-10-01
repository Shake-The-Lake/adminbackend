package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SearchParameterMapper {

    /**
     *
     * Maps the SearchParameterDto to the SearchParameter entity
     *
     * @param boats to be mapped
     * @param activityTypes to be mapped
     * @return the mapped SearchParameterDto
     */
    SearchParameterDto toDto(List<BoatDto> boats, List<ActivityTypeDto> activityTypes);
}
