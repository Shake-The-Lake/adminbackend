package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.User;
import ch.fhnw.shakethelakebackend.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test");
        user.setRole("test");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("test");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));
        userService.saveUser(user);
        User createdUser = userRepository.findByUsername(user.getUsername()).get();
        assertEquals(user, createdUser);
    }

    @Test
    void testGetUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));
        UserDetails foundUser = userService.loadUserByUsername(user.getUsername());
        assertEquals(user.getUsername(), foundUser.getUsername());
    }
}
