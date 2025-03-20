package com.example.tasktodo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.tasktodo.iterceptor.AuthInterceptor;

//@Configuration
public class WebConfig implements WebMvcConfigurer {
	
//    @Autowired
//    private AuthInterceptor authInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/**"); // Intercetta tutto
//    }
//	
//    @Bean
//    WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")
//                        .allowedOrigins("http://localhost:3000")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Aggiungi OPTIONS!
//                        .allowedHeaders("*") // Consenti qualsiasi header
//                        .allowCredentials(true)
//                        .maxAge(3600); // Cache preflight per un'ora
//                System.out.println("CORS Configuration applied");  // Aggiungi log
//            }
//        };
//    }
}
