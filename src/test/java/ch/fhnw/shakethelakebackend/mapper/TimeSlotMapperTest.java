package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.config.FirebaseConfigTest;
import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Import(FirebaseConfigTest.class)
class TimeSlotMapperTest {
    @InjectMocks
    private final TimeSlotMapper timeSlotMapper = Mappers.getMapper(TimeSlotMapper.class);

    @Test
    void testToEntity() {
        // Given
        CreateTimeSlotDto timeSlotDto = new CreateTimeSlotDto();
        // set properties of createTimeSlotDto
        LocalTime fromTime = LocalTime.now();
        LocalTime untilTime = LocalTime.now().plusHours(1);
        timeSlotDto.setFromTime(fromTime);
        timeSlotDto.setUntilTime(untilTime);

        // When
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDto);

        // Then
        assertEquals(fromTime, timeSlot.getFromTime());
        assertEquals(untilTime, timeSlot.getUntilTime());
    }

    @Test
    void testToDto() {
        // Given
        LocalTime fromTime = LocalTime.now();
        LocalTime untilTime = LocalTime.now().plusHours(1);
        Boat boat = new Boat();
        TimeSlot timeSlot = new TimeSlot();
        // set properties of timeSlot
        boat.setId(1L);
        timeSlot.setId(1L);
        timeSlot.setFromTime(fromTime);
        timeSlot.setUntilTime(untilTime);
        timeSlot.setBoat(boat);
        timeSlot.setBookings(new HashSet<>());

        // When
        TimeSlotDto timeSlotDto = timeSlotMapper.toDto(timeSlot);

        // Then
        assertEquals(1L, timeSlotDto.getId());
        assertEquals(fromTime, timeSlotDto.getFromTime());
        assertEquals(untilTime, timeSlotDto.getUntilTime());
        assertEquals(1L, timeSlotDto.getBoatId());
        assertTrue(timeSlotDto.getBookingIds().isEmpty());
    }

    @Test
    void toEntityShouldHandleNull() {
        TimeSlot timeSlot = timeSlotMapper.toEntity(null);
        assertNull(timeSlot);
    }

    @Test
    void toDtoShouldHandleNull() {
        TimeSlotDto timeSlotDto = timeSlotMapper.toDto(null);
        assertNull(timeSlotDto);
    }

    @Test
    void bookingsToBookingIdsShouldMapCorrectly() {
        Set<Booking> bookings = new HashSet<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        bookings.add(booking1);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        bookings.add(booking2);

        Set<Long> bookingIds = timeSlotMapper.bookingsToBookingIds(bookings);

        assertTrue(bookingIds.contains(1L));
        assertTrue(bookingIds.contains(2L));
    }

    @Test
    void bookingsToBookingIdsShouldHandleEmptySet() {
        Set<Long> bookingIds = timeSlotMapper.bookingsToBookingIds(new HashSet<>());
        assertTrue(bookingIds.isEmpty());
    }

    @Test
    void bookingsToBookingIdsShouldHandleNonNullElements() {
        Set<Booking> bookings = new HashSet<>();
        Booking booking1 = new Booking();
        booking1.setId(1L);
        bookings.add(booking1);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        bookings.add(booking2);

        Set<Long> bookingIds = timeSlotMapper.bookingsToBookingIds(bookings);

        assertTrue(bookingIds.contains(1L));
        assertTrue(bookingIds.contains(2L));
    }

    @Test
    void bookingIdsToBookingsShouldMapCorrectly() {
        Set<Booking> bookings = new HashSet<>();
        bookings.add(Booking.builder().id(1L).build());
        bookings.add(Booking.builder().id(2L).build());

        Set<Long> bookingIds = timeSlotMapper.bookingsToBookingIds(bookings);

        assertEquals(2, bookings.size());
        assertTrue(bookings.stream().anyMatch(booking -> booking.getId().equals(1L)));
        assertTrue(bookings.stream().anyMatch(booking -> booking.getId().equals(2L)));
    }
}
