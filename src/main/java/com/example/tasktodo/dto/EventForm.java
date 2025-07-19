package com.example.tasktodo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EventForm {
	
	@NotBlank(message = "Il titolo è obbligatorio")
    private String title;
    private String description;
    
    @NotNull(message = "La data/ora di inizio è obbligatoria")
    @Future(message = "La data di inizio deve essere nel futuro")
    private LocalDateTime startDateTime;
    
    @NotNull(message = "La data/ora di fine è obbligatoria")
    @Future(message = "La data di fine deve essere nel futuro")
    private LocalDateTime endDateTime;
    
	private Integer reminderMinutesBefore;
	
	@AssertTrue(message = "La data di fine deve essere successiva a quella di inizio")
	public boolean isEndAfterStart() {
		if (startDateTime == null || endDateTime == null) return true;
		return endDateTime.isAfter(startDateTime);
	}
}
