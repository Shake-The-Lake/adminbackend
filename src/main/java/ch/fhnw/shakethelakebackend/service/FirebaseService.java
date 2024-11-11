package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.config.Roles;
import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class FirebaseService {

    private final FirebaseApp firebaseApp;
    private final FirebaseAuth firebaseAuth;
    private final Firestore firestore;
    private final EventService eventService;

    public UsernamePasswordAuthenticationToken authenticate(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
        String uid = firebaseToken.getUid();
        String role = Roles.ROLE_PREFIX + Roles.ANONYMOUS;
        try {
            Map<String, Object> user = firestore.collection("users").document(uid).get().get().getData();
            if (Objects.nonNull(user)) {
                role = (String) user.get("role");
            }
        } catch (Exception ignored) {

        }
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(uid, null, authorities);
    }

    public void createUser(String uid, CreateUserDto user) {

        Event event = eventService.getEvent(user.getEventId());
        String role = "";
        if (event.getCustomerSecret().equals(user.getSecret())) {
            role = Roles.ROLE_PREFIX + Roles.CUSTOMER;
        } else if (event.getEmployeeBarcode().equals(user.getSecret())) {
            role = Roles.ROLE_PREFIX + Roles.EMPLOYEE;
        } else {
            throw new IllegalArgumentException("Invalid secret");
        }

        Map<String, Object> userMap = user.getAsMap();
        userMap.put("role", role);
        firestore.collection("users").document(uid).set(userMap);
    }


}
