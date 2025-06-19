package com.example.tasktodo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private LocalDate dueDate; // per Task

    private boolean completed; // per Task

    private LocalDateTime reminderDateTime;

    private boolean reminderSent;

    private LocalDateTime sortKey;

    private LocalDateTime startDateTime; // per Event

    private LocalDateTime endDateTime;
}