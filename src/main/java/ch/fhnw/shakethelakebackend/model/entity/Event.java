package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

/**
 *
 * Entity for events
 *
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Event extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "event_title")
    private String title;

    @NotNull
    @Column(name = "event_description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDate date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ActivityType> activityTypes;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Boat> boats;

    private String employeeSecret;
    @Column(columnDefinition = "TEXT")
    private String employeeBarcode;

    private String customerSecret;
    @Column(columnDefinition = "TEXT")
    private String customerBarcode;

    public Set<ActivityType> getActivityTypes() {
        return activityTypes == null ? Set.of() : activityTypes;
    }

    public Set<Boat> getBoats() {
        return boats == null ? Set.of() : boats;
    }

}
