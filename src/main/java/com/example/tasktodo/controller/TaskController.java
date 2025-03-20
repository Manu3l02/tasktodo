package com.example.tasktodo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasktodo.model.Task;
import com.example.tasktodo.model.User;
import com.example.tasktodo.service.TaskService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/tasks")
//@CrossOrigin(origins = "http://localhost:3000", 
//	methods = {
//			RequestMethod.GET, 
//			RequestMethod.POST, 
//			RequestMethod.DELETE, 
//			RequestMethod.PUT, 
//			RequestMethod.OPTIONS},
//	allowCredentials = "true")// 🔥 Permetti i cookie
public class TaskController {
	
	@Autowired
	private TaskService taskService;

    // Rispondi esplicitamente alla richiesta OPTIONS
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
	
    @GetMapping
    public List<Task> getTasks(HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	if(user == null) {
    		throw new RuntimeException("Utente non autenticato");
    	}
        return taskService.findByUser(user); // Prendi le task dell'utente loggato;
    }

    @PostMapping
    public Task addTask(@RequestBody Task task, HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	if (user == null) {
    		throw new RuntimeException("Utente non autenticato");
    	}
    	task.setUser(user);
        return taskService.saveTask(task);
    }
	
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	if(user == null) {
    		throw new RuntimeException("Utente non autenticato");
    	}
        boolean deleted = taskService.deleteTaskById(id);
        
        if (deleted) {
            return ResponseEntity.ok("Task eliminata con successo");
        } else {
            return ResponseEntity.status(404).body("Task non trovata");
        }
    }
	
}
