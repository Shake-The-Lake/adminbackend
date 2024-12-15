package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotSubscriptionRepository;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class TimeSlotSubscriptionService {

    private final TaskScheduler taskScheduler;
    private final TimeSlotSubscriptionRepository timeSlotSubscriptionRepository;
    private final ExpoNotificationService expoNotificationService;

    public ScheduledFuture<?> createScheduledTimeSlotNotification(ExpoNotification notification,
                                                                  Long timeSlotId,
                                                                  LocalDateTime time,
                                                                  Runnable onCompletion) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = time.atZone(zone);
        Instant instant = zonedDateTime.toInstant();
        log.info("Scheduling notification for {}", instant);
        log.info("Time slot id: {}", timeSlotId);

        return taskScheduler.schedule(() -> {
            List<ExpoNotification> notifications = timeSlotSubscriptionRepository.findAllByTimeSlotId(timeSlotId).stream()
                    .map(subscription -> {
                        ExpoNotification notificationCopy = new ExpoNotification(notification);
                        notificationCopy.setTo(subscription.getExpoToken());
                        return notificationCopy;
                    }).collect(Collectors.toList());
            expoNotificationService.sendNotification(notifications);
            onCompletion.run();
        }, instant);
    }

    public void subscribeToTimeSlot(Authentication authentication, Long timeSlotId, String expoToken) {
        String uid = ((FirebaseToken) authentication.getCredentials()).getUid();
        log.info("Subscribing user {} to time slot {}", uid, timeSlotId);
        if (!timeSlotSubscriptionRepository.existsByTimeSlotIdAndFirebaseUserId(timeSlotId, uid)) {
            timeSlotSubscriptionRepository.save(TimeSlotSubscription.builder()
                    .firebaseUserId(uid)
                    .timeSlotId(timeSlotId)
                    .expoToken(expoToken)
                    .build());
        }
    }

    // TODO: Remove me when feature is implemented
    public void sendTestNotificationToAllDevices(ExpoNotification notification) {
        List<ExpoNotification> notifications = timeSlotSubscriptionRepository.findAll().stream()
                .map(userId -> {
                    ExpoNotification notificationCopy = new ExpoNotification(notification);
                    notificationCopy.setTo(userId.getExpoToken());
                    return notificationCopy;
                }).collect(Collectors.toList());
        expoNotificationService.sendNotification(notifications);
    }
}
