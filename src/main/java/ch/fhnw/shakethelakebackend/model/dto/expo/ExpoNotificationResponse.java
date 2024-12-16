package ch.fhnw.shakethelakebackend.model.dto.expo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpoNotificationResponse {
    private Map<String, Object> data;
    private List<Map<String, String>> errors;
}
