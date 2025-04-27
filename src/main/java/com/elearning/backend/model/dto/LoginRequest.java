package com.elearning.backend.model.dto;

///
/// LoginRequest
/// params: None
/// return: None
/// description: This class represents a request for user login, containing email and password fields.
public class LoginRequest {
    private String email;
    private String password;

    // Getters & Setters
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
