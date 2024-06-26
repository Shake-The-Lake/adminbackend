package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeSlotMapper {

    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "activityType.id", source = "activityTypeId")
    TimeSlot toEntity(CreateTimeSlotDto timeSlotDto);

    @ToDtoDefault
    @Mapping(target = "boatId", expression = "java(timeSlot.getBoat().getId())")
    @Mapping(target = "bookingIds", expression = "java(bookingsToBookingIds(timeSlot.getBookings()))")
    @Mapping(target = "activityTypeId", source = "activityType.id")
    @Mapping(target = "boat", ignore = true)
    TimeSlotDto toDto(TimeSlot timeSlot);

    default Set<Long> bookingsToBookingIds(Set<Booking> bookings) {
        return bookings.stream().map(Booking::getId).collect(Collectors.toSet());
    }

}
