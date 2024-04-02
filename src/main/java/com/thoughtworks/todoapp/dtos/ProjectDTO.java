package com.thoughtworks.todoapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectDTO {
    private long id;
    private String title;
    private String description;

    public ProjectDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
