package com.example.tasktodo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.tasktodo.model.CalendarItem;

@Repository
public interface CalendarItemRepository extends JpaRepository<CalendarItem, Long> {
    List<CalendarItem> findByUserUserId(Long userId);
}
