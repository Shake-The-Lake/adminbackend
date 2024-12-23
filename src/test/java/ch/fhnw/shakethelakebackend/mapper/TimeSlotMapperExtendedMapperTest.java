package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.config.FirebaseConfigTest;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotExtendedMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import(FirebaseConfigTest.class)
public class TimeSlotMapperExtendedMapperTest {

    @Mock
    private BoatMapper boatMapper;
    @Mock
    private TimeSlotExtendedMapper mapper;

    @InjectMocks
    private final TimeSlotExtendedMapper timeSlotExtendedMapper = Mappers.getMapper(TimeSlotExtendedMapper.class);

    @Test
    void testTimeSlotMapperWithBoatName() {
        Boat boat = Boat.builder().name("Boat").id(1L).build();
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(Set.of()).build();
        BoatDto boatDto = new BoatDto();
        boatDto.setName("Boat");

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto timeSlotDto = timeSlotExtendedMapper.toDtoWithBoat(timeSlot);

        assertEquals("Boat", timeSlotDto.getBoat().getName());
        assertEquals(1L, timeSlotDto.getBoatId());
    }

    @Test
    void testTimeSlotMapperBookings() {
        Boat boat = Boat.builder().name("Boat").id(1L).build();
        Set<Booking> bookings = Set.of(Booking.builder().id(1L).build());
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(bookings).build();
        BoatDto boatDto = BoatDto.builder().name("Boat").id(1L).build();

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto timeSlotDto = timeSlotExtendedMapper.toDtoWithBoat(timeSlot);

        assertEquals("Boat", timeSlotDto.getBoat().getName());
        assertEquals(1L, timeSlotDto.getBoat().getId());
        assertEquals(Set.of(1L), timeSlotDto.getBookingIds());
    }

    @Test
    void testToDtoWithBoatNullTimeSlot() {
        TimeSlotDto result = mapper.toDtoWithBoat(null);
        assertNull(result, "Result should be null when input TimeSlot is null");
    }

    @Test
    void testToDtoWithBoatEmptyBookingsSet() {
        Boat boat = Boat.builder().id(1L).name("Boat").build();
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(Collections.emptySet()).build();
        BoatDto boatDto = BoatDto.builder().id(1L).name("Boat").build();

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto result = mapper.toDtoWithBoat(timeSlot);

        assertNull(result);
    }

    @Test
    void testToDtoWithBoatNullBoat() {
        TimeSlot timeSlot = TimeSlot.builder().boat(null).build();

        TimeSlotDto result = mapper.toDtoWithBoat(timeSlot);

        assertNull(result);
    }

    @Test
    void testToDtoWithBoatNullBookingsSet() {
        Boat boat = Boat.builder().id(1L).name("Boat").build();
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(null).build();
        BoatDto boatDto = BoatDto.builder().id(1L).name("Boat").build();

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto result = mapper.toDtoWithBoat(timeSlot);

        assertNull(result);
    }

    @Test
    void testToDtoWithBoatBookingWithNullFields() {
        Boat boat = Boat.builder().id(1L).name("Boat").build();
        Booking booking = Booking.builder().id(null).build(); // Booking with null ID
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(Set.of(booking)).build();
        BoatDto boatDto = BoatDto.builder().id(1L).name("Boat").build();

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto result = mapper.toDtoWithBoat(timeSlot);

        assertNull(result);
    }

    @Test
    void testToDtoWithBoatPartialActivityType() {
        Boat boat = Boat.builder().id(1L).name("Boat").build();
        TimeSlot timeSlot = TimeSlot.builder()
            .boat(boat)
            .activityType(null) // Missing ActivityType
            .build();
        BoatDto boatDto = BoatDto.builder().id(1L).name("Boat").build();

        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto result = mapper.toDtoWithBoat(timeSlot);

        assertNull(result);
    }
}
