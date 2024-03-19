package com.najjar.taskmanagementsystem.controller;

import com.najjar.taskmanagementsystem.constants.AppConstants;
import com.najjar.taskmanagementsystem.dto.TaskDto;
import com.najjar.taskmanagementsystem.dto.TaskResponse;
import com.najjar.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for Task Resource"
)
public class TaskController {
    private TaskService taskService;

    @Operation(
            summary = "Create Task REST API",
            description = "Create Task REST API is used to save Task into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{userId}/tasks")
    public ResponseEntity<TaskDto> createTask(@PathVariable Long userId,@Valid @RequestBody TaskDto taskDto){
        return new ResponseEntity<>(taskService.createTask(userId,taskDto), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get All Tasks For Specific User REST API",
            description = "Get All Tasks For Specific User REST API is used to fetch all the tasks from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<TaskResponse> getTasksByUserId(
            @PathVariable Long userId,
            @RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        return ResponseEntity.ok(taskService.getTasksByUserId(userId,pageNo,pageSize,sortBy,sortDir));
    }

    @Operation(
            summary = "Get Task For Specific User By Id REST API",
            description = "Get Task For Specific User By Id REST API is used to get single task from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/users/{userId}/tasks/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long userId,@PathVariable(name = "id") Long taskId){
        return ResponseEntity.ok(taskService.getTaskById(userId,taskId));
    }


    @Operation(
            summary = "update Task REST API",
            description = "Update Task REST API is used to update a particular task in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long userId,@PathVariable(name = "id") Long taskId,@Valid @RequestBody TaskDto taskDto){
        return ResponseEntity.ok(taskService.updateTask(userId,taskId,taskDto));
    }

    @Operation(
            summary = "delete Task REST API",
            description = "Update Task REST API is used to update a particular task in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long userId,@PathVariable(name = "id") Long taskId){
        taskService.deleteTask(userId,taskId);
        return new ResponseEntity<>("Task deleted successfully.",HttpStatus.OK);
    }


    @Operation(
            summary = "Get All Tasks REST API",
            description = "Get All Tasks REST API is used to fetch all the tasks from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/tasks")
    public ResponseEntity<TaskResponse> getAllTasks(
            @RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,required = false) String sortDir
    ){
        return ResponseEntity.ok(taskService.getAllTasks(pageNo,pageSize,sortBy,sortDir));
    }


    @Operation(
            summary = "Get Task By Id REST API",
            description = "Get Task By Id REST API is used to get single task from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(taskService.getTaskByTaskId(taskId));
    }

}
