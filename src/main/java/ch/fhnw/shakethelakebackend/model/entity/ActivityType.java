package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activityType")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String activityName;

    // String icon ?
    private String icon;
    private String description;
    private String checklist;

}
