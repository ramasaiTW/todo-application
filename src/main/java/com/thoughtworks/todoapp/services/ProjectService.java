package com.thoughtworks.todoapp.services;

import com.thoughtworks.todoapp.dtos.UserDTO;
import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.models.User;
import com.thoughtworks.todoapp.repositories.ProjectRepository;
import com.thoughtworks.todoapp.repositories.UserRepository;
import com.thoughtworks.todoapp.security.jwt.JwtUtils;
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
    private final SequenceGeneratorService sequenceGeneratorService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ProjectService(ProjectRepository projectRepository,
                          SequenceGeneratorService sequenceGeneratorService,
                          JwtUtils jwtUtils,
                          UserRepository userRepository,
                          TokenService tokenService){
        this.projectRepository = projectRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }
    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        List<Project> projects = projectRepository.findAllByUserId(userId);
        return ResponseEntity.ok().body(projects);
    }

    public ResponseEntity<Project> getProjectById(HttpServletRequest request, int id) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Project project = projectRepository.findByIdAndUser_Id(id, userId);
        return ResponseEntity.ok().body(project);
    }

    public ResponseEntity<Project> createProject(HttpServletRequest request, Project data) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);
        UserDTO userDTO = new UserDTO(user.get().getId(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail());
        data.setId(sequenceGeneratorService.generateSequence(Project.SEQUENCE_NAME));
        data.setUser(userDTO);
        Project project = projectRepository.save(data);
        return ResponseEntity.ok().body(project);
    }

    public ResponseEntity<Project> updateProject(HttpServletRequest request, int id, Project data) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Project project = projectRepository.findByIdAndUser_Id(id, userId);
        project.setTitle(data.getTitle());
        project.setDescription(data.getDescription());
        projectRepository.save(project);
        return ResponseEntity.ok().body(project);
    }

    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, int id) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Project project = projectRepository.findByIdAndUser_Id(id, userId);
        projectRepository.delete(project);
        Map<String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok().body(response);
    }
}
