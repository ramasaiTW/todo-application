package com.thoughtworks.taskmaster.models;

import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import com.thoughtworks.taskmaster.dtos.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "tasks")
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Transient
    public static final String SEQUENCE_NAME = "task_sequence";

    @Id
    private long id;
    private String title;
    private String description;
    private int priority;
    private LocalDateTime deadline;
    private ProjectDTO project;
    private UserDTO user;
}
