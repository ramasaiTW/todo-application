package com.thoughtworks.todoapp.services;

import com.thoughtworks.todoapp.dtos.ProjectDTO;
import com.thoughtworks.todoapp.dtos.UserDTO;
import com.thoughtworks.todoapp.exceptions.DataNotFoundException;
import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.models.Task;
import com.thoughtworks.todoapp.models.User;
import com.thoughtworks.todoapp.repositories.TaskRepository;
import com.thoughtworks.todoapp.repositories.UserRepository;
import com.thoughtworks.todoapp.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final ProjectService projectService;

    public TaskService(TaskRepository taskRepository,
                       SequenceGeneratorService sequenceGeneratorService,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       TokenService tokenService,
                       ProjectService projectService){
        this.taskRepository = taskRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.projectService = projectService;
    }

    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Task>> tasks = taskRepository.findAllByUserId(userId);

        if(tasks.isPresent() && tasks.get().isEmpty()){
            throw new DataNotFoundException("Tasks not found!!!");
        }
        return ResponseEntity.ok().body(tasks.get());
    }

    public ResponseEntity<Task> getTaskById(HttpServletRequest request, int id) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Task> task = taskRepository.findByIdAndUser_Id(id, userId);

        if(task.isEmpty()){
            throw new DataNotFoundException("Task not found!!!");
        }
        return ResponseEntity.ok().body(task.get());
    }

    public ResponseEntity<Task> createTask(HttpServletRequest request, Task data) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);
        UserDTO userDTO = new UserDTO(user.get().getId(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail());

        ProjectDTO projectDTO = data.getProject();

        Project project = projectService.createProjectAlongWithTask(projectDTO, userId);

        projectDTO.setId(project.getId());

        data.setId(sequenceGeneratorService.generateSequence(Task.SEQUENCE_NAME));
        data.setUser(userDTO);
        Task task = taskRepository.save(data);

        return ResponseEntity.ok().body(task);
    }

    public ResponseEntity<Task> updateTask(HttpServletRequest request, int id, Task data) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Task> taskData = taskRepository.findByIdAndUser_Id(id, userId);

        if(taskData.isEmpty()){
            throw new DataNotFoundException("Task not found!!!");
        }

        if (data.getTitle()!=null){
            taskData.get().setTitle(data.getTitle());
        }
        if (data.getDescription() != null){
            taskData.get().setDescription(data.getDescription());
        }
        if (data.getDeadline()!=null){
            taskData.get().setDeadline(data.getDeadline());
        }
        if (data.getPriority()>0){
            taskData.get().setPriority(data.getPriority());
        }
        if (data.getProject()!=null){
            ProjectDTO project = projectService.updateProjectAlongWithTask(data.getProject(), userId);
            taskData.get().setProject(project);
        }

        taskRepository.save(taskData.get());

        return ResponseEntity.ok().body(taskData.get());
    }

    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, int id) throws DataNotFoundException {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Task> task = taskRepository.findByIdAndUser_Id(id, userId);

        if(task.isEmpty()){
            throw new DataNotFoundException("Task not found!!!");
        }

        taskRepository.delete(task.get());
        Map<String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok().body(response);
    }
}
