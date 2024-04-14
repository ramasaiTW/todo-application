package com.thoughtworks.taskmaster.repositories;

import com.thoughtworks.taskmaster.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    boolean existsByUser_Email(String email);

    Token findByUser_Email(String email);

    Token findByToken(String jwt);
}
