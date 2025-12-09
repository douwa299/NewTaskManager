package com.TaskManager.TaskManager.services.employee;

import com.TaskManager.TaskManager.dto.TaskDTO;

import java.util.List;

public interface EmployeeService {


   List<TaskDTO> getTaskByUserId(Long id);

    TaskDTO  updateTask(Long id,String status);
}
