package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long id;

    @NotNull
    @Column(name = "event_title")
    private String title;

    @NotNull
    @Column(name = "event_description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

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

}
