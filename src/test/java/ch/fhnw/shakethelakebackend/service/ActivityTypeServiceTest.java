package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.Icon;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.repository.ActivityTypeRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityTypeServiceTest {

    @Mock
    private ActivityTypeRepository activityTypeRepository;

    @Mock
    private ActivityTypeMapper activityTypeMapper;

    @Mock
    private EventService eventService;

    @Mock
    private IconService iconService;

    @InjectMocks
    private ActivityTypeService activityTypeService;

    private ActivityType activityType;
    private CreateActivityTypeDto createActivityTypeDto;
    private Event event;
    private Icon icon;

    @BeforeEach
    void setUp() {
        activityType = new ActivityType();
        activityType.setId(1L);

        createActivityTypeDto = new CreateActivityTypeDto();
        event = new Event();
        event.setId(1L);

        icon = Icon.builder().id(1L).build();

    }

    @Test
    void getAllActivityTypesReturnsActivityTypes() {
        List<ActivityType> activityTypes = new ArrayList<>();
        activityTypes.add(activityType);
        when(activityTypeRepository.findAll()).thenReturn(activityTypes);
        when(activityTypeMapper.toDto(any(ActivityType.class))).thenReturn(new ActivityTypeDto());

        List<ActivityTypeDto> result = activityTypeService.getAllActivityTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(activityTypeRepository).findAll();
        verify(activityTypeMapper).toDto(any(ActivityType.class));
    }

    @Test
    void getActivityTypeDtoWithValidIdReturnsDto() {
        when(activityTypeRepository.findById(1L)).thenReturn(Optional.of(activityType));
        when(activityTypeMapper.toDto(activityType)).thenReturn(new ActivityTypeDto());


        ActivityTypeDto result = activityTypeService.getActivityTypeDto(1L);

        assertNotNull(result);
        verify(activityTypeRepository).findById(1L);
        verify(activityTypeMapper).toDto(activityType);
    }

    @Test
    void getActivityTypeDtoWithInvalidId() {
        when(activityTypeRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            activityTypeService.getActivityTypeDto(1L);
        });

        assertEquals(ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void createActivityTypeCreatesAndReturnsDto() {

        createActivityTypeDto.setEventId(1L);

        when(activityTypeMapper.toEntity(createActivityTypeDto)).thenReturn(activityType);
        when(activityTypeRepository.save(activityType)).thenReturn(activityType);
        when(activityTypeMapper.toDto(activityType)).thenReturn(new ActivityTypeDto());

        ActivityTypeDto result = activityTypeService.createActivityType(createActivityTypeDto);

        assertNotNull(result);
        verify(activityTypeRepository).save(activityType);
        verify(activityTypeMapper).toDto(activityType);
    }

    @Test
    void deleteActivityTypeDeletesActivityType() {
        Long activityTypeId = 1L;
        when(activityTypeRepository.existsById(activityTypeId)).thenReturn(true);
        doNothing().when(activityTypeRepository).deleteById(activityTypeId);

        activityTypeService.deleteActivityType(activityTypeId);

        verify(activityTypeRepository).existsById(activityTypeId);
        verify(activityTypeRepository).deleteById(activityTypeId);
    }

    @Test
    void createActivityTypeWithInvalidEventId() {
        createActivityTypeDto = new CreateActivityTypeDto();
        createActivityTypeDto.setEventId(9999L);  // Assuming 999L is an invalid ID

        when(eventService.getEvent(9999L)).thenThrow(new EntityNotFoundException("Event not found"));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            activityTypeService.createActivityType(createActivityTypeDto);
        });

        assertEquals("Event not found", exception.getMessage());
        verify(activityTypeMapper, never()).toDto(any(ActivityType.class));
        verify(activityTypeRepository, never()).save(any(ActivityType.class));
    }

    @Test
    void deleteActivityTypeWithInvalidId() {
        Long activityTypeId = 1L;
        when(activityTypeRepository.existsById(activityTypeId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            activityTypeService.deleteActivityType(activityTypeId);
        });

        assertEquals(ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND, exception.getMessage());
        verify(activityTypeRepository).existsById(activityTypeId);
        verify(activityTypeRepository, never()).deleteById(activityTypeId);
    }

    @Test
    void updateActivityTypeWithInvalidId() {
        Long activityTypeId = 1L;
        when(activityTypeRepository.existsById(activityTypeId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            activityTypeService.updateActivityType(activityTypeId, createActivityTypeDto);
        });

        assertEquals(ActivityTypeService.ACTIVITY_TYPE_NOT_FOUND, exception.getMessage());
        verify(activityTypeRepository).existsById(activityTypeId);
        verify(activityTypeRepository, never()).save(any(ActivityType.class));
    }
}
