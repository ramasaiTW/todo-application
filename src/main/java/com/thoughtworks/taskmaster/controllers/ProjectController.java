package com.thoughtworks.taskmaster.controllers;

import com.thoughtworks.taskmaster.dtos.payload.request.ProjectRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.ProjectResponse;
import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }


    @GetMapping()
    public ResponseEntity<List<ProjectResponse>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {
        return projectService.getAllProjects(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(HttpServletRequest request, @PathVariable("id") long id) throws DataNotFoundException {
        return projectService.getProjectById(request, id);
    }

    @PostMapping()
    public ResponseEntity<ProjectResponse> createProject(HttpServletRequest request, @RequestBody ProjectRequest projectRequestData){
        return projectService.createProject(request, projectRequestData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(HttpServletRequest request, @PathVariable long id, @RequestBody ProjectRequest projectRequestData) throws DataNotFoundException {
        return projectService.updateProject(request, id, projectRequestData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, @PathVariable long id) throws DataNotFoundException {
        return projectService.deleteProjectById(request, id);
    }
}
