package com.thoughtworks.todoapp.services;

import com.thoughtworks.todoapp.dtos.ProjectDTO;
import com.thoughtworks.todoapp.dtos.UserDTO;
import com.thoughtworks.todoapp.exceptions.DataNotFoundException;
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
    public ResponseEntity<List<Project>> getAllProjects(HttpServletRequest request) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Project>> projects = projectRepository.findAllByUserId(userId);

        if(projects.isPresent() && projects.get().isEmpty()){
            throw new DataNotFoundException("Projects not found!!!");
        }
        return ResponseEntity.ok().body(projects.get());
    }

    public ResponseEntity<Project> getProjectById(HttpServletRequest request, int id) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if(project.isEmpty()){
            throw new DataNotFoundException("Project not found!!!");
        }

        return ResponseEntity.ok().body(project.get());
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

    public ResponseEntity<Project> updateProject(HttpServletRequest request, int id, Project data) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if(project.isEmpty()){
            throw new DataNotFoundException("Project not found!!!");
        }

        if(project.get().getTitle()!=null){
            project.get().setTitle(data.getTitle());
        }
        if(project.get().getDescription()!=null){
            project.get().setDescription(data.getDescription());
        }

        projectRepository.save(project.get());
        return ResponseEntity.ok().body(project.get());
    }

    public ResponseEntity<Map<String, Boolean>> deleteProjectById(HttpServletRequest request, int id) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Project> project = projectRepository.findByIdAndUser_Id(id, userId);

        if(project.isEmpty()){
            throw new DataNotFoundException("Project not found!!!");
        }
        projectRepository.delete(project.get());

        Map<String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok().body(response);
    }

    public ProjectDTO updateProjectAlongWithTask(ProjectDTO projectDTO, long userId){
        Optional<Project> project = projectRepository.findByIdAndUser_Id(projectDTO.getId(), userId);

        if(project.isEmpty()){
            throw new DataNotFoundException("Project not found!!!");
        }

        if(projectDTO.getTitle()!=null){
            project.get().setTitle(projectDTO.getTitle());
        }
        if(projectDTO.getDescription()!=null){
            project.get().setDescription(projectDTO.getDescription());
        }

        Project projectSave = projectRepository.save(project.get());

        projectDTO.setTitle(projectSave.getTitle());
        projectDTO.setDescription(projectSave.getDescription());

        return projectDTO;
    }

    public Project createProjectAlongWithTask(ProjectDTO projectDTO, int userId){
        Optional<List<Project>> allProjects = projectRepository.findAllByUserId(userId);

        if(allProjects.isPresent() && !allProjects.get().isEmpty()){
            for(int i=0; i<allProjects.get().size(); i++){
                if(allProjects.get().get(i).getTitle().equals(projectDTO.getTitle())){
                    return allProjects.get().get(i);
                }
            }
        }

        Optional<User> user = userRepository.findById(userId);
        UserDTO userDTO = new UserDTO(user.get().getId(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail());

        Project project = new Project();
        project.setId(sequenceGeneratorService.generateSequence(Project.SEQUENCE_NAME));

        if(projectDTO.getTitle()==null){
            project.setTitle("Personal");
        }
        else {
            project.setTitle(projectDTO.getTitle());
        }

        if (projectDTO.getDescription()==null){
            project.setDescription("My personal project");
        }
        else {
            project.setDescription(projectDTO.getDescription());
        }
        project.setUser(userDTO);

        projectRepository.save(project);

        return project;
    }
}
