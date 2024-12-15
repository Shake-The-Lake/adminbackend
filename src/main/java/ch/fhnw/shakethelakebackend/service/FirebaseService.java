package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.config.Roles;
import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@AllArgsConstructor
@Log4j2
public class FirebaseService {

    private final FirebaseAuth firebaseAuth;
    private final Firestore firestore;
    private final EventService eventService;
    private final FirebaseMessaging firebaseMessaging;
    private final TaskScheduler taskScheduler;
    private final TimeSlotSubscriptionService timeSlotSubscriptionService;

    /**
     * Authenticate a user with a Firebase token
     *
     * @param token the Firebase token
     * @return the authentication user details
     * @throws FirebaseAuthException if an error occurs
     */
    public UsernamePasswordAuthenticationToken authenticate(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
        String uid = firebaseToken.getUid();
        String role = Roles.ROLE_PREFIX + Roles.ANONYMOUS;
        String name = "anonymous";
        try {
            Map<String, Object> user = firestore.collection("users").document(uid).get().get().getData();
            if (Objects.nonNull(user)) {
                role = (String) user.get("role");
                name = user.get("firstName") + " " + user.get("lastName");
            }
        } catch (Exception ignored) {

        }
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(name, firebaseToken, authorities);
    }

    /**
     * Create a user
     *
     * @param auth    the authentication
     * @param user    the user data
     * @param eventId the event id
     * @param secret  the secret
     */
    public void createUser(Authentication auth, CreateUserDto user, Optional<Long> eventId, Optional<String> secret) {
        if (eventId.isPresent() != secret.isPresent()) {
            throw new IllegalArgumentException("Event id and secret must be both present or both absent");
        }

        String uid = ((FirebaseToken) auth.getCredentials()).getUid();
        if (eventId.isEmpty()) {
            createUser(uid, user, Roles.ROLE_PREFIX + Roles.CUSTOMER);
            return;
        }

        Event event = eventService.getEvent(eventId.get());
        String role;
        if (event.getCustomerSecret().equals(secret.get())) {
            role = Roles.ROLE_PREFIX + Roles.CUSTOMER;
        } else if (event.getEmployeeSecret().equals(secret.get())) {
            role = Roles.ROLE_PREFIX + Roles.EMPLOYEE;
        } else {
            throw new IllegalArgumentException("Invalid request");
        }

        createUser(uid, user, role);
    }

    /**
     * Create a user
     *
     * @param uid  the user id
     * @param user the user data
     * @param role the user role
     */
    public void createUser(String uid, CreateUserDto user, String role) {
        Map<String, Object> userMap = user.getAsMap();
        userMap.put("role", role);
        firestore.collection("users").document(uid).set(userMap);
    }

    /**
     * Send a notification to a topic
     *
     * @param topic        the topic
     * @param title        the title
     * @param text         the text
     * @param time         the time
     * @param onCompletion the on completion
     * @return the scheduled future
     */
    public ScheduledFuture<?> createScheduledNotification(String topic,
                                                          String title,
                                                          String text,
                                                          LocalDateTime time,
                                                          Runnable onCompletion) {
        Notification notification = Notification.builder().setTitle(title).setBody(text).build();
        Message message = Message.builder().setNotification(notification).setTopic(topic).build();

        ZoneId zone = ZoneId.systemDefault(); // or use ZoneOffset.UTC for UTC
        ZonedDateTime zonedDateTime = time.atZone(zone);
        Instant instant = zonedDateTime.toInstant();
        log.info("Scheduling notification for {}", instant);
        log.info("Topic: {}", topic);

        return taskScheduler.schedule(() -> {
            sendNotification(message, onCompletion);
        }, instant);
    }

    /**
     * Send a notification
     *
     * @param message the message
     */
    private void sendNotification(Message message, Runnable onCompletion) {
        log.info("Sending notification");
        try {
            String res = firebaseMessaging.send(message);
            log.info("Notification sent {}", res);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send notification", e);
        }
        onCompletion.run();
    }

}
