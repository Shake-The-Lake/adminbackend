package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.expo.ExpoNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ExpoNotificationService {
    private final RestClient restClient;

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
