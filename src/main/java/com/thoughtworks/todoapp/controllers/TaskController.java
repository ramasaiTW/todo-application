package com.thoughtworks.todoapp.controllers;

import com.thoughtworks.todoapp.exceptions.DataNotFoundException;
import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.models.Task;
import com.thoughtworks.todoapp.services.TaskService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request) throws DataNotFoundException {
        return taskService.getAllTasks(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(HttpServletRequest request, @PathVariable("id") int id) throws DataNotFoundException {
        return taskService.getTaskById(request, id);
    }

    @PostMapping()
    public ResponseEntity<Task> createTask(HttpServletRequest request, @RequestBody Task data){
        return taskService.createTask(request, data);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(HttpServletRequest request, @PathVariable int id, @RequestBody Task data) throws DataNotFoundException {
        return taskService.updateTask(request, id, data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, @PathVariable int id) throws DataNotFoundException {
        return taskService.deleteTaskById(request, id);
    }
}
