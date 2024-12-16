package ch.fhnw.shakethelakebackend.model.dto.expo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
