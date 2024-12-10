package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
