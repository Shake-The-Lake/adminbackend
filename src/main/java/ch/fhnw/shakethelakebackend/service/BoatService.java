package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.enums.PersonType;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BoatService {

    public static final String BOAT_NOT_FOUND = "Boat not found";
    public static final String PERSON_IS_NOT_BOAT_DRIVER = "Person is not a boat driver";
    private final BoatMapper boatMapper;
    private final BoatRepository boatRepository;
    private final PersonService personService;
    private final ActivityTypeService activityTypeService;
    private final EventService eventService;
    private final PersonDto testUser;
    private final ExpandHelper expandHelper;

    public BoatService(BoatMapper boatMapper, BoatRepository boatRepository, PersonService personService,
        ActivityTypeService activityTypeService, EventService eventService, ExpandHelper expandHelper) {
        this.boatMapper = boatMapper;
        this.boatRepository = boatRepository;
        this.personService = personService;
        this.activityTypeService = activityTypeService;
        this.eventService = eventService;
        this.expandHelper = expandHelper;

        this.testUser = this.personService.createPerson(
            CreatePersonDto.builder().firstName("Charon").lastName("Fährmann").personType(PersonType.BOAT_DRIVER)
                .emailAddress("mymy@ti8m.ch").phoneNumber("079 hät si gseit").build());
    }

    public BoatDto getBoatDto(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));

        return boatMapper.toDto(boat);
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(BOAT_NOT_FOUND));
    }

    public BoatDto createBoat(CreateBoatDto createBoatDto) {
        // FIXME this is temporary while users can't be created in the frontend
        if (this.testUser != null) {
            createBoatDto.setBoatDriverId(this.testUser.getId());
            createBoatDto.setActivityTypeId(createDummyActivityType(createBoatDto.getEventId()).getId());
        }
        Person driver = personService.getPerson(createBoatDto.getBoatDriverId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException(PERSON_IS_NOT_BOAT_DRIVER);
        }
        ActivityType activityType = activityTypeService.getActivityType(createBoatDto.getActivityTypeId());
        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat boat = boatMapper.toEntity(createBoatDto);
        boat.setBoatDriver(driver);
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
        Person driver = personService.getPerson(createBoatDto.getBoatDriverId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException(PERSON_IS_NOT_BOAT_DRIVER);
        }
        ActivityType activityType = activityTypeService.getActivityType(createBoatDto.getActivityTypeId());
        Event event = eventService.getEvent(createBoatDto.getEventId());
        Boat newBoat = boatMapper.toEntity(createBoatDto);
        newBoat.setBoatDriver(driver);
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
        List<Boat> boats = boatRepository.findAll();
        List<BoatDto> boatDtos = getAllBoats();

        boatDtos = boatDtos.stream().map(boat -> getBoatWithDetails(boat.getId(), expand)).toList();

        List<BoatDto> finalBoatDtos = boatDtos;
        boatDtos = expandHelper.applyExpansion(expand, Set.of("activityType", "timeSlots"), shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                return boats.stream().map(boatMapper::toDtoWithTimeSlotsAndActivityType).toList();
            } else {
                return finalBoatDtos;
            }
        });

        return boatDtos;
    }

    public BoatDto getBoatWithDetails(Long id, Optional<String> expand) {
        Boat boat = getBoat(id);
        BoatDto boatDto = getBoatDto(id);
        BoatDto originalBoatDto = boatDto;

        boatDto = expandHelper.applyExpansion(expand, "timeSlots", shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                return boatMapper.toDtoWithTimeSlots(boat);
            } else {
                return originalBoatDto;
            }
        });
        boatDto.setActivityType(expandHelper.applyExpansion(expand, "activityType", shouldExpand -> {
            if (shouldExpand.isPresent() && shouldExpand.get()) {
                return activityTypeService.toDto(boat.getActivityType());
            } else {
                return null;
            }
        }));

        return boatDto;
    }
}
