package com.example.tasktodo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.model.Event;
import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.EventRepository;
import com.example.tasktodo.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
		form.setDueDateTime(LocalDateTime.of(2025, 6, 30, 19, 0));
		form.setReminderMinutesBefore(5);
		
		when(taskRepo.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);
		
		var result = calendarService.createTask(form, userDetails);
		
		assertEquals("Test Task", result.getTitle());
		assertEquals("Descrizione", result.getDescription());
		assertEquals(LocalDateTime.of(2025, 6, 30, 19, 0), result.getDueDateTime());
		assertEquals(5, result.getReminderMinutesBefore());
		assertFalse(result.isCompleted());
		assertFalse(result.isReminderSent());
		
		verify(taskRepo).save(any(Task.class));
		verify(userService).findByUsername("testUser");
	}
	
	@Test
	void testCompleteTask_marksTaskAsCompleted() {
	    // Utente mock
	    User user = new User();
	    user.setUserId(1L);
	    user.setUsername("testUser");

	    // UserDetails mock
	    UserDetails userDetails = mock(UserDetails.class);
	    when(userDetails.getUsername()).thenReturn("testUser");
	    when(userService.findByUsername("testUser")).thenReturn(user);

	    // Task mock
	    Task task = new Task();
	    task.setId(100L);
	    task.setUser(user);
	    task.setTitle("Task incompleto");
	    task.setCompleted(false);

	    when(taskRepo.findById(100L)).thenReturn(java.util.Optional.of(task));
	    when(taskRepo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

	    // Esegui metodo
	    var result = calendarService.completeTask(100L, userDetails);

	    // Verifica che sia stato completato
	    assertEquals(100L, result.getId());
	    assertEquals("Task incompleto", result.getTitle());
	    assertTrue(result.isCompleted());

	    // Verifica interazioni
	    verify(taskRepo).findById(100L);
	    verify(taskRepo).save(any(Task.class));
	}
	
	@Test
	void testFindAll_shouldReturnSortedMixedList() {
	    // Setup utente e UserDetails
	    User user = new User();
	    user.setUserId(1L);
	    user.setUsername("testUser");

	    UserDetails userDetails = mock(UserDetails.class);
	    when(userDetails.getUsername()).thenReturn("testUser");
	    when(userService.findByUsername("testUser")).thenReturn(user);

	    // Creo 1 task con dueDate 2025-07-10
	    Task task = new Task();
	    task.setId(1L);
	    task.setUser(user);
	    task.setTitle("Task 1");
	    task.setDueDateTime(LocalDateTime.of(2025, 7, 10, 8, 0));
	    task.setCompleted(false);

	    // Creo 1 evento con startDateTime 2025-07-05
	    Event event = new Event();
	    event.setId(2L);
	    event.setUser(user);
	    event.setTitle("Evento A");
	    event.setStartDateTime(LocalDateTime.of(2025, 7, 5, 14, 0));
	    event.setEndDateTime(LocalDateTime.of(2025, 7, 5, 16, 0));

	    when(taskRepo.findByUserUserId(1L)).thenReturn(List.of(task));
	    when(eventRepo.findByUserUserId(1L)).thenReturn(List.of(event));

	    var result = calendarService.findAll(userDetails);

	    // Verifica: 2 elementi, ordinati
	    assertEquals(2, result.size());

	    // L’evento dovrebbe venire prima (2025-07-05 vs 2025-07-10)
	    assertEquals("EVENT", result.get(0).getType());
	    assertEquals("TASK", result.get(1).getType());

	    // Verifica titoli
	    assertEquals("Evento A", result.get(0).getTitle());
	    assertEquals("Task 1", result.get(1).getTitle());
	}


}
