package com.example.tasktodo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasktodo.dto.RegistrationForm;
import com.example.tasktodo.model.User;
import com.example.tasktodo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class RegistrationController {

	private final UserService userService;
	
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/signup")
	public void signup(@Valid @RequestBody RegistrationForm regForm) {
		User newUser = new User();
		
		newUser.setUsername(regForm.getUsername());
		newUser.setEmail(regForm.getEmail());
		newUser.setPassword(regForm.getPassword());
		
		userService.createNewUser(newUser);
		System.out.println("Qui creo l'utente da inviare al DB");
	}
}
