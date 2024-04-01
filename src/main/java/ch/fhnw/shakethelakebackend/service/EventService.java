package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event getEvent(Long id) throws NotFoundException {
        return eventRepository.findById(id).orElseThrow(NotFoundException::new);
    }
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}
