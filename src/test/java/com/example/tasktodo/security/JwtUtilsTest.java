package com.example.tasktodo.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // imposta i valori privati tramite reflection
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "TestSecretKey1234567890");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1000);
    }

    @Test
    void testGenerateAndParseToken() {
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtUtils.generateToken(userDetails.getUsername());

        String extractedUsername = jwtUtils.getUsernameFromToken(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        UserDetails userDetails = User.withUsername("expireduser").password("password").roles("USER").build();
        String token = jwtUtils.generateToken(userDetails.getUsername());

        // Aspetta che il token scada
        Thread.sleep(1500);

        assertThrows(TokenExpiredException.class, () -> jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void testInvalidTokenShouldThrowException() {
        String invalidToken = "invalid.token.value";

        assertThrows(JWTVerificationException.class, () -> jwtUtils.getUsernameFromToken(invalidToken));
    }
}
