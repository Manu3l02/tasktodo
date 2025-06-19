package com.example.tasktodo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskForm {
	
	private String title;
	private String description;
	private LocalDate dueDate;
	private LocalDateTime reminderDateTime;
	
}
