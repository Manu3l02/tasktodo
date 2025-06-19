package com.example.tasktodo.security;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.example.tasktodo.model.User;
import com.example.tasktodo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SessionAuthenticationFilter(UserRepository userRepository,
                                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // 1) Controlla se c'è un userId in sessione
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj instanceof Long) {
            Long userId = (Long) userIdObj;
            System.out.println("SESSION userId = " + userIdObj); // 👈 debug temporaneo

            // 2) Se non abbiamo già un Authentication
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // 3) Carica il User dal database
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    // 4) Carica i dettagli via UserDetailsService (con ruoli e password mascarata)
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

                    // 5) Crea le authority (es. ruoli)
                    Collection<SimpleGrantedAuthority> authorities = userDetails.getAuthorities()
                            .stream()
                            .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                            .collect(Collectors.toList());

                    // 6) Costruisci il token con UserDetails (ha username, password, authorities)
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // 7) Imposta il contesto di sicurezza
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        // Procede col prossimo filtro
        chain.doFilter(request, response);
    }
}
