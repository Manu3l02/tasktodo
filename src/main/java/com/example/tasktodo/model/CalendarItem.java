package com.example.tasktodo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "calendar_item")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class CalendarItem {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "title", length = 100, nullable = false)
    protected String title;

    @Column(name = "description", columnDefinition = "TEXT")
    protected String description;
    
    @Column(name = "reminder_minutes_before")
    protected Integer reminderMinutesBefore;

    @Column(name = "reminder_sent", nullable = false)
    protected boolean reminderSent = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    protected User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    protected Priority priority = Priority.MEDIUM;
}
