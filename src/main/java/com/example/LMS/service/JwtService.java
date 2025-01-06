package com.example.LMS.service;

import com.example.LMS.entity.Role;
import com.example.LMS.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
    private String generateToken(Map<String, Object> claims, User user) {
        List<String> roleNames = getRoleNames(user.getRoles());
        String id = user.getId();
        return Jwts.builder()
                .claims(claims)
                .claim("scope", roleNames)
                .claim("id", id)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean isValid(String token, UserDetails user) {
        final String email = extractEmail(token);
        return email.equals(user.getUsername()) && !isTokenExpired(token).before(new Date());
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token) {
            return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    private Date isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public List<String> getRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
