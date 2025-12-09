package com.TaskManager.TaskManager.repositories;

import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.entities.Task;
import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task>  findAllByUserId(Long id);
    List<Task> findAllByTitleContaining(String title);
}
