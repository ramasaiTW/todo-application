package com.thoughtworks.taskmaster.dtos.payload.request;

import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaskRequest {

    private String title;
    private String description;
    private int priority;
    private LocalDateTime deadline;
    private ProjectDTO projectDTO;
}
