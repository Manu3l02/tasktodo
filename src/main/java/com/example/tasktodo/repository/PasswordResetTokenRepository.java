package com.example.tasktodo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.tasktodo.model.PasswordResetToken;
import com.example.tasktodo.model.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	Optional<PasswordResetToken> findByToken(String token);

	@Modifying
	@Transactional
	void deleteByUser(User user);
}
