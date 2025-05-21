package com.elearning.backend.controller;

import com.elearning.backend.model.Role;
import com.elearning.backend.model.User;
import com.elearning.backend.model.dto.RegisterRequest;
import com.elearning.backend.repository.UserRepository;
import com.elearning.backend.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Generate token
                String token = JwtTokenUtil.generateToken(user.getEmail(), user.getRole().name());

                // Setup authentication in security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("email", user.getEmail());
                response.put("fullName", user.getFullName());
                response.put("role", user.getRole().name());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Authentication failed: " + e.getMessage()));
        }
    }

    // Register endpoint remains the same
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        // Your existing code
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // Extract token from Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Check if we have authentication from SecurityContextHolder as fallback
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

            if (existingAuth == null || !existingAuth.isAuthenticated() ||
                    existingAuth.getPrincipal().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Not authenticated"));
            }

            // Use the existing authentication
            String email = existingAuth.getName();
            return getUserInfoResponseByEmail(email);
        }

        // Extract and validate token
        String token = authHeader.substring(7);

        try {
            if (!JwtTokenUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Invalid token"));
            }

            String email = JwtTokenUtil.getUserEmailFromToken(token);

            // Set authentication in SecurityContext
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            return getUserInfoResponseByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token validation failed: " + e.getMessage()));
        }
    }

    private ResponseEntity<?> getUserInfoResponseByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("email", user.getEmail());
            userInfo.put("fullName", user.getFullName());
            userInfo.put("role", user.getRole().name());

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching user data: " + e.getMessage()));
        }
    }

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}