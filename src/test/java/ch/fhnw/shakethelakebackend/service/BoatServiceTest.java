package ch.fhnw.shakethelakebackend.service;


import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.PostBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.PersonType;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BoatServiceTest {
    @Mock
    private BoatRepository boatRepository;

    @Mock
    private BoatMapper boatMapper;

    @Mock
    private PersonService personService;

    @InjectMocks
    private BoatService boatService;

    private Boat boat;

    private Person person;
    private PostBoatDto postBoatDto;
    private BoatDto boatDto;

    @BeforeEach
    void setUp() {
        // Sample data setup
        boat = new Boat();
        boat.setId(1L);
        boat.setName("Odyssey");
        boat.setType("Yacht");
        person = new Person();
        person.setPersonType(PersonType.BOAT_DRIVER);
        person.setId(1L);
        boat.setBoatDriver(person);

        postBoatDto = new PostBoatDto();
        postBoatDto.setBoatDriverId(1L);
        postBoatDto.setName("Odyssey");
        postBoatDto.setType("Yacht");

        boatDto = new BoatDto();
        boatDto.setId(1L);
        boatDto.setBoatDriverId(1L);
        boatDto.setName("Odyssey");
        boatDto.setType("Yacht");

    }

    @Test
    void testCreateBoatSuccess() {
        //when
        when(personService.getPerson(any())).thenReturn(person);
        when(boatMapper.toEntity(postBoatDto)).thenReturn(boat);
        when(boatMapper.toDto(boat)).thenReturn(boatDto);
        // Act
        BoatDto result = boatService.createBoat(postBoatDto);

        // Assert
        assertEquals(boatDto, result);
        verify(boatRepository).save(boat);
        verify(boatMapper).toEntity(postBoatDto);
        verify(boatMapper).toDto(boat);
    }

    @Test
    void testCreateBoatWithNullDto() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> boatService.createBoat(null),
                "Expected createBoat to throw, but it did not");
        verify(boatRepository, never()).save(any(Boat.class));
    }

    @Test
    void testCreateBoatWithInvalidData() {
        when(personService.getPerson(any())).thenReturn(person);
        // Arrange
        when(boatMapper.toEntity(any(PostBoatDto.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> boatService.createBoat(postBoatDto),
                "Expected IllegalArgumentException due to invalid data");
        verify(boatRepository, never()).save(any(Boat.class));
    }

    @Test
    void testCreateBoatPersonNotDriver() {
        // Arrange
        person.setPersonType(PersonType.CUSTOMER);
        when(personService.getPerson(any())).thenReturn(person);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> boatService.createBoat(postBoatDto),
                "Expected IllegalArgumentException due to person not being a driver");
        verify(boatRepository, never()).save(any(Boat.class));
    }

    @Test
    void testGetBoatDtoByIdSuccess() {
        when(boatRepository.findById(any())).thenReturn(Optional.of(boat));
        when(boatMapper.toDto(any())).thenReturn(boatDto);

        BoatDto result = boatService.getBoatDto(1L);

        assertEquals(boatDto, result);
        verify(boatRepository).findById(1L);
        verify(boatMapper).toDto(boat);
    }

    @Test
    void testGetBoatDtoByIdFailure() {
        when(boatRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.getBoatDto(1L),
                "Expected EntityNotFoundException due to non-existing boat");
    }

    @Test
    void testUpdateBoatSuccess() {
        when(boatRepository.existsById(any())).thenReturn(true);
        when(personService.getPerson(any())).thenReturn(person);
        when(boatMapper.toEntity(any())).thenReturn(boat);
        when(boatRepository.save(any())).thenReturn(boat);
        when(boatMapper.toDto(any())).thenReturn(boatDto);

        BoatDto result = boatService.updateBoat(5L, postBoatDto);

        assertEquals(boatDto, result);
        assertEquals(5L, boat.getId());
        verify(boatRepository).save(boat);
    }

    @Test
    void testDeleteBoatSuccess() {
        when(boatRepository.findById(any())).thenReturn(Optional.of(boat));

        boatService.deleteBoat(1L);

        verify(boatRepository).delete(boat);
    }

    @Test
    void testDeleteBoatFailure() {
        when(boatRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.deleteBoat(1L),
                "Expected EntityNotFoundException due to non-existing boat");
    }

    @Test
    void testGetAllBoats() {
        when(boatRepository.findAll()).thenReturn(List.of(boat));
        when(boatMapper.toDto(any())).thenReturn(boatDto);

        List<BoatDto> result = boatService.getAllBoats();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(boatDto, result.get(0));
        verify(boatRepository).findAll();
        verify(boatMapper, times(1)).toDto(boat);
    }
}
