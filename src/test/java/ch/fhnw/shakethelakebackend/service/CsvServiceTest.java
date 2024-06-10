package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvServiceTest {

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @Mock
    private BoatService boatService;

    @InjectMocks
    private CsvService csvService;


    @Test
    void testExportTimeSlotsFromBoatSuccess() {
        // Setup
        Long boatId = 1L;
        String filename = "timeslots.csv";
        Boat boat = mock(Boat.class);
        Set<TimeSlot> timeSlots = Set.of(mock(TimeSlot.class));
        TimeSlotDto dto = new TimeSlotDto();

        when(boatService.getBoat(boatId)).thenReturn(boat);
        when(boat.getTimeSlots()).thenReturn(timeSlots);
        when(boatService.getBoat(boatId)).thenReturn(boat);
        when(timeSlotMapper.toDtoWithBoatName(any())).thenReturn(dto);

        // Execution
        ResponseEntity<String> response = csvService.exportTimeSlotsFromBoat(boatId, filename);

        // Verification
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey("Content-Disposition"));
    }

    @Test
    void testExportTimeSlotsFromBoatNoContent() {
        Long boatId = 1L;
        String filename = "empty.csv";
        Boat boat = mock(Boat.class);

        when(boatService.getBoat(boatId)).thenReturn(boat);
        when(boat.getTimeSlots()).thenReturn(emptySet()); // No slots

        ResponseEntity<String> response = csvService.exportTimeSlotsFromBoat(boatId, filename);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }





}
