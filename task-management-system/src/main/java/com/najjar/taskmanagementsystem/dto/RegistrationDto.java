package com.najjar.taskmanagementsystem.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
    private Long id;
    @NotBlank(message = "Username must not be null or empty")
    @Size(min = 3, max = 25, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password must not be null or empty")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=*]).{8,25}$"
            ,message = "Password must contain at least one number, one lowercase letter, one uppercase letter, one valid special character, and have a length between 8 and 25 characters"
    )
    private String password;
    private List<TaskDto> tasks;
}
