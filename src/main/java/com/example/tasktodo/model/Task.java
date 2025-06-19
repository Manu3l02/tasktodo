package com.example.tasktodo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task")
@DiscriminatorValue("TASK")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Task extends CalendarItem {

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;

    @Column(name = "reminder_date_time")
    private LocalDateTime reminderDateTime;

    @Column(name = "reminder_sent", nullable = false)
    private boolean reminderSent = false;
}
