package ch.fhnw.shakethelakebackend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String secret;
    private Long eventId;

    @JsonIgnore
    public Map<String, Object> getAsMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", this.firstName);
        user.put("lastName", this.lastName);
        user.put("phoneNumber", this.phoneNumber);
        return user;
    }
}
