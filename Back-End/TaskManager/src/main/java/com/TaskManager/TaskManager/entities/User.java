package com.TaskManager.TaskManager.entities;


import com.TaskManager.TaskManager.dto.UserDto;
import com.TaskManager.TaskManager.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.SimpleTimeZone;

@Entity
@Data
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;


    private String name;
    private String email;
    private String  password;
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("=== getAuthorities() DEBUG ===");
        System.out.println("UserRole: " + this.userRole);
        System.out.println("UserRole.name(): " + (this.userRole != null ? this.userRole.name() : "null"));

        // Should return exactly "EMPLOYEE" or "ADMIN"
        return List.of(new SimpleGrantedAuthority(
                this.userRole != null ? this.userRole.name() : "EMPLOYEE"
        ));
    }

    @Override
    public @Nullable String getPassword() {
        return password ;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //This method converts a User into a UserDto
    //Data Transfer Object, meaning it’s a lightweight object used
    // to send data outside (like in your API responses),
    // without exposing sensitive info.
    //Doenst expose the  password
    //We get to controll what data we can display
    public UserDto getUserDto(){
        UserDto  userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail (email);
        userDto.setUserRole(userRole);
        return userDto;
    }
}
