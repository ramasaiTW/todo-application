package com.thoughtworks.todoapp.controllers;

import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.services.ProjectService;
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
    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request){
        return projectService.getAllProjects(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(HttpServletRequest request, @PathVariable("id") int id){
        return projectService.getProjectById(request, id);
    }

    @PostMapping()
    public ResponseEntity<Project> createProject(HttpServletRequest request, @RequestBody Project data){
        return projectService.createProject(request, data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(HttpServletRequest request, @PathVariable int id, @RequestBody Project data){
        return projectService.updateProject(request, id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, @PathVariable int id){
        return projectService.deleteProjectById(request, id);
    }
}
