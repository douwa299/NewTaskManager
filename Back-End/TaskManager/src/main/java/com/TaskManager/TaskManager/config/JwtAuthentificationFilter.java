package com.TaskManager.TaskManager.config;

import com.TaskManager.TaskManager.services.jwt.UserService;
import com.TaskManager.TaskManager.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.TaskManager.TaskManager.entities.User;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthentificationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;  // Changed from JwtService to JwtUtils
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("Request URI: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail;

        try {
            userEmail = jwtUtils.extractUserName(jwt);
            System.out.println("Extracted email: " + userEmail);
        } catch (Exception e) {
            System.err.println("Failed to extract email: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                System.out.println("Loading UserDetails for: " + userEmail);
                UserDetails userDetails = userService.userDetailService().loadUserByUsername(userEmail);

                System.out.println("=== USER DETAILS DEBUG ===");
                System.out.println("Username: " + userDetails.getUsername());
                System.out.println("Authorities: " + userDetails.getAuthorities());
                System.out.println("Class: " + userDetails.getClass().getName());

                if (userDetails instanceof User) {
                    User user = (User) userDetails;
                    System.out.println("User role from entity: " + user.getUserRole());
                    System.out.println("User role name(): " + user.getUserRole().name());
                }

                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    System.out.println("Token is valid");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()  // ← This should be ["EMPLOYEE"]
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication set with authorities: " + authToken.getAuthorities());
                } else {
                    System.out.println("Token is INVALID");
                }
            } catch (UsernameNotFoundException e) {
                System.err.println("User not found: " + userEmail);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}