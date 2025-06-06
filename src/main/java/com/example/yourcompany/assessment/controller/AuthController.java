package com.example.yourcompany.assessment.controller;

import com.example.yourcompany.assessment.dto.AuthRequest;
import com.example.yourcompany.assessment.dto.AuthResponse;
import com.example.yourcompany.assessment.dto.UserDTO;
import com.example.yourcompany.assessment.entity.ResponseEntityMyself1;
import com.example.yourcompany.assessment.entity.User;
import com.example.yourcompany.assessment.security.JwtTokenUtil;
import com.example.yourcompany.assessment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
//    TODO 这里注释掉了

    @Autowired
    AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final UserService userService;


    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping("/login")
    public ResponseEntity<ResponseEntityMyself1> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Optional<UserDTO> obj2=userService.getUserByUsername(authRequest.getUsername());

        ResponseEntityMyself1  obj1=new ResponseEntityMyself1(new AuthResponse(jwt),obj2);

        return ResponseEntity.ok(obj1);

    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}