package ch.fhnw.shakethelakebackend.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Profile("test")
@TestConfiguration
public class FirebaseConfigTest {

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {
        return mock(FirebaseApp.class);
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return mock(FirebaseAuth.class);
    }

    @Bean
    public Firestore firestore() {
        return mock(Firestore.class);
    }
}
