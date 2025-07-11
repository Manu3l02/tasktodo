package com.example.tasktodo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarItemDTO {
    private Long id;

    private String type; // "TASK" o "EVENT"

    private String title;

    private String description;
    
    private LocalDateTime dueDateTime; // per Task

    private boolean completed; // per Task

    private Integer reminderMinutesBefore;

    private boolean reminderSent;

    private LocalDateTime sortKey;

    private LocalDateTime startDateTime; // per Event

    private LocalDateTime endDateTime; // per Event
}