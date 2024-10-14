package ch.fhnw.shakethelakebackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        jwtService.setSecret(
                "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");
    }
    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken("test");
        assertNotNull(token);
    }

    @Test
    void testValidateToken() {
        String token = jwtService.generateToken("test");
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtService.generateToken("test");
        String username = jwtService.extractUsername(token);
        assertEquals("test", username);
    }

}
