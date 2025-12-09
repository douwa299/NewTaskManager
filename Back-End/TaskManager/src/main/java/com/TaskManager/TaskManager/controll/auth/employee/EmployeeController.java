package com.TaskManager.TaskManager.controll.auth.employee;


import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/employee")

@RequiredArgsConstructor


@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<TaskDTO>>  getTasksByUserId(@PathVariable Long id){
        return   ResponseEntity.ok(employeeService.getTaskByUserId(id));
    }
    @PutMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO>  updateStatus(@PathVariable Long id,@PathVariable String status){
        TaskDTO updatedTask = employeeService.updateTask(id, status);

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }
}
