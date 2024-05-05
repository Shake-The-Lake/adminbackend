package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final BoatService boatService;

    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        if (timeSlotRepository.existsById(timeSlot.getId())) {
            throw new IllegalArgumentException("TimeSlot already exists");
        }
        //Time slot must be in boats time
        Boat boat = boatService.getBoat(timeSlot.getBoat().getId());
        if (boat.getAvailableFrom().isAfter(timeSlot.getFromTime()) || boat.getAvailableUntil()
            .isBefore(timeSlot.getUntilTime())) {
            throw new IllegalArgumentException("Time slot must be in boats available time");
        }

        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot updateTimeSlot(long id, TimeSlot timeSlot) {
        if (!timeSlotRepository.existsById(id)) {
            throw new EntityNotFoundException("TimeSlot not found");
        }

        timeSlot.setId(id);
        return timeSlotRepository.save(timeSlot);
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
}
