package com.elearning.backend.model.dto;

///
/// AuthResponse
/// params: None
/// return: None
/// description: This class represents the response for authentication, containing a token.
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
