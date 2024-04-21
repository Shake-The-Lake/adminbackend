package ch.fhnw.shakethelakebackend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "boat")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Person boatDriver;

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
    private LocalDateTime availableFrom;

    @Column(name = "available_until", columnDefinition = "TIMESTAMP")
    private LocalDateTime availableUntil;

    // Must ignore boat in TimeSlot to avoid infinite recursion
    @JsonManagedReference(value = "boat-timeSlots")
    @OneToMany(mappedBy = "boat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TimeSlot> timeSlots;

}
