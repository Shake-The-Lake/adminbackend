package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private BoatRepository boatRepository;

    @Mock
    private BoatService boatService;

    @InjectMocks
    private TimeSlotService timeSlotService;

    @Test
    void testGetTimeSlotFound() {
        Long timeSlotId = 1L;
        TimeSlot timeSlot = new TimeSlot();
        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.of(timeSlot));

        TimeSlot foundTimeSlot = timeSlotService.getTimeSlot(timeSlotId);

        assertNotNull(foundTimeSlot);
        verify(timeSlotRepository).findById(timeSlotId);
    }

    @Test
    void testGetTimeSlotNotFound() {
        Long timeSlotId = 1L;
        when(timeSlotRepository.findById(timeSlotId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> timeSlotService.getTimeSlot(timeSlotId));
    }

    @Test
    void testCreateTimeSlot() {
        TimeSlot timeSlot = new TimeSlot();
        Boat boat = new Boat();

        boat.setId(1L);
        boat.setAvailableFrom(LocalDateTime.now());
        boat.setAvailableUntil(LocalDateTime.now().plusHours(1));

        timeSlot.setBoat(boat);
        timeSlot.setFromTime(boat.getAvailableFrom());
        timeSlot.setUntilTime(boat.getAvailableUntil());

        when(timeSlotRepository.existsById(timeSlot.getId())).thenReturn(false);
        when(boatService.getBoat(boat.getId())).thenReturn(boat);
        when(timeSlotRepository.save(timeSlot)).thenReturn(timeSlot);

        timeSlotService.createTimeSlot(timeSlot);

        verify(timeSlotRepository).save(timeSlot);
    }

    @Test
    void testCreateTimeSlotInvalidTime() {
        TimeSlot timeSlot = new TimeSlot();
        Boat boat = new Boat();

        boat.setId(1L);
        boat.setAvailableFrom(LocalDateTime.now());
        boat.setAvailableUntil(LocalDateTime.now().plusHours(1));

        timeSlot.setBoat(boat);
        timeSlot.setFromTime(boat.getAvailableFrom().minusHours(1));
        timeSlot.setUntilTime(boat.getAvailableUntil().plusHours(1));

        when(timeSlotRepository.existsById(timeSlot.getId())).thenReturn(false);
        when(boatService.getBoat(boat.getId())).thenReturn(boat);

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.createTimeSlot(timeSlot));
    }
    
    @Test
    void testUpdateTimeSlot() {
        TimeSlot timeSlotToUpdate = new TimeSlot();
        timeSlotToUpdate.setId(1L);
        when(timeSlotRepository.existsById(timeSlotToUpdate.getId())).thenReturn(true);

        timeSlotService.updateTimeSlot(timeSlotToUpdate.getId(), timeSlotToUpdate);

        verify(timeSlotRepository).save(timeSlotToUpdate);
    }

    @Test
    void testUpdateNonExistingTimeSlot() {
        TimeSlot timeSlotToUpdate = new TimeSlot();
        timeSlotToUpdate.setId(1L);
        when(timeSlotRepository.existsById(timeSlotToUpdate.getId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> timeSlotService.updateTimeSlot(timeSlotToUpdate.getId(), timeSlotToUpdate));
    }

}
