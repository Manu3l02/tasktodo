package com.example.tasktodo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.UserRepository;

public class UserServiceTest {
	
	@Test
	void testCreateNewUser_encodesPasswordAndSaveUser() {
		
		UserRepository userRepo = mock(UserRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		UserService userService = new UserService(userRepo, passwordEncoder);
		
		User inputUser = new User();
		inputUser.setUsername("testUser");
		inputUser.setEmail("test@example.com");
		inputUser.setPassword("plaintext");
		
		when(passwordEncoder.encode("plaintext")).thenReturn("encodedPassword");
		when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
		
		User savedUser = userService.createNewUser(inputUser);
		
		assertEquals("testUser", savedUser.getUsername());
		assertEquals("test@example.com", savedUser.getEmail());
		assertEquals("encodedPassword", savedUser.getPassword());
		
		verify(userRepo).save(any(User.class));
		verify(passwordEncoder).encode("plaintext");
	}

}
