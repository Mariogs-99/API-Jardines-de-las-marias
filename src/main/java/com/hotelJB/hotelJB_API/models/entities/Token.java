package com.hotelJB.hotelJB_API.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name="token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private int IdToken;

    @Column(name = "content")
    private String token;

    @Column(name = "f_creation",insertable = false, updatable = false)
    private Date fCreation;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User_ user;

    public Token(String content, User_ user, Boolean active) {
        this.active = active;
        this.token = content;
        this.user = user;
    }
}
