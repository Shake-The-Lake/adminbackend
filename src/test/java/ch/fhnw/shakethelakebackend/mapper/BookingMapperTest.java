package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookingMapperTest {
    @InjectMocks
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void toEntityHandlesNull() {
        Booking booking = bookingMapper.toEntity(null);
        assertNull(booking);
    }

    @Test
    void toDtoMapsCorrectly() {
        Booking booking = new Booking();
        booking.setId(1L);
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(2L);
        booking.setTimeSlot(timeSlot);
        Person person = new Person();
        person.setId(3L);
        booking.setPerson(person);

        BookingDto bookingDto = bookingMapper.toDto(booking);

        assertEquals(1L, bookingDto.getId());
        assertEquals(2L, bookingDto.getTimeSlotId());
        assertEquals(3L, bookingDto.getPersonId());
    }

    @Test
    void toDtoHandlesNull() {
        BookingDto bookingDto = bookingMapper.toDto(null);
        assertNull(bookingDto);
    }
}