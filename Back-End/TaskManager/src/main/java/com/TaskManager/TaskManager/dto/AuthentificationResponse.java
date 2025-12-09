package com.TaskManager.TaskManager.dto;


import com.TaskManager.TaskManager.enums.UserRole;
import lombok.Data;

@Data
public class AuthentificationResponse {

    private String jwt ;
    private Long userId;
    private UserRole userRole;

}
