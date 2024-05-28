package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class EventManagementFacade {

    private final EventMapper eventMapper;

    private EventService eventService;

    private BoatService boatService;

    private ActivityTypeService activityTypeService;

    public EventDto getEventWithDetails(Long id, Optional<String> expand) {
        Event event = eventService.getEvent(id);
        EventDto eventDto = eventMapper.toDto(event);

        if (expand.isPresent()) {
            Set<String> expands = new HashSet<>(Arrays.asList(expand.get().split(",")));
            if (expands.contains("boats")) {
                List<BoatDto> boatDtos = boatService.getBoatsByEvent(id);
                eventDto.setBoats(boatDtos);
            }
            if (expands.contains("activityTypes")) {
                List<ActivityTypeDto> activityTypeDtos = activityTypeService.getActivityTypesByEvent(id);
                eventDto.setActivityTypes(activityTypeDtos);
            }
        }

        return eventDto;
    }
}