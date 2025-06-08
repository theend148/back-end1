package com.example.yourcompany.assessment.service.impl;

import com.example.yourcompany.assessment.repository.UserRepository;
import com.example.yourcompany.assessment.service.UserService;
import com.example.yourcompany.assessment.dto.UserDTO;
import com.example.yourcompany.assessment.entity.User;
import com.example.yourcompany.assessment.entity.UserRole;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(User user) {
        // TODO 这里是密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // user.setPassword(user.getPassword());

        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(User user) {
        // 获取现有用户
        User existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getUserId()));

        // 检查是否更新密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // 如果有oldPassword字段，这是密码更新
            if (user.getOldPassword() != null) {
                // 验证旧密码
                if (!passwordEncoder.matches(user.getOldPassword(), existingUser.getPassword())) {
                    throw new RuntimeException("Old password is incorrect");
                }
                // 更新为新密码
                existingUser.setPassword(passwordEncoder.encode(user.getNewPassword()));
            } else {
                // 直接更新密码
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        // 更新基本信息
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        return convertToDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserDTO> getUserById(Integer userId) {
        return userRepository.findById(userId).map(this::convertToDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO updateUserRole(Integer userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        try {
            UserRole userRole = UserRole.valueOf(role);
            user.setRole(userRole);
            return convertToDTO(userRepository.save(user));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().toString());
        return dto;
    }
}