package com.TaskManager.TaskManager.services.auth;


import com.TaskManager.TaskManager.dto.SignUpRequest;
import com.TaskManager.TaskManager.dto.UserDto;
import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.UserRole;
import com.TaskManager.TaskManager.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

            private final UserRepository userRepository;

            //Once bean created an all dependicies are injected
            //It will run this code once before application fully started
            @PostConstruct
            public void createAndAdminAccount(){
                //Look for user account if it already exist
               Optional<User> optionalUser= userRepository.findByUserRole(UserRole.ADMIN) ;

               //if it doesnt exist well create a new user as an ADMIN
               if (optionalUser.isEmpty()){
                    User user = new User ();
                    user.setEmail("admin@test.com");
                    user.setName("Admin");
                    user.setPassword(new BCryptPasswordEncoder().encode("admin"));
                    user.setUserRole(UserRole.ADMIN);
                   userRepository.save(user);
                   System.out.println("Admin account created successfully ");
               }else{
                   System.out.println("Admin already exists ");
               }
            }


    //Sign up is for registering a new employee
    //We're creating an api for regestring

    @Override
    public UserDto signupUser(SignUpRequest signUpRequest) {
                User user = new User();
                user.setName(signUpRequest.getName());
                user.setEmail(signUpRequest.getEmail());
                user.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
                user.setUserRole(UserRole.EMPLOYEE);
                User createdUser=userRepository.save(user);
        return createdUser.getUserDto();
    }


    //Check if this user already exists if not
    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
