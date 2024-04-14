package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Person createPerson(Person person) {
        //TODO check if person is not boat driver then email must not be null
        return personRepository.save(person);
    }

    public Person updatePerson(Person boatDriver) {
        if (!personRepository.existsById(boatDriver.getId())) {
            throw new EntityNotFoundException("Person not found");
        }
        return personRepository.save(boatDriver);
    }

    public void deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Person not found"));
        personRepository.delete(person);
    }
}
