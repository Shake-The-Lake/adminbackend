package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String street;

    private String streetNr;

    private int postalCode;

    private String town;

    private String canton;
}
