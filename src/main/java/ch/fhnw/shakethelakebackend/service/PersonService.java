package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
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
    private final PersonRepository personRepository;

    private final BoatRepository boatRepository;

    public Person createPerson(Person person) {
        if (person.getId() != null && personRepository.existsById(person.getId())) {
            throw new IllegalArgumentException("Person already exists");
        }
        return personRepository.save(person);
    }

    public Person updatePerson(Long id, Person person) {
        Person existingPerson = personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Person not found"));

        existingPerson.setId(id);
        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setEmailAddress(person.getEmailAddress());
        existingPerson.setPhoneNumber(person.getPhoneNumber());
        existingPerson.setPersonType(person.getPersonType());

        return personRepository.save(existingPerson);
    }

    public void deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Person not found"));
        List<Boat> boats = new ArrayList<>(boatRepository.findAll());

        if (boats.stream().anyMatch(boat -> boat.getBoatDriver().getId().equals(id))) {
            throw new IllegalArgumentException("Person is a boat driver");
        }
        personRepository.delete(person);
    }


    public Person getPerson(Long id) {
        return personRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Person not found"));
    }
}
