package com.example.tasktodo.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tasktodo.dto.LoginForm;
import com.example.tasktodo.model.User;
import com.example.tasktodo.security.JwtUtils; // Import JwtUtils
import com.example.tasktodo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class AuthController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils; // Inject JwtUtils

	public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) { // Add JwtUtils
																											// to
																											// constructor
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/login")
	public ResponseEntity<?> processLogin(
			@RequestBody LoginForm loginForm,
			HttpSession session) { // No HttpSession needed
								   // if not using sessions
		User user;
		try {
			user = userService.findByUsername(loginForm.getUsername().trim());
		} catch (NoSuchElementException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali errate!");
		}

		if (passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
			String jwt = jwtUtils.generateToken(user.getUsername()); // Generate the JWT
			return ResponseEntity.ok(Map.of(
					"token", jwt,
					"userId", user.getUserId(),
					"username", user.getUsername(),
					"profileImageUrl", user.getProfileImageUrl() != null ? user.getProfileImageUrl() : "" // AGGIUNTO
			));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali errate!");
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok("Logout effettuato con successo");
	}

	@GetMapping("/check-auth")
	public ResponseEntity<?> checkAuth(@RequestHeader("Authorization") String authHeader) {
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        String token = authHeader.substring(7);
	        try {
	            String username = jwtUtils.getUsernameFromToken(token);
	            User user = userService.findByUsername(username); // recupera userId e altri dati

	            return ResponseEntity.ok(Map.of(
	                "username", user.getUsername(),
	                "userId", user.getUserId(),
	                "profileImageUrl", user.getProfileImageUrl() != null ? user.getProfileImageUrl() : "" // AGGIUNTO QUI!
	            ));
	        } catch (JWTVerificationException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
	        } catch (NoSuchElementException e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token provided");
	    }
	}

}