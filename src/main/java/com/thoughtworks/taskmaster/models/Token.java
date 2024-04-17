package com.thoughtworks.taskmaster.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private boolean revoked;
    private boolean expired;
    private String token;
    private Date createdAt;
    @ManyToOne
    private User user;

    public Token(boolean revoked, boolean expired, String jwt, Date date, User user) {
        this.revoked = revoked;
        this.expired = expired;
        this.token = jwt;
        this.createdAt = date;
        this.user = user;
    }
}
