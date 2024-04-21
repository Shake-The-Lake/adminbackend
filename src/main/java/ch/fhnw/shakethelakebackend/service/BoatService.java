package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.PersonType;
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
        return boatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Boat not found"));
    }


    public Boat createBoat(Boat boat) {
        if (boat.getId() != null && boatRepository.existsById(boat.getId())) {
            throw new IllegalArgumentException("Boat already exists");
        }
        Person driver = personService.getPerson(boat.getBoatDriver().getId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException("Person is not a boat driver");
        }
        boat.setBoatDriver(driver);
        return boatRepository.save(boat);
    }

    public Boat updateBoat(Long id, Boat boat) {
        if (!boatRepository.existsById(id)) {
            throw new EntityNotFoundException("Boat not found");
        }
        Person driver = personService.getPerson(boat.getBoatDriver().getId());
        if (driver.getPersonType() != PersonType.BOAT_DRIVER) {
            throw new IllegalArgumentException("Person is not a boat driver");
        }
        boat.setBoatDriver(driver);
        boat.setId(id);
        return boatRepository.save(boat);

    }

    public void deleteBoat(Long id) {
        Boat boat = boatRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Boat not found"));
        boatRepository.delete(boat);
    }
}
