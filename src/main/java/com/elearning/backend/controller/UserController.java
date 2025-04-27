package com.elearning.backend.controller;

import com.elearning.backend.model.User;
import com.elearning.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    ///
    /// Get all users
    /// params: None
    /// return: List<User>
    /// description: This method retrieves all users from the database.
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    ///
    /// Get user by ID
    /// params: Long id
    /// return: User
    /// description: This method retrieves a user by its ID.
    /// throws: RuntimeException if the user is not found
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

    ///
    /// Endpoint for creating a new user
    /// params: User user
    /// return: User
    /// description: This method creates a new user and saves it to the database.
    /// It is important to ensure that the password is encrypted before saving.
    /// throws: RuntimeException if the user already exists
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Endpoint pentru a actualiza un utilizator existent
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword()); // Asigurați-vă că parola este criptată
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    ///
    /// Endpoint for deleting a user by ID
    /// params: Long id
    /// return: None
    /// description: This method deletes a user by its ID.
    /// throws: RuntimeException if the user is not found
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
