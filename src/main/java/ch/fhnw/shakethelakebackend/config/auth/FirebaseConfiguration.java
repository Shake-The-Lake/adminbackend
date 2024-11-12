package ch.fhnw.shakethelakebackend.config.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Profile("!test")
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(FirebaseConfigurationProperties.class)
public class FirebaseConfiguration {

    private final FirebaseConfigurationProperties firebaseConfigurationProperties;

    @Bean
    @SneakyThrows
    public FirebaseApp firebaseApp() {
        final String privateKey = firebaseConfigurationProperties.getFirebase().getPrivateKey();
        final var serviceAccount = new ByteArrayInputStream(Base64.getDecoder().decode(privateKey));
        final var firebaseOptions = new com.google.firebase.FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(firebaseOptions);
    }

    @Bean
    public Firestore firestore(final FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    public FirebaseAuth firebaseAuth(final FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }

}
