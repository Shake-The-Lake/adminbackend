package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
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

    @Mock
    private ActivityTypeService activityTypeService;

    @Mock
    private EventService eventService;

    @InjectMocks
    private BoatService boatService;

    private Boat boat;

    private CreateBoatDto createBoatDto;
    private BoatDto boatDto;
    private ActivityType activityType;

    @BeforeEach
    void setUp() {
        // Sample data setup
        boat = new Boat();
        boat.setId(1L);
        boat.setName("Odyssey");
        boat.setType("Yacht");
        boat.setOperator("John Doe");

        createBoatDto = new CreateBoatDto();
        createBoatDto.setName("NewOdyssey");
        createBoatDto.setType("NewYacht");

        boatDto = new BoatDto();
        boatDto.setId(1L);
        boatDto.setName("Odyssey");
        boatDto.setType("Yacht");

    }

    @Test
    void testCreateBoatSuccess() {
        //when
        when(boatMapper.toEntity(createBoatDto)).thenReturn(boat);
        when(boatMapper.toDto(boat)).thenReturn(boatDto);
        // Act
        BoatDto result = boatService.createBoat(createBoatDto);

        // Assert
        assertEquals(boatDto, result);
        verify(boatRepository).save(boat);
        verify(boatMapper).toEntity(createBoatDto);
        verify(boatMapper).toDto(boat);
    }

    @Test
    void testCreateBoatWithInvalidData() {
        // Arrange
        when(boatMapper.toEntity(any(CreateBoatDto.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> boatService.createBoat(createBoatDto),
                "Expected IllegalArgumentException due to invalid data");
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
        when(boatRepository.findById(any())).thenReturn(Optional.of(boat));
        when(boatRepository.save(any())).thenReturn(boat);
        when(boatMapper.toDto(any())).thenReturn(boatDto);

        BoatDto result = boatService.updateBoat(5L, createBoatDto);

        assertEquals(boatDto, result);
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
