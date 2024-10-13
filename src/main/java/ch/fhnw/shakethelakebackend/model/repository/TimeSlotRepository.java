package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Repository for time slots
 *
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}
