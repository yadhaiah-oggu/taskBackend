package com.taskMgnt.taskBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExisted extends RuntimeException{
    private String message;
    public UserAlreadyExisted(String message){
        super(message);
        this.message = message;
    }

}
