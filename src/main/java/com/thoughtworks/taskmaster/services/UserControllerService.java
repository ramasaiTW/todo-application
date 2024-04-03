package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.exceptions.EmailExistsException;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.dtos.payload.request.LoginRequest;
import com.thoughtworks.taskmaster.dtos.payload.request.SignupRequest;
import com.thoughtworks.taskmaster.dtos.payload.responce.JwtResponse;
import com.thoughtworks.taskmaster.dtos.payload.responce.MessageResponse;
import com.thoughtworks.taskmaster.repositories.TokenRepository;
import com.thoughtworks.taskmaster.repositories.UserRepository;
import com.thoughtworks.taskmaster.security.jwt.JwtUtils;
import com.thoughtworks.taskmaster.security.services.UserDetailsImpl;
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

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final TokenService tokenService;

    public UserControllerService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository,
                                 PasswordEncoder encoder,
                                 JwtUtils jwtUtils,
                                 SequenceGeneratorService sequenceGeneratorService,
                                 TokenRepository tokenRepository,
                                 TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.tokenService = tokenService;
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailExistsException("Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        tokenService.storeTokenIntoDB(jwt, userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                roles));
    }
}
