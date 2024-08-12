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

import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Schema(hidden = true)
@Table(name = "boat")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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

    @Column(name = "available_from", columnDefinition = "TIMESTAMP")
    private ZonedDateTime availableFrom;

    @Column(name = "available_until", columnDefinition = "TIMESTAMP")
    private ZonedDateTime availableUntil;

    @OneToMany(mappedBy = "boat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TimeSlot> timeSlots;

    @ManyToOne
    private Event event;

    public Set<TimeSlot> getTimeSlots() {
        return timeSlots == null ? Set.of() : Set.copyOf(timeSlots);
    }

}
