package ch.fhnw.shakethelakebackend.model.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {PersonMapper.class, TimeSlotExtendedMapper.class})
public interface BookingMapper {

    PersonMapper INSTANCE_PERSON = Mappers.getMapper(PersonMapper.class);
    TimeSlotExtendedMapper INSTANCE_TIMESLOT = Mappers.getMapper(TimeSlotExtendedMapper.class);

    /**
     *
     * Maps the CreateBookingDto to the Booking entity
     *
     * @param bookingDto to be mapped
     * @return the mapped Booking entity
     */
    @Mapping(target = "timeSlot", ignore = true)
    @Mapping(target = "person", ignore = true)
    Booking toEntity(CreateBookingDto bookingDto);

    /**
     *
     * Maps the Booking entity to the BookingDto
     *
     * @param booking to be mapped
     * @return the mapped BookingDto
     */
    @Mapping(target = "timeSlotId", expression = "java(booking.getTimeSlot().getId())")
    @Mapping(target = "personId", expression = "java(booking.getPerson().getId())")
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    BookingDto toDto(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void update(CreateBookingDto createBookingDto, @MappingTarget Booking booking);

}
