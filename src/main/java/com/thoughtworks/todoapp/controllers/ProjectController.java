package com.thoughtworks.todoapp.controllers;

import com.thoughtworks.todoapp.exceptions.DataNotFoundException;
import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.services.ProjectService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {
        return projectService.getAllProjects(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(HttpServletRequest request, @PathVariable("id") int id) throws DataNotFoundException {
        return projectService.getProjectById(request, id);
    }

    @PostMapping()
    public ResponseEntity<Project> createProject(HttpServletRequest request, @RequestBody Project data){
        return projectService.createProject(request, data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(HttpServletRequest request, @PathVariable int id, @RequestBody Project data) throws DataNotFoundException {
        return projectService.updateProject(request, id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, @PathVariable int id) throws DataNotFoundException {
        return projectService.deleteProjectById(request, id);
    }
}
