package ch.fhnw.shakethelakebackend.config.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "spring.application.shakethelake")
public class FirebaseConfigurationProperties {

    @Valid
    private FireBase firebase = new FireBase();


    @Getter
    @Setter
    public static class FireBase {
        @NotBlank(message = "Firebase private key must not be blank")
        private String privateKey;
    }
}
