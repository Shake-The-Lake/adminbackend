package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
@Getter
public class User {
    private String firebaseToken;
    private String firebaseUserName;
}
