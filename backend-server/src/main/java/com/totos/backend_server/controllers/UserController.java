package com.totos.backend_server.controllers;

import com.totos.backend_server.models.User;
import com.totos.backend_server.repositories.UserRepository;
import com.totos.backend_server.services.JWTService;
import com.totos.backend_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    // Add a simple test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println("Register request received for: " + user.getUsername());
        // Check if user already exists by username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(400).body("Username already exists!");
        }
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        System.out.println("Login request received for: " + user.getUsername());
//        Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());
//
//        if (existingUserOptional.isEmpty()) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//
//        User existingUser = existingUserOptional.get();
//
//        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//
//        String token = jwtService.generateToken(user.getUsername());
//        Map<String, String> response = new HashMap<>();
//        response.put("token", "Bearer " + token);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Login endpoint reached!");
    }
}