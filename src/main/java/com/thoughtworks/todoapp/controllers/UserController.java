package com.thoughtworks.todoapp.controllers;

import com.thoughtworks.todoapp.dtos.payload.request.LoginRequest;
import com.thoughtworks.todoapp.dtos.payload.request.SignupRequest;
import com.thoughtworks.todoapp.models.Project;
import com.thoughtworks.todoapp.services.UserControllerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.version}/auth")
public class UserController {

    private final UserControllerService userControllerService;

    public UserController(UserControllerService userControllerService) {
        this.userControllerService = userControllerService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid details supplied",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userControllerService.registerUser(signUpRequest);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged in Successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request ",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode ="500", description = "Oops, Something went wrong",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userControllerService.authenticateUser(loginRequest);
    }
}
