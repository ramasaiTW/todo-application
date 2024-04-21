package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.dtos.ProjectDTO;
import com.thoughtworks.taskmaster.dtos.payload.request.ProjectRequest;
import com.thoughtworks.taskmaster.dtos.payload.request.TaskRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.TaskResponse;
import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.models.Project;
import com.thoughtworks.taskmaster.models.Task;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       JwtUtils jwtUtils,
                       UserRepository userRepository,
                       TokenService tokenService,
                       ProjectService projectService,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.projectRepository = projectRepository;
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

    public ResponseEntity<TaskResponse> getTaskById(HttpServletRequest request, long id) throws DataNotFoundException {
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

        Optional<Project> project = projectRepository.findByIdAndUser_Id(taskRequestData.getProjectId(), userId);

        if(project.isEmpty()){
            throw new DataNotFoundException("Project not found!!!");
        }

        Task taskData = convertTaskRequestToTask(taskRequestData, user.get(), project.get());

        Task task = taskRepository.save(taskData);

        TaskResponse taskResponse = convertTaskToTaskResponse(task);

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<TaskResponse> updateTask(HttpServletRequest request, long id, TaskRequest taskRequestData) throws DataNotFoundException {
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
        if (taskRequestData.isStatus()) {
            taskData.get().setStatus(true);
        }
        if (!taskRequestData.isStatus()) {
            taskData.get().setStatus(false);
        }
        if (taskRequestData.getProjectId() > 0) {
            Optional<Project> project = projectRepository.findByIdAndUser_Id(taskRequestData.getProjectId(), userId);

            if(project.isEmpty()){
                throw new DataNotFoundException("Project not found!!!");
            }

            taskData.get().setProject(project.get());
        }

        Task updatedtask = taskRepository.save(taskData.get());

        TaskResponse taskResponse = convertTaskToTaskResponse(updatedtask);

        return ResponseEntity.ok().body(taskResponse);
    }

    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, long id) throws DataNotFoundException {
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

    public ResponseEntity<List<TaskResponse>> getAllTasksByProjectId(HttpServletRequest request, long id) {
        if (!tokenService.isValidToken(request)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        int userId = jwtUtils.getUserIdFromToken(request);
        Optional<List<Task>> tasks = taskRepository.findAllByUserIdAndProjectIdOrderByDeadlineAscPriorityAsc(userId, id);

        if (tasks.isPresent() && tasks.get().isEmpty()) {
            throw new DataNotFoundException("Tasks not found!!!");
        }

        List<TaskResponse> taskResponses = new ArrayList<>();

        for(int i=0; i<tasks.get().size(); i++){
            taskResponses.add(convertTaskToTaskResponse(tasks.get().get(i)));
        }

        return ResponseEntity.ok().body(taskResponses);
    }

    private TaskResponse convertTaskToTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setDeadline(task.getDeadline());
        taskResponse.setStatus(task.isStatus());

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
        task.setStatus(false);
        task.setUser(user);
        task.setProject(project);

        return task;
    }
}
