package ch.fhnw.shakethelakebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails userAdmin = User.withUsername(adminName)
                .password(encoder.encode(adminPassword)).roles(ADMIN).build();

        UserDetails userCustomer = User.withUsername(customerName)
                .password(encoder.encode(customerName)).roles(CUSTOMER).build();

        UserDetails userEmployee = User.withUsername(employeeName)
                .password(encoder.encode(employeePassword)).roles(EMPLOYEE).build();

        return new InMemoryUserDetailsManager(userAdmin, userCustomer, userEmployee);
    }

    /**
     * Create security filter chain
     *
     * @param http the http security
     * @return the security filter chain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(HttpMethod.GET, "/**").hasAnyRole(ADMIN, CUSTOMER, EMPLOYEE)
                            .requestMatchers(HttpMethod.POST, "/booking").hasAnyRole(ADMIN, CUSTOMER, EMPLOYEE)
                            .requestMatchers("/**").hasRole(ADMIN)
                            .requestMatchers("/public/**", "/auth/**").permitAll())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).build();
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
