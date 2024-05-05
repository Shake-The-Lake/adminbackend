package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityTypeMapper {
    @Mapping(source = "eventId", target = "event.id")
    ActivityType toEntity(CreateActivityTypeDto createActivityTypeDto);

    @Mapping(source = "event.id", target = "eventId")
    ActivityTypeDto toDto(ActivityType activityType);
}
