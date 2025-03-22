package com.example.tasktodo.config;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.tasktodo.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain)
											throws ServletException, IOException {
		// Recupera l'utente dalla sessione
		Object principal = request.getSession().getAttribute("user");
		
		// Se esiste un utente e il SecurityContext non è già popolato, imposta l'autenticazione
		if (principal != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (principal instanceof User) {
				User domainUser = (User) principal;
				// Creo un token di autenticazione usando il nome utente
				// Qui se ho dei ruoli li posso aggiungere alla lista di GrantedAuthority
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(
								domainUser.getUsername(), null, new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		// Procede con il filtro successivo nella catena
		filterChain.doFilter(request, response);
	}

}
