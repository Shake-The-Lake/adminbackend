package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpoNotificationServiceTest {
    @Mock
    private TimeSlotSubscriptionRepository timeSlotSubscriptionRepository;

    @Mock
    private ExpoNotificationService expoNotificationService;

    @Test
    void givenTimeSlotAndScheduler_whenSchedulerTriggers_thenNotificationsAreSent() throws InterruptedException {

        // GIVEN subscription with different time slot ids
        var timeSlotId = 1L;
        var expoToken1 = "ExpoToken[1]";
        var expoToken2 = "ExpoToken[2]";
        var expoToken3 = "ExpoToken[3]";
        when(timeSlotSubscriptionRepository.findAllByTimeSlotId(timeSlotId)).thenReturn(
                Stream.of(
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(timeSlotId)
                                .firebaseUserId("<user1>")
                                .expoToken(expoToken1)
                                .build(),
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(2L)
                                .firebaseUserId("<user3>")
                                .expoToken(expoToken3)
                                .build(),
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(timeSlotId)
                                .firebaseUserId("<user2>")
                                .expoToken(expoToken2)
                                .build()
                ).toList()
        );
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        var notificationService = new TimeSlotSubscriptionService(
                scheduler,
                timeSlotSubscriptionRepository,
                expoNotificationService
        );

        // WHEN scheduling a notification for a specific time slot
        var futureTime = LocalDateTime.now().plusSeconds(3);
        notificationService.createScheduledTimeSlotNotification(
                ExpoNotification
                        .builder()
                        .title("MyTitle")
                        .body("MyBody")
                        .build(),
                timeSlotId,
                futureTime,
                () -> System.out.println("Notification sent")
        );
        Thread.sleep(4000);
        verify(expoNotificationService, times(1)).sendNotification(any());
    }
}
