package ch.fhnw.shakethelakebackend.model.mapper;


import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)

public interface SearchMapper {

    // Must be mapped otherwise mapper is trying to get the boat from timeSlot
    @Mapping(target = "boat", source = "boat")
    SearchDto toDto(BoatDto boat, PersonDto person, TimeSlotDto timeSlot, ActivityTypeDto activityType);
}
