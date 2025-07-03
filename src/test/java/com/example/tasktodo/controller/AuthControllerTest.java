package com.example.tasktodo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tasktodo.dto.LoginForm;
import com.example.tasktodo.model.User;
import com.example.tasktodo.security.JwtUtils;
import com.example.tasktodo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtils jwtUtils;
    
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginSuccess_returnsTokenAndUser() throws Exception {
        LoginForm form = new LoginForm();
        form.setUsername("testuser");
        form.setPassword("password123");

        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("$2a$10$hashed"); // hashed simulato
        mockUser.setProfileImageUrl("/uploads/profile.jpg");

        when(userService.findByUsername("testuser")).thenReturn(mockUser);
        when(jwtUtils.generateToken("testuser")).thenReturn("fake.jwt.token");
        when(passwordEncoder.matches("password123", "$2a$10$hashed")).thenReturn(true);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.profileImageUrl").value("/uploads/profile.jpg"));
    }

    @Test
    void testLoginFailure_returns401() throws Exception {
        LoginForm form = new LoginForm();
        form.setUsername("wronguser");
        form.setPassword("wrongpass");

        when(userService.findByUsername("wronguser")).thenThrow(new NoSuchElementException("User not found"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenziali errate!"));
    }

    @Test
    void testCheckAuthSuccess_returnsUserInfo() throws Exception {
        String token = "Bearer fake.jwt.token";

        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setProfileImageUrl(null); // test fallback ""

        when(jwtUtils.getUsernameFromToken("fake.jwt.token")).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/api/check-auth")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.profileImageUrl").value(""));
    }

    @Test
    void testCheckAuthInvalidToken_returns401() throws Exception {
        when(jwtUtils.getUsernameFromToken("fake.jwt.token")).thenThrow(new JWTVerificationException("Token non valido"));

        mockMvc.perform(get("/api/check-auth")
                        .header("Authorization", "Bearer fake.jwt.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));
    }

    @Test
    void testCheckAuthNoToken_returns401() throws Exception {
        mockMvc.perform(get("/api/check-auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("No token provided"));
    }
}
