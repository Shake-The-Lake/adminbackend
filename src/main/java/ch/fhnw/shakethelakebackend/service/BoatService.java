package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BoatService {

    private final BoatRepository boatRepository;
    private final PersonService personService;

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow();
    }

    public Boat createBoat(Boat boat) {
        personService.createPerson(boat.getBoatDriver());
        return boatRepository.save(boat);
    }

    //TODO review update boat. Currently any person can be updated in the body of the boat.
    //This might be unwanted behavior.
    public Boat updateBoat(Long id, Boat boat) {
        if (!boatRepository.existsById(id)) {
            throw new EntityNotFoundException("Boat not found");
        }
        Person updatedBoatDriver = personService.updatePerson(boat.getBoatDriver());
        boat.setId(id);
        boat.setBoatDriver(updatedBoatDriver);
        return boatRepository.save(boat);

    }
}
