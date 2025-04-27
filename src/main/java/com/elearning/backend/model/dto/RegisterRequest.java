package com.elearning.backend.model.dto;

///
/// RegisterRequest
/// params: None
/// return: None
/// description: This class represents a request for user registration, containing full name, email, and password fields.
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;

    // Getters & Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
