package com.example.yourcompany.assessment.service;

import com.example.yourcompany.assessment.dto.UserDTO;
import com.example.yourcompany.assessment.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO updateUser(User user);
    void deleteUser(Integer userId);
    Optional<UserDTO> getUserById(Integer userId);
    Optional<UserDTO> getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
} 