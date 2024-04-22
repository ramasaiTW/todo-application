package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import com.thoughtworks.taskmaster.dtos.payload.request.ProjectRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.ProjectResponse;
import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.exceptions.ProjectAlreadyExistsException;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.ProjectRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import com.thoughtworks.taskmaster.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

   Logger logger= LogManager.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ProjectService(ProjectRepository projectRepository,
                          JwtUtils jwtUtils,
                          UserRepository userRepository,
                          TokenService tokenService) {
        this.projectRepository = projectRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Log
    public ResponseEntity<List<ProjectResponse>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {

        if (!tokenService.isValidToken(request)) {
            logger.error("User Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Project>> projects = projectRepository.findAllByUserId(userId);

        if (projects.isPresent() && projects.get().isEmpty()) {
            throw new DataNotFoundException("Projects not found!!!");
        }

        List<ProjectResponse> projectResponses = new ArrayList<>();
        for(int i=0; i<projects.get().size(); i++){
            projectResponses.add(convertProjectToProjectResponse(projects.get().get(i)));
        }
        logger.info("All projects fetched successfully");
        return ResponseEntity.ok().body(projectResponses);
    }

    @Log
    public ResponseEntity<ProjectResponse> getProjectById(HttpServletRequest request, long id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            logger.error("User Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }
        logger.info("Project fetched successfully");

        ProjectResponse projectResponse = convertProjectToProjectResponse(project.get());

        return ResponseEntity.ok().body(projectResponse);
    }

    @Log
    public ResponseEntity<ProjectResponse> createProject(HttpServletRequest request, ProjectRequest projectRequestData) {

        if (!tokenService.isValidToken(request)) {
            logger.error("User Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (projectRepository.existsByTitle(projectRequestData.getTitle())){
            throw new ProjectAlreadyExistsException("Project Already In Use");
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);

        Project projectData = convertProjectRequestToProject(projectRequestData, user.get());

        Project project = projectRepository.save(projectData);

        ProjectResponse projectResponse = convertProjectToProjectResponse(project);

        logger.info(" Project created successfully");
        return ResponseEntity.ok().body(projectResponse);
    }

    @Log
    public ResponseEntity<ProjectResponse> updateProject(HttpServletRequest request, long id, ProjectRequest projectRequestData) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            logger.error("User Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }

        if (project.get().getTitle() != null) {
            project.get().setTitle(projectRequestData.getTitle());
        }
        if (project.get().getDescription() != null) {
            project.get().setDescription(projectRequestData.getDescription());
        }

        Project updateProject = projectRepository.save(project.get());
        ProjectResponse projectResponse = convertProjectToProjectResponse(updateProject);

        logger.info(" Project updated successfully");
        return ResponseEntity.ok().body(projectResponse);
    }

    @Log
    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, long id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            logger.error("User Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }
        projectRepository.delete(project.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        logger.info(" Project deleted successfully");
        return ResponseEntity.ok().body(response);
    }

    @Log
    public Project getProjectByProjectDTO(ProjectDTO projectDTO){
        return projectRepository.findById(projectDTO.getId()).get();
    }

    @Log
    private Project convertProjectRequestToProject(ProjectRequest projectRequest, User user){
        Project project = new Project();
        project.setTitle(projectRequest.getTitle());
        project.setDescription(projectRequest.getDescription());
        project.setUser(user);
        logger.info("Converted ProjectRequest to Project ");
        return project;
    }

    @Log
    private ProjectResponse convertProjectToProjectResponse(Project project){
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setTitle(project.getTitle());
        projectResponse.setDescription(project.getDescription());
        logger.info("Converted Project to ProjectResponse");
        return projectResponse;
    }
}
