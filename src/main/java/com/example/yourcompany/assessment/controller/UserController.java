package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.UserDTO;
import com.example.yourcompany.assessment.entity.User;
import com.example.yourcompany.assessment.service.UserService;
//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody User user) {
        try {
            user.setUserId(id);
            return ResponseEntity.ok(userService.updateUser(user));
        } catch (DataIntegrityViolationException e) {
            Map<String, String> response = new HashMap<>();
            if (e.getMessage().contains("users.username")) {
                response.put("message", "用户名已存在，请使用其他用户名");
            } else if (e.getMessage().contains("users.email")) {
                response.put("message", "邮箱已存在，请使用其他邮箱");
            } else {
                response.put("message", "数据冲突，请检查输入信息");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String role = payload.get("role");
        System.out.println("role:" + role);
        if (role == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    // 全局异常处理
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        Map<String, String> response = new HashMap<>();
        if (e.getMessage().contains("users.username")) {
            response.put("message", "用户名已存在，请使用其他用户名");
        } else if (e.getMessage().contains("users.email")) {
            response.put("message", "邮箱已存在，请使用其他邮箱");
        } else {
            response.put("message", "数据冲突，请检查输入信息");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}