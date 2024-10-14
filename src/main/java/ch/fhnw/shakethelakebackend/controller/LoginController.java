package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.AuthRequestTokenDto;
import ch.fhnw.shakethelakebackend.model.dto.AuthRequestUserDto;
import ch.fhnw.shakethelakebackend.model.dto.AuthRequestVerifyDto;
import ch.fhnw.shakethelakebackend.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class LoginController {


    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/verify")
    public AuthRequestVerifyDto verify(@RequestBody AuthRequestTokenDto authRequestDto) {
        return AuthRequestVerifyDto.builder().valid(jwtService.validateToken(authRequestDto.getToken())).build();
    }

    @PostMapping("/login")
    public AuthRequestTokenDto login(@RequestBody AuthRequestUserDto authRequestDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
        
        if (auth.isAuthenticated()) {
            return AuthRequestTokenDto.builder().token(jwtService.generateToken(auth.getName())).build();
        }
        return null;
    }
}
