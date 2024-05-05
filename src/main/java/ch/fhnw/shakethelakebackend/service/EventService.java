package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    public static final String EVENT_NOT_FOUND = "Event not found";
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventDto getEventDto(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
        return eventMapper.toDto(event);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream().map(eventMapper::toDto).toList();
    }

    public EventDto createEvent(CreateEventDto createEventDto) {
        //TODO; location not mvp
        // Location location = locationRepository.save(createEventDto.getLocationId());
        Event event = eventMapper.toEntity(createEventDto);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    // May need to be discussed what should be updatable or not
    // Just a first implementation which allows us to update all properties
    public Event updateEvent(Long id, Event event) {
        Event existingEvent = eventRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));

        Event updatedEvent = Event.builder().id(existingEvent.getId())
            .date(event.getDate() != null ? event.getDate() : existingEvent.getDate())
            .customerCode(event.getCustomerCode() != null ? event.getCustomerCode() : existingEvent.getCustomerCode())
            .employeeCode(event.getEmployeeCode() != null ? event.getEmployeeCode() : existingEvent.getEmployeeCode())
            .customerOnlyTime(
                event.getCustomerOnlyTime() != null ? event.getCustomerOnlyTime() : existingEvent.getCustomerOnlyTime())
            .startedAt(event.getStartedAt() != null ? event.getStartedAt() : existingEvent.getStartedAt())
            .endedAt(event.getEndedAt() != null ? event.getEndedAt() : existingEvent.getEndedAt()).build();
        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(Long id) {
        //TODO: discuss whether an event should remove everything=?
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
        eventRepository.delete(event);
    }
}
