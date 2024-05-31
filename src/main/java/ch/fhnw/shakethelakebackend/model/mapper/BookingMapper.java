package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {

    @Mapping(target = "timeSlot", ignore = true)
    @Mapping(target = "person", ignore = true)
    Booking toEntity(CreateBookingDto bookingDto);

    @Mapping(target = "timeSlotId", expression = "java(booking.getTimeSlot().getId())")
    @Mapping(target = "personId", expression = "java(booking.getPerson().getId())")
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    BookingDto toDto(Booking booking);
}
