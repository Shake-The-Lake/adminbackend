package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PersonMapper.class, TimeSlotMapper.class})
public interface BookingMapper {

    PersonMapper INSTANCE_PERSON = Mappers.getMapper(PersonMapper.class);
    TimeSlotMapper INSTANCE_TIMESLOT = Mappers.getMapper(TimeSlotMapper.class);

    @Mapping(target = "timeSlot", ignore = true)
    @Mapping(target = "person", ignore = true)
    Booking toEntity(CreateBookingDto bookingDto);

    @Mapping(target = "timeSlotId", expression = "java(booking.getTimeSlot().getId())")
    @Mapping(target = "personId", expression = "java(booking.getPerson().getId())")
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    BookingDto toDto(Booking booking);

    @Mapping(target = "timeSlotId", expression = "java(booking.getTimeSlot().getId())")
    @Mapping(target = "personId", expression = "java(booking.getPerson().getId())")
    @Mapping(target = "person", qualifiedBy = ToDtoDefault.class)
    @Mapping(target = "timeSlot", qualifiedBy = ToDtoExtended.class)
    BookingDto toDtoExtended(Booking booking);
}
