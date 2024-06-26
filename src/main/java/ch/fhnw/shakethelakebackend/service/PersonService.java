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

@Service
@AllArgsConstructor
public class PersonService {
    public static final String PERSON_NOT_FOUND = "Person not found";
    private final PersonRepository personRepository;
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
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException(PERSON_NOT_FOUND);
        }
        personRepository.deleteById(id);
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
