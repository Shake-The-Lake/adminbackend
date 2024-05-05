package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {

    @Mapping(target = "activityTypes", ignore = true)
    Event toEntity(CreateEventDto createEventDto);

    @Mapping(target = "activityTypeIds", expression = "java(mapActivityTypesToIds(event.getActivityTypes()))")
    EventDto toDto(Event event);

    default Set<Long> mapActivityTypesToIds(Set<ActivityType> activityTypes) {
        return activityTypes.stream().map(ActivityType::getId).collect(Collectors.toSet());

    }
}
