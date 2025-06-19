package com.example.tasktodo.security;

  import java.io.IOException;

  import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
  import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
  import org.springframework.stereotype.Component;
  import org.springframework.web.filter.OncePerRequestFilter;

  import com.auth0.jwt.exceptions.JWTVerificationException;

  import jakarta.servlet.FilterChain;
  import jakarta.servlet.ServletException;
  import jakarta.servlet.http.HttpServletRequest;
  import jakarta.servlet.http.HttpServletResponse;

  @Component //  Make sure this is a @Component
  public class JwtAuthenticationFilter extends OncePerRequestFilter {

      private final JwtUtils jwtUtils;
      private final UserDetailsService userDetailsService;

      public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                     UserDetailsService userDetailsService) {
          this.jwtUtils = jwtUtils;
          this.userDetailsService = userDetailsService;
      }

      @Override
      protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
              throws ServletException, IOException {
          String authHeader = req.getHeader("Authorization");
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
              String token = authHeader.substring(7);
              try {
                  String username = jwtUtils.getUsernameFromToken(token);
                  UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                  UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities());
                  SecurityContextHolder.getContext().setAuthentication(auth);
              } catch (JWTVerificationException ex) {
                  //  token non valido → ignoralo, sarà gestito da Spring come 401
                  //  **IMPORTANT:** Log this exception!
                  System.err.println("Invalid JWT Token: " + ex.getMessage());
              }
          }
          chain.doFilter(req, res);
      }
  }