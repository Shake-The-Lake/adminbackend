package ch.fhnw.shakethelakebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This class is the entry point of the application.
 */
@SpringBootApplication()
@EnableScheduling
public class ShakeTheLakeBackendApplication {

    /**
     * This constructor is protected to prevent instantiation.
     */
    protected ShakeTheLakeBackendApplication() { }

    /**
     * This method is the entry point of the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(ShakeTheLakeBackendApplication.class, args);
    }

}
