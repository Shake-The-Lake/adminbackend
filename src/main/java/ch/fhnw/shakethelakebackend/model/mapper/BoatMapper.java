package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.PostBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BoatMapper {
    @Mapping(source = "boatDriverId", target = "boatDriver.id")
    Boat toEntity(PostBoatDto postBoatDto);

    @Mapping(target = "timeSlotIds", expression = "java(timeSlotsToTimeSlotIds(boat.getTimeSlots()))")
    @Mapping(source = "boatDriver.id", target = "boatDriverId")
    BoatDto toDto(Boat boat);

    @Mapping(source = "boatDriver.id", target = "boatDriverId")
    PostBoatDto toPostDto(Boat boat);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "boatDriverId", target = "boatDriver.id")
    Boat partialUpdate(BoatDto boatDto, @MappingTarget Boat boat);

    default Set<Long> timeSlotsToTimeSlotIds(Set<TimeSlot> timeSlots) {
        return timeSlots.stream().map(TimeSlot::getId).collect(Collectors.toSet());
    }
}
