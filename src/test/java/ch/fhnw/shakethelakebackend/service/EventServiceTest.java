package ch.fhnw.shakethelakebackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.Location;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import ch.fhnw.shakethelakebackend.model.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService(eventRepository, locationRepository);
    }

    @Test
    void testCreateEvent() {
        Location location = Location.builder()
            .name("Location Name")
            .build();

        Event event = Event.builder()
            .location(location)
            .build();
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event savedEvent = eventService.createEvent(event);
        assertNotNull(savedEvent);
        verify(eventRepository).save(event);
        verify(locationRepository).save(location);
    }

    @Test
    void testUpdateEventWhenExist() {
        Location location = Location.builder()
            .canton("Bern")
            .town("Bern")
            .postalCode(3000)
            .build();

        Event event = Event.builder()
            .id(12345)
            .date(LocalDateTime.now())
            .customerCode("TEST12345")
            .location(location)
            .build();

        Event updatedEventDetails = Event.builder().id(12345)
            .location(Location.builder().canton("Zürich").town("Zürich").postalCode(8000).build())
            .date(LocalDateTime.now().plusDays(1))
            .customerCode("UPDATED12345")
            .build();

        when(eventRepository.save(updatedEventDetails)).thenReturn(updatedEventDetails);
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        Event updatedEvent = eventService.updateEvent(event.getId(), updatedEventDetails);
        assertEquals(updatedEventDetails.getLocation(), updatedEvent.getLocation());
        assertEquals(updatedEventDetails.getDate(), updatedEvent.getDate());
        assertEquals(updatedEventDetails.getCustomerCode(), updatedEvent.getCustomerCode());
        verify(eventRepository).save(updatedEventDetails);
    }

    @Test
    void testUpdateEventWhenNotFound() {
        Event nonExistingEvent = Event.builder()
            .id(99999)
            .build();
        when(eventRepository.findById(nonExistingEvent.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.updateEvent(nonExistingEvent.getId(), nonExistingEvent);
        });
    }

    @Test
    void testDeleteEventWhenExist() {
        Event event = Event.builder()
            .id(12345)
            .date(LocalDateTime.now())
            .customerCode("TEST12345")
            .build();
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).delete(event);
        eventService.deleteEvent(event.getId());
        verify(eventRepository).delete(event);
    }

    @Test
    void testDeleteEventWhenNotFound() {
        Event event = Event.builder()
            .id(12345)
            .build();
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventService.deleteEvent(event.getId()));
    }

    @Test
    @SneakyThrows
    void testGetEventWhenExist() {
        Event event = Event.builder()
            .id(12345)
            .build();
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        Event foundEvent = eventService.getEvent(event.getId());
        assertNotNull(foundEvent);
        verify(eventRepository).findById(event.getId());
    }

    @Test
    void testGetEventWhenNotFound() {
        Event event = Event.builder()
            .id(12345)
            .build();
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> eventService.getEvent(event.getId()));
    }
}
