package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.dtos.payload.request.LoginRequest;
import com.thoughtworks.taskmaster.dtos.payload.request.SignupRequest;
import com.thoughtworks.taskmaster.dtos.payload.response.JwtResponse;
import com.thoughtworks.taskmaster.dtos.payload.response.MessageResponse;
import com.thoughtworks.taskmaster.exceptions.EmailExistsException;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.TokenRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import com.thoughtworks.taskmaster.security.jwt.JwtUtils;
import com.thoughtworks.taskmaster.security.services.UserDetailsImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserControllerService {

    Logger logger= LogManager.getLogger(UserControllerService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;

    public UserControllerService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository,
                                 PasswordEncoder encoder,
                                 JwtUtils jwtUtils,
                                 TokenRepository tokenRepository,
                                 TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }
    @Log
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailExistsException("Email is already in use!");
        }

        logger.info("Creating a new user's account");
        User user = new User(signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);
        logger.info("User registered successfully!");
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @Log
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        logger.info("Authenticating User");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("User Authenticated , generating token");
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        logger.info("Token generated , saving token to DB");
        tokenService.storeTokenIntoDB(jwt, userDetails);

        logger.info("User Authenticated Successfully");
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                roles));
    }
}
