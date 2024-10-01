package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {


    @Mapping(target = "activityTypes", ignore = true)
    @Mapping(target = "boats", ignore = true)
    Event toEntity(CreateEventDto createEventDto);

    @Mapping(target = "activityTypeIds", expression = "java(mapActivityTypesToIds(event.getActivityTypes()))")
    @Mapping(target = "boatIds", expression = "java(mapBoatsToIds(event.getBoats()))")
    @Mapping(target = "boats", ignore = true)
    @Mapping(target = "activityTypes", ignore = true)
    EventDto toDto(Event event);

    default Set<Long> mapActivityTypesToIds(Set<ActivityType> activityTypes) {
        return activityTypes.stream().map(ActivityType::getId).collect(Collectors.toSet());
    }

    default Set<Long> mapBoatsToIds(Set<Boat> boats) {
        return boats.stream().map(Boat::getId).collect(Collectors.toSet());
    }
}
