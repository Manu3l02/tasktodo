package com.example.tasktodo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		// Disabilito CSRF per semplificare ( in produzione, da valutare CSRF tokens)
		.csrf(csrf -> csrf.disable())
		// Attiva CORS (uso la configurazione CORS già defenita)
		.cors(Customizer.withDefaults())
		// Aggiunge un filtro custom prima del filtro standard per l'autenticazione
		.addFilterBefore(new SessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		// Configura le regole di autorizzazione
		.authorizeHttpRequests(authz -> authz
			// Permettere le richieste OPTIONS (necessarie per le preflight requests)
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			// Rende pubblici gli endpoint per il login e il check-auth
			.requestMatchers("/api/signup", "/api/login", "/api/check-auth").permitAll()
			// Tutti gli altri endpoint devono essere autenticati
			.anyRequest().authenticated()
			)
		
		// Rimuovo il form login e httpBasic per delegare il login al mio endpoint custom
		.httpBasic(httpBasic -> httpBasic.disable())
		.formLogin(form -> form.disable());
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Puoi passare "strength" (cost factor) come parametro.
		return new BCryptPasswordEncoder(); 		
	}
	
}
