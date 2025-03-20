package com.example.tasktodo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskForm {
	
	private String title;
	private String description;
	private LocalDate dueDate;
	
}
