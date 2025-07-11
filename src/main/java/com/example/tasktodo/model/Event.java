package com.example.tasktodo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event")
@DiscriminatorValue("EVENT")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Event extends CalendarItem {

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

}
