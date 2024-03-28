package com.thoughtworks.todoapp.dtos.payload.responce;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, long id, String firstName, String lasttName, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lasttName;
        this.email = email;
        this.roles = roles;
    }
}
