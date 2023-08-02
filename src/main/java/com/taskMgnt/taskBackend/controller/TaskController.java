package com.taskMgnt.taskBackend.controller;

import com.taskMgnt.taskBackend.entity.Task;
import com.taskMgnt.taskBackend.exception.APIException;
import com.taskMgnt.taskBackend.exception.TaskNotFound;
import com.taskMgnt.taskBackend.exception.UserNotFound;
import com.taskMgnt.taskBackend.payload.DeleteTaskDto;
import com.taskMgnt.taskBackend.payload.TaskDto;
import com.taskMgnt.taskBackend.payload.CreateTaskDto;
import com.taskMgnt.taskBackend.payload.UpdateTaskDto;
import com.taskMgnt.taskBackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //POST to save task
    @PostMapping("/tasks")
    public ResponseEntity<Object> saveTask(
           @Valid @RequestBody CreateTaskDto taskDto
    ){
       try {
           return new ResponseEntity<>(taskService.saveTask(taskDto), HttpStatus.CREATED);
       }
       catch (APIException e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
       }
       catch (UserNotFound e){
           String error = "User Details Not Found";
           return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
       }
       catch (Exception e){
           return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
       }
    }
    //GET all tasks
    @GetMapping("/tasks")
    public ResponseEntity<Object> getAllTasks(
    ){
        try {
            return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
        }
        catch (APIException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFound e){
            String error = "User Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/admin/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllUserTasks(
    ){
        try {
            return new ResponseEntity<>(taskService.getAllUsersTasks(), HttpStatus.OK);
        }
        catch (APIException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    //GET individual task
    @GetMapping("/tasks/{taskid}")
    public ResponseEntity<Object> getTask(
        @PathVariable(name = "taskid") long taskid
    ){


        try {
            return new ResponseEntity<>(taskService.getTask(taskid),HttpStatus.OK);
        }
        catch (UserNotFound e){
            String error = "User Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (TaskNotFound e){
            String error = "Task Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (APIException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
        }
    }

    //DELETE individual task
    @DeleteMapping("/tasks/{taskid}")
    public ResponseEntity<Object> deleteTask(
            @PathVariable(name = "taskid") long taskid
    ){
        DeleteTaskDto deleteTaskDto = new DeleteTaskDto();
        try {
            TaskDto deletedTask = taskService.deleteTask(taskid);
            deleteTaskDto.setMessage("Deleted Successfully");
            return new ResponseEntity<>(deleteTaskDto,HttpStatus.OK);
        }
        catch (UserNotFound e){
            String error = "User Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (TaskNotFound e){
            String error = "Task Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (APIException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
        }



    }
    @PutMapping("/tasks/{taskid}")
    public ResponseEntity<Object> updateTask(
            @PathVariable(name = "taskid") long taskid,
            @RequestBody UpdateTaskDto updateTaskDto
    ){
        try{
            return new ResponseEntity<>(taskService.updateTask(taskid,updateTaskDto),HttpStatus.OK);
        }
        catch (UserNotFound e){
            String error = "User Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (TaskNotFound e){
            String error = "Task Details Not Found";
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }
        catch (APIException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
        }
    }
}