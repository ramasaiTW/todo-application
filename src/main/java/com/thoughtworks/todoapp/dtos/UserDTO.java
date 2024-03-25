package com.thoughtworks.todoapp.dtos;

import com.thoughtworks.todoapp.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles = new HashSet<>();

    public UserDTO(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}