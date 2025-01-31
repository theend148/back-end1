package com.example.yourcompany.assessment.entity;

import com.example.yourcompany.assessment.dto.AuthRequest;
import com.example.yourcompany.assessment.dto.AuthResponse;
import com.example.yourcompany.assessment.dto.UserDTO;

import java.util.Optional;

/**
 * @author Qianyue
 * @Date 2024.11.27 23:14
 **/
public class ResponseEntityMyself1 {
    private AuthResponse jwt;
    private Optional<UserDTO> user;

    // Constructors, getters and setters

    public ResponseEntityMyself1(AuthResponse jwt, Optional<UserDTO> user) {
        this.jwt = jwt;
        this.user = user;
    }

    public ResponseEntityMyself1() {
    }

    public AuthResponse getJwt() {
        return jwt;
    }

    public void setJwt(AuthResponse jwt) {
        this.jwt = jwt;
    }

    public Optional<UserDTO> getUser() {
        return user;
    }

    public void setUser(Optional<UserDTO> user) {
        this.user = user;
    }
}
