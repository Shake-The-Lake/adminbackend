package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private Expander expander;

    @Mock
    private BoatMapper boatMapper;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventDto eventDto;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setDate(LocalDate.now());
        eventDto = new EventDto();
        eventDto.setId(1L);

    }

    @Test
    void getEventDtoReturnsEventDto() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toDto(event)).thenReturn(eventDto);

        EventDto result = eventService.getEventDto(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(eventRepository).findById(1L);
        verify(eventMapper).toDto(event);
    }

    @Test
    void getAllEventsReturnsListOfEventDtos() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));
        when(eventMapper.toDto(event)).thenReturn(eventDto);

        List<EventDto> results = eventService.getAllEvents();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(eventRepository).findAll();
        verify(eventMapper, times(1)).toDto(event);
    }

    @Test
    void createEventSavesAndReturnsEventDto() throws JsonProcessingException {
        when(eventMapper.toEntity(any())).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventDto);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"id\":1,\"secret\":\"test\"}");

        EventDto result = eventService.createEvent(CreateEventDto.builder().build());

        assertNotNull(result);
        verify(eventRepository, times(2)).save(event);
        verify(eventMapper).toDto(event);
    }

    @Test
    void updateEventUpdatesAndReturnsEvent() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(eventDto);

        EventDto result = eventService.updateEvent(1L, CreateEventDto.builder().build());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(eventDto.getBoats(), result.getBoats());
        assertEquals(eventDto.getActivityTypes(), result.getActivityTypes());
        verify(eventRepository).existsById(1L);
        verify(eventRepository).save(event);
        verify(eventMapper).toDto(event);
    }

    @Test
    void deleteEventDeletesEvent() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);

        assertDoesNotThrow(() -> eventService.deleteEvent(1L));

        verify(eventRepository).findById(1L);
        verify(eventRepository).delete(event);
    }

    @Test
    void testGetEventWithDetailsExpandActivityTypes() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.getEventWithDetails(1L, Optional.of("activityTypes"));

        verify(boatMapper, never()).toDtoWithTimeSlots(any());
    }

    @Test
    void testGetEventWithDetailsExpandBoats() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.getEventWithDetails(1L, Optional.of("boats"));

        verify(boatMapper, never()).toDtoWithTimeSlots(any());
    }

    @Test
    void testGetEventWithDetailsExpandBoatsAndActivityTypes() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        eventService.getEventWithDetails(1L, Optional.of("boats,activityTypes"));

        verify(boatMapper, never()).toDtoWithTimeSlots(any());
    }

}
