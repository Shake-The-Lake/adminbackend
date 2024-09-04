package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.Icon;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.repository.ActivityTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ActivityTypeService {
    public static final String ACTIVITY_TYPE_NOT_FOUND = "Activity type not found";
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;
    private final EventService eventService;
    private final IconService iconService;

    public List<ActivityTypeDto> getAllActivityTypes() {
        return activityTypeRepository.findAll().stream().map(activityTypeMapper::toDto).toList();
    }

    public ActivityTypeDto getActivityTypeDto(Long id) {
        ActivityType activityType = activityTypeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND));
        return activityTypeMapper.toDto(activityType);
    }

    public ActivityType getActivityType(Long id) {
        return activityTypeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND));
    }

    public ActivityTypeDto createActivityType(CreateActivityTypeDto createActivityTypeDto) {
        ActivityType activityType = activityTypeMapper.toEntity(createActivityTypeDto);
        Event event = eventService.getEvent(createActivityTypeDto.getEventId());
        Icon icon = iconService.getIcon(createActivityTypeDto.getIconId());
        activityType.setEvent(event);
        activityType.setIcon(icon);
        activityTypeRepository.save(activityType);
        return activityTypeMapper.toDto(activityType);
    }

    public void deleteActivityType(Long id) {
        if (!activityTypeRepository.existsById(id)) {
            throw new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND);
        }
        activityTypeRepository.deleteById(id);
    }

    public ActivityTypeDto updateActivityType(Long id, CreateActivityTypeDto createActivityTypeDto) {
        if (!activityTypeRepository.existsById(id)) {
            throw new EntityNotFoundException(ACTIVITY_TYPE_NOT_FOUND);
        }
        ActivityType updateActivityType = activityTypeMapper.toEntity(createActivityTypeDto);
        Event event = eventService.getEvent(createActivityTypeDto.getEventId());
        Icon icon = iconService.getIcon(createActivityTypeDto.getIconId());
        updateActivityType.setEvent(event);
        updateActivityType.setIcon(icon);
        updateActivityType.setId(id);
        activityTypeRepository.save(updateActivityType);
        return activityTypeMapper.toDto(updateActivityType);
    }

    public ActivityTypeDto toDto(ActivityType activityType) {
        return activityTypeMapper.toDto(activityType);
    }
}
