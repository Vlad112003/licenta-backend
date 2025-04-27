package com.elearning.backend.controller;

import com.elearning.backend.model.Role;
import com.elearning.backend.model.User;
import com.elearning.backend.model.dto.RegisterRequest;
import com.elearning.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ///
    /// Login endpoint
    /// params: LoginRequest request
    /// return: String
    /// description: This method handles user login by checking the provided credentials against the database.
    @PostMapping("login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid credentials";
        }
    }

    static class LoginRequest {
        private String email;
        private String password;

        // Getters & Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    ///
    /// Register endpoint
    /// params: RegisterRequest registerRequest
    /// return: ResponseEntity<String>
    /// description: This method handles user registration by checking if the email already exists and saving the new user to the database.
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // Verificam dacă exista deja un utilizator cu acest email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Cream un nou utilizator
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);  // Poți schimba acest rol dacă ai mai multe roluri

        // Salvam utilizatorul în baza de date
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    ///
    /// Logout endpoint
    /// params: HttpServletRequest request
    /// return: ResponseEntity<String>
    /// description: This method handles user logout by clearing the security context.
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
}
