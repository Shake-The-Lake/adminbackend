package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.repository.BoatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BoatService {

    private final BoatRepository boatRepository;

    public Boat getBoat(Long id) {
        return boatRepository.findById(id).orElseThrow();
    }

    public Boat createBoat(Boat boat) {
        return boatRepository.save(boat);
    }

    public Boat updateBoat(Long id, Boat boat) {
        
    }
}
