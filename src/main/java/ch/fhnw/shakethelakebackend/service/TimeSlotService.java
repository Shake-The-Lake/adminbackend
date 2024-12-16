package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.MoveTimeSlotDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Service for time slots
 */
@RequiredArgsConstructor
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
    private final FirebaseService firebaseService;

    private final Map<Long, ScheduledFuture<?>> scheduledNotifications = new ConcurrentHashMap<>();
    private final DateTimeFormatter textFormat = DateTimeFormatter.ofPattern("HH-mm");

    /**
     * Bootstrap the service
     */
    @EventListener(ApplicationReadyEvent.class)
    public void bootstrap() {
        timeSlotRepository.findAll().forEach(t -> createScheduledNotification(t, 15));
    }

    /**
     * Get the time slot time
     *
     * @param timeSlot to get the time slot time
     * @return LocalDateTime of the time slot
     */
    LocalDateTime getTimeSlotDateTime(TimeSlot timeSlot) {
        return timeSlot.getBoat().getEvent().getDate().atTime(timeSlot.getFromTime());
    }

    /**
     * Create a notification for a time slot
     *
     * @param timeSlot to create a notification for
     * @param minutes minutes before the start of the timeslot to create the notification
     */
    private void createScheduledNotification(TimeSlot timeSlot, int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timeSlotTime = getTimeSlotDateTime(timeSlot);
        LocalDateTime notificationTime = timeSlotTime.minusMinutes(minutes);

        if (notificationTime.isBefore(now)) {
            return;
        }

        Long id = timeSlot.getId();
        ScheduledFuture<?> future = firebaseService.createScheduledNotification(
            timeSlot.getTopic(),
            "You have a booking in " + minutes + " minutes",
            "Make yourself ready for the upcoming ride",
            notificationTime,
            () -> {
                scheduledNotifications.remove(id);
            }
        );
        scheduledNotifications.put(id, future);
    }

    void createNotificationNow(TimeSlot timeSlot, String title, String text) {
        Long id = timeSlot.getId();
        firebaseService.createScheduledNotification(
            timeSlot.getTopic(),
            title,
            text,
            null,
            () -> {
                scheduledNotifications.remove(id);
            }
        );
    }

    /**
     * Create a new time slot
     *
     * @param timeSlotDto to create a new time slot
     * @return TimeSlotDto created from the given CreateTimeSlotDto
     */
    public TimeSlotDto createTimeSlot(CreateTimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDto);
        ActivityType activityType = activityTypeService.getActivityType(timeSlotDto.getActivityTypeId());
        Boat boat = boatService.getBoat(timeSlotDto.getBoatId());

        validateTimeRange(boat, timeSlot, 0);

        timeSlot.setActivityType(activityType);
        timeSlot.setBoat(boat);
        timeSlot.setTopic(UUID.randomUUID().toString());
        timeSlot.setBookings(new HashSet<>());
        timeSlot = timeSlotRepository.save(timeSlot);
        createScheduledNotification(timeSlot, 15);
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
        TimeSlot timeSlot = getTimeSlot(id);
        Boat boat = boatService.getBoat(createTimeSlotDto.getBoatId());
        ActivityType activityType = activityTypeService.getActivityType(createTimeSlotDto.getActivityTypeId());

        if (!(timeSlot.getFromTime().equals(createTimeSlotDto.getFromTime()))) {
            timeSlot.setOriginalFromTime(timeSlot.getFromTime());
        }
        if (!(timeSlot.getUntilTime().equals(createTimeSlotDto.getUntilTime()))) {
            timeSlot.setOriginalUntilTime(timeSlot.getUntilTime());
        }

        timeSlotMapper.update(createTimeSlotDto, timeSlot);
        timeSlot.setActivityType(activityType);
        timeSlot.setBoat(boat);

        validateTimeRange(boat, timeSlot, id);

        timeSlotRepository.save(timeSlot);

        removeNotification(id);
        createScheduledNotification(timeSlot, 15);

        return timeSlotMapper.toDto(timeSlot);
    }

    void removeNotification(long id) {
        if (scheduledNotifications.containsKey(id)) {
            scheduledNotifications.get(id).cancel(true);
            scheduledNotifications.remove(id);
        }
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
    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
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


    /**
     * Validate the time range of a timeslot
     * @param boat
     * @param timeSlot
     * @param id of the time slot to update. pass 0 for create
     */
    void validateTimeRange(Boat boat, TimeSlot timeSlot, long id) {
        Set<TimeSlot> boatTimeSlots = boat.getTimeSlots().stream().filter(t -> t.getId() != id).collect(
            Collectors.toSet());
        if (boatTimeSlots.stream().anyMatch(timeSlot::overlaps)) {
            throw new IllegalArgumentException("Time slot overlaps with existing time slot");
        }

        if (timeSlot.getFromTime().isAfter(timeSlot.getUntilTime())) {
            throw new IllegalArgumentException("From time must be before until time");
        }

        if (timeSlot.getUntilTime().isBefore(timeSlot.getFromTime())) {
            throw new IllegalArgumentException("Until time must be after from time");
        }

        //Time slot must be in boats time
        if (boat.getAvailableFrom().isAfter(timeSlot.getFromTime())
            || boat.getAvailableUntil().isBefore(timeSlot.getUntilTime())) {
            throw new IllegalArgumentException("Time slot must be in boats available time");
        }
    }

    /**
     * Move a time slot by id including the timeslot that follow
     *
     * @param id the id of the time slot
     * @param timeSlot the moveTimeSlotDto
     * @return the moved time slot
     */
    public List<TimeSlotDto> moveTimeSlot(Long id, @Valid MoveTimeSlotDto timeSlot) {
        TimeSlot timeSlotToMove = getTimeSlot(id);
        Long oldDuration = Duration.between(timeSlotToMove.getFromTime(), timeSlotToMove.getUntilTime()).toMinutes();
        Long newDuration = Duration.between(timeSlot.getFromTime(), timeSlot.getUntilTime()).toMinutes();
        if (timeSlot.getFromTime().isBefore(timeSlotToMove.getFromTime())
            || timeSlot.getUntilTime().isBefore(timeSlotToMove.getUntilTime())) {
            throw new IllegalArgumentException("Time slot must be moved to a later time");
        }
        if (timeSlot.getFromTime().isAfter(timeSlotToMove.getFromTime()) && !newDuration.equals(oldDuration)) {
            throw new IllegalArgumentException("Time slot cannot be moved to a later time with a different duration");
        }

        long addedMinutes = timeSlot.getFromTime().equals(timeSlotToMove.getFromTime())
            ? Duration.between(timeSlotToMove.getUntilTime(), timeSlot.getUntilTime()).toMinutes()
            : Duration.between(timeSlotToMove.getFromTime(), timeSlot.getFromTime()).toMinutes();

        List<TimeSlot> succeedingTimeSlots = getSucceedingTimeSlots(timeSlotToMove);

        moveTimeSlot(timeSlotToMove, timeSlot);
        moveTimeSlots(succeedingTimeSlots, addedMinutes);

        succeedingTimeSlots.add(timeSlotToMove); // add current timeslot for notification and validation

        Boat boat = timeSlotToMove.getBoat();
        succeedingTimeSlots.forEach(t -> validateTimeRange(boat, t, t.getId()));

        List<TimeSlot> timeSlots = timeSlotRepository.saveAll(succeedingTimeSlots);
        notifyMove(succeedingTimeSlots, "Your starting time has changed!");
        List<TimeSlotDto> timeSlotsDtos = timeSlots.stream().map(timeSlotMapper::toDto).toList();
        return timeSlotsDtos.stream().sorted(Comparator.comparing(TimeSlotDto::getFromTime)).toList();
    }

    private void notifyMove(List<TimeSlot> timeSlots, String title) {
        timeSlots.forEach(t -> {
            removeNotification(t.getId());
            String text = String.format("Your TimeSlot has been moved to the new Time: %s",
                    t.getFromTime().format(textFormat));
            createNotificationNow(t, title, text);
            createScheduledNotification(t, 15);
        });
    }

    private static List<TimeSlot> getSucceedingTimeSlots(TimeSlot timeSlotToMove) {
        Boat boat = timeSlotToMove.getBoat();
        return boat.getTimeSlots().stream()
            .filter(t -> t.getFromTime().isAfter(timeSlotToMove.getFromTime())).collect(Collectors.toList());
    }

    /**
     * Move the time slots
     *
     * @param succeedingTimeSlots the time slots to move
     * @param addedMinutes the minutes to add
     */
    void moveTimeSlots(List<TimeSlot> succeedingTimeSlots, long addedMinutes) {
        succeedingTimeSlots.forEach(t -> {
            t.setOriginalFromTime(t.getFromTime());
            t.setOriginalUntilTime(t.getUntilTime());
            t.setFromTime(t.getFromTime().plusMinutes(addedMinutes));
            t.setUntilTime(t.getUntilTime().plusMinutes(addedMinutes));
        });
    }

    void moveTimeSlot(TimeSlot timeSlotToMove, MoveTimeSlotDto timeSlot) {
        timeSlotToMove.setOriginalFromTime(timeSlotToMove.getFromTime());
        timeSlotToMove.setOriginalUntilTime(timeSlotToMove.getUntilTime());
        timeSlotToMove.setFromTime(timeSlot.getFromTime());
        timeSlotToMove.setUntilTime(timeSlot.getUntilTime());
    }
}
