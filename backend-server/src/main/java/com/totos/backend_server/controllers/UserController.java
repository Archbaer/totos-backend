package com.totos.backend_server.controllers;

import com.totos.backend_server.models.User;
import com.totos.backend_server.repositories.UserRepository;
import com.totos.backend_server.services.JWTService;
import com.totos.backend_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Base64;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println("Login request received for: " + user.getUsername());
        Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());

        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials!");
        }

        User existingUser = existingUserOptional.get();

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials!");
        }

        String token = jwtService.generateToken(existingUser);
        Map<String, String> response = new HashMap<>();
        response.put("token", "Bearer " + token);
        return ResponseEntity.ok(response);
    }

    //Protected endpoint, accessible only using JWT token
    @GetMapping("/secured")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("Welcome to secured endpoint");
    }

    @PreAuthorize("hasRole('API')")
    @GetMapping("/public-key")
    public String getPublicKey() {
        PublicKey publicKey = jwtService.getPublicKey();
        System.out.println("Working!");
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
//        System.out.println("Login request received with credentials: "+ credentials);
//
//        String username = credentials.get("username");
//        String password = credentials.get("password");
//
//        if (username == null || password == null) {
//            return ResponseEntity.badRequest().body("Username and password are required.");
//        }
//
//        System.out.println("Step 1");
//
//        Optional<User> existingUserOptional = userRepository.findByUsername(username);
//
//        if (existingUserOptional.isEmpty()) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//
//        System.out.println("Step 2");
//
//        User existingUser = existingUserOptional.get();
//
//        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//
//        System.out.println("Step 3");
//
//        String token = jwtService.generateToken(username);
//        Map<String, String> response = new HashMap<>();
//
//        System.out.println("Step 4");
//
//        response.put("token", "Bearer "+ token);
//        return ResponseEntity.ok(response);
//    }
}