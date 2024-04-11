package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;


    @Test
    void testUpdateNonExistingPerson() {
        Person personToUpdate = new Person();
        personToUpdate.setId(1L);

        when(personRepository.existsById(personToUpdate.getId())).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> personService.updatePerson(personToUpdate));
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
    void testCreatePersonSuccess() {
        Person person = new Person();
        when(personRepository.save(any(Person.class))).thenReturn(person);

        personService.createPerson(person);

        verify(personRepository).save(person);
    }

    @Test
    void testUpdatePersonSuccess() {
        Person personToUpdate = new Person();
        personToUpdate.setId(1L);

        when(personRepository.existsById(personToUpdate.getId())).thenReturn(true);
        when(personRepository.save(any(Person.class))).thenReturn(personToUpdate);

        personService.updatePerson(personToUpdate);

        verify(personRepository).save(personToUpdate);
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
