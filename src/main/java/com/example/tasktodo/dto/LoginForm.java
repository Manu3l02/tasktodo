package com.example.tasktodo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

	@NotBlank(message = "Lo username non può essere vuoto")
	private String username;

	@NotBlank(message = "La password non può essere vuota")
	private String password;
}
