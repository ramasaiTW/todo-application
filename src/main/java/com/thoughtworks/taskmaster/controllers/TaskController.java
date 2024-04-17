package com.thoughtworks.taskmaster.controllers;

import com.thoughtworks.taskmaster.dtos.payload.request.TaskRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.TaskResponse;
import com.thoughtworks.taskmaster.exceptions.DataNotFoundException;
import com.thoughtworks.taskmaster.models.Task;
import com.thoughtworks.taskmaster.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getAllTasks(HttpServletRequest request) throws DataNotFoundException {
        return taskService.getAllTasks(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(HttpServletRequest request, @PathVariable("id") int id) throws DataNotFoundException {
        return taskService.getTaskById(request, id);
    }

    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(HttpServletRequest request, @RequestBody TaskRequest taskRequestData){
        return taskService.createTask(request, taskRequestData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(HttpServletRequest request, @PathVariable int id, @RequestBody TaskRequest taskRequestData) throws DataNotFoundException {
        return taskService.updateTask(request, id, taskRequestData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, @PathVariable int id) throws DataNotFoundException {
        return taskService.deleteTaskById(request, id);
    }
}
