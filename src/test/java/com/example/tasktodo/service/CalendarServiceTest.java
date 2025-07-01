package com.example.tasktodo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.EventRepository;
import com.example.tasktodo.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalendarServiceTest {
	
	private TaskRepository taskRepo;
	private EventRepository eventRepo;
	private UserService userService;
	private CalendarService calendarService;
	
	@BeforeEach
	void setup() {
		taskRepo = mock(TaskRepository.class);
		eventRepo = mock(EventRepository.class);
		userService = mock(UserService.class);
		calendarService = new CalendarService(taskRepo, eventRepo, userService);
	}
	
	@Test
	void testCreateTask_createsAndSavesTaskForUser() {
		User user = new User();
		user.setUserId(1L);
		user.setUsername("testUser");
		
		UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getUsername()).thenReturn("testUser");
		when(userService.findByUsername("testUser")).thenReturn(user);
		
		TaskForm form = new TaskForm();
		form.setTitle("Test Task");
		form.setDescription("Descrizione");
		form.setDueDate(LocalDate.of(2025, 6, 30));
		form.setReminderDateTime(LocalDateTime.of(2025, 6, 29, 15, 0));
		
		when(taskRepo.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);
		
		var result = calendarService.createTask(form, userDetails);
		
		assertEquals("Test Task", result.getTitle());
		assertEquals("Descrizione", result.getDescription());
		assertEquals(LocalDate.of(2025, 6, 30), result.getDueDate());
		assertEquals(LocalDateTime.of(2025, 6, 29, 15, 0), result.getReminderDateTime());
		assertFalse(result.isCompleted());
		assertFalse(result.isReminderSent());
		
		verify(taskRepo).save(any(Task.class));
		verify(userService).findByUsername("testUser");
	}
	

}
