package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
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

    /**
     *
     * Maps the CreateTimeSlotDto to the TimeSlot entity
     *
     * @param timeSlotDto to be mapped
     * @return the mapped TimeSlot entity
     */
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "activityType.id", source = "activityTypeId")
    TimeSlot toEntity(CreateTimeSlotDto timeSlotDto);

    /**
     *
     * Maps the TimeSlot entity to the TimeSlotDto
     *
     * @param timeSlot to be mapped
     * @return the mapped TimeSlotDto
     */
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
    @Mapping(target = "boat", ignore = true)
    @Mapping(target = "activityType", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    TimeSlotDto toDto(TimeSlot timeSlot);

    /**
     *
     * Calculates the available seats for a given time slot and boat
     *
     * @param timeSlot containing the bookings
     * @param boat containing the seats
     * @return the available seats
     */
    default long getAvailableSeats(TimeSlot timeSlot, Boat boat) {
        int totalSeats = boat.getSeatsViewer() + boat.getSeatsRider();
        Set<Booking> bookings = timeSlot.getBookings();
        long totalBookedSeats = bookings.size();

        return totalSeats - totalBookedSeats;
    }

    /**
     *
     * Calculates the available rider seats for a given time slot and boat
     *
     * @param timeSlot containing the bookings
     * @param boat containing the seats
     * @return the available rider seats
     */
    default long getAvailableRiderSeats(TimeSlot timeSlot, Boat boat) {
        Set<Booking> bookings = timeSlot.getBookings();
        long bookedRiders = bookings.stream().filter(Booking::getIsRider).count();

        return boat.getSeatsRider() - bookedRiders;
    }

    /**
     *
     * Calculates the available viewer seats for a given time slot and boat
     *
     * @param timeSlot containing the bookings
     * @param boat containing the seats
     * @return the available viewer seats
     */
    default long getAvailableViewerSeats(TimeSlot timeSlot, Boat boat) {
        Set<Booking> bookings = timeSlot.getBookings();
        long bookedViewers = bookings.stream().filter(b -> !(b.getIsRider())).count();

        return boat.getSeatsViewer() - bookedViewers;
    }

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
