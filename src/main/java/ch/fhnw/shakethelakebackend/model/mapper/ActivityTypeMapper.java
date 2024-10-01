package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ActivityTypeMapper {
    @Mapping(source = "eventId", target = "event.id")
    ActivityType toEntity(CreateActivityTypeDto createActivityTypeDto);

    @Mapping(source = "event.id", target = "eventId")
    ActivityTypeDto toDto(ActivityType activityType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void update(CreateActivityTypeDto createActivityTypeDto, @MappingTarget ActivityType activityType);

}
