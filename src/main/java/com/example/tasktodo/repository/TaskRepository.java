package com.example.tasktodo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
	
}
