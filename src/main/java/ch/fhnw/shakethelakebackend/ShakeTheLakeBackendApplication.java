package ch.fhnw.shakethelakebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class ShakeTheLakeBackendApplication {

    protected ShakeTheLakeBackendApplication() {
        // This constructor is protected to prevent instantiation
    }

    public static void main(String[] args) {
        SpringApplication.run(ShakeTheLakeBackendApplication.class, args);
    }

}
