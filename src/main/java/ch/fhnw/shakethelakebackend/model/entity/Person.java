package ch.fhnw.shakethelakebackend.model.entity;

import ch.fhnw.shakethelakebackend.model.entity.enums.PersonType;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private PersonType personType;

    @OneToMany(mappedBy = "boatDriver", cascade = CascadeType.ALL)
    private List<Boat> boats;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String emailAddress;

    @NotNull
    private String phoneNumber;

    @OneToMany(mappedBy = "person")
    private Set<Booking> bookings;

    public Set<Booking> getBookings() {
        return bookings == null ? Set.of() : Set.copyOf(bookings);
    }

    public List<Boat> getBoats() {
        return boats == null ? List.of() : List.copyOf(boats);
    }

}
