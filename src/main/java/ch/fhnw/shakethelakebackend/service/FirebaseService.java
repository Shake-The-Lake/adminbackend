package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.config.Roles;
import ch.fhnw.shakethelakebackend.model.dto.CreateUserDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FirebaseService {

    private final FirebaseAuth firebaseAuth;
    private final Firestore firestore;
    private final EventService eventService;

    public UsernamePasswordAuthenticationToken authenticate(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = firebaseAuth.verifyIdToken(token);
        String uid = firebaseToken.getUid();
        String role = Roles.ROLE_PREFIX + Roles.ANONYMOUS;
        String name = "anonymous";
        try {
            Map<String, Object> user = firestore.collection("users").document(uid).get().get().getData();
            if (Objects.nonNull(user)) {
                role = (String) user.get("role");
                name = user.get("firstName") + " " + user.get("lastName");
            }
        } catch (Exception ignored) {

        }
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(name, firebaseToken, authorities);
    }

    public void createUser(Authentication auth, CreateUserDto user, Optional<Long> eventId, Optional<String> secret) {
        if (eventId.isPresent() != secret.isPresent()) {
            throw new IllegalArgumentException("Event id and secret must be both present or both absent");
        }

        String uid = ((FirebaseToken) auth.getCredentials()).getUid();
        if (eventId.isEmpty()) {
            createUser(uid, user, Roles.ROLE_PREFIX + Roles.CUSTOMER);
            return;
        }

        Event event = eventService.getEvent(eventId.get());
        String role;
        if (event.getCustomerSecret().equals(secret.get())) {
            role = Roles.ROLE_PREFIX + Roles.CUSTOMER;
        } else if (event.getEmployeeSecret().equals(secret.get())) {
            role = Roles.ROLE_PREFIX + Roles.EMPLOYEE;
        } else {
            throw new IllegalArgumentException("Invalid request");
        }

        createUser(uid, user, role);
    }

    public void createUser(String uid, CreateUserDto user, String role) {
        Map<String, Object> userMap = user.getAsMap();
        userMap.put("role", role);
        firestore.collection("users").document(uid).set(userMap);
    }

}
