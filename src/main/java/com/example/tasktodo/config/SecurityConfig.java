package com.example.tasktodo.config;

  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.http.HttpMethod;
  import org.springframework.security.config.Customizer;
  import org.springframework.security.config.annotation.web.builders.HttpSecurity;
  import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
  import org.springframework.security.config.http.SessionCreationPolicy;
  import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
  import org.springframework.security.crypto.password.PasswordEncoder;
  import org.springframework.security.web.SecurityFilterChain;
  import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
  import org.springframework.web.cors.CorsConfiguration;
  import org.springframework.web.cors.CorsConfigurationSource;
  import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

  import com.example.tasktodo.security.JwtAuthenticationFilter;
  import com.example.tasktodo.security.SessionAuthenticationFilter;

  import java.util.List;

  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {

      private final JwtAuthenticationFilter jwtAuthFilter;

      public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
          this.jwtAuthFilter = jwtAuthFilter;
      }

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/login","/api/signup").permitAll()
                .requestMatchers("/api/check-auth").permitAll() //  Adjust as needed
                .anyRequest().authenticated()
             )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
          // o disable se non serve

          return http.build();
      }

      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration config = new CorsConfiguration();
          // Solo il front-end in dev
          config.setAllowedOrigins(List.of("http://localhost:3000", "https://localhost:3000"));  //  Adjust for HTTPS in production!
          config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          config.setAllowCredentials(true);
          config.setAllowedHeaders(List.of("*"));

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", config);
          return source;
      }

      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }
  }