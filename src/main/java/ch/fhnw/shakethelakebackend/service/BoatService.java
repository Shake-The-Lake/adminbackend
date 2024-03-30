package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class BoatService {

    public BoatService(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }
    private final BoatRepository boatRepository;

    public Boat getBoat(Long id) {
        return new Boat();
    }

    public Boat createBoat(Boat boat) {
        return boatRepository.save(boat);
    }
}
