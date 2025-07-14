package com.example.tasktodo.service;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.tasktodo.model.PasswordResetToken;
import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.PasswordResetTokenRepository;
import com.example.tasktodo.repository.UserRepository;

@Service
public class PasswordResetService {

	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final EmailNotifier emailNotifier;
	
	public PasswordResetService(UserRepository userRepository,
								PasswordResetTokenRepository tokenRepository,
								EmailNotifier emailNotifier) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.emailNotifier = emailNotifier;
	}
	
	public boolean sendResetLink(String email) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		
		if (userOpt.isEmpty()) return false;
		
		User user = userOpt.get();
		
		// Elimina token vecchi
		tokenRepository.deleteByUser(user);
		
		// Crea nuovo token
		String token = UUID.randomUUID().toString();
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);
		
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiryDate(expiry);
		
		tokenRepository.save(resetToken);
		
        // link per il reset
        String link = "http://localhost:3000/reset-password?token=" + token;
		
        // Invia l'email
        String subject = "Recupero password - TaskTodo";
        String body = """
        		<p>Ciao,</p>
        		<p>Hai richiesto di resettare la tua password.</p>
        		<p><a href="%s" style="display:inline-block; background-color:#4CAF50; 
        		      color:white padding:10px 20px; text-decoration:none; border-radius:4px;">
        		      🔐 Clicca qui per resettarla
        		      </a>
        		</p>
        		<p>Questo link è valido per 30 minuti.</p>
        		<hr>
        		<p style="font-size: 12px; color: #888;">
        		   Se non hai richiesto nulla, ignora questa email.
        		</p>
        		""".formatted(link);
        
            return emailNotifier.sendReminder(email, subject, body);
	}
        
        public Optional<User> validateToken(String token) {
            Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
            if (tokenOpt.isEmpty()) return Optional.empty();

            PasswordResetToken resetToken = tokenOpt.get();
            if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(resetToken); // pulisce i token scaduti
                return Optional.empty();
            }

            return Optional.of(resetToken.getUser());
        }

        public void resetPassword(User user, String newPasswordHash) {
            user.setPassword(newPasswordHash);
            userRepository.save(user);
            tokenRepository.deleteByUser(user);
        }
		
}
