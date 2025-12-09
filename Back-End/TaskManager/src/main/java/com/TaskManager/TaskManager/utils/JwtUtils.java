package com.TaskManager.TaskManager.utils;


import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final UserRepository userRepository;
    private static final String SECRET_KEY = "/oV3TWKp1vLbi5ISXAYiN/XWLNwL+I30v3VKsTWDHcg="; // 32+ bytes

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }




    public boolean isTokenValid(String token, UserDetails userDetails){
         final String username=extractUserName(token);
         return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUserName(String token){
         return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers) {
         final Claims claims =extractAllClaims(token);
         return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private boolean isTokenExpired(String token){
         return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
         return extractClaim(token,Claims::getExpiration)  ;
    }
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("=== DEBUG: Authentication ===");
        System.out.println("Authentication: " + authentication);

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            System.out.println("Principal type: " + principal.getClass().getName());
            System.out.println("Principal: " + principal);

            // Handle different types of principals
            if (principal instanceof User) {
                User user = (User) principal;
                System.out.println("User ID from principal: " + user.getId());
                Optional<User> optionalUser = userRepository.findById(user.getId());
                return optionalUser.orElse(null);
            } else if (principal instanceof UserDetails) {
                // Extract username and find user from repository
                String username = ((UserDetails) principal).getUsername();
                System.out.println("Username from UserDetails: " + username);
                return userRepository.findFirstByEmail(username)
                        .orElse(null);
            } else if (principal instanceof String) {
                // Principal is just a username string
                String username = (String) principal;
                System.out.println("Username from String: " + username);
                return userRepository.findFirstByEmail(username)  // FIXED: Changed to findByEmail
                        .orElse(null);
            }
        }
        return null;
    }

}
