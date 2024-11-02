package ch.fhnw.shakethelakebackend.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

/**
 *
 * Entity for boats
 *
 */
@Entity
@Schema(hidden = true)
@Table(name = "boat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Where(clause = "deleted=false")
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String operator;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    @Column(name = "seats_rider")
    private int seatsRider;

    @NotNull
    @Column(name = "seats_viewer")
    private int seatsViewer;

    @NotNull
    @Column(name = "slot_duration_in_mins")
    private int slotDurationInMins;

    @Column(name = "available_from", columnDefinition = "TIME")
    private LocalTime availableFrom;

    @Column(name = "available_until", columnDefinition = "TIME")
    private LocalTime availableUntil;

    @OneToMany(mappedBy = "boat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TimeSlot> timeSlots;

    private String createdBy = "TempUser";
    private String updatedBy = "TempUser";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    private boolean deleted = Boolean.FALSE;
    @ManyToOne
    private Event event;

    public Set<TimeSlot> getTimeSlots() {
        return timeSlots == null ? Set.of() : Set.copyOf(timeSlots);
    }

}
