package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.PersonType;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import ch.fhnw.shakethelakebackend.model.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;
    private Person person;
    private CreatePersonDto createPersonDto;
    private PersonDto personDto;

    @BeforeEach
    void setUp() {
        // Sample data setup
        person = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123456789")
                .personType(PersonType.CUSTOMER)
                .build();

        createPersonDto = CreatePersonDto.builder()
                .firstName("Jane")
                .lastName("Doe")
                .emailAddress("jane.doe@example.com")
                .phoneNumber("987654321")
                .personType(PersonType.CUSTOMER)
                .build();

        personDto = PersonDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .emailAddress("john.doe@example.com")
                .phoneNumber("123456789")
                .personType(PersonType.CUSTOMER)
                .build();
    }


    @Test
    void testCreatePerson() {
        // Mocking repository method
        when(personMapper.toEntity(createPersonDto)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(personDto);

        // Calling service method
        PersonDto createdPersonDto = personService.createPerson(createPersonDto);

        // Verifying interactions
        verify(personRepository, times(1)).save(person);
        verify(personMapper, times(1)).toEntity(createPersonDto);
        verify(personMapper, times(1)).toDto(person);

        // Asserting result
        assertEquals(personDto, createdPersonDto);
    }

    @Test
    void testUpdatePerson() {
        // Mocking repository method
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toDto(person)).thenReturn(personDto);

        // Calling service method
        PersonDto updatedPersonDto = personService.updatePerson(1L, createPersonDto);

        // Verifying interactions
        verify(personRepository, times(1)).findById(1L);
        verify(personRepository, times(1)).save(person);
        verify(personMapper, times(1)).toDto(person);

        // Asserting result
        assertEquals(personDto, updatedPersonDto);
    }

    @Test
    void testDeletePerson() {
        // Mocking repository method
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(boatRepository.findAll()).thenReturn(new ArrayList<>());

        // Calling service method
        personService.deletePerson(1L);

        // Verifying interactions
        verify(personRepository, times(1)).findById(1L);
        verify(boatRepository, times(1)).findAll();
        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void testDeletePersonThrowsExceptionWhenPersonIsBoatDriver() {
        // Mocking repository method
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        List<Boat> boats = new ArrayList<>();
        Boat boat = new Boat();
        boat.setBoatDriver(person);
        boats.add(boat);
        when(boatRepository.findAll()).thenReturn(boats);

        // Calling service method and asserting exception
        assertThrows(IllegalArgumentException.class, () -> personService.deletePerson(1L));

        // Verifying interactions
        verify(personRepository, times(1)).findById(1L);
        verify(boatRepository, times(1)).findAll();
        verify(personRepository, never()).delete(any());
    }

    @Test
    void testGetPerson() {
        // Mocking repository method
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // Calling service method
        Person result = personService.getPerson(1L);

        // Verifying interactions
        verify(personRepository, times(1)).findById(1L);

        // Asserting result
        assertNotNull(result);
        assertEquals(person, result);
    }

    @Test
    void testGetPersonDto() {
        // Mocking repository method
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(personDto);

        // Calling service method
        PersonDto result = personService.getPersonDto(1L);

        // Verifying interactions
        verify(personRepository, times(1)).findById(1L);
        verify(personMapper, times(1)).toDto(person);

        // Asserting result
        assertNotNull(result);
        assertEquals(personDto, result);
    }

    @Test
    void testGetAllPersonsDto() {
        // Mocking repository method
        when(personRepository.findAll()).thenReturn(List.of(person));
        when(personMapper.toDto(person)).thenReturn(personDto);

        // Calling service method
        List<PersonDto> result = personService.getAllPersonsDto();

        // Verifying interactions
        verify(personRepository, times(1)).findAll();
        verify(personMapper, times(1)).toDto(person);

        // Asserting result
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(personDto, result.get(0));
    }



}
