package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {

    /**
     *
     * Maps the CreatePersonDto to the Person entity
     *
     * @param createPersonDto to be mapped
     * @return the mapped Person entity
     */
    @Mapping(target = "bookings", ignore = true)
    Person toEntity(CreatePersonDto createPersonDto);

    /**
     *
     * Maps the Person entity to the PersonDto
     *
     * @param person to be mapped
     * @return the mapped PersonDto
     */
    @ToDtoDefault
    @Mapping(target = "bookingIds", expression = "java(bookingsToBookingIds(person.getBookings()))")
    PersonDto toDto(Person person);

    /**
     *
     * Maps the Person entity to the PersonDto
     *
     * @param boats to be mapped
     * @return the mapped boat ids
     */
    default List<Long> boatsToBoatIds(List<Boat> boats) {
        return boats.stream().map(Boat::getId).toList();
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
