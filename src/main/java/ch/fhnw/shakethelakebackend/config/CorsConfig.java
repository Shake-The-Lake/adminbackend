package ch.fhnw.shakethelakebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for CORS
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Add CORS mappings
     *
     * @param registry the CORS registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // TODO Adjust for deployment
            .allowedOrigins("http://localhost:3000", "https://shake-the-lake.ch", "http://localhost:8080", "http://localhost:80")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS").allowedHeaders("*")
            .allowCredentials(true);
    }
}
