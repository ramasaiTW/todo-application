package com.thoughtworks.taskmaster.dtos.payload.response;

import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskResponse {
    private long id;
    private String title;
    private String description;
    private int priority;
    private LocalDateTime deadline;
    private boolean status;
    private ProjectDTO projectDTO;
}
