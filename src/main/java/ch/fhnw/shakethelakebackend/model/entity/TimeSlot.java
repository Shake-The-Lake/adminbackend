package ch.fhnw.shakethelakebackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Setter
@Getter
@Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "from_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime fromTime;

    @NotNull
    @Column(name = "until_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime untilTime;

    // Must ignore timeSlots in Boat to avoid infinite recursion
    @JsonBackReference(value = "boat-timeSlots")
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "boat_id", nullable = false)
    private Boat boat;

    @JsonManagedReference(value = "timeSlot-bookings")
    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Booking> bookings;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(id, timeSlot.id) && Objects.equals(fromTime, timeSlot.fromTime) && Objects.equals(
                untilTime, timeSlot.untilTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromTime, untilTime);
    }
}
