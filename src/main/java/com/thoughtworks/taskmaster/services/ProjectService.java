package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.ProjectRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import com.thoughtworks.taskmaster.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Project>> projects = projectRepository.findAllByUserId(userId);

        if (projects.isPresent() && projects.get().isEmpty()) {
            throw new DataNotFoundException("Projects not found!!!");
        }
        return ResponseEntity.ok().body(projects.get());
    }

    public ResponseEntity<Project> getProjectById(HttpServletRequest request, int id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }

        return ResponseEntity.ok().body(project.get());
    }

    public ResponseEntity<Project> createProject(HttpServletRequest request, Project data) {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);
        data.setUser(user.get());
        Project project = projectRepository.save(data);
        return ResponseEntity.ok().body(project);
    }

    public ResponseEntity<Project> updateProject(HttpServletRequest request, int id, Project data) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if (project.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }

        if (project.get().getTitle() != null) {
            project.get().setTitle(data.getTitle());
        }
        if (project.get().getDescription() != null) {
            project.get().setDescription(data.getDescription());
        }

        projectRepository.save(project.get());
        return ResponseEntity.ok().body(project.get());
    }

    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, int id) throws DataNotFoundException {
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

    public Project updateProjectAlongWithTask(Project project, long userId) {
        Optional<Project> projectData = projectRepository.findByIdAndUser_Id(project.getId(), userId);

        if (projectData.isEmpty()) {
            throw new DataNotFoundException("Project not found!!!");
        }

        if (project.getTitle() != null) {
            projectData.get().setTitle(project.getTitle());
        }
        if (project.getDescription() != null) {
            projectData.get().setDescription(project.getDescription());
        }

        Project projectSave = projectRepository.save(projectData.get());

        project.setTitle(projectSave.getTitle());
        project.setDescription(projectSave.getDescription());

        return project;
    }

    public Project createProjectAlongWithTask(Project project, int userId) {
        Optional<List<Project>> allProjects = projectRepository.findAllByUserId(userId);

        if (allProjects.isPresent() && !allProjects.get().isEmpty()) {
            for (int i = 0; i < allProjects.get().size(); i++) {
                if (allProjects.get().get(i).getTitle().equals(project.getTitle())) {
                    return allProjects.get().get(i);
                }
            }
        }

        Optional<User> user = userRepository.findById(userId);

        Project newProject = new Project();

        if (project == null) {
            newProject.setTitle("Personal");
            newProject.setDescription("My personal project");
        } else {
            newProject.setTitle(project.getTitle());
            newProject.setDescription(project.getDescription());
        }

        newProject.setUser(user.get());

        return projectRepository.save(newProject);
    }
}
