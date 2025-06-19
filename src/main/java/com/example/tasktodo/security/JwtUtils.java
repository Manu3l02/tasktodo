package com.example.tasktodo.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }

    public String generateToken(String username) {
        return JWT.create()
                  .withSubject(username)
                  .withIssuedAt(new Date())
                  .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                  .sign(algorithm());
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm()).build().verify(token);
        return jwt.getSubject();
    }
}
