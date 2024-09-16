package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "event_title")
    private String title;

    @NotNull
    @Column(name = "event_description")
    private String description;

    @NotNull
    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDate date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ActivityType> activityTypes;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Boat> boats;

    private String employeeCode;
    @Column(columnDefinition = "TEXT")
    private String employeeBarcode;

    private String customerCode;
    @Column(columnDefinition = "TEXT")
    private String customerBarcode;

    public Set<ActivityType> getActivityTypes() {
        return activityTypes == null ? Set.of() : activityTypes;
    }

    public Set<Boat> getBoats() {
        return boats == null ? Set.of() : boats;
    }
}
