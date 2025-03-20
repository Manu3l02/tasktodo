package com.example.tasktodo.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long userId;
	
	@Column(name = "username", nullable = false, length = 50)
	private String username;
	
	@Column(name = "email", unique = true, nullable = false, length = 50)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@OneToMany(mappedBy = "user")
	private List<Task> tasks;

}
