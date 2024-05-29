package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    public static final String EVENT_NOT_FOUND = "Event not found";
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final BoatMapper boatMapper;
    private final ActivityTypeMapper activityTypeMapper;
    private final ExpandHelper expandHelper;

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

    public EventDto updateEvent(Long id, CreateEventDto createEventDto) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException(EVENT_NOT_FOUND);
        }

        Event newEvent = eventMapper.toEntity(createEventDto);
        newEvent.setId(id);
        eventRepository.save(newEvent);
        return eventMapper.toDto(newEvent);
    }

    public void deleteEvent(Long id) {
        //TODO: discuss whether an event should remove everything=?
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
        eventRepository.delete(event);
    }

    public EventDto getEventWithDetails(Long id, Optional<String> expand) {
        Event event = getEvent(id);
        EventDto eventDto = eventMapper.toDto(event);

        expandHelper.applyExpansion(expand, "boats", shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                List<BoatDto> boatDtos = event.getBoats().stream().map(boatMapper::toDto).toList();
                eventDto.setBoats(boatDtos);
            }
        });
        expandHelper.applyExpansion(expand, "boats.timeSlots", shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                List<BoatDto> boatDtos = event.getBoats().stream().map(boatMapper::toDtoWithTimeSlots).toList();
                eventDto.setBoats(boatDtos);
            }
        });
        expandHelper.applyExpansion(expand, "activityTypes", shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                List<ActivityTypeDto> activityTypeDtos = event.getActivityTypes().stream()
                    .map(activityTypeMapper::toDto).toList();
                eventDto.setActivityTypes(activityTypeDtos);
            }
        });

        return eventDto;
    }
}
