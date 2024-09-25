package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateTimeSlotDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
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

    public TimeSlotDto updateTimeSlot(long id, CreateTimeSlotDto timeSlotDto) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException(TIMESLOT_NOT_FOUND);
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
            throw new EntityNotFoundException(TIMESLOT_NOT_FOUND);
        }

        timeSlotRepository.deleteById(id);
    }

    public TimeSlot getTimeSlot(Long id) {
        return timeSlotRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TIMESLOT_NOT_FOUND));
    }

    public TimeSlotDto getTimeSlotDto(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TIMESLOT_NOT_FOUND));

        return timeSlotMapper.toDto(timeSlot);

    }

    public List<TimeSlotDto> getAllTimeSlots() {
        return timeSlotRepository.findAll().stream().map(timeSlotMapper::toDto).collect(Collectors.toList());
    }

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

    public List<TimeSlotDto> getAllTimeSlots(Optional<String> expand, Optional<Long> eventId) {
        return timeSlotRepository.findAll()
                .stream()
                .filter(timeSlot -> eventId.map(aLong -> timeSlot.getBoat().getEvent().getId().equals(aLong))
                        .orElse(true))
                .map(timeSlot -> getTimeSlotDto(timeSlot.getId(), expand))
                .toList();
    }
}
