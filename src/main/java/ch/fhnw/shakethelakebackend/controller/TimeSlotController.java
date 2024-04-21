package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.service.TimeSlotService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/timeslot")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PostMapping
    public TimeSlot createTimeSlot(@RequestBody @Valid TimeSlot timeSlot) {
        return timeSlotService.createTimeSlot(timeSlot);
    }

    @GetMapping("/{id}")
    public TimeSlot getTimeSlot(@PathVariable Long id) {
        return timeSlotService.getTimeSlot(id);
    }

    @PutMapping("/{id}")
    public TimeSlot updateTimeSlot(@PathVariable Long id, @RequestBody @Valid TimeSlot timeSlot) {
        return timeSlotService.updateTimeSlot(id, timeSlot);
    }

    @DeleteMapping("/{id}")
    public void deleteTimeSlot(@PathVariable Long id) {
        timeSlotService.deleteTimeSlot(id);
    }

}
