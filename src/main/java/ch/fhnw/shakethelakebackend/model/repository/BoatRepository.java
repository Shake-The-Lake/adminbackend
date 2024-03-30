package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {
}
