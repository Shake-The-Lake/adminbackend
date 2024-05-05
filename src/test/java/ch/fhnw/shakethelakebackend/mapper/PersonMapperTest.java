package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonMapperTest {
    @InjectMocks
    private PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Test
    void testToEntity() {
        // Given
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setFirstName("John");
        createPersonDto.setLastName("Doe");

        // When
        Person person = personMapper.toEntity(createPersonDto);

        // Then
        assertEquals(createPersonDto.getFirstName(), person.getFirstName());
        assertEquals(createPersonDto.getLastName(), person.getLastName());
    }

    @Test
    void testToDto() {
        // Given
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");

        // When
        PersonDto personDto = personMapper.toDto(person);

        // Then
        assertEquals(person.getId(), personDto.getId());
        assertEquals(person.getFirstName(), personDto.getFirstName());
        assertEquals(person.getLastName(), personDto.getLastName());
    }

    @Test
    void testBoatsToBoatIds() {
        // Given
        Boat boat1 = mock(Boat.class);
        when(boat1.getId()).thenReturn(1L);
        Boat boat2 = mock(Boat.class);
        when(boat2.getId()).thenReturn(2L);

        List<Boat> boats = Arrays.asList(boat1, boat2);

        // When
        List<Long> boatIds = personMapper.boatsToBoatIds(boats);

        // Then
        assertEquals(2, boatIds.size());
        assertEquals(1L, boatIds.get(0));
        assertEquals(2L, boatIds.get(1));
    }

    @Test
    void testBookingsToBookingIds() {
        // Given
        Booking booking1 = mock(Booking.class);
        when(booking1.getId()).thenReturn(1L);
        Booking booking2 = mock(Booking.class);
        when(booking2.getId()).thenReturn(2L);

        Set<Booking> bookings = new HashSet<>(Arrays.asList(booking1, booking2));

        // When
        Set<Long> bookingIds = personMapper.bookingsToBookingIds(bookings);

        // Then
        assertEquals(2, bookingIds.size());
        assertEquals(1L, bookingIds.iterator().next());
        assertEquals(2L, bookingIds.stream().skip(1).findFirst().orElse(null));
    }
}
