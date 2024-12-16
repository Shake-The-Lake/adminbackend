package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
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
public interface TimeSlotMapper {

    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "activityType.id", source = "activityTypeId")
    TimeSlot toEntity(CreateTimeSlotDto timeSlotDto);

    @ToDtoDefault
    @Mapping(target = "boatId", expression = "java(timeSlot.getBoat().getId())")
    @Mapping(target = "bookingIds", expression = "java(bookingsToBookingIds(timeSlot.getBookings()))")
    @Mapping(target = "activityTypeId", source = "activityType.id")
    @Mapping(target = "seatsRider", source = "boat.seatsRider")
    @Mapping(target = "seatsViewer", source = "boat.seatsViewer")
    @Mapping(target = "availableSeats", expression =
        "java(getAvailableSeats(timeSlot, timeSlot.getBoat()))")
    @Mapping(target = "availableRiderSeats", expression =
        "java(getAvailableRiderSeats(timeSlot, timeSlot.getBoat()))")
    @Mapping(target = "availableViewerSeats", expression =
        "java(getAvailableViewerSeats(timeSlot, timeSlot.getBoat()))")
    @Mapping(target = "originalFromTime", source = "originalFromTime")
    @Mapping(target = "originalUntilTime", source = "originalUntilTime")
    @Mapping(target = "boat", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    TimeSlotDto toDto(TimeSlot timeSlot);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void update(CreateTimeSlotDto createTimeSlotDto, @MappingTarget TimeSlot timeSlot);

    default long getAvailableSeats(TimeSlot timeSlot, Boat boat) {
        int totalSeats = boat.getSeatsViewer() + boat.getSeatsRider();
        Set<Booking> bookings = timeSlot.getBookings();
        long totalBookedSeats = bookings.size();

        return totalSeats - totalBookedSeats;
    }

    default long getAvailableRiderSeats(TimeSlot timeSlot, Boat boat) {
        Set<Booking> bookings = timeSlot.getBookings();
        long bookedRiders = bookings.stream().filter(Booking::getIsRider).count();

        return boat.getSeatsRider() - bookedRiders;
    }
    default long getAvailableViewerSeats(TimeSlot timeSlot, Boat boat) {
        Set<Booking> bookings = timeSlot.getBookings();
        long bookedViewers = bookings.stream().filter(b -> !(b.getIsRider())).count();

        return boat.getSeatsViewer() - bookedViewers;
    }

    default Set<Long> bookingsToBookingIds(Set<Booking> bookings) {
        return bookings.stream().map(Booking::getId).collect(Collectors.toSet());
    }

}
