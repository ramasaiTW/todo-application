package com.thoughtworks.todoapp.services;

import com.thoughtworks.todoapp.dtos.UserDTO;
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

    public TaskService(TaskRepository taskRepository,
                       SequenceGeneratorService sequenceGeneratorService,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       TokenService tokenService){
        this.taskRepository = taskRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request){
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return ResponseEntity.ok().body(tasks);
    }

    public ResponseEntity<Task> getTaskById(HttpServletRequest request, int id) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Task task = taskRepository.findByIdAndUser_Id(id, userId);
        return ResponseEntity.ok().body(task);
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
                user.get().getEmail(),
                user.get().getRoles());

        data.setId(sequenceGeneratorService.generateSequence(Task.SEQUENCE_NAME));
        data.setUser(userDTO);
        Task task = taskRepository.save(data);
        return ResponseEntity.ok().body(task);
    }

    public ResponseEntity<Task> updateTask(HttpServletRequest request, int id, Task data) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Task taskData = taskRepository.findByIdAndUser_Id(id, userId);
        taskData.setTitle(data.getTitle());
        taskData.setDescription(data.getDescription());
        taskData.setDeadline(data.getDeadline());
        taskData.setPriority(data.getPriority());
        taskData.setProject(data.getProject());
        taskRepository.save(taskData);

        return ResponseEntity.ok().body(taskData);
    }

    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, int id) {
        if(!tokenService.isValidToken(request)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Task task = taskRepository.findByIdAndUser_Id(id, userId);
        taskRepository.delete(task);
        Map<String, Boolean > response = new HashMap< >();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok().body(response);
    }
}
