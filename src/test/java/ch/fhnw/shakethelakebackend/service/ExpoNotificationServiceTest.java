package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlotSubscription;
import ch.fhnw.shakethelakebackend.model.repository.TimeSlotSubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExpoNotificationServiceTest {
    @Mock
    private TimeSlotSubscriptionRepository timeSlotSubscriptionRepository;

    @Mock
    private ExpoNotificationService expoNotificationService;

    private final long timeSlotId = 1L;

    void before() {
        when(timeSlotSubscriptionRepository.findAllByTimeSlotId(timeSlotId)).thenReturn(
                Stream.of(
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(timeSlotId)
                                .firebaseUserId("<user1>")
                                .expoToken("ExpoToken[1]")
                                .build(),
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(2L)
                                .firebaseUserId("<user3>")
                                .expoToken("ExpoToken[3]")
                                .build(),
                        TimeSlotSubscription.builder()
                                .id(UUID.randomUUID())
                                .timeSlotId(timeSlotId)
                                .firebaseUserId("<user2>")
                                .expoToken("ExpoToken[2]")
                                .build()
                ).toList()
        );
    }

    @Test
    void givenTimeSlotAndSchedulerWhenSchedulerTriggersThenNotificationsAreSent() throws InterruptedException {

        // GIVEN subscription with different time slot ids
        before();
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
