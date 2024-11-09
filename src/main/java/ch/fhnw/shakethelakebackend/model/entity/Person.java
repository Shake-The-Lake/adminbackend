package ch.fhnw.shakethelakebackend.model.entity;

import ch.fhnw.shakethelakebackend.model.entity.enums.PersonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.Set;

/**
 *
 * Entity for persons
 *
 */
@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Where(clause = "deleted=false")
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

    @Email
    private String emailAddress;

    @NotNull
    private String phoneNumber;

    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "person")
    private Set<Booking> bookings;

    public Set<Booking> getBookings() {
        return bookings == null ? Set.of() : Set.copyOf(bookings);
    }

    @PrePersist
    private void setDefaults() {
        if (this.createdBy == null) {
            this.createdBy = "TempUser";
        }
        if (this.updatedBy == null) {
            this.updatedBy = "TempUser";
        }
    }
}
