package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notification_subscription")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeSlotSubscription extends BaseEntityAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private long timeSlotId;
    private String firebaseUserId;
    private String expoToken;
}
