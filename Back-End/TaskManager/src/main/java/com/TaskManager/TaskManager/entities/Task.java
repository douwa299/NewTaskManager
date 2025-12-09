package com.TaskManager.TaskManager.entities;


import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String description;
    private String title;
    private TaskStatus taskStatus;
    private Date dueDate;
    private String priority;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id",nullable=false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    public TaskDTO getTaskDTO() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(id);
        taskDTO.setTitle(title);
        taskDTO.setDescription(description);
        taskDTO.setTaskStatus(taskStatus);
        taskDTO.setDueDate(dueDate);
        taskDTO.setPriority(priority);
        taskDTO.setEmployeeId(user.getId());
        taskDTO.setEmployeeName(user.getName());
        return taskDTO;
    }
}
