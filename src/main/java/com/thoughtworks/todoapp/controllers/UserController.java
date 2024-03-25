package com.thoughtworks.todoapp.controllers;

import com.thoughtworks.todoapp.dtos.payload.request.LoginRequest;
import com.thoughtworks.todoapp.dtos.payload.request.SignupRequest;
import com.thoughtworks.todoapp.services.UserControllerService;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userControllerService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userControllerService.authenticateUser(loginRequest);
    }
}
