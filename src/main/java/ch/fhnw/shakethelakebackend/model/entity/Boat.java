package ch.fhnw.shakethelakebackend.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "boat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String type;

    private int seatsRider;

    private int seatsViewer;

    private int slotDurationInMins;

    private Time availableFrom;

    private Time availableUntil;

    
}
