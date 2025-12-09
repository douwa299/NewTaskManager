package com.TaskManager.TaskManager.controll.auth.admin;


import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")

@CrossOrigin("*")
public class AdminController {


    private final AdminService adminService;
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
       return  ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {


            TaskDTO createddTaskDTO = adminService.createTask(taskDTO);
            if(createddTaskDTO == null ) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(createddTaskDTO);

    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(){
        return  ResponseEntity.ok(adminService.getAllTasks());
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void>  deleteTASK(@PathVariable Long id ){

        adminService.deleteTask(id);
        return  ResponseEntity.ok(null);

    }
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDTO>  getTaskById(@PathVariable Long id ){
        return  ResponseEntity.ok(adminService.getTaskById(id));

    }


    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = adminService.updateTask(id, taskDTO);

        if (updatedTask == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/task/search/{title}")
    public ResponseEntity<List<TaskDTO>> searchTaskByTitle(@PathVariable String title ) {


        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }


}
