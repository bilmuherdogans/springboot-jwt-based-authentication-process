package com.timeissecuritytime.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.timeissecuritytime.util.JwtTokenUtil;
import com.timeissecuritytime.domain.LoginRequest;
import com.timeissecuritytime.domain.RegisterRequest;
import com.timeissecuritytime.entity.User;
import com.timeissecuritytime.service.UserService;
import com.timeissecuritytime.Constants;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword());
        return ResponseEntity.ok(Constants.REGISTRATION_SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        String token = jwtTokenUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(String.format(Constants.TOKEN_RESPONSE, token));
    }
}