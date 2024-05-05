package ch.fhnw.shakethelakebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // TODO Adjust for deployment
            .allowedOrigins("http://localhost:3000", "https://shake-the-lake.ch")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS").allowedHeaders("*")
            .allowCredentials(true);
    }
}
