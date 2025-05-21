package com.elearning.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours

    // Using a static key for simplicity, but in production consider injecting this from properties
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Generate a JWT token for the given user email and role
     */
    public static String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    /**
     * Extract the email (subject) from the token
     */
    public static String getUserEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the role from token claims
     */
    public static String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Get the expiration date from the token
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extract a specific claim from the token
     */
    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse the token and extract all claims
     */
    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validate if token is valid (not expired)
     */
    public static Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate token with specific email
     */
    public static Boolean validateToken(String token, String email) {
        try {
            final String tokenEmail = getUserEmailFromToken(token);
            return (tokenEmail.equals(email) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if token is expired
     */
    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}