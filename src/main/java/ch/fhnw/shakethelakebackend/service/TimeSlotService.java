package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.chrono.ChronoLocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TimeSlotService {

    public static final String TIMESLOT_NOT_FOUND = "TimeSlot not found";

    private final TimeSlotRepository timeSlotRepository;
    private final BoatService boatService;
    private final TimeSlotMapper timeSlotMapper;
    private final ActivityTypeService activityTypeService;

    public TimeSlotDto createTimeSlot(CreateTimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDto);
        ActivityType activityType = activityTypeService.getActivityType(timeSlotDto.getActivityTypeId());

        //Time slot must be in boats time
        Boat boat = boatService.getBoat(timeSlotDto.getBoatId());

        if (
            boat.getAvailableFrom().isAfter(ChronoLocalDateTime.from(timeSlot.getFromTime()))
                || boat.getAvailableUntil().isBefore(ChronoLocalDateTime.from(timeSlot.getUntilTime()))) {
            throw new IllegalArgumentException("Time slot must be in boats available time");
        }
        timeSlot.setActivityType(activityType);
        timeSlot.setBoat(boat);
        timeSlot.setBookings(new HashSet<>());
        timeSlot = timeSlotRepository.save(timeSlot);

        return timeSlotMapper.toDto(timeSlot);
    }

    public TimeSlotDto updateTimeSlot(long id, CreateTimeSlotDto timeSlotDto) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException("TimeSlot not found");
        }

        Set<Booking> bookings = getTimeSlot(id).getBookings();
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDto);
        Boat boat = boatService.getBoat(timeSlotDto.getBoatId());
        ActivityType activityType = activityTypeService.getActivityType(timeSlotDto.getActivityTypeId());

        timeSlot.setActivityType(activityType);
        timeSlot.setBookings(bookings);
        timeSlot.setBoat(boat);
        timeSlot.setId(id);
        timeSlotRepository.save(timeSlot);
        return timeSlotMapper.toDto(timeSlot);
    }

    public void deleteTimeSlot(Long id) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException("TimeSlot not found");
        }

        timeSlotRepository.deleteById(id);
    }

    public TimeSlot getTimeSlot(Long id) {
        return timeSlotRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TimeSlot not found"));
    }

    public TimeSlotDto getTimeSlotDto(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TimeSlot not found"));
        return timeSlotMapper.toDto(timeSlot);
    }

    public List<TimeSlotDto> getAllTimeSlots() {
        return timeSlotRepository.findAll().stream().map(timeSlotMapper::toDto).collect(Collectors.toList());
    }
}
