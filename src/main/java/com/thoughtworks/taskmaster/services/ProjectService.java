package com.thoughtworks.taskmaster.services;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

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

    public ResponseEntity<List<ProjectResponse>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
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

        return ResponseEntity.ok().body(projectResponses);
    }

    public ResponseEntity<ProjectResponse> getProjectById(HttpServletRequest request, long id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }

        ProjectResponse projectResponse = convertProjectToProjectResponse(project.get());

        return ResponseEntity.ok().body(projectResponse);
    }

    public ResponseEntity<ProjectResponse> createProject(HttpServletRequest request, ProjectRequest projectRequestData) {
        if (!tokenService.isValidToken(request)) {
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

        return ResponseEntity.ok().body(projectResponse);
    }

    public ResponseEntity<ProjectResponse> updateProject(HttpServletRequest request, long id, ProjectRequest projectRequestData) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
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

        return ResponseEntity.ok().body(projectResponse);
    }

    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, long id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
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
        return ResponseEntity.ok().body(response);
    }

    public Project getProjectByProjectDTO(ProjectDTO projectDTO){
        return projectRepository.findById(projectDTO.getId()).get();
    }

    private Project convertProjectRequestToProject(ProjectRequest projectRequest, User user){
        Project project = new Project();
        project.setTitle(projectRequest.getTitle());
        project.setDescription(projectRequest.getDescription());
        project.setUser(user);

        return project;
    }

    private ProjectResponse convertProjectToProjectResponse(Project project){
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setTitle(project.getTitle());
        projectResponse.setDescription(project.getDescription());

        return projectResponse;
    }
}
