package com.TaskManager.TaskManager.services.admin;

import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.dto.UserDto;
import org.springframework.scheduling.config.Task;

import java.util.List;

public interface AdminService {

     List<UserDto> getUsers();

     TaskDTO createTask(TaskDTO taskDTO);
     List<TaskDTO>   getAllTasks();
     void deleteTask(Long id);
         TaskDTO getTaskById(Long id);
    TaskDTO updateTask(Long id,TaskDTO taskDTO);
    List<TaskDTO>   searchTaskByTitle(String title);

}
