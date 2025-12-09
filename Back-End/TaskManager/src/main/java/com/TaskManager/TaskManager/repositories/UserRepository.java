package com.TaskManager.TaskManager.repositories;

import com.TaskManager.TaskManager.entities.User;
import com.TaskManager.TaskManager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);

    Optional<User> findByUserRole(UserRole userRole);


}
