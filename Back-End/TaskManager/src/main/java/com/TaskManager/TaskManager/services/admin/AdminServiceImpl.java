package com.TaskManager.TaskManager.services.admin;


import com.TaskManager.TaskManager.dto.TaskDTO;
import com.TaskManager.TaskManager.dto.UserDto;
import com.TaskManager.TaskManager.entities.Task;
import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.TaskStatus;
import com.TaskManager.TaskManager.enums.UserRole;
import com.TaskManager.TaskManager.repositories.TaskRepository;
import com.TaskManager.TaskManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().filter(user->user.getUserRole()== UserRole.EMPLOYEE)
                .map(User::getUserDto)
                 .collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Task task = new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setDueDate(taskDTO.getDueDate());
            task.setPriority(taskDTO.getPriority());
            task.setUser(optionalUser.get()); // Assuming Task stores employee ID

            // Set default status
            task.setTaskStatus(TaskStatus.INPROGRESS); // or whatever your default status is


            // Save the task
            Task savedTask = taskRepository.save(task);

            // Return the saved task as DTO
            return savedTask.getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
         taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(Long id) {

        Optional<Task>  optionalTask= taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User>  optionalUser=userRepository.findById(taskDTO.getEmployeeId());
        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task existingTask = optionalTask.get();

            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription()); // FIXED
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());       // FIXED

            // Handle taskStatus safely to avoid NullPointerException
            if (taskDTO.getTaskStatus() != null) {
                existingTask.setTaskStatus(
                        mapStringToTaskStatus(taskDTO.getTaskStatus().toString())
                );
            }
            // If taskStatus is null, keep the existing status
            existingTask.setUser(optionalUser.get());
            Task saved = taskRepository.save(existingTask);

            return saved.getTaskDTO();
        }

        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
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
