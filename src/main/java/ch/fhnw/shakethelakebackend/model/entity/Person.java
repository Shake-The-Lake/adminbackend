package ch.fhnw.shakethelakebackend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @Email(message = "Email should be valid")
    private String emailAddress;

    @NotNull
    private String phoneNumber;

    @JsonManagedReference(value = "person-bookings")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private Set<Booking> bookings;
}
