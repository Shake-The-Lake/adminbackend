package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}

