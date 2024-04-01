package com.thoughtworks.todoapp.controllers;

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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All tasks found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)) }),

            @ApiResponse(responseCode = "404", description = "Tasks not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(HttpServletRequest request){
        return taskService.getAllTasks(request);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid task-id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(HttpServletRequest request, @PathVariable("id") int id){
        return taskService.getTaskById(request, id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid Task ",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @PostMapping()
    public ResponseEntity<Task> createTask(HttpServletRequest request, @RequestBody Task data){
        return taskService.createTask(request, data);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task Updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid task-id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(HttpServletRequest request, @PathVariable int id, @RequestBody Task data){
        return taskService.updateTask(request, id, data);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid task-id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTaskById(HttpServletRequest request, @PathVariable int id){
        return taskService.deleteTaskById(request, id);
    }
}
