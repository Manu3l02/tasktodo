package com.example.tasktodo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @MockitoBean
    private CalendarService calendarService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void testAddTask_returns201AndTaskData() throws Exception {

        TaskForm form = new TaskForm();
        form.setTitle("Nuova task");
        form.setDescription("Descrizione di esempio.");
        form.setDueDate(LocalDate.of(2025, 7, 1));
        form.setReminderDateTime(LocalDateTime.of(2025, 6, 30, 14, 0));

        CalendarItemDTO resultDto = new CalendarItemDTO();
        resultDto.setId(1L);
        resultDto.setTitle("Nuova task");
        resultDto.setDescription("Descrizione di esempio.");
        resultDto.setType("TASK");
        resultDto.setDueDate(form.getDueDate());
        resultDto.setReminderDateTime(form.getReminderDateTime());

        when(calendarService.createTask(any(TaskForm.class), any())).thenReturn(resultDto);

        mockMvc.perform(post("/api/calendar/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nuova task"))
                .andExpect(jsonPath("$.description").value("Descrizione di esempio."))
                .andExpect(jsonPath("$.type").value("TASK"))
                .andExpect(jsonPath("$.dueDate").value("2025-07-01"))
                .andExpect(jsonPath("$.reminderDateTime").value("2025-06-30T14:00:00"));
    }
}
