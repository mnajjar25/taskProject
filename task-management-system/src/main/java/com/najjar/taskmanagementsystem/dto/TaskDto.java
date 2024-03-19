package com.najjar.taskmanagementsystem.dto;

import com.najjar.taskmanagementsystem.enums.Status;
import com.najjar.taskmanagementsystem.validation.EnumValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be current date or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    @EnumValue(enumClass = Status.class)
    @NotBlank(message = "status is required")
    private String status;

//    private String username;
}
