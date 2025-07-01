package com.example.tasktodo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasktodo.dto.CalendarItemDTO;
import com.example.tasktodo.service.CalendarService;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"}, 
			 allowCredentials = "true")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /** Restituisce tutti gli items (Task + Event) per l’utente */
    @GetMapping("/items")
    public List<CalendarItemDTO> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return calendarService.findAll(userDetails);
    }
}
