package com.example.tasktodo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	Task findTaskByTitle(String title);
	
	List<Task> findByUser(User user);
	
	List<Task> findByUserUserId(Long userId);

//    @Modifying
//    @Query("UPDATE Task t SET t.isCompleted = true "
//    		+ "WHERE t.taskId = :id")
//    int markTaskCompleted(@Param("id") Long id);

//    @Modifying
//    @Query("UPDATE Task t SET t.title = :#{#task.title},"
//    		+ " t.description = :#{#task.description},"
//    		+ " t.dueDate = :#{#task.dueDate},"
//    		+ " t.reminderDateTime = :#{#task.reminderDateTime}"
//    		+ " WHERE t.taskId = :#{#task.taskId}")
//    int updateTask(@Param("task") Task task);
	
}
