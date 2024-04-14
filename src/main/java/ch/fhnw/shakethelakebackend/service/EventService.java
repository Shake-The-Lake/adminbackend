package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class
EventService {

    private final EventRepository eventRepository;

    public Event getEvent(Long id) throws NotFoundException {
        return eventRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // May need to be discussed what should be updatable or not
    // Just a first implementation which allows us to update all properties
    public Event updateEvent(Long id, Event event){
        Event existingEvent = eventRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        
        Event updatedEvent = Event.builder()
            .id(existingEvent.getId())
            .location(event.getLocation() != null ? event.getLocation() : existingEvent.getLocation())
            .date(event.getDate() != null ? event.getDate() : existingEvent.getDate())
            .customerCode(event.getCustomerCode() != null ? event.getCustomerCode() : existingEvent.getCustomerCode())
            .employeeCode(event.getEmployeeCode() != null ? event.getEmployeeCode() : existingEvent.getEmployeeCode())
            .customerOnlyTime(event.getCustomerOnlyTime() != null ? event.getCustomerOnlyTime() : existingEvent.getCustomerOnlyTime())
            .startedAt(event.getStartedAt() != null ? event.getStartedAt() : existingEvent.getStartedAt())
            .endedAt(event.getEndedAt() != null ? event.getEndedAt() : existingEvent.getEndedAt())
            .build();
        return eventRepository.save(updatedEvent);
    }

    public void deleteEvent(Long id){
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        eventRepository.delete(event);
    }
}
