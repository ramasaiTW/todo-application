package com.thoughtworks.taskmaster.dtos.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProjectRequest {

    private String title;
    private String description;
}
