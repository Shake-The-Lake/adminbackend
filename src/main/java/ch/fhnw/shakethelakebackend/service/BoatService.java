package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.PersonType;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BoatService {

    private final BoatMapper boatMapper;
    private final BoatRepository boatRepository;
    private final PersonService personService;
    private static final String BOAT_NOT_FOUND = "Boat not found";
    private static final String PERSON_NOT_FOUND = "Person not found";

    public BoatDto getBoatDto(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(BOAT_NOT_FOUND));

        return boatMapper.toDto(boat);
    }

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(BOAT_NOT_FOUND));
    }


    public BoatDto createBoat(CreateBoatDto createBoatDto) {
        if (createBoatDto == null) {
            throw new IllegalArgumentException("Boat data is missing");
        }
        Person driver = personService.getPerson(createBoatDto.getBoatDriverId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException(PERSON_NOT_FOUND);
        }
        Boat boat = boatMapper.toEntity(createBoatDto);
        boatRepository.save(boat);
        return boatMapper.toDto(boat);
    }

    public BoatDto updateBoat(Long id, CreateBoatDto createBoatDto) {
        if (!boatRepository.existsById(id)) {
            throw new EntityNotFoundException(BOAT_NOT_FOUND);
        }
        Person driver = personService.getPerson(createBoatDto.getBoatDriverId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException(PERSON_NOT_FOUND);
        }
        Boat newBoat = boatMapper.toEntity(createBoatDto);
        newBoat.setBoatDriver(driver);
        newBoat.setId(id);
        boatRepository.save(newBoat);
        return boatMapper.toDto(newBoat);

    }

    public void deleteBoat(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(BOAT_NOT_FOUND));
        boatRepository.delete(boat);
    }

    public List<BoatDto> getAllBoats() {
        return boatRepository.findAll().stream().map(boatMapper::toDto).collect(Collectors.toList());
    }
}
