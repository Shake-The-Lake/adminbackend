package ch.fhnw.shakethelakebackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
import java.util.Objects;

/**
 *
 * Entity for bookings
 *
 */
@Entity
@Table(name = "booking")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "deleted=false")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Boolean isRider;

    @NotNull
    private Boolean isManual;

    private Integer pagerNumber;

    @JsonBackReference(value = "person-bookings")
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @JsonBackReference(value = "timeSlot-bookings")
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "timeSlot_id", nullable = false)
    private TimeSlot timeSlot;

    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    private boolean deleted = Boolean.FALSE;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(isRider, booking.isRider) && Objects.equals(isManual,
            booking.isManual) && Objects.equals(pagerNumber, booking.pagerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isRider, isManual, pagerNumber);
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
