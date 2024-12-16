package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.TimeSlotSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeSlotSubscriptionRepository extends JpaRepository<TimeSlotSubscription, UUID> {
    List<TimeSlotSubscription> findAllByTimeSlotId(long timeSlotId);
    boolean existsByTimeSlotIdAndFirebaseUserId(long timeSlotId, String firebaseUserId);
}
