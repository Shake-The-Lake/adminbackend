package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    public static final String PERSON_NOT_FOUND = "Person not found";
    public static final String PERSON_IS_BOAT_DRIVER = "Person is still a boat driver";
    private final PersonRepository personRepository;
    private final BoatRepository boatRepository;
    private final PersonMapper personMapper;

    public PersonDto createPerson(CreatePersonDto createPersonDto) {
        Person person = personMapper.toEntity(createPersonDto);
        personRepository.save(person);
        return personMapper.toDto(person);
    }

    public PersonDto updatePerson(Long id, CreatePersonDto createPersonDto) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException(PERSON_NOT_FOUND);
        }
        Person updatePerson = personMapper.toEntity(createPersonDto);
        updatePerson.setId(id);
        personRepository.save(updatePerson);
        return personMapper.toDto(updatePerson);

    }

    public void deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PERSON_NOT_FOUND));
        List<Boat> boats = new ArrayList<>(boatRepository.findAll());

        if (boats.stream().anyMatch(boat -> boat.getBoatDriver().getId().equals(id))) {
            throw new IllegalArgumentException(PERSON_IS_BOAT_DRIVER);
        }
        personRepository.delete(person);
    }

    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PERSON_NOT_FOUND));
    }

    public PersonDto getPersonDto(Long id) {
        Person person = getPerson(id);
        return personMapper.toDto(person);
    }

    public List<PersonDto> getAllPersonsDto() {
        return personRepository.findAll().stream().map(personMapper::toDto).toList();
    }
}
