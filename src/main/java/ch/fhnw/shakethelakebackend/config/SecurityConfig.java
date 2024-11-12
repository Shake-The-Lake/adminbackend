package ch.fhnw.shakethelakebackend.config;

import ch.fhnw.shakethelakebackend.filter.FirebaseAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Create security filter chain
     *
     * @param http          the http security
     * @param firebaseAuthFilter the jwt auth filter
     * @return the security filter chain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            FirebaseAuthFilter firebaseAuthFilter) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers("/public/**", "/auth/**",
                                        "/swagger-ui/**", "v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/user").hasRole(Roles.ANONYMOUS)
                                .requestMatchers(HttpMethod.GET, "/**")
                                .hasAnyRole(Roles.ADMIN, Roles.CUSTOMER, Roles.EMPLOYEE)
                                .requestMatchers(HttpMethod.POST, "/booking", "/person")
                                .hasAnyRole(Roles.ADMIN, Roles.CUSTOMER, Roles.EMPLOYEE)
                                .requestMatchers("/**").hasRole(Roles.ADMIN))
                .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable).build();
    }

    /**
     * Create authentication manager
     * @param config the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Create password encoder
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
