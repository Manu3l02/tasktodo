package com.example.tasktodo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		super();
		this.taskRepository = taskRepository;
	}
	
	public List<Task> findByUser(User user) {
	    return taskRepository.findByUser(user);
	}
	
	public Task saveTask(Task task) {
		return taskRepository.save(task);
	}
	
	public boolean deleteTaskById(Long id) {
		if(taskRepository.existsById(id)) {
			taskRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
