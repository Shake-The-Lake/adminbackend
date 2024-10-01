package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = { TimeSlotMapper.class })
public interface BoatMapper {

    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

    /**
     *
     * Maps the CreateBoatDto to the Boat entity
     *
     * @param createBoatDto to be mapped
     * @return the mapped Boat entity
     */
    @Mapping(target = "event.id", source = "eventId")
    Boat toEntity(CreateBoatDto createBoatDto);

    /**
     *
     * Maps the Boat entity to the BoatDto
     *
     * @param boat to be mapped
     * @return the mapped BoatDto
     */
    @ToDtoDefault
    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "timeSlots", ignore = true)
    BoatDto toDto(Boat boat);

    /**
     *
     * Maps the Boat entity to the BoatDto
     *
     * @param boat to be mapped
     * @return the mapped BoatDto
     */
    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(target = "timeSlots", qualifiedBy = ToDtoDefault.class)
    @Mapping(target = "eventId", source = "event.id")
    BoatDto toDtoWithTimeSlots(Boat boat);

    /**
     *
     * Maps time slots to time slot ids
     *
     * @param timeSlots to be mapped
     * @return the mapped time slot ids
     */
    default Set<Long> timeSlotsToTimeSlotIds(Set<TimeSlot> timeSlots) {
        return timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toSet());
    }
}
