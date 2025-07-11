package com.example.tasktodo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.tasktodo.dto.CalendarItemDTO;
import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.service.CalendarService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //MockBean è stato deprecato in favore di MockitoBean... tanto per dire
    @MockitoBean
    private CalendarService calendarService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void testPostTasks_shouldReturnCreatedTask() throws Exception {

        TaskForm form = new TaskForm();
        form.setTitle("Nuova task");
        form.setDescription("Descrizione di esempio.");
        form.setDueDateTime(LocalDateTime.of(2025, 7, 1, 14, 00));
        form.setReminderMinutesBefore(5);

        CalendarItemDTO resultDto = new CalendarItemDTO();
        resultDto.setId(1L);
        resultDto.setTitle("Nuova task");
        resultDto.setDescription("Descrizione di esempio.");
        resultDto.setType("TASK");
        resultDto.setDueDateTime(form.getDueDateTime());
        resultDto.setReminderMinutesBefore(form.getReminderMinutesBefore());

        when(calendarService.createTask(any(TaskForm.class), any())).thenReturn(resultDto);

        mockMvc.perform(post("/api/calendar/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nuova task"))
                .andExpect(jsonPath("$.description").value("Descrizione di esempio."))
                .andExpect(jsonPath("$.type").value("TASK"))
                .andExpect(jsonPath("$.dueDateTime").value("2025-07-01T14:00:00"))
                .andExpect(jsonPath("$.reminderMinutesBefore").value("5"));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testGetTasks_returnsTaskListForUser() throws Exception {
        // Arrange: creiamo una lista di DTO simulati
        CalendarItemDTO task1 = new CalendarItemDTO();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Descrizione 1");
        task1.setType("TASK");
        task1.setDueDateTime(LocalDateTime.of(2025, 7, 1, 12, 30));
        task1.setReminderMinutesBefore(10);

        CalendarItemDTO task2 = new CalendarItemDTO();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Descrizione 2");
        task2.setType("TASK");
        task2.setDueDateTime(LocalDateTime.of(2025, 7, 2, 18, 00));
        task2.setReminderMinutesBefore(15);

        when(calendarService.findAll(any())).thenReturn(List.of(task1, task2));

        // Act + Assert
        mockMvc.perform(get("/api/calendar/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    void testCompleteTask_returnsUpdatedTask() throws Exception {
        CalendarItemDTO completed = new CalendarItemDTO();
        completed.setId(1L);
        completed.setTitle("Completato");
        completed.setType("TASK");
        completed.setCompleted(true);

        when(calendarService.completeTask(eq(1L), any())).thenReturn(completed);

        mockMvc.perform(patch("/api/calendar/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }


    
}
