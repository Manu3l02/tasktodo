package com.example.tasktodo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tasktodo.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {    
    List<Event> findByUserUserId(Long userId);
}