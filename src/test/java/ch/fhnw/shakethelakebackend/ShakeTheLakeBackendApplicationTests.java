package ch.fhnw.shakethelakebackend;

import ch.fhnw.shakethelakebackend.config.FirebaseConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(FirebaseConfigTest.class)
class ShakeTheLakeBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
