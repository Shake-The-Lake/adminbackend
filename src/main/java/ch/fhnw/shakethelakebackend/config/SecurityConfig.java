package ch.fhnw.shakethelakebackend.config;

import ch.fhnw.shakethelakebackend.filter.JwtAuthFilter;
import ch.fhnw.shakethelakebackend.model.repository.UserRepository;
import ch.fhnw.shakethelakebackend.service.UserService;
import ch.fhnw.shakethelakebackend.model.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private static final String ADMIN = "ADMIN";
    private static final String CUSTOMER = "USER";
    private static final String EMPLOYEE = "EMPLOYEE";

    @Value("${ADMIN_NAME:admin}")
    private String adminName;
    @Value("${ADMIN_PW:admin}")
    private String adminPassword;


    @Value("${CUSTOMER_NAME:customer}")
    private String customerName;
    @Value("${CUSTOMER_PW:customer}")
    private String customerPassword;


    @Value("${EMPLOYEE_NAME:employee}")
    private String employeeName;
    @Value("${EMPLOYEE_PW:employee}")
    private String employeePassword;

    /**
     * Create user details service
     *
     * @param encoder the password encoder
     * @return the user details service
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {

        userService.saveUser(User.builder().username(adminName).password(adminPassword).role(ADMIN).build());
        userService.saveUser(User.builder().username(customerName).password(customerPassword).role(CUSTOMER).build());
        userService.saveUser(User.builder().username(employeeName).password(employeePassword).role(EMPLOYEE).build());

        return userService;
    }

    /**
     * Create security filter chain
     *
     * @param http the http security
     * @return the security filter chain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers("/public/**", "/auth/**", "/swagger-ui/**", "v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/**").hasAnyRole(ADMIN, CUSTOMER, EMPLOYEE)
                            .requestMatchers(HttpMethod.POST, "/booking").hasAnyRole(ADMIN, CUSTOMER, EMPLOYEE)
                            .requestMatchers("/**").hasRole(ADMIN))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider(userService))
                .cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).build();
    }

    /**
     * Create password encoder
     *
     * @return the password encoder
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
