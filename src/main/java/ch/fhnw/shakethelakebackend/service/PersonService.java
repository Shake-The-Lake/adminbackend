package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Service for persons
 *
 */
@Service
@AllArgsConstructor
public class PersonService {
    public static final String PERSON_NOT_FOUND = "Person not found";
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    /**
     * Create a new person
     *
     * @param createPersonDto to create a new person
     * @return PersonDto created from the given CreatePersonDto
     */
    public PersonDto createPerson(CreatePersonDto createPersonDto) {
        Person person = personMapper.toEntity(createPersonDto);
        personRepository.save(person);
        return personMapper.toDto(person);
    }

    /**
     * Update a person
     *
     * @param id of the person to update
     * @param createPersonDto to update the person
     * @return PersonDto updated from the given CreatePersonDto
     */
    public PersonDto updatePerson(Long id, CreatePersonDto createPersonDto) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException(PERSON_NOT_FOUND);
        }
        Person person = getPerson(id);
        personMapper.update(createPersonDto, person);
        personRepository.save(person);
        return personMapper.toDto(person);

    }

    /**
     * Delete a person
     *
     * @param id of the person to delete
     */
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException(PERSON_NOT_FOUND);
        }
        personRepository.deleteById(id);
    }

    /**
     * Get a person by id
     *
     * @param id of the person
     * @return Person with the given id
     */
    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PERSON_NOT_FOUND));
    }

    /**
     * Get a person by id
     *
     * @param id of the person
     * @return PersonDto with the given id
     */
    public PersonDto getPersonDto(Long id) {
        Person person = getPerson(id);
        return personMapper.toDto(person);
    }

    /**
     * Get all persons
     *
     * @return List of all persons
     */
    public List<PersonDto> getAllPersonsDto() {
        return personRepository.findAll().stream().map(personMapper::toDto).toList();
    }
}
