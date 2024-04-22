package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.PersonType;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BoatRepository boatRepository;

    @InjectMocks
    private PersonService personService;


    @Test
    void testUpdateNonExistingPerson() {
        Person personToUpdate = new Person();
        personToUpdate.setId(1L);

        assertThrows(EntityNotFoundException.class,
                () -> personService.updatePerson(1L, personToUpdate));
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void testDeleteNonExistingPerson() {
        Long personId = 1L;
        when(personRepository.findById(personId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> personService.deletePerson(personId));
        verify(personRepository, never()).delete(any(Person.class));
    }

    @Test
    void testDeletePersonIsBoatDriver() {
        Long id = 1L;
        Person person = new Person();
        person.setId(id);
        List<Boat> boats = new ArrayList<>();
        Boat boat = new Boat();
        boat.setBoatDriver(person);
        boats.add(boat);
        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(boatRepository.findAll()).thenReturn(boats);

        assertThrows(IllegalArgumentException.class, () -> personService.deletePerson(id));

        verify(personRepository, times(1)).findById(id);
        verify(boatRepository, times(1)).findAll();
        verify(personRepository, never()).delete(person);
    }

    @Test
    void testCreatePersonSuccess() {
        Person person = new Person();
        person.setId(1L);
        when(personRepository.existsById(person.getId())).thenReturn(false);
        when(personRepository.save(person)).thenReturn(person);

        Person createdPerson = personService.createPerson(person);

        assertEquals(person, createdPerson);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testCreatePersonThrowsIllegalArgumentException() {
        Person person = new Person();
        person.setId(1L);
        when(personRepository.existsById(person.getId())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(person));

        verify(personRepository, never()).save(any());
    }

    @Test
    void testUpdatePersonSucess() {
        Long id = 1L;
        Person existingPerson = new Person();
        existingPerson.setId(id);
        Person updatedPerson = new Person();
        updatedPerson.setId(id);
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setEmailAddress("john.doe@example.com");
        updatedPerson.setPhoneNumber("123456789");
        updatedPerson.setPersonType(PersonType.EMPLOYEE);
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(updatedPerson)).thenReturn(updatedPerson);

        Person result = personService.updatePerson(id, updatedPerson);

        assertEquals(updatedPerson, result);
        verify(personRepository, times(1)).findById(id);
        verify(personRepository, times(1)).save(updatedPerson);
    }

    @Test
    void testUpdatePersonNonExisting() {
        Long id = 1L;
        Person updatedPerson = new Person();
        updatedPerson.setId(id);
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setEmailAddress("john.doe@example.com");
        updatedPerson.setPhoneNumber("123456789");
        updatedPerson.setPersonType(PersonType.CUSTOMER);
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> personService.updatePerson(id, updatedPerson));

        verify(personRepository, times(1)).findById(id);
        verify(personRepository, never()).save(any());
    }


    @Test
    void testDeletePersonSuccess() {
        Long personId = 1L;
        Person person = new Person();
        when(personRepository.findById(personId)).thenReturn(java.util.Optional.of(person));

        personService.deletePerson(personId);

        verify(personRepository).delete(person);
    }


}
