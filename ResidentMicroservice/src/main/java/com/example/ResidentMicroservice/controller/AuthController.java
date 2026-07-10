package com.example.ResidentMicroservice.controller;

import com.example.ResidentMicroservice.model.GoogleLogin;
import com.example.ResidentMicroservice.model.Login;
import com.example.ResidentMicroservice.model.User;
import com.example.ResidentMicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.createUser(user);
        if (newUser == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Login> login(@RequestBody User user) {
        Login loginDTO = userService.loginUser(user);
        if (loginDTO == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(loginDTO);
    }

    @PostMapping("/google-login")
    public ResponseEntity<Login> googleLogin(@RequestBody GoogleLogin dto) {
        Login loginDTO = userService.googleLogin(dto);
        return ResponseEntity.ok(loginDTO);
    }
}

