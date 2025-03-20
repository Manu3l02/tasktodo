package com.example.tasktodo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.tasktodo.dto.LoginForm;
import com.example.tasktodo.dto.TaskForm;
import com.example.tasktodo.model.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping("/")
	public String home(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/login"; // 🔒 Se non loggato, torna al login
	    }
	    
	    model.addAttribute("taskForm", new TaskForm());
	    model.addAttribute("user", user);
	    return "home"; 
	}
	
	@ResponseBody
	@GetMapping("/hello")
	public String hello() {
		return "Hello World! Ciao a tutti sono il primo programma al mondo "
				+ "(di *inserire nome* (riguardo il web più precisamente Spring Web))";
	}

}

