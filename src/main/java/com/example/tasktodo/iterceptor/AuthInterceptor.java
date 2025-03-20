package com.example.tasktodo.iterceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // Non crea nuova sessione

        Object user = (session != null) ? session.getAttribute("user") : null;

        String uri = request.getRequestURI();

        // Qui escludiamo il login corretto per te + eventuali richieste pubbliche
        boolean isPublic = uri.startsWith("/api/login") || uri.startsWith("/api/register") ||
                           uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images");

        if (user == null && !isPublic) {
            // Redirect all'endpoint corretto (potresti avere frontend che gestisce diversamente)
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	return false;
        }
        return true;
    }
}
