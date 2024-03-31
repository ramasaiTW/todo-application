package com.thoughtworks.todoapp.controllers;

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All projects found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)) }),
            @ApiResponse(responseCode = "404", description = "Projects not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @GetMapping()
    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request){
        return projectService.getAllProjects(request);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(HttpServletRequest request, @PathVariable("id") int id){
        return projectService.getProjectById(request, id);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Project data supplied",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong")})
    @PostMapping()
    public ResponseEntity<Project> createProject(HttpServletRequest request, @RequestBody Project data){
        return projectService.createProject(request, data);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid project-id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong")})
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(HttpServletRequest request, @PathVariable int id, @RequestBody Project data){
        return projectService.updateProject(request, id, data);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid project-id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, @PathVariable int id){
        return projectService.deleteProjectById(request, id);
    }
}
