package com.example.tasktodo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@DiscriminatorValue("TASK")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Task extends CalendarItem {

    @Column(name = "due_date_time")
    private LocalDateTime dueDateTime;

    @Column(name = "is_completed", nullable = false)
    private boolean completed = false;

}
