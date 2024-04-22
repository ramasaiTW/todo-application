package com.thoughtworks.taskmaster.services;

import com.thoughtworks.taskmaster.annotations.Log;
import com.thoughtworks.taskmaster.models.Token;
import com.thoughtworks.taskmaster.models.User;
import com.thoughtworks.taskmaster.repositories.TokenRepository;
import com.thoughtworks.taskmaster.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    Logger logger= LogManager.getLogger(TokenService.class);
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Log
    public void storeTokenIntoDB(String jwt, UserDetailsImpl userDetails) {

        if (tokenRepository.existsByUser_Email(userDetails.getEmail())) {
            logger.info("Existing user, updating old token with new one ");
            Token token = tokenRepository.findByUser_Email(userDetails.getEmail());
            token.setExpired(false);
            token.setRevoked(false);
            token.setToken(jwt);
            token.setCreatedAt(new Date(System.currentTimeMillis()));
            tokenRepository.save(token);
        } else {
            logger.info("New user, storing token to DB ");
            User userData = new User(userDetails.getId(),
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail());

            Token token = new Token(
                    false,
                    false,
                    jwt,
                    new Date(System.currentTimeMillis()),
                    userData);

            tokenRepository.save(token);
        }
        logger.info("Token stored successfully");
    }

    @Log
    public String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").split(" ")[1];
    }
    @Log
    public boolean isValidToken(HttpServletRequest request) {
        String jwt = getTokenFromRequest(request);
        Token token = tokenRepository.findByToken(jwt);
        return !token.isExpired() && !token.isRevoked();
    }
}
