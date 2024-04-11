package ch.fhnw.shakethelakebackend.service;


import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BoatServiceTest {
    @Mock
    private BoatRepository boatRepository;

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
    void testCreateBoat() {
        Boat boat = new Boat();
        Person boatDriver = new Person();
        boat.setBoatDriver(boatDriver);

        when(personService.createPerson(any(Person.class))).thenReturn(boatDriver);
        when(boatRepository.save(any(Boat.class))).thenReturn(boat);

        Boat savedBoat = boatService.createBoat(boat);

        assertNotNull(savedBoat);
        verify(personService).createPerson(boatDriver);
        verify(boatRepository).save(boat);
    }

    @Test
    void testUpdateBoatExists() {
        Long boatId = 1L;
        Boat boat = new Boat();
        Person boatDriver = new Person();
        boat.setBoatDriver(boatDriver);

        when(boatRepository.existsById(boatId)).thenReturn(true);
        when(personService.updatePerson(any(Person.class))).thenReturn(boatDriver);
        when(boatRepository.save(any(Boat.class))).thenReturn(boat);

        Boat updatedBoat = boatService.updateBoat(boatId, boat);

        assertNotNull(updatedBoat);
        assertEquals(boatId, updatedBoat.getId());
        verify(boatRepository).existsById(boatId);
        verify(personService).updatePerson(boatDriver);
        verify(boatRepository).save(boat);
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
        Long boatId = 1L;
        Person boatDriver = new Person();
        boatDriver.setId(2L);
        Boat boat = new Boat();
        boat.setId(boatId);
        boat.setBoatDriver(boatDriver);

        when(boatRepository.findById(boatId)).thenReturn(Optional.of(boat));
        doNothing().when(personService).deletePerson(boatDriver.getId());
        doNothing().when(boatRepository).delete(boat);

        Boat deletedBoat = boatService.deleteBoat(boatId);

        assertNotNull(deletedBoat);
        verify(boatRepository, times(1)).findById(boatId);
        verify(personService, times(1)).deletePerson(boatDriver.getId());
        verify(boatRepository, times(1)).delete(boat);
    }

    @Test
    void testDeleteBoatNotFound() {
        Long boatId = 1L;
        when(boatRepository.findById(boatId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> boatService.deleteBoat(boatId));
    }

    @Test
    void testUpdateBoatWithPersonUpdateSuccess() {
        Long boatId = 1L;
        Person originalDriver = new Person();
        originalDriver.setId(2L); // Assume existing IDs
        Boat originalBoat = new Boat();
        originalBoat.setId(boatId);
        originalBoat.setBoatDriver(originalDriver);

        Person updatedDriver = new Person();
        updatedDriver.setId(2L);
        Boat updatedBoat = new Boat();
        updatedBoat.setBoatDriver(updatedDriver);

        when(boatRepository.existsById(boatId)).thenReturn(true);
        when(personService.updatePerson(updatedDriver)).thenReturn(updatedDriver);
        when(boatRepository.save(updatedBoat)).thenReturn(updatedBoat);

        Boat resultBoat = boatService.updateBoat(boatId, updatedBoat);

        assertNotNull(resultBoat);
        assertEquals(updatedDriver, resultBoat.getBoatDriver());
        verify(boatRepository, times(1)).existsById(boatId);
        verify(personService, times(1)).updatePerson(updatedDriver);
        verify(boatRepository, times(1)).save(updatedBoat);
    }

    @Test
    void testUpdateBoatNotFound() {
        Long boatId = 1L;
        Boat boat = new Boat();

        when(boatRepository.existsById(boatId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> boatService.updateBoat(boatId, boat));
    }


}
