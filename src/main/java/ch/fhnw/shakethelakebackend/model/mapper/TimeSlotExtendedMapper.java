package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = { BoatMapper.class })
public interface TimeSlotExtendedMapper {

    /**
     *
     * Maps the TimeSlot entity to the TimeSlotDto
     *
     * @param timeSlot to be mapped
     * @return the mapped TimeSlotDto
     */
    @ToDtoExtended
    @Mapping(target = "boatId", expression = "java(timeSlot.getBoat().getId())")
    @Mapping(target = "bookingIds", expression = "java(bookingsToBookingIds(timeSlot.getBookings()))")
    @Mapping(target = "boat", qualifiedBy = ToDtoDefault.class)
    TimeSlotDto toDtoWithBoat(TimeSlot timeSlot);

    /**
     *
     * Maps Bookings to Booking ids
     *
     * @param bookings to be mapped
     * @return the mapped booking ids
     */
    default Set<Long> bookingsToBookingIds(Set<Booking> bookings) {
        return bookings.stream().map(Booking::getId).collect(Collectors.toSet());
    }

}
