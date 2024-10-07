package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.repository.ActivityTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Service for activity types
 *
 */
@AllArgsConstructor
@Service
public class ActivityTypeService {
    public static final String ACTIVITY_TYPE_NOT_FOUND = "Activity type not found";
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;
    private final EventService eventService;

    /**
     *
     *  Get all activity types
     *
     * @return List of all activity types
     */
    public List<ActivityTypeDto> getAllActivityTypes() {
        return activityTypeRepository.findAll().stream().map(activityTypeMapper::toDto).toList();
    }

    /**
     *
     * Get an activity type by id
     *
     * @param id of the activity type
     * @return ActivityTypeDto with the given id
     */
    public ActivityTypeDto getActivityTypeDto(Long id) {
        ActivityType activityType = activityTypeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND));
        return activityTypeMapper.toDto(activityType);
    }

    /**
     *
     * Get an activity type by id
     *
     * @param id of the activity type
     * @return ActivityType with the given id
     */
    public ActivityType getActivityType(Long id) {
        return activityTypeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND));
    }

    /**
     *
     * Create a new activity type
     *
     * @param createActivityTypeDto to create a new activity type
     * @return ActivityTypeDto created from the given CreateActivityTypeDto
     */
    public ActivityTypeDto createActivityType(CreateActivityTypeDto createActivityTypeDto) {
        ActivityType activityType = activityTypeMapper.toEntity(createActivityTypeDto);
        Event event = eventService.getEvent(createActivityTypeDto.getEventId());
        activityType.setEvent(event);
        activityTypeRepository.save(activityType);
        return activityTypeMapper.toDto(activityType);
    }

    /**
     *
     * Delete an activity type by id
     *
     * @param id of the activity type to delete
     */
    public void deleteActivityType(Long id) {
        if (!activityTypeRepository.existsById(id)) {
            throw new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND);
        }
        activityTypeRepository.deleteById(id);
    }

    /**
     *
     * Update an activity type
     *
     * @param id of the activity type to update
     * @param createActivityTypeDto to update the activity type
     * @return ActivityTypeDto updated from the given CreateActivityTypeDto
     */
    public ActivityTypeDto updateActivityType(Long id, CreateActivityTypeDto createActivityTypeDto) {
        if (!activityTypeRepository.existsById(id)) {
            throw new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND);
        }
        ActivityType activityType = getActivityType(id);
        Event event = eventService.getEvent(createActivityTypeDto.getEventId());
        activityTypeMapper.update(createActivityTypeDto, activityType);
        activityType.setEvent(event);
        activityTypeRepository.save(activityType);
        return activityTypeMapper.toDto(activityType);
    }

    /**
     *
     * Convert an ActivityType to a DTO
     *
     * @param activityType to convert to a DTO
     * @return ActivityTypeDto converted from the given ActivityType
     */
    public ActivityTypeDto toDto(ActivityType activityType) {
        return activityTypeMapper.toDto(activityType);
    }
}
