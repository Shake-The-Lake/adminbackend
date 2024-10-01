package ch.fhnw.shakethelakebackend.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * Entity for localized strings
 *
 */
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class LocalizedString {

    private String en;
    private String de;
    private String swissGerman;

}
