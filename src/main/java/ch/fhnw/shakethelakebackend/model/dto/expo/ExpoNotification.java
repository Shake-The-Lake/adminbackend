package ch.fhnw.shakethelakebackend.model.dto.expo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpoNotification {
    @JsonProperty
    private String to;
    @JsonProperty
    private String body;
    @JsonProperty
    private String title;

    public ExpoNotification(ExpoNotification notification) {
        this.to = notification.getTo();
        this.body = notification.getBody();
        this.title = notification.getTitle();
    }
}
