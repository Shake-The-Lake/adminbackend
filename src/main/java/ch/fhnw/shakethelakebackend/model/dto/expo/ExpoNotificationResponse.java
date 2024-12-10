package ch.fhnw.shakethelakebackend.model.dto.expo;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpoNotificationResponse {
    Map<String, Object> data;
    List<Map<String, String>> errors;
}
