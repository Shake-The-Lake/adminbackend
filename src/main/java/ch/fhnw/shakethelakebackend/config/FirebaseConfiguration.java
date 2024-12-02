package ch.fhnw.shakethelakebackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
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

    /**
     * Create a Firebase app
     *
     * @return the Firebase app
     */
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

    /**
     * Create a Firestore client
     *
     * @param firebaseApp the Firebase app
     * @return the Firestore client
     */
    @Bean
    public Firestore firestore(final FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    /**
     * Create a Firebase authentication
     *
     * @param firebaseApp the Firebase app
     * @return the Firebase authentication
     */
    @Bean
    public FirebaseAuth firebaseAuth(final FirebaseApp firebaseApp) {
        return FirebaseAuth.getInstance(firebaseApp);
    }


    @Bean
    public FirebaseMessaging firebaseMessaging(final FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}
