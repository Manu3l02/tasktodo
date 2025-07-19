package com.example.tasktodo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationForm {

	@NotBlank(message = "Il nome utente è obbligatorio")
	private String username;
	
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
	private String email;
    
    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
	private String password;
	
}
