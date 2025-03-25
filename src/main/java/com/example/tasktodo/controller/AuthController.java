package com.example.tasktodo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasktodo.dto.LoginForm;
import com.example.tasktodo.model.User;
import com.example.tasktodo.service.TaskService;
import com.example.tasktodo.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final TaskService taskService;
    
    public AuthController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }
    
//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        model.addAttribute("loginForm", new LoginForm());
//        return "login";
//    }
     
    @PostMapping("/login")
    public ResponseEntity<?> processLogin(@RequestBody LoginForm loginForm, HttpSession session) {
        System.out.println("Tentativo di login con username: " + loginForm.getUsername());
        User user = userService.findByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword());    
        
        if (user != null) {
            user.setTasks(taskService.findByUser(user)); 
            session.setAttribute("user", user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("Credenziali errate!");
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        // Invalidare la sessione quando si effettua il logout
        session.invalidate();
        return ResponseEntity.ok("Logout effettuato con successo");
    }
    
    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(HttpSession session) {
        // Verifica se c'è un utente nella sessione
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user.getUsername()); // Ritorna il nome utente o qualsiasi altro dato che ti serve
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
    }
}
