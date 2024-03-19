package com.najjar.taskmanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TaskAPIException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public TaskAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public TaskAPIException(String message, HttpStatus status, String msg) {
        super(message);
        this.status = status;
        this.message = msg;
    }
}
