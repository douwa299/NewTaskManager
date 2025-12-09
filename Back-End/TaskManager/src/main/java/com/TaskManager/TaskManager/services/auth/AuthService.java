package com.TaskManager.TaskManager.services.auth;

import com.TaskManager.TaskManager.dto.SignUpRequest;
import com.TaskManager.TaskManager.dto.UserDto;

public interface AuthService {

    UserDto signupUser(SignUpRequest signUpRequest);

    boolean hasUserWithEmail(String email);
}
