package ch.fhnw.shakethelakebackend.config;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        var userId = ((FirebaseToken) authentication.getCredentials()).getUid();

        return Optional.of("%s|%s" .formatted(authentication.getName(), userId));
    }
}
