package com.TaskManager.TaskManager.services.jwt;


import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    @Override
    public UserDetailsService userDetailService() {
        return username -> {
            System.out.println("=== UserService.loadUserByUsername ===");
            System.out.println("Username: " + username);

            // Use findByEmail (or findFirstByEmail if you keep that)
            Optional<User> userOptional = userRepository.findFirstByEmail(username);
            // OR: Optional<User> userOptional = userRepository.findFirstByEmail(username);

            if (userOptional.isEmpty()) {
                System.out.println("User NOT found: " + username);
                throw new UsernameNotFoundException("User Not Found: " + username);
            }

            User user = userOptional.get();
            System.out.println("User found - Email: " + user.getEmail());
            System.out.println("User found - Role: " + user.getUserRole());
            System.out.println("User found - Authorities: " + user.getAuthorities());

            return user;
        };
}}
