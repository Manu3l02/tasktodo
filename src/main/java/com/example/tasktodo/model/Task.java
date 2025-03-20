package com.example.tasktodo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Long taskId;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_completed")
	private boolean isCompleted;
	
	@Column(name = "due_date")
	private LocalDate dueDate; // Data di scadenza, solo giorno
	
	// TODO: Aggiungere parametri alle task
//	@Column(name = "reminder_date_time")
//	private LocalDateTime reminderDateTime; // L'orario per il promemoria
//	
//	@Column(name = "scheduled_date_time")
//	private LocalDateTime scheduledDateTime; // Data e ora in cui l'utente intende svolgere la task
//	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore  // Evita il loop infinito nella serializzazione
	private User user;
	
}
