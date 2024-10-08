package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * Service for time slots
 *
 */
@AllArgsConstructor
@Service
public class TimeSlotService {

    public static final String TIMESLOT_NOT_FOUND = "TimeSlot not found";

    private final Expander expander;
    private final TimeSlotRepository timeSlotRepository;
    private final BoatService boatService;
    private final TimeSlotMapper timeSlotMapper;
    private final BookingMapper bookingMapper;
    private final BoatMapper boatMapper;
    private final ActivityTypeMapper activityTypeMapper;
    private final ActivityTypeService activityTypeService;

    /**
     * Create a new time slot
     *
     * @param timeSlotDto to create a new time slot
     * @return TimeSlotDto created from the given CreateTimeSlotDto
     */
    public TimeSlotDto createTimeSlot(CreateTimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDto);
        ActivityType activityType = activityTypeService.getActivityType(timeSlotDto.getActivityTypeId());

        //Time slot must be in boats time
        Boat boat = boatService.getBoat(timeSlotDto.getBoatId());

        if (boat.getAvailableFrom().isAfter(timeSlot.getFromTime()) || boat.getAvailableUntil()
                .isBefore(timeSlot.getUntilTime())) {
            throw new IllegalArgumentException("Time slot must be in boats available time");
        }
        timeSlot.setActivityType(activityType);
        timeSlot.setBoat(boat);
        timeSlot.setBookings(new HashSet<>());
        timeSlot = timeSlotRepository.save(timeSlot);

        return timeSlotMapper.toDto(timeSlot);
    }

    /**
     * Update a time slot
     *
     * @param id of the time slot to update
     * @param createTimeSlotDto to update the time slot
     * @return TimeSlotDto updated from the given CreateTimeSlotDto
     */
    public TimeSlotDto updateTimeSlot(long id, CreateTimeSlotDto createTimeSlotDto) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException(TIMESLOT_NOT_FOUND);
        }

        Boat boat = boatService.getBoat(createTimeSlotDto.getBoatId());
        ActivityType activityType = activityTypeService.getActivityType(createTimeSlotDto.getActivityTypeId());
        TimeSlot timeSlot = getTimeSlot(id);

        timeSlotMapper.update(createTimeSlotDto, timeSlot);
        timeSlot.setActivityType(activityType);
        timeSlot.setBoat(boat);
        timeSlotRepository.save(timeSlot);
        return timeSlotMapper.toDto(timeSlot);
    }

    /**
     * Delete a time slot
     *
     * @param id of the time slot to delete
     */
    public void deleteTimeSlot(Long id) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException(TIMESLOT_NOT_FOUND);
        }

        timeSlotRepository.deleteById(id);
    }

    /**
     * Get a time slot by id
     *
     * @param id of the time slot
     * @return TimeSlot with the given id
     */
    public TimeSlot getTimeSlot(Long id) {
        return timeSlotRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TIMESLOT_NOT_FOUND));
    }

    /**
     * Get a time slot by id
     *
     * @param id of the time slot
     * @return TimeSlotDto with the given id
     */
    public TimeSlotDto getTimeSlotDto(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TIMESLOT_NOT_FOUND));

        return timeSlotMapper.toDto(timeSlot);

    }

    /**
     * Get all time slots
     *
     * @return List of all time slots
     */
    public List<TimeSlotDto> getAllTimeSlots() {
        return timeSlotRepository.findAll().stream().map(timeSlotMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get all time slots with details
     *
     * @param id of the time slot
     * @param expand optional parameter to expand the details
     * @return List of all time slots with details
     */
    public TimeSlotDto getTimeSlotDto(Long id, Optional<String> expand) {
        TimeSlot timeSlot = getTimeSlot(id);
        TimeSlotDto timeSlotDto = getTimeSlotDto(id);

        expander.applyExpansion(expand, "activityType", () -> {
            ActivityTypeDto activityTypeDto = activityTypeMapper.toDto(timeSlot.getActivityType());
            timeSlotDto.setActivityType(activityTypeDto);
        });

        expander.applyExpansion(expand, "bookings", () -> {
            Set<BookingDto> bookingDtos = timeSlot.getBookings().stream().map(bookingMapper::toDto).collect(
                Collectors.toSet());
            timeSlotDto.setBookings(bookingDtos);
        });

        expander.applyExpansion(expand, "boat", () -> {
            BoatDto boatDto = boatMapper.toDto(timeSlot.getBoat());
            timeSlotDto.setBoat(boatDto);
        });

        return timeSlotDto;
    }

    /**
     * Get all time slots
     *
     * @param expand optional parameter to expand the details
     * @param eventId of the event
     * @return List of all time slots with details
     */
    public List<TimeSlotDto> getAllTimeSlots(Optional<String> expand, Optional<Long> eventId) {
        return timeSlotRepository.findAll()
                .stream()
                .filter(timeSlot -> eventId.map(aLong -> timeSlot.getBoat().getEvent().getId().equals(aLong))
                        .orElse(true))
                .map(timeSlot -> getTimeSlotDto(timeSlot.getId(), expand))
                .toList();
    }
}
