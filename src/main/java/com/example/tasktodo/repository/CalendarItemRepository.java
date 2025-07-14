package com.example.tasktodo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.tasktodo.model.CalendarItem;

@Repository
public interface CalendarItemRepository extends JpaRepository<CalendarItem, Long> {
    List<CalendarItem> findByUserUserId(Long userId);
   
    @Query(value = """
		SELECT ci.id, ci.title, ci.reminder_minutes_before, ci.reminder_sent,
		       t.due_date_time, e.start_date_time, e.end_date_time,
		       CASE 
		           WHEN t.id IS NOT NULL THEN 'TASK' 
		           WHEN e.id IS NOT NULL THEN 'EVENT'
		       END AS item_type
		FROM calendar_item ci
		LEFT JOIN task t ON ci.id = t.id
		LEFT JOIN event e ON ci.id = e.id
		WHERE ci.reminder_sent = false
		  AND ci.reminder_minutes_before IS NOT NULL;
        """, nativeQuery = true)
        List<Object[]> findReminderEligibleItemsRaw();
}
