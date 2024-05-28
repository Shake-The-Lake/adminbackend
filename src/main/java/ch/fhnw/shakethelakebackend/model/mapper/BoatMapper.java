package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
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
public interface BoatMapper {
    @Mapping(source = "boatDriverId", target = "boatDriver.id")
    @Mapping(target = "activityType.id", source = "activityTypeId")
    @Mapping(target = "event.id", source = "eventId")
    Boat toEntity(CreateBoatDto createBoatDto);

    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(source = "boatDriver.id", target = "boatDriverId")
    @Mapping(target = "activityTypeId", source = "activityType.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "timeSlots", ignore = true)
    BoatDto toDto(Boat boat);

    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(source = "boatDriver.id", target = "boatDriverId")
    @Mapping(target = "activityTypeId", source = "activityType.id")
    @Mapping(target = "eventId", source = "event.id")
    BoatDto toDtoWithTimeSlots(Boat boat);

    @Mapping(source = "boatDriver.id", target = "boatDriverId")
    CreateBoatDto toCreateDto(Boat boat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "boatDriverId", target = "boatDriver.id")
    Boat partialUpdate(BoatDto boatDto, @MappingTarget Boat boat);

    default Set<Long> timeSlotsToTimeSlotIds(Set<TimeSlot> timeSlots) {
        return timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toSet());
    }
}
