package com.TaskManager.TaskManager.services.employee;


import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.entities.Task;
import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.TaskStatus;
import com.TaskManager.TaskManager.repositories.TaskRepository;
import com.TaskManager.TaskManager.repositories.UserRepository;
import com.TaskManager.TaskManager.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final TaskRepository taskRepository;
    private final UserRepository    userRepository ;

    @Override
    public List<TaskDTO> getTaskByUserId(Long id ) {
        Optional<User> optionalUser= userRepository.findById(id);

        if (optionalUser.isPresent()) {
            return taskRepository.findAllByUserId(optionalUser.get().getId())
                    .stream()
                    .sorted(Comparator.comparing(Task::getDueDate).reversed())
                    .map(Task::getTaskDTO)
                    .collect(Collectors.toList());
        }
        throw new EntityNotFoundException("User Not Found");
    }

    @Override
    public TaskDTO updateTask(Long id, String status ) {
        Optional<Task>  taskToUpdate=taskRepository.findById(id);
        if(taskToUpdate.isPresent()){
            Task  task=taskToUpdate.get();
            // Handle taskStatus safely to avoid NullPointerException
                task.setTaskStatus(
                        mapStringToTaskStatus(status)
                );
            return taskRepository.save(task).getTaskDTO();
        }
        return  null;
    }

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED"  -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }
}
