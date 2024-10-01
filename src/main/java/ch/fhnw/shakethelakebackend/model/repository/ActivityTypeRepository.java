package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * Repository for activity types
 *
 */
@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
}
