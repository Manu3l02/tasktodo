package com.example.tasktodo.controller;

import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.dto.CalendarItemDTO;
import com.example.tasktodo.service.CalendarService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calendar/tasks")
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"}, 
			 allowCredentials = "true")
public class TaskController {

    private final CalendarService calendarService;

    public TaskController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<CalendarItemDTO> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return calendarService.findAll(userDetails).stream()
            .filter(i -> "TASK".equals(i.getType()))
            .toList();
    }

    @PostMapping
    public ResponseEntity<CalendarItemDTO> addTask(
            @Valid @RequestBody TaskForm form,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalendarItemDTO dto = calendarService.createTask(form, userDetails);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarItemDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskForm form,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalendarItemDTO dto = calendarService.updateTask(id, form, userDetails);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        calendarService.deleteTask(id, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<CalendarItemDTO> completeTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalendarItemDTO dto = calendarService.completeTask(id, userDetails);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/reminder-sent")
    public ResponseEntity<Void> markReminderSent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        calendarService.markReminderSent(id, userDetails);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/server-time")
    public ResponseEntity<String> getServerTime() {
        return ResponseEntity.ok(LocalDateTime.now().toString());
    }
}
