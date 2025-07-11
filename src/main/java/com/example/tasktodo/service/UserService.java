package com.example.tasktodo.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	
	public User getUser() {
		return userRepository.findByEmail("admin@example.com");
	}
	
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                       .orElseThrow(() -> new NoSuchElementException("Utente non trovato"));
    }
	
	public User createNewUser(User user) {
		// Codifica la password prima di salvare
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User updateUserProfile(User user) {
	    return userRepository.save(user);
	}
	
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Utente non trovato"));
	}
}
