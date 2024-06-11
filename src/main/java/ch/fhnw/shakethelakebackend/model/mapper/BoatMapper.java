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
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = { TimeSlotMapper.class })
public interface BoatMapper {

    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);


    @Mapping(target = "event.id", source = "eventId")
    Boat toEntity(CreateBoatDto createBoatDto);

    @ToDtoDefault
    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "timeSlots", ignore = true)
    BoatDto toDto(Boat boat);

    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "timeSlots", qualifiedBy = ToDtoDefault.class)
    @Mapping(target = "eventId", source = "event.id")
    BoatDto toDtoWithTimeSlots(Boat boat);

    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "timeSlots", qualifiedBy = ToDtoDefault.class)
    @Mapping(target = "eventId", source = "event.id")
    BoatDto toDtoWithTimeSlotsAndActivityType(Boat boat);

    @ToDtoExtended
    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "timeSlots", ignore = true)
    @Mapping(target = "eventId", source = "event.id")
    BoatDto toDtoWithActivityType(Boat boat);

    CreateBoatDto toCreateDto(Boat boat);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Boat partialUpdate(BoatDto boatDto, @MappingTarget Boat boat);

    default Set<Long> timeSlotsToTimeSlotIds(Set<TimeSlot> timeSlots) {
        return timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toSet());
    }
}
