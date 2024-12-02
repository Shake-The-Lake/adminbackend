package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private ActivityTypeService activityTypeService;

    @Mock
    private BoatService boatService;

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @InjectMocks
    private TimeSlotService timeSlotService;

    private TimeSlot timeSlot;

    private TimeSlotDto timeSlotDto;

    private CreateTimeSlotDto createTimeSlotDto;

    private Booking booking;

    private Boat boat;

    private Event event;

    private ActivityType activityType;

    private LocalTime fromTime;
    private LocalTime untilTime;

    @BeforeEach
    void setup() {
        activityType = ActivityType.builder().id(1L).build();
        fromTime = LocalTime.now();
        untilTime = LocalTime.now().plusHours(1);

        event = Event.builder().id(1L).date(LocalDate.now()).build();

        boat = Boat.builder().seatsRider(2).seatsViewer(2).id(1L).event(event).availableFrom(
                fromTime).availableUntil(untilTime)
            .build();
        timeSlot = TimeSlot.builder().fromTime(fromTime).untilTime(untilTime).boat(boat).id(1L)
            .activityType(activityType).build();
        timeSlotDto = TimeSlotDto.builder().fromTime(fromTime).untilTime(untilTime).boatId(1L).id(1L)
            .activityTypeId(1L).build();
        createTimeSlotDto = CreateTimeSlotDto.builder().fromTime(fromTime).untilTime(untilTime).boatId(1L)
            .activityTypeId(1L).build();

        booking = Booking.builder().id(1L).build();
    }

    @Test
    void testCreateTimeSlot() {
        when(timeSlotMapper.toEntity(any(CreateTimeSlotDto.class))).thenReturn(timeSlot);
        when(boatService.getBoat(1L)).thenReturn(boat);
        when(activityTypeService.getActivityType(1L)).thenReturn(activityType);

        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(timeSlot);
        when(timeSlotMapper.toDto(any(TimeSlot.class))).thenReturn(timeSlotDto);

        TimeSlotDto result = timeSlotService.createTimeSlot(createTimeSlotDto);

        assertEquals(timeSlotDto, result);
        verify(timeSlotRepository).save(timeSlot);
    }

    @Test
    void testUpdateTimeSlot() {
        when(timeSlotRepository.existsById(1L)).thenReturn(true);
        when(timeSlotMapper.toDto(any(TimeSlot.class))).thenReturn(timeSlotDto);
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
        when(activityTypeService.getActivityType(1L)).thenReturn(activityType);
        when(boatService.getBoat(1L)).thenReturn(boat);
        when(timeSlotService.updateTimeSlot(1L, createTimeSlotDto)).thenReturn(timeSlotDto);

        TimeSlotDto result = timeSlotService.updateTimeSlot(1L, createTimeSlotDto);

        assertEquals(timeSlotDto, result);

    }

    @Test
    void testUpdateTimeSlotNotFound() {
        when(timeSlotRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.updateTimeSlot(1L, createTimeSlotDto));
    }


    @Test
    void testDeleteTimeSlot() {
        when(timeSlotRepository.existsById(1L)).thenReturn(true);

        timeSlotService.deleteTimeSlot(1L);

        verify(timeSlotRepository).deleteById(1L);
    }

    @Test
    void testDeleteTimeSlotNotFound() {
        when(timeSlotRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.deleteTimeSlot(1L));
    }

    @Test
    void testGetTimeSlot() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));

        TimeSlot result = timeSlotService.getTimeSlot(1L);

        assertEquals(timeSlot, result);
    }

    @Test
    void testGetTimeSlotNotFound() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.getTimeSlot(1L));
    }

    @Test
    void testGetTimeSlotDto() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
        when(timeSlotMapper.toDto(timeSlot)).thenReturn(timeSlotDto);


        TimeSlotDto result = timeSlotService.getTimeSlotDto(1L);

        assertEquals(timeSlotDto, result);
    }

    @Test
    void testGetTimeSlotDtoNotFound() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.getTimeSlotDto(1L));
    }

    @Test
    void testGetTimeSlotDtoWithExpandNotFound() {
        when(timeSlotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.getTimeSlotDto(1L, Optional.of("expand")));
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotOverlaps() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(10, 30)).untilTime(LocalTime.of(11, 30)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenFromTimeIsAfterUntilTime() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(12, 0)).untilTime(LocalTime.of(11, 0)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenUntilTimeIsBeforeFromTime() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(11, 0)).untilTime(LocalTime.of(10, 0)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotIsOutsideBoatAvailableTime() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(7, 0)).untilTime(LocalTime.of(9, 0)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

    @Test
    void shouldNotThrowExceptionForValidTimeSlot() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(9, 0)).untilTime(LocalTime.of(10, 0)).build();

        timeSlotService.validateTimeRange(boat, timeSlot, 0);
    }

    @Test
    void shouldThrowExceptionWhenNewTimeSlotStartsAtSameTimeAsExistingOne() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 30)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenNewTimeSlotEndsAtSameTimeAsExistingOne() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(9, 30)).untilTime(LocalTime.of(11, 0)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldNotThrowExceptionWhenUpdatingTimeSlotWithSameValues() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot updatedTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();

        assertDoesNotThrow(() -> timeSlotService.validateTimeRange(boat, updatedTimeSlot, 1L));
    }

    @Test
    void shouldNotThrowExceptionForNonOverlappingTimeSlot() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(11, 0)).untilTime(LocalTime.of(12, 0)).build();

        assertDoesNotThrow(() -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenNewTimeSlotCompletelyOverlapsExistingOne() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(9, 30)).untilTime(LocalTime.of(11, 30)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldNotThrowExceptionForAdjacentTimeSlots() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot existingTimeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();
        boat.setTimeSlots(Set.of(existingTimeSlot));
        TimeSlot newTimeSlot = TimeSlot.builder().id(2L).
            fromTime(LocalTime.of(11, 0)).untilTime(LocalTime.of(12, 0)).build();

        assertDoesNotThrow(() -> timeSlotService.validateTimeRange(boat, newTimeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenTimeSlotSpansEntireBoatAvailability() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(7, 0)).untilTime(LocalTime.of(19, 0)).build();

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

    @Test
    void shouldNotThrowExceptionForSingleMinuteTimeSlotWithinAvailability() {
        Boat boat = Boat.builder().availableFrom(LocalTime.of(8, 0)).availableUntil(LocalTime.of(18, 0)).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(10, 1)).build();

        assertDoesNotThrow(() -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

    @Test
    void shouldThrowExceptionWhenBoatHasNoAvailableTimeSet() {
        Boat boat = Boat.builder().build(); // No availableFrom or availableUntil set
        TimeSlot timeSlot = TimeSlot.builder().id(1L).
            fromTime(LocalTime.of(10, 0)).untilTime(LocalTime.of(11, 0)).build();

        assertThrows(NullPointerException.class, () -> timeSlotService.validateTimeRange(boat, timeSlot, 0));
    }

}
