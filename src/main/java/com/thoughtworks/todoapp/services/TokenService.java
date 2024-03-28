package com.thoughtworks.todoapp.services;

import com.thoughtworks.todoapp.dtos.UserDTO;
import com.thoughtworks.todoapp.models.Token;
import com.thoughtworks.todoapp.repositories.TokenRepository;
import com.thoughtworks.todoapp.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public TokenService(TokenRepository tokenRepository,
                        SequenceGeneratorService sequenceGeneratorService) {
        this.tokenRepository = tokenRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    public void storeTokenIntoDB(String jwt, UserDetailsImpl userDetails){
        if(tokenRepository.existsByUser_Email(userDetails.getEmail())){
            Token token = tokenRepository.findByUser_Email(userDetails.getEmail());
            token.setExpired(false);
            token.setRevoked(false);
            token.setToken(jwt);
            token.setCreatedAt(new Date(System.currentTimeMillis()));
            tokenRepository.save(token);
        }
        else{
            UserDTO userDTO = new UserDTO(userDetails.getId(),
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail());

            Token token = new Token(sequenceGeneratorService.generateSequence(Token.SEQUENCE_NAME),
                    false,
                    false,
                    jwt,
                    new Date(System.currentTimeMillis()),
                    userDTO);

            tokenRepository.save(token);
        }
    }

    public String getTokenFromRequest(HttpServletRequest request){
        return request.getHeader("Authorization").split(" ")[1];
    }

    public boolean isValidToken(HttpServletRequest request){
        String jwt = getTokenFromRequest(request);
        Token token = tokenRepository.findByToken(jwt);
        return !token.isExpired() && !token.isRevoked();
    }
}
