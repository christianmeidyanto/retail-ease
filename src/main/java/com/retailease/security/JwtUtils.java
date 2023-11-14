package com.retailease.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${retailease.jwt.secret}")
    private String jwtSecret;

    @Value("${retailease.jwt.expiration}")
    private Long jwtExpiration;

    public String getEmailByToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String generateToken (String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .compact();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            log.error("invalid jwt token {}", e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("Jwt token is expired {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("Unsupported Jwt token {}", e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("Jwt claims string is empty {}", e.getMessage());
        }
        return false;
    }
}
