package ch.fhnw.shakethelakebackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link ch.fhnw.shakethelakebackend.model.entity.Icon}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IconDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private byte[] icon;
}
