package com.najjar.taskmanagementsystem.service;

import com.najjar.taskmanagementsystem.dto.TaskDto;
import com.najjar.taskmanagementsystem.dto.TaskResponse;
import com.najjar.taskmanagementsystem.entity.Task;
import com.najjar.taskmanagementsystem.entity.User;
import com.najjar.taskmanagementsystem.enums.Status;
import com.najjar.taskmanagementsystem.exception.ResourceNotFoundException;
import com.najjar.taskmanagementsystem.exception.TaskAPIException;
import com.najjar.taskmanagementsystem.repository.TaskRepository;
import com.najjar.taskmanagementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    public TaskDto createTask(Long userId,TaskDto taskDto){

        Task task = mapToEntity(taskDto);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User","id",userId)
        );
        task.setUser(user);
        Task newTask = taskRepository.save(task);
        log.info("Task created successfully");
        return mapToDTO(newTask);
    }

    public TaskResponse getTasksByUserId(Long userId,int pageNo, int pageSize,String sortBy,String sortDir){


        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Task> tasks = taskRepository.findByUserId(userId,pageable);

        List<Task> listOfTasks = tasks.getContent();

        List<TaskDto> content =  listOfTasks.stream().map(task -> mapToDTO(task)).toList();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(content);
        taskResponse.setPageNo(tasks.getNumber());
        taskResponse.setPageSize(tasks.getSize());
        taskResponse.setTotalElements(tasks.getTotalElements());
        taskResponse.setTotalPages(tasks.getTotalPages());
        taskResponse.setLastPage(tasks.isLast());
        log.info("Tasks retrieved successfully");
        return taskResponse;
    }

    public TaskDto getTaskById(Long userId,Long taskId){
        Task task = getTaskIfValid(userId,taskId);
        log.info("Task retrieved successfully");
        return mapToDTO(task);
    }

    public TaskDto updateTask(Long userId,Long taskId,TaskDto taskDto){
        Task task = getTaskIfValid(userId,taskId);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(Status.valueOf(taskDto.getStatus()));
        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully");
        return mapToDTO(updatedTask);
    }

    public void deleteTask(Long userId,Long taskId){
        Task task = getTaskIfValid(userId,taskId);
        taskRepository.delete(task);
        log.info("Task deleted successfully");
    }

    private Task getTaskIfValid(Long userId,Long taskId){
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User","id",userId));

        Task task = taskRepository.findById(taskId).
                orElseThrow(() -> new ResourceNotFoundException("Task","id",taskId));

        if(!task.getUser().getId().equals(user.getId())){
            throw new TaskAPIException(HttpStatus.BAD_REQUEST,"Task does not belong to user");
        }
        return task;
    }

    public TaskResponse getAllTasks(int pageNo, int pageSize,String sortBy,String sortDir){


        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Task> tasks = taskRepository.findAll(pageable);

        List<Task> listOfTasks = tasks.getContent();

        List<TaskDto> content =  listOfTasks.stream().map(task -> mapToDTO(task)).toList();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setContent(content);
        taskResponse.setPageNo(tasks.getNumber());
        taskResponse.setPageSize(tasks.getSize());
        taskResponse.setTotalElements(tasks.getTotalElements());
        taskResponse.setTotalPages(tasks.getTotalPages());
        taskResponse.setLastPage(tasks.isLast());
        return taskResponse;
    }

    public TaskDto getTaskByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task","id",taskId));

        return mapToDTO(task);
    }

    private TaskDto mapToDTO(Task task){
        TaskDto taskDto = mapper.map(task,TaskDto.class);
        return taskDto;
    }

    private Task mapToEntity(TaskDto taskDto){
        Task task = mapper.map(taskDto,Task.class);
        return task;
    }


}
