package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotExtendedMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingMapperTest {

    @Mock
    private PersonMapper personMapper;
    @Mock
    private TimeSlotExtendedMapper timeSlotMapper;
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

    @Test
    void toDtoExtendedMapsCorrectly() {

        TimeSlot timeSlot = TimeSlot.builder().id(2L).build();
        Person person = Person.builder().id(3L).build();
        Booking booking = Booking.builder().id(1L).timeSlot(timeSlot).person(person).build();

        when(timeSlotMapper.toDtoWithBoat(timeSlot)).thenReturn(
                new TimeSlotDto(2L, null, null, 1L, new HashSet<>(), null, new BoatDto()));
        when(personMapper.toDto(person)).thenReturn(new PersonDto(3L, null, "", "", "", "", new HashSet<>()));

        BookingDto bookingDto = bookingMapper.toDtoExtended(booking);

        assertEquals(1L, bookingDto.getId());
        assertEquals(2L, bookingDto.getTimeSlotId());
        assertEquals(3L, bookingDto.getPersonId());
    }
}
