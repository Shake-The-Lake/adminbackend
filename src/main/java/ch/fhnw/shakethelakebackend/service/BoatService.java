package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Event;
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
    private final BoatMapper boatMapper;
    private final BoatRepository boatRepository;

    private final EventService eventService;
    private final Expander expander;
    private final TimeSlotMapper timeSlotMapper;

    public BoatDto getBoatDto(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));

        return boatMapper.toDto(boat);
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));
    }

    public BoatDto createBoat(CreateBoatDto createBoatDto) {
        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat boat = boatMapper.toEntity(createBoatDto);
        boat.setEvent(event);
        boatRepository.save(boat);
        return boatMapper.toDto(boat);
    }


    public BoatDto updateBoat(Long id, CreateBoatDto createBoatDto) {
        if (!boatRepository.existsById(id)) {
            throw new EntityNotFoundException(BOAT_NOT_FOUND);
        }

        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat newBoat = getBoat(id);
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

        return boatDto;
    }
}
