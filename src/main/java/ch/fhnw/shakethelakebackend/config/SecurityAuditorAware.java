package ch.fhnw.shakethelakebackend.config;

import ch.fhnw.shakethelakebackend.model.entity.User;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

class SecurityAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String userId = ((FirebaseToken) authentication.getCredentials()).getUid();
        User user = User.builder().firebaseToken(userId).firebaseUserName(authentication.getName()).build();
        return Optional.of(user);
    }
}
