package com.TaskManager.TaskManager.dto;


import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
public class TaskDTO {
    private Long id ;

    private String description;
    private String title;
    private TaskStatus taskStatus;
    private Date dueDate;
    private String priority;

    private Long employeeId ;
    private String employeeName;
}
