package com.example.tasktodo.controller;

import com.example.tasktodo.dto.EventForm;
import com.example.tasktodo.dto.CalendarItemDTO;
import com.example.tasktodo.service.CalendarService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/calendar/events")
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"}, 
			 allowCredentials = "true")
public class EventController {

    private final CalendarService calendarService;

    public EventController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping
    public List<CalendarItemDTO> getEvents(@AuthenticationPrincipal UserDetails userDetails) {
        return calendarService.findAll(userDetails).stream()
            .filter(i -> "EVENT".equals(i.getType()))
            .toList();
    }

    @PostMapping
    public ResponseEntity<CalendarItemDTO> addEvent(
            @Valid @RequestBody EventForm form,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalendarItemDTO dto = calendarService.createEvent(form, userDetails);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarItemDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventForm form,
            @AuthenticationPrincipal UserDetails userDetails) {
        CalendarItemDTO dto = calendarService.updateEvent(id, form, userDetails);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        calendarService.deleteEvent(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}
