package com.thoughtworks.todoapp.models;

import com.thoughtworks.todoapp.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {
    @Transient
    public static final String SEQUENCE_NAME = "token_sequence";
    @Id
    private long id;
    private boolean revoked;
    private boolean expired;
    private String token;
    private Date createdAt;
    private UserDTO user;
}
