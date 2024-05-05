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

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "event")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Event {

    // it is not yet on the class diagramm but i would recommend giving an id on a event as well
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "event_title")
    private String title;

    @NotNull
    @Column(name = "event_description")
    private String description;

    /* TODO: not in mvp
    @Null
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;*/

    @NotNull
    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    @NotNull
    @Column(name = "customer_code")
    private String customerCode;

    @NotNull
    @Column(name = "employee_code")
    private String employeeCode;

    @NotNull
    @Column(name = "customer_only_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime customerOnlyTime;

    @NotNull
    @Column(name = "is_started")
    private boolean isStarted;

    @NotNull
    @Column(name = "started_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime startedAt;

    @NotNull
    @Column(name = "ended_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime endedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<ActivityType> activityTypes;

    public Set<ActivityType> getActivityTypes() {
        return activityTypes == null ? Set.of() : activityTypes;
    }
}
