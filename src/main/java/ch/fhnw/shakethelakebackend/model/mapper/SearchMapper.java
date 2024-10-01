package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface SearchMapper {

    /**
     *
     * Maps the Search entity to the SearchDto
     *
     * @param boat to be mapped
     * @param person to be mapped
     * @param timeSlot to be mapped
     * @param activityType to be mapped
     * @param booking to be mapped
     * @return the mapped SearchDto
     */
    @Mapping(target = "boat", source = "boat")
    @Mapping(target = "person", source = "person")
    // Must be mapped otherwise mapper is trying to get the boat from timeSlot
    @Mapping(target = "timeSlot", source = "timeSlot")
    @Mapping(target = "activityType", source = "activityType")
    @Mapping(target = "booking", source = "booking")
    SearchDto toDto(BoatDto boat, PersonDto person, TimeSlotDto timeSlot, ActivityTypeDto activityType,
            BookingDto booking);
}
