package com.thoughtworks.todoapp.models;

import com.thoughtworks.todoapp.dtos.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
@NoArgsConstructor
@Getter
@Setter
public class Project {
    @Transient
    public static final String SEQUENCE_NAME = "project_sequence";

    @Id
    private long id;
    private String title;
    private String description;
    private UserDTO user;
}
