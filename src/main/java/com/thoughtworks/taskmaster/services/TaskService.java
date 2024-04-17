package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import com.thoughtworks.taskmaster.dtos.payload.request.ProjectRequest;
import com.thoughtworks.taskmaster.dtos.payload.request.TaskRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.TaskResponse;
import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.Task;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.TaskRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import com.thoughtworks.taskmaster.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final ProjectService projectService;

    public TaskService(TaskRepository taskRepository,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       TokenService tokenService,
                       ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.projectService = projectService;
    }

    public ResponseEntity<List<TaskResponse>> getAllTasks(HttpServletRequest request) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Task>> tasks = taskRepository.findAllByUserId(userId);

        if (tasks.isPresent() && tasks.get().isEmpty()) {
            throw new DataNotFoundException("Tasks not found!!!");
        }

        List<TaskResponse> taskResponses = new ArrayList<>();

        for(int i=0; i<tasks.get().size(); i++){
            taskResponses.add(convertTaskToTaskResponse(tasks.get().get(i)));
        }

        return ResponseEntity.ok().body(taskResponses);
    }

    public ResponseEntity<TaskResponse> getTaskById(HttpServletRequest request, int id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Task> task = taskRepository.findByIdAndUser_Id(id, userId);

        if (task.isEmpty()) {
            throw new DataNotFoundException("Task not found!!!");
        }

        TaskResponse taskResponse = convertTaskToTaskResponse(task.get());

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<TaskResponse> createTask(HttpServletRequest request, TaskRequest taskRequestData) {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);

        ProjectDTO projectRequest = taskRequestData.getProjectDTO();

        Project project = projectService.createProjectAlongWithTask(projectRequest, userId);

        Task taskData = convertTaskRequestToTask(taskRequestData, user.get(), project);

        Task task = taskRepository.save(taskData);

        TaskResponse taskResponse = convertTaskToTaskResponse(task);

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<TaskResponse> updateTask(HttpServletRequest request, int id, TaskRequest taskRequestData) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<User> user = userRepository.findById(userId);
        Optional<Task> taskData = taskRepository.findByIdAndUser_Id(id, userId);

        if (taskData.isEmpty()) {
            throw new DataNotFoundException("Task not found!!!");
        }

        if (taskRequestData.getTitle() != null) {
            taskData.get().setTitle(taskRequestData.getTitle());
        }
        if (taskRequestData.getDescription() != null) {
            taskData.get().setDescription(taskRequestData.getDescription());
        }
        if (taskRequestData.getDeadline() != null) {
            taskData.get().setDeadline(taskRequestData.getDeadline());
        }
        if (taskRequestData.getPriority() > 0) {
            taskData.get().setPriority(taskRequestData.getPriority());
        }
        if (taskRequestData.getProjectDTO() != null) {
            taskData.get().setProject(projectService.getProjectByProjectDTO(taskRequestData.getProjectDTO()));
        }

        Task updatedtask = taskRepository.save(taskData.get());

        TaskResponse taskResponse = convertTaskToTaskResponse(updatedtask);

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, int id) throws DataNotFoundException {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<Task> task = taskRepository.findByIdAndUser_Id(id, userId);

        if (task.isEmpty()) {
            throw new DataNotFoundException("Task not found!!!");
        }
        taskRepository.delete(task.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return ResponseEntity.ok().body(response);
    }

    private TaskResponse convertTaskToTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setDeadline(task.getDeadline());

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(task.getProject().getId());
        projectDTO.setTitle(task.getProject().getTitle());
        projectDTO.setDescription(task.getProject().getDescription());

        taskResponse.setProjectDTO(projectDTO);

        return taskResponse;
    }

    private Task convertTaskRequestToTask(TaskRequest taskRequest, User user, Project project){
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setDeadline(taskRequest.getDeadline());
        task.setUser(user);
        task.setProject(project);

        return task;
    }
}
