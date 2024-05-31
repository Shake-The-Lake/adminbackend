package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoatService {

    public static final String BOAT_NOT_FOUND = "Boat not found";
    public static final String PERSON_IS_NOT_BOAT_DRIVER = "Person is not a boat driver";
    private final BoatMapper boatMapper;
    private final BoatRepository boatRepository;

    private final ActivityTypeService activityTypeService;
    private final EventService eventService;
    private final Expander expander;
    private final TimeSlotMapper timeSlotMapper;
    private final ActivityTypeMapper activityTypeMapper;

    public BoatDto getBoatDto(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));

        return boatMapper.toDto(boat);
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));
    }

    public BoatDto createBoat(CreateBoatDto createBoatDto) {
        ActivityType activityType = activityTypeService.getActivityType(createBoatDto.getActivityTypeId());
        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat boat = boatMapper.toEntity(createBoatDto);
        boat.setActivityType(activityType);
        boat.setEvent(event);
        boatRepository.save(boat);
        return boatMapper.toDto(boat);
    }

    private ActivityTypeDto createDummyActivityType(Long eventId) {
        // FIXME this is only for test purpose
        return this.activityTypeService.createActivityType(CreateActivityTypeDto.builder().eventId(eventId)
                .description(new LocalizedString("Wakeboarding", "Wakeboarding", "Wakeboarding"))
                .checklist(new LocalizedString("Wakeboarding", "Wakeboarding", "Wakeboarding")).icon("icon").build());
    }

    public BoatDto updateBoat(Long id, CreateBoatDto createBoatDto) {
        if (!boatRepository.existsById(id)) {
            throw new EntityNotFoundException(BOAT_NOT_FOUND);
        }

        ActivityType activityType = activityTypeService.getActivityType(createBoatDto.getActivityTypeId());
        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat newBoat = boatMapper.toEntity(createBoatDto);
        newBoat.setActivityType(activityType);
        newBoat.setEvent(event);
        newBoat.setId(id);
        boatRepository.save(newBoat);
        return boatMapper.toDto(newBoat);
    }

    public void deleteBoat(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));
        boatRepository.delete(boat);
    }

    public List<BoatDto> getAllBoats() {
        return boatRepository.findAll().stream().map(boatMapper::toDto).toList();
    }

    public List<BoatDto> getBoatsWithDetails(Optional<String> expand) {
        List<BoatDto> boatDtos = getAllBoats();

        boatDtos = boatDtos.stream().map(boat -> getBoatWithDetails(boat.getId(), expand)).toList();

        return boatDtos;
    }

    public BoatDto getBoatWithDetails(Long id, Optional<String> expand) {
        Boat boat = getBoat(id);
        BoatDto boatDto = getBoatDto(id);

        expander.applyExpansion(expand, "timeSlots", () -> {
            Set<TimeSlotDto> timeSlotDto = boat.getTimeSlots().stream().map(timeSlotMapper::toDto)
                    .collect(Collectors.toSet());
            boatDto.setTimeSlots(timeSlotDto);
        });

        expander.applyExpansion(expand, "activityType", () -> {
            ActivityTypeDto activityTypeDto = activityTypeMapper.toDto(boat.getActivityType());
            boatDto.setActivityType(activityTypeDto);
        });

        return boatDto;
    }
}
