package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private PersonType personType;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String emailAddress;

    @NotNull
    private String phoneNumber;
}
