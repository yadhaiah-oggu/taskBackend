package com.taskMgnt.taskBackend.controller;

import com.taskMgnt.taskBackend.entity.Task;
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

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //POST to save task
    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> saveTask(
            @RequestBody CreateTaskDto taskDto
    ){
       return new ResponseEntity<>(taskService.saveTask(taskDto), HttpStatus.CREATED);
    }
    //GET all tasks
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(
    ){
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }
    @GetMapping("/admin/tasks")
    public ResponseEntity<List<TaskDto>> getAllUserTasks(
    ){
        return new ResponseEntity<>(taskService.getAllUsersTasks(), HttpStatus.OK);
    }
    //GET individual task
    @GetMapping("/tasks/{taskid}")
    public ResponseEntity<TaskDto> getTask(
        @PathVariable(name = "taskid") long taskid
    ){
        return new ResponseEntity<>(taskService.getTask(taskid),HttpStatus.OK);
    }

    //DELETE individual task
    @DeleteMapping("/tasks/{taskid}")
    public ResponseEntity<DeleteTaskDto> deleteTask(
            @PathVariable(name = "taskid") long taskid
    ){
        DeleteTaskDto deleteTaskDto = new DeleteTaskDto();
        try {
            TaskDto deletedTask = taskService.deleteTask(taskid);
            deleteTaskDto.setMessage("Deleted Successfully");
        }
        catch (Exception e){
            deleteTaskDto.setMessage("Something Went wrong ...!");
        }

        return new ResponseEntity<>(deleteTaskDto,HttpStatus.OK);
    }
    @PutMapping("/tasks/{taskid}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(name = "taskid") long taskid,
            @RequestBody UpdateTaskDto updateTaskDto
    ){
        return new ResponseEntity<>(taskService.updateTask(taskid,updateTaskDto),HttpStatus.OK);
    }
}