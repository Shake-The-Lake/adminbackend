package ch.fhnw.shakethelakebackend.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class FirebaseService {

    private final FirebaseApp firebaseApp;
    private final FirebaseAuth firebaseAuth;

    public UsernamePasswordAuthenticationToken authenticate(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
        String role = (String) firebaseToken.getClaims().get("role");
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(firebaseToken.getUid(), null, authorities);
    }
}
