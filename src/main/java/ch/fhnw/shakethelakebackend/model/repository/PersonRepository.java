package ch.fhnw.shakethelakebackend.model.repository;

import ch.fhnw.shakethelakebackend.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * Repository for persons
 *
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
