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
public class BoatServiceTest {
    @Mock
    private BoatRepository boatRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private BoatService boatService;

    @Test
    void testGetBoatFound() {
        Long boatId = 1L;
        Boat boat = new Boat();
        when(boatRepository.findById(boatId)).thenReturn(Optional.of(boat));

        Boat foundBoat = boatService.getBoat(boatId);

        assertNotNull(foundBoat);
        verify(boatRepository).findById(boatId);
    }

    @Test
    void testGetBoatNotFound() {
        Long boatId = 1L;
        when(boatRepository.findById(boatId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.getBoat(boatId));
    }

    @Test
    void testCreateBoatAndReturn() {
        Boat boat = new Boat();
        boat.setId(1L);
        Person driver = new Person();
        driver.setId(1L);
        driver.setPersonType(PersonType.BOAT_DRIVER);
        boat.setBoatDriver(driver);
        when(boatRepository.existsById(boat.getId())).thenReturn(false);
        when(personService.getPerson(boat.getBoatDriver().getId())).thenReturn(driver);
        when(boatRepository.save(boat)).thenReturn(boat);

        Boat createdBoat = boatService.createBoat(boat);

        assertEquals(boat, createdBoat);
        verify(boatRepository, times(1)).save(boat);
    }

    @Test
    void testCreateBoatInvalidPerson() {
        Boat boat = new Boat();
        boat.setId(1L);
        Person driver = new Person();
        boat.setBoatDriver(driver);
        when(boatRepository.existsById(boat.getId())).thenReturn(false);
        when(personService.getPerson(boat.getBoatDriver().getId())).thenReturn(driver);


        assertThrows((IllegalArgumentException.class), () -> boatService.createBoat(boat));
        verify(boatRepository, times(0)).save(boat);
    }

    @Test
    void testUpdateBoatSuccess() {
        Long id = 1L;
        Boat existingBoat = new Boat();
        existingBoat.setId(id);
        Boat updatedBoat = new Boat();
        Person boatDriver = new Person();
        boatDriver.setPersonType(PersonType.BOAT_DRIVER);
        updatedBoat.setId(id);
        updatedBoat.setBoatDriver(boatDriver);
        when(boatRepository.existsById(id)).thenReturn(true);
        when(boatRepository.save(updatedBoat)).thenReturn(updatedBoat);
        when(personService.getPerson(updatedBoat.getBoatDriver().getId())).thenReturn(updatedBoat.getBoatDriver());

        Boat result = boatService.updateBoat(id, updatedBoat);

        assertEquals(updatedBoat, result);
        verify(boatRepository, times(1)).existsById(id);
        verify(boatRepository, times(1)).save(updatedBoat);
        verify(personService, times(1)).getPerson(updatedBoat.getBoatDriver().getId());
    }

    @Test
    void testUpdateBoatNonExisting() {
        Long id = 1L;
        Boat updatedBoat = new Boat();
        updatedBoat.setId(id);
        updatedBoat.setBoatDriver(new Person());
        when(boatRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> boatService.updateBoat(id, updatedBoat));

        verify(boatRepository, times(1)).existsById(id);
        verify(boatRepository, never()).save(any());
        verify(personService, never()).getPerson(any());
    }

    @Test
    void testUpdateBoatNotExists() {
        Long boatId = 1L;
        Boat boat = new Boat();

        when(boatRepository.existsById(boatId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> boatService.updateBoat(boatId, boat));
    }

    @Test
    void testDeleteBoatSuccess() {
        Long id = 1L;
        Boat boat = new Boat();
        boat.setId(id);
        when(boatRepository.findById(id)).thenReturn(Optional.of(boat));

        boatService.deleteBoat(id);

        verify(boatRepository, times(1)).findById(id);
        verify(boatRepository, times(1)).delete(boat);
    }

    @Test
    void testDeleteBoatNonExisting() {
        Long id = 1L;
        when(boatRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.deleteBoat(id));

        verify(boatRepository, times(1)).findById(id);
        verify(boatRepository, never()).delete(any());
    }

    @Test
    void testDeleteBoatNotFound() {
        Long boatId = 1L;
        when(boatRepository.findById(boatId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.deleteBoat(boatId));
    }


    @Test
    void testUpdateBoatNotFound() {
        Long boatId = 1L;
        Boat boat = new Boat();

        when(boatRepository.existsById(boatId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> boatService.updateBoat(boatId, boat));
    }


}

