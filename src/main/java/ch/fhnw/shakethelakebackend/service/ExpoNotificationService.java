package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotificationResponse;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotSubscriptionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ExpoNotificationService {

    private final RestClient restClient = RestClient.create();
    private final TaskScheduler taskScheduler;
    private final TimeSlotSubscriptionRepository timeSlotSubscriptionRepository;


    public ExpoNotificationService(TaskScheduler taskScheduler,TimeSlotSubscriptionRepository timeSlotSubscriptionRepository) {
        this.taskScheduler = taskScheduler;
        this.timeSlotSubscriptionRepository = timeSlotSubscriptionRepository;
    }

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
            sendNotification(notifications);
            onCompletion.run();
        }, instant);
    }

    public void sendTestNotificationToAllDevices(ExpoNotification notification) {
        List<ExpoNotification> notifications = timeSlotSubscriptionRepository.findAll().stream()
                .map(userId -> {
                    ExpoNotification notificationCopy = new ExpoNotification(notification);
                    notificationCopy.setTo(userId.getExpoToken());
                    return notificationCopy;
                }).collect(Collectors.toList());
        sendNotification(notifications);
    }

    public void subscribeToTimeSlot(Authentication authentication, Long timeSlotId, String expoToken) {
        String uid = ((FirebaseToken) authentication.getCredentials()).getUid();
        log.info("Subscribing user {} to time slot {}", uid, timeSlotId);
        timeSlotSubscriptionRepository.save(TimeSlotSubscription.builder()
                .firebaseUserId(uid)
                .timeSlotId(timeSlotId)
                .expoToken(expoToken)
                .build());
    }

    public void sendNotification(List<ExpoNotification> notifications) {
        log.info("Notification triggered for {}", notifications);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(notifications);
            log.info("Sending notification: {}", s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        var result = restClient
                .post()
                .uri("https://exp.host/--/api/v2/push/send")
                .body(notifications)
                .retrieve()
                .toEntity(String.class);
        log.info("Notification sent: {}", result);
    }
}
